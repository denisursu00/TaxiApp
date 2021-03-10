import { Component, OnInit, Input, AfterViewInit } from "@angular/core";
import { Validators, FormControl, FormGroup, FormBuilder, ValidatorFn, AbstractControl } from "@angular/forms";
import { 
	AppError, 
	MessageDisplayer, 
	IconConstants, 
	MetadataDefinitionModel, 
	ListMetadataItemModel, 
	MetadataInstanceModel,
	MetadataCollectionInstanceModel,
	MetadataCollectionInstanceRowModel,
	DocumentModel,
	ObjectUtils,
	DateUtils,
	ArrayUtils,
	Message,
	WorkflowStateModel,
	BooleanUtils,
	StringUtils,
	FormUtils
} from "@app/shared";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";
import { 
	MetadataTextFormControl, 
	MetadataNumericFormControl,
	MetadataAutoNumberFormControl,
	MetadataDateFormControl,
	MetadataDateTimeFormControl,
	MetadataListFormControl,
	MetadataTextAreaFormControl,
	MetadataUserFormControl,
	MetadataFormControl,
	MetadataNomenclatorFormControl,
	MetadataCollectionFormControl,
	MetadataMonthFormControl,
	MetadataGroupFormControl,
	MetadataDocumentFormControl, 
	MetadataCalendarFormControl,
	MetadataProjectFormControl
} from "./document-metadata/metadata-form-controls";
import { MetadataUtils } from "./document-metadata/metadata-utils";
import { Observable } from "rxjs/Observable";
import { MetadataEventMediator, MetadataEventName, MetadataEvent } from "./document-metadata/metadata-event-mediator";
import { DocumentMetadataInputData } from "../index";
import { DocumentFormDataProvider } from "./document-form-data-provider";

@Component({
	selector: "app-document-general-tab-content",
	templateUrl: "./document-general-tab-content.component.html"
})
export class DocumentGeneralTabContentComponent extends DocumentTabContent implements AfterViewInit {

	private static readonly FORM_CONTROL_DOCUMENT_NAME: string = "documentName";
	private static readonly FORM_CONTROL_DOCUMENT_DESCRIPTION: string = "documentDescription";

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public documentMetadatas: DocumentMetadata[];

	public formVisible: boolean = false;
	
	private messages: Message[];

	public metadataEventMediator: MetadataEventMediator;

	private documentFormDataProvider: DocumentFormDataProvider;
	private metadataInitializedEventCounter: number = 0;
	private metadatasValuesAtMetadataInitializedEventMap: any = {};

	public constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
		this.metadataEventMediator = new MetadataEventMediator();
	}

	protected doWhenOnInit(): void {
		this.formGroup = this.formBuilder.group([]);
		this.documentMetadatas = [];
		this.documentFormDataProvider = this.buildDocumentFormDataProvider();
	}

	public ngAfterViewInit(): void {		
	}

	private get documentNameFormControl() { 
		return this.formGroup.get(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME); 
	}

	private get documentWorkflowState(): WorkflowStateModel {
		return this.inputData.currentState;
	}

	private get documentDescriptionFormControl() { 
		return this.formGroup.get(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION); 
	}

	private get areMetadatasAvailable(): boolean {	
		return ArrayUtils.isNotEmpty(this.inputData.documentType.metadataDefinitions);
	}

	public prepareForAdd(): void {
		
		let addInfo: DocumentAddInfo = <DocumentAddInfo> this.inputData;

		let documentNameFormCntrol: FormControl = new FormControl(addInfo.documentType.name, Validators.required);
		this.formGroup.addControl(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME, documentNameFormCntrol);
		this.formGroup.addControl(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION, new FormControl());
		
		if (ArrayUtils.isNotEmpty(addInfo.documentType.metadataDefinitions)) {
			this.sortMetadataDefinitions(addInfo.documentType.metadataDefinitions);
			addInfo.documentType.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
				this.formGroup.addControl(metadataDefinition.name, this.getMetadataFormControl(metadataDefinition, null, addInfo.currentState, this.readonly));
				this.documentMetadatas.push({
					metadataDefinition: metadataDefinition,
					inputData: {
						documentType: addInfo.documentType,
						documentLocationRealName: addInfo.documentLocationRealName,
						metadataDefinition: metadataDefinition,
						documentWorkflowState: addInfo.currentState,
						documentFormDataProvider: this.documentFormDataProvider
					}
				});	
			});
		}
		this.formVisible = true;
		this.subscribeToMetadataInitializedEvent();
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

	private getMetadataFormControl(metadataDefinition: MetadataDefinitionModel, document: DocumentModel, documentWorkflowState: WorkflowStateModel, readonly: boolean): MetadataFormControl<any> {
		let metadataControl: MetadataFormControl<any> = null;
		if (metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {		
			let metadataCollectionInstance: MetadataCollectionInstanceModel = <MetadataCollectionInstanceModel> this.getMetadataInstance(metadataDefinition, document);
			metadataControl = new MetadataCollectionFormControl(metadataDefinition, metadataCollectionInstance, documentWorkflowState, readonly);
		} else {
			let metadataInstance: MetadataInstanceModel = <MetadataInstanceModel> this.getMetadataInstance(metadataDefinition, document);	
			if (metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT) {
				metadataControl = new MetadataTextFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NUMERIC) {
				metadataControl = new MetadataNumericFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
				metadataControl = new MetadataAutoNumberFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT_AREA) {
				metadataControl = new MetadataTextAreaFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
				metadataControl = new MetadataDateFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
				metadataControl = new MetadataDateTimeFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
				metadataControl = new MetadataUserFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
				metadataControl = new MetadataListFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
				metadataControl = new MetadataNomenclatorFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			}  else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_MONTH) {
				metadataControl = new MetadataMonthFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_GROUP) {
				metadataControl = new MetadataGroupFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DOCUMENT) {
				metadataControl = new MetadataDocumentFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_CALENDAR) {
				metadataControl = new MetadataCalendarFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_PROJECT) {
				metadataControl = new MetadataProjectFormControl(metadataDefinition, metadataInstance, documentWorkflowState, readonly);
			} else {
				throw new Error("Necunoscut metadata definition type [" + metadataDefinition.type + "]");
			}
		}
		if (MetadataUtils.isMetadataInvisible(metadataDefinition, documentWorkflowState)) {
			metadataControl.disable();
		}

		return metadataControl;
	}

	private getMetadataInstance(metadataDefinition: MetadataDefinitionModel, document: DocumentModel): MetadataInstanceModel | MetadataCollectionInstanceModel {
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

	public prepareForViewOrEdit(): void {
		
		let viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;

		let documentNameFormCntrol: FormControl = new FormControl(viewOrEditInfo.document.documentName, Validators.required);
		this.formGroup.addControl(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME, documentNameFormCntrol);
		this.formGroup.addControl(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION, new FormControl(viewOrEditInfo.document.documentDescription));

		if (ArrayUtils.isNotEmpty(viewOrEditInfo.documentType.metadataDefinitions)) {
			this.sortMetadataDefinitions(viewOrEditInfo.documentType.metadataDefinitions);
			viewOrEditInfo.documentType.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
				this.formGroup.addControl(metadataDefinition.name, this.getMetadataFormControl(metadataDefinition, viewOrEditInfo.document, viewOrEditInfo.currentState, this.readonly));
				this.documentMetadatas.push({
					metadataDefinition: metadataDefinition,
					inputData: {
						documentId: viewOrEditInfo.document.id,
						documentLocationRealName: viewOrEditInfo.document.documentLocationRealName,
						documentType: viewOrEditInfo.documentType,
						metadataDefinition: metadataDefinition,
						documentWorkflowState: viewOrEditInfo.currentState,
						documentFormDataProvider: this.documentFormDataProvider
					}
				});	
			});
		}
		this.formVisible = true;
		this.subscribeToMetadataInitializedEvent();
	}

	private buildDocumentFormDataProvider(): DocumentFormDataProvider {
		let thisComponent = this;
		return {							
			getMetadataValue(metadataName: string): any {
				if (StringUtils.isBlank(metadataName)) {
					return null;
				}
				let formControl: FormControl = <FormControl> thisComponent.formGroup.get(metadataName);
				if (ObjectUtils.isNullOrUndefined(formControl)) {
					return null;
				}
				return formControl.value;
			}
		};
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

	public populateDocument(document: DocumentModel): void {		
		document.documentName = this.getControlValue(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_NAME);
		document.documentDescription = this.getControlValue(DocumentGeneralTabContentComponent.FORM_CONTROL_DOCUMENT_DESCRIPTION);
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

	private subscribeToMetadataInitializedEvent(): void {
		this.metadataEventMediator.subscribe({
			eventName: MetadataEventName.METADATA_INITIALIZED,
			handle: (event: MetadataEvent) => {
				this.metadataInitializedEventCounter++;
				this.metadatasValuesAtMetadataInitializedEventMap[event.metadataDefinition.name] = event.metadataValue;
				if (this.metadataInitializedEventCounter === this.documentMetadatas.length) {
					setTimeout(() => {
						this.metadataEventMediator.fireEvent({
							eventName: MetadataEventName.ALL_METADATAS_READY,
							metadatasValues: this.metadatasValuesAtMetadataInitializedEventMap
						});
					}, 0);
				}
			}
		});
	}
}

interface DocumentMetadata {
	metadataDefinition: MetadataDefinitionModel;
	inputData: DocumentMetadataInputData;
}