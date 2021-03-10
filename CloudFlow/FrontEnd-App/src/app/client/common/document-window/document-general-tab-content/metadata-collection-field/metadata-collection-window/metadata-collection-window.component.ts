import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { AppError, MessageDisplayer, MetadataDefinitionModel, ArrayUtils, MetadataCollectionInstanceRowModel, BooleanUtils, DocumentTypeModel, DocumentConstants, BaseWindow } from "@app/shared"; 
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
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";
import { MetadataCollectionWindowDynamicContentComponent } from "./metadata-collection-window-dynamic-content.component";
import { ViewChild } from "@angular/core";
import { MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent } from "./metadata-collection-window-informatii-participanti-of-prezenta-comisii-gl-content.component";
import { DocumentFormDataProvider } from "./../../document-form-data-provider";
import { MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent } from "./metadata-collection-window-prezenta-membrii-of-prezenta-aga-content.component";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-metadata-collection-window",
	templateUrl: "./metadata-collection-window.component.html"
})
export class MetadataCollectionWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public inputData: MetadataCollectionWindowInputData;

	@Input()
	public documentMode: "add" | "viewOrEdit";

	@Output()
	private canceled: EventEmitter<void>;

	@Output()
	private saved: EventEmitter<MetadataCollectionInstanceRowModel>;

	@ViewChild(MetadataCollectionWindowDynamicContentComponent)
	private dynamicContentComponent: MetadataCollectionWindowDynamicContentComponent;

	@ViewChild(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent)
	private informatiiParticipantiOfPrezentaComisiiGLContentComponent: MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent;

	@ViewChild(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent)
	private prezentaMembriiOfPrezentaAgaContentComponent: MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent;

	private documentService: DocumentService;

	private metadataCollectionDefinition: MetadataDefinitionModel;
	private documentWorkflowState: WorkflowStateModel;

	public windowVisible: boolean;
	public title: string;
	public width: number;
	public height: any;
	
	public businessValidationMessages: Message[];
	public documentMessagesWindowVisible: boolean = false;
	public documentMessagesWindowMessages: Message[];

	public dynamicContentEnabled: boolean = false;
	public informatiiParticipantiOfPrezentaComisiiGlContentEnabled: boolean = false;
	public prezentaMembriiOfPrezentaAgaContentEnabled: boolean = false;

	public constructor(documentService: DocumentService) {
		super();
		this.documentService = documentService;
		this.saved = new EventEmitter();
		this.canceled = new EventEmitter();
		this.unlock();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("inputData cannot be null");
		}		
		this.metadataCollectionDefinition = this.inputData.metadataCollectionDefinition;
		this.documentWorkflowState = this.inputData.documentWorkflowState;
		this.init();
	}
	
	private init(): void {
		this.title = this.metadataCollectionDefinition.label;
		this.enableContent();
		this.windowVisible = true;
	}

	private enableContent() {
		if (this.inputData.documentType.name === DocumentConstants.DOCUMENT_TYPE_NAME_PREZENTA_GOMISII_GL
				&& this.metadataCollectionDefinition.name === DocumentConstants.METADATA_NAME_INFORMATII_PARTICIPANTI_OF_PREZENTA_COMISII_GL) {
			this.informatiiParticipantiOfPrezentaComisiiGlContentEnabled = true;
		} else if (this.inputData.documentType.name === DocumentConstants.DOCUMENT_TYPE_NAME_PREZENTA_AGA
			&& this.metadataCollectionDefinition.name === DocumentConstants.METADATA_NAME_PREZENTA_MEMBRII_OF_PREZENTA_AGA) {
		this.prezentaMembriiOfPrezentaAgaContentEnabled = true;
		} else {
			this.dynamicContentEnabled = true;
		}
	}

	private displayValidationMessages(): void {
		this.documentMessagesWindowMessages = [];
		if (ArrayUtils.isNotEmpty(this.businessValidationMessages)) {
			this.businessValidationMessages.forEach((message: Message) => {
				this.documentMessagesWindowMessages.push(message);
			});
		}
		this.documentMessagesWindowVisible = true;
	}

	public onDocumentMessagesWindowClosed(event: any) {
		this.documentMessagesWindowVisible = false;
	}

	public onSaveAction(event: any) {
		this.lock();
		this.validate((valid: boolean): void => {
			if (valid) {
				this.saved.emit(this.prepareMetadataCollectionInstance());
				this.windowVisible = false;
			} else {
				this.unlock();
			}
		});
	}	

	private prepareMetadataCollectionInstance(): MetadataCollectionInstanceRowModel {		
		let rowModel: MetadataCollectionInstanceRowModel = new MetadataCollectionInstanceRowModel();
		rowModel.id = ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow) ? this.inputData.collectionInstanceRow.id : null;		
		rowModel.metadataInstanceList = this.getContentComponent().getMetadataInstances();
		return rowModel;
	}

	private validate(callback: (valid: boolean) => void): void {
		
		this.businessValidationMessages = [];
		
		let isValid: boolean = this.getContentComponent().validate();
		if (!isValid) {
			this.businessValidationMessages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT"));
			this.displayValidationMessages();
			callback(false);
			return;
		}

		let validationRequest: DocumentCollectionValidationRequestModel = new DocumentCollectionValidationRequestModel();
		validationRequest.documentId =  this.inputData.documentId;
		validationRequest.documentLocationRealName = this.inputData.documentLocationRealName;
		validationRequest.documentTypeId = this.inputData.documentType.id;
		validationRequest.metadataCollectionDefinitionId = this.metadataCollectionDefinition.id;	
		validationRequest.metatadaInstances = this.getContentComponent().getMetadataInstances();		
		this.documentService.validateDocumentCollection(validationRequest, {
			onSuccess: (validationResponse: DocumentValidationResponseModel) => {				
				if (validationResponse.valid) {
					callback(true);
				} else {
					validationResponse.messages.forEach((message: string) => {
						this.businessValidationMessages.push(Message.createForError(message, false));
					});
					this.displayValidationMessages();
					callback(false);
				}
			},
			onFailure: (error: AppError) => {
				this.businessValidationMessages.push(Message.createForError(error.errorCode));
				this.displayValidationMessages();				
				callback(false);
			}
		});
	}

	private getContentComponent(): MetadataCollectionWindowContent {
		if (ObjectUtils.isNotNullOrUndefined(this.dynamicContentComponent)) {
			return this.dynamicContentComponent;
		} else if (ObjectUtils.isNotNullOrUndefined(this.informatiiParticipantiOfPrezentaComisiiGLContentComponent)) {
			return this.informatiiParticipantiOfPrezentaComisiiGLContentComponent;
		} else if (ObjectUtils.isNotNullOrUndefined(this.prezentaMembriiOfPrezentaAgaContentComponent)) {
			return this.prezentaMembriiOfPrezentaAgaContentComponent;
		}
		throw new Error("nu s-a putut determina componenta de content");
	}

	public onCloseAction(event: any) {
		this.windowVisible = false;
		this.canceled.emit();
	}

	public onHide(event: any): void {
		this.canceled.emit();
	}
}

export interface MetadataCollectionWindowInputData {

	documentId?: string;
	documentLocationRealName?: string;
	documentType: DocumentTypeModel;
	
	metadataCollectionDefinition: MetadataDefinitionModel;
	documentWorkflowState?: WorkflowStateModel;
	collectionInstanceRow: MetadataCollectionInstanceRowModel;

	documentFormDataProvider: DocumentFormDataProvider;
}