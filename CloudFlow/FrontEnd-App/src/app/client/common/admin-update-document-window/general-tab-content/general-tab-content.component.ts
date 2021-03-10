import { Component, OnInit, Input, AfterViewInit } from "@angular/core";
import { Validators, FormControl, FormGroup, FormBuilder, ValidatorFn, AbstractControl } from "@angular/forms";
import {
	MetadataDefinitionModel, 
	MetadataInstanceModel,
	MetadataCollectionInstanceModel,
	MetadataCollectionInstanceRowModel,
	ObjectUtils,
	DateUtils,
	ArrayUtils,
	Message,
	BooleanUtils,
	StringUtils,
	FormUtils,
	AdminUpdateDocumentModel
} from "@app/shared";
import { AdminUpdateDocumentWindowTabContent } from "./../admin-update-document-window-tab-content";
import { MetadataFormControl } from "./document-metadata/metadata-form-control";
import { AdminUpdateDocumentWindowTabInputData } from "./../admin-update-document-window-tab-input-data";
import { AdminUpdateDocumentMetadataInputData } from "./document-metadata";

@Component({
	selector: "app-admin-update-document-general-tab-content",
	templateUrl: "./general-tab-content.component.html"
})
export class AdminUpdateDocumentWindowGeneralTabContentComponent extends AdminUpdateDocumentWindowTabContent implements AfterViewInit {

	private static readonly FORM_CONTROL_DOCUMENT_NAME: string = "documentName";
	private static readonly FORM_CONTROL_DOCUMENT_DESCRIPTION: string = "documentDescription";

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public documentMetadatas: DocumentMetadata[];

	public formVisible: boolean = false;
	
	private messages: Message[];

	public constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
	}

	protected doWhenOnInit(): void {
		this.formGroup = this.formBuilder.group([]);
		this.documentMetadatas = [];
	}

	public ngAfterViewInit(): void {		
	}

	private get documentNameFormControl() { 
		return this.formGroup.get(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME); 
	}

	private get documentDescriptionFormControl() { 
		return this.formGroup.get(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION); 
	}

	private get areMetadatasAvailable(): boolean {	
		return ArrayUtils.isNotEmpty(this.inputData.documentType.metadataDefinitions);
	}

	private sortMetadataDefinitions(metadataDefinitions: MetadataDefinitionModel[]): void {
		metadataDefinitions.sort((md1: MetadataDefinitionModel, md2: MetadataDefinitionModel): number => {
			if (md1.orderNumber < md2.orderNumber) {
				return -1;
			}
			if (md1.orderNumber > md2.orderNumber) {
				return 1;
			}
			return 0;
		});
	}

	private getMetadataFormControl(metadataDefinition: MetadataDefinitionModel, document: AdminUpdateDocumentModel): MetadataFormControl {
		let metadataControl: MetadataFormControl = null;
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {		
			let metadataCollectionInstance: MetadataCollectionInstanceModel = <MetadataCollectionInstanceModel> this.getMetadataInstance(metadataDefinition, document);
			metadataControl = new MetadataFormControl(metadataDefinition, metadataCollectionInstance);
		} else {
			let metadataInstance: MetadataInstanceModel = <MetadataInstanceModel> this.getMetadataInstance(metadataDefinition, document);	
			metadataControl = new MetadataFormControl(metadataDefinition, metadataInstance);			
		}
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
			metadataControl.disable();
		}
		return metadataControl;
	}

	private getMetadataInstance(metadataDefinition: MetadataDefinitionModel, document: AdminUpdateDocumentModel): MetadataInstanceModel | MetadataCollectionInstanceModel {
		let metadataInstance: MetadataInstanceModel | MetadataCollectionInstanceModel = null;
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {			
			if (ObjectUtils.isNotNullOrUndefined(document) && ArrayUtils.isNotEmpty(document.metadataCollectionInstances)) {
				document.metadataCollectionInstances.forEach((instance: MetadataCollectionInstanceModel) => {
					if (instance.metadataDefinitionId === metadataDefinition.id) {
						metadataInstance = instance;
					}
				});
			}
		} else {
			if (ObjectUtils.isNotNullOrUndefined(document) && ArrayUtils.isNotEmpty(document.metadataInstances)) {
				document.metadataInstances.forEach((instance: MetadataInstanceModel) => {
					if (instance.metadataDefinitionId === metadataDefinition.id) {
						metadataInstance = instance;
					}
				});
			}
		}
		return metadataInstance;
	}

	public prepareForUpdate(): void {

		let documentNameFormCntrol: FormControl = new FormControl(this.inputData.document.documentName, Validators.required);
		this.formGroup.addControl(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME, documentNameFormCntrol);
		this.formGroup.addControl(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION, new FormControl(this.inputData.document.documentDescription));

		if (ArrayUtils.isNotEmpty(this.inputData.documentType.metadataDefinitions)) {
			this.sortMetadataDefinitions(this.inputData.documentType.metadataDefinitions);
			this.inputData.documentType.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
				this.formGroup.addControl(metadataDefinition.name, this.getMetadataFormControl(metadataDefinition, this.inputData.document));
				this.documentMetadatas.push({
					metadataDefinition: metadataDefinition,
					inputData: {
						documentId: this.inputData.document.id,
						documentLocationRealName: this.inputData.document.documentLocationRealName,
						documentType: this.inputData.documentType,
						metadataDefinition: metadataDefinition
					}
				});	
			});
		}
		this.formVisible = true;
	}

	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		this.messages = [];
		this.validateAllFormFields();
		let isValid: boolean = this.formGroup.valid;
		if (!isValid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT"));
		}
		return isValid;
	}

	private validateAllFormFields(): void {
		FormUtils.validateAllFormFields(this.formGroup);
	}

	private getControlValue(controlName: string): any {
		if (controlName === null) {
			return null;
		}
		return this.formGroup.get(controlName).value;
	}

	private getMetadataControlValues(controlName: string, metadataType: string): string[] {
		let controlValue = this.getControlValue(controlName);
		if (ObjectUtils.isNullOrUndefined(controlValue)) {
			return [];
		}
		if (controlValue instanceof Array) {
			let valuesAsString: string[] = [];
			let controlValueAsArray: Array<any> = <Array<any>> controlValue;
			controlValueAsArray.forEach((elem: any) => {
				valuesAsString.push(String(elem));
			});
			return valuesAsString;
		}
		let metadataValues: string[] = [];
		if (controlValue instanceof Date) {
			if (metadataType === MetadataDefinitionModel.TYPE_DATE) {
				metadataValues.push(DateUtils.formatForStorage(controlValue)); 
			} else if (metadataType === MetadataDefinitionModel.TYPE_DATE_TIME) {
				metadataValues.push(DateUtils.formatDateTimeForStorage(controlValue)); 
			} else if (metadataType === MetadataDefinitionModel.TYPE_MONTH) {
				metadataValues.push(DateUtils.formatMonthYearForStorage(controlValue)); 
			}
		} else if (typeof controlValue === "number" || typeof controlValue === "boolean") {
			metadataValues.push(controlValue.toString());
		} else if (typeof controlValue === "string") {
			metadataValues.push(controlValue);
		} else {
			throw new Error("Metadata value type nu este cunoscut [" + (typeof controlValue) + "]");
		}
		return metadataValues;
	}

	public populateDocument(document: AdminUpdateDocumentModel): void {		
		document.documentName = this.getControlValue(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME);
		document.documentDescription = this.getControlValue(AdminUpdateDocumentWindowGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION);
		document.metadataInstances = this.getMetadataInstances();
		document.metadataCollectionInstances = this.getMetadataCollectionInstances();
	}

	public getMetadataInstances(): MetadataInstanceModel[] {
		let metadataInstances: MetadataInstanceModel[] = [];
		this.documentMetadatas.forEach((documentMetadata: DocumentMetadata) => {
			if (documentMetadata.metadataDefinition.type !== MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
				let metadataInstance: MetadataInstanceModel = new MetadataInstanceModel();
				metadataInstance.metadataDefinitionId = documentMetadata.metadataDefinition.id;
				metadataInstance.values = this.getMetadataControlValues(documentMetadata.metadataDefinition.name, documentMetadata.metadataDefinition.type);
				metadataInstances.push(metadataInstance);
			}			
		});
		return metadataInstances;
	}

	private getMetadataCollectionInstances(): MetadataCollectionInstanceModel[] {
		let metadataCollectionInstances: MetadataCollectionInstanceModel[] = [];
		this.documentMetadatas.forEach((documentMetadata: DocumentMetadata) => {
			if (documentMetadata.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
				let metadataCollectionInstance: MetadataCollectionInstanceModel = this.getControlValue(documentMetadata.metadataDefinition.name);
				if (ObjectUtils.isNullOrUndefined(metadataCollectionInstance)) {
					metadataCollectionInstance = new MetadataCollectionInstanceModel();
					metadataCollectionInstance.metadataDefinitionId = documentMetadata.metadataDefinition.id;
					metadataCollectionInstance.collectionInstanceRows = [];
				}
				metadataCollectionInstances.push(metadataCollectionInstance);
			}		
		});
		return metadataCollectionInstances;
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}

interface DocumentMetadata {
	metadataDefinition: MetadataDefinitionModel;
	inputData: AdminUpdateDocumentMetadataInputData;
}