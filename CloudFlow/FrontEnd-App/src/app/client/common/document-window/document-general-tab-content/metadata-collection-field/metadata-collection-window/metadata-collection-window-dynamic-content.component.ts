import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";
import { AppError, MessageDisplayer, MetadataDefinitionModel, ArrayUtils, MetadataCollectionInstanceRowModel, BooleanUtils, DocumentTypeService } from "@app/shared"; 
import { FormUtils, Message, DocumentService, MetadataCollectionInstanceModel, DocumentValidationResponseModel } from "@app/shared";
import { 
	MetadataTextFormControl, 
	MetadataNumericFormControl,
	MetadataAutoNumberFormControl,
	MetadataDateFormControl,
	MetadataDateTimeFormControl,
	MetadataListFormControl,
	MetadataNomenclatorFormControl,
	MetadataTextAreaFormControl,
	MetadataUserFormControl,
	MetadataFormControl,
	MetadataGroupFormControl
} from "./../../document-metadata/metadata-form-controls";
import {
	IconConstants, 
	ListMetadataItemModel, 
	MetadataInstanceModel ,
	DocumentModel,
	ObjectUtils,
	DateUtils,
	WorkflowStateModel,
	DocumentCollectionValidationRequestModel
} from "@app/shared";
import { MetadataUtils } from "./../../document-metadata/metadata-utils";
import { MetadataEventMediator, MetadataEventName } from "./../../document-metadata/metadata-event-mediator";
import { DocumentMetadataInputData } from "./../../document-metadata/document-metadata.component";

@Component({
	selector: "app-metadata-collection-dynamic-content",
	template: `
		<form [formGroup]="formGroup">
			<div *ngFor="let collectionMetadata of collectionMetadatas" class="ui-fluid">
				<app-document-metadata 
					[formGroup]="formGroup" 
					[inputData]="collectionMetadata.inputData"
					[documentReadonly]="readonly"
					[documentMode]="documentMode"
					[eventMediator]="metadataEventMediator">
				</app-document-metadata>
			</div>
		</form>
	`
})
export class MetadataCollectionWindowDynamicContentComponent extends MetadataCollectionWindowContent implements OnInit, AfterViewInit {

	public formBuilder: FormBuilder;
	public formGroup: FormGroup;

	private metadataCollectionDefinition: MetadataDefinitionModel;
	private documentWorkflowState: WorkflowStateModel;

	public collectionMetadatas: CollectionMetadata[];

	public metadataEventMediator: MetadataEventMediator;

	constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
		this.formGroup = this.formBuilder.group([]);
		this.metadataEventMediator = new MetadataEventMediator();
	}

	public ngOnInit() {
		this.init();
	}

	public ngAfterViewInit(): void {
		setTimeout(() => {
			this.metadataEventMediator.fireEvent({
				eventName: MetadataEventName.ALL_METADATAS_READY
			});
		}, 0);		
	}

	private init(): void {
		this.collectionMetadatas = [];
		this.metadataCollectionDefinition = this.inputData.metadataCollectionDefinition;
		this.metadataCollectionDefinition.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
			this.formGroup.addControl(metadataDefinition.name, this.prepareMetadataFormControlFromDefinition(metadataDefinition));
			this.collectionMetadatas.push({
				metadataDefinition: metadataDefinition,
				inputData: {
					documentId: this.inputData.documentId,
					documentLocationRealName: this.inputData.documentLocationRealName,
					documentType: this.inputData.documentType,
					documentWorkflowState: this.inputData.documentWorkflowState,
					metadataDefinition: metadataDefinition,
					documentFormDataProvider: this.inputData.documentFormDataProvider
				}
			});				
		});	
	}

	private prepareMetadataFormControlFromDefinition(metadataDefinition: MetadataDefinitionModel): MetadataFormControl<any> {
		let readonly: boolean = false;
		let metadataInstance: MetadataInstanceModel = null;
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow)) {
			this.inputData.collectionInstanceRow.metadataInstanceList.forEach((instance: MetadataInstanceModel) => {
				if (instance.metadataDefinitionId === metadataDefinition.id) {
					metadataInstance = instance;
				}
			});
		}
		let metadataFormControl: MetadataFormControl<any> = null;
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT) {
			metadataFormControl = new MetadataTextFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NUMERIC) {
			metadataFormControl = new MetadataNumericFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
			metadataFormControl = new MetadataAutoNumberFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
			metadataFormControl = new MetadataDateFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
			metadataFormControl = new MetadataDateTimeFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
			metadataFormControl = new MetadataListFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			metadataFormControl = new MetadataNomenclatorFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT_AREA) {
			metadataFormControl = new MetadataTextAreaFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
			metadataFormControl = new MetadataUserFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_GROUP) {
			metadataFormControl = new MetadataGroupFormControl(metadataDefinition, metadataInstance, this.documentWorkflowState, readonly);
		}  else {
			throw new Error("Incompatible metadata definition type [" + metadataDefinition.type + "]");
		}
		if (!MetadataUtils.isMetadataVisible(metadataDefinition, this.documentWorkflowState)) {
			metadataFormControl.disable();
		}
		return metadataFormControl;
	}

	public getMetadataInstances(): MetadataInstanceModel[] {
		let metadataInstances: MetadataInstanceModel[] = [];	
		this.collectionMetadatas.forEach((collectionMetadata: CollectionMetadata) => {
			let metadataInstance: MetadataInstanceModel = new MetadataInstanceModel();
			metadataInstance.metadataDefinitionId = collectionMetadata.metadataDefinition.id;
			metadataInstance.values = MetadataUtils.getValueAsStringArray(this.getMetadataFormControl(collectionMetadata.metadataDefinition.name), collectionMetadata.metadataDefinition.type);
			metadataInstances.push(metadataInstance);
		});
		return metadataInstances;
	}

	private getMetadataFormControl(controlName: string): MetadataFormControl<any> {
		if (controlName === null) {
			return null;
		}
		return <MetadataFormControl<any>> this.formGroup.get(controlName);
	}

	public validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}
}

interface CollectionMetadata {
	metadataDefinition: MetadataDefinitionModel;
	inputData: DocumentMetadataInputData;
}