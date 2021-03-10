import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";
import { AppError, MessageDisplayer, MetadataDefinitionModel, ArrayUtils, MetadataCollectionInstanceRowModel, BooleanUtils, DocumentTypeService } from "@app/shared"; 
import { FormUtils, Message, DocumentService, MetadataCollectionInstanceModel, DocumentValidationResponseModel } from "@app/shared";
import { MetadataFormControl } from "./../../document-metadata/metadata-form-control";
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
import { AdminUpdateDocumentMetadataInputData } from "./../../document-metadata/document-metadata.component";

@Component({
	selector: "app-admin-update-document-metadata-collection-dynamic-content",
	template: `
		<form [formGroup]="formGroup">
			<div *ngFor="let collectionMetadata of collectionMetadatas" class="ui-fluid">
				<app-admin-update-document-metadata 
					[formGroup]="formGroup" 
					[inputData]="collectionMetadata.inputData">
				</app-admin-update-document-metadata>
			</div>
		</form>
	`
})
export class AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent extends MetadataCollectionWindowContent implements OnInit, AfterViewInit {

	public formBuilder: FormBuilder;
	public formGroup: FormGroup;

	private metadataCollectionDefinition: MetadataDefinitionModel;
	private documentWorkflowState: WorkflowStateModel;

	public collectionMetadatas: CollectionMetadata[];

	constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
		this.formGroup = this.formBuilder.group([]);
	}

	public ngOnInit() {
		this.init();
	}

	public ngAfterViewInit(): void {		
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
					metadataDefinition: metadataDefinition
				}
			});				
		});	
	}

	private prepareMetadataFormControlFromDefinition(metadataDefinition: MetadataDefinitionModel): MetadataFormControl {
		let readonly: boolean = false;
		let metadataInstance: MetadataInstanceModel = null;
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow)) {
			this.inputData.collectionInstanceRow.metadataInstanceList.forEach((instance: MetadataInstanceModel) => {
				if (instance.metadataDefinitionId === metadataDefinition.id) {
					metadataInstance = instance;
				}
			});
		}
		let metadataFormControl: MetadataFormControl = new MetadataFormControl(metadataDefinition, metadataInstance);
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
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

	private getMetadataFormControl(controlName: string): MetadataFormControl {
		if (controlName === null) {
			return null;
		}
		return <MetadataFormControl> this.formGroup.get(controlName);
	}

	public validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}
}

interface CollectionMetadata {
	metadataDefinition: MetadataDefinitionModel;
	inputData: AdminUpdateDocumentMetadataInputData;
}