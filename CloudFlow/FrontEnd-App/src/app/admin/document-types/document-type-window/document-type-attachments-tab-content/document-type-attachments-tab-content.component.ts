import { Component, OnInit, ViewChild} from "@angular/core";
import { AppError, MimeTypeService, MimeTypeModel, MessageDisplayer, WorkflowStateModel, ArrayUtils, StringUtils } from "@app/shared";
import { DocumentTypeModel } from "@app/shared";
import { DocumentTypeTabContent } from "./../document-type-tab-content";
import { Message } from "@app/shared";
import { FormGroup, FormBuilder, Validators, AbstractControl } from "@angular/forms";

@Component({
	selector: "app-document-type-attachments-tab-content",
	templateUrl: "./document-type-attachments-tab-content.component.html",
	styleUrls: ["./document-type-attachments-tab-content.component.css"]
})
export class DocumentTypeAttachmentsTabContentComponent extends DocumentTypeTabContent {

	private static readonly WORKFLOW_STATES_UI_SEPARATOR: string = ", ";
	private static readonly WORKFLOW_STATES_STORAGE_SEPARATOR: string = ";";
	private static readonly EMPTY_DEFAULT_VALUE: string = "";

	private mimeTypeService: MimeTypeService;
	private messageDisplayer: MessageDisplayer;
	private messages: Message[];
	private formBuilder: FormBuilder;

	public form: FormGroup;

	public allAttachmentTypes: MimeTypeModel[] = [];
	public allowedAttachmentTypes: MimeTypeModel[] = [];

	public possibleWorkflowStates: WorkflowStateModel[] = [];
	public selectedStateCodes: string = "";
	public selectedStateCodesAsArray: string[] = [];
	public mandatoryAttachmentStatesNames: string[] = [];
	public mandatoryAttachmentStatesNamesCSSClassName: string = "";
	public mandatoryAttachmentStatesNamesJoined: string = "";

	public mandatoryAttachmentCheckBoxValue: boolean = false;
	public mandatoryAttachmentInStatesFieldSetCollapsed: boolean = true;
	public mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed: boolean = true;

	public workflowStatesSelectionWindowVisible: boolean = false;

	public selectStateButtonDisabled: boolean = false;
	public scrollHeight: string;

	public constructor(mimeTypeService: MimeTypeService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.mimeTypeService = mimeTypeService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareForm();
	}

	protected doWhenNgOnInit(): void {
		// Nothing to see here, just carry on
	}

	private reset() {
		this.selectedStateCodesAsArray = [];
		this.selectedStateCodes = "";
		this.mandatoryAttachmentStatesNames = [];
		this.mandatoryAttachmentStatesNamesJoined = "";
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			metadataName: [ DocumentTypeAttachmentsTabContentComponent.EMPTY_DEFAULT_VALUE, Validators.required ],
			metadataValue: [ DocumentTypeAttachmentsTabContentComponent.EMPTY_DEFAULT_VALUE, Validators.required ]
		});
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	public get metadataNameFormControl(): AbstractControl {
		return this.getControlByName("metadataName");
	}
	
	public get metadataValueFormControl(): AbstractControl {
		return this.getControlByName("metadataValue");
	}

	protected prepareForAdd(): void {
		this.getAllMimeTypes();
		this.selectStateButtonDisabled = true;
	}
	
	protected prepareForEdit(): void {

		let documentType: DocumentTypeModel = this.inputData.documentType;
		this.possibleWorkflowStates = this.inputData.workflowStates;

		this.selectStateButtonDisabled = ArrayUtils.isEmpty(this.possibleWorkflowStates);

		this.mandatoryAttachmentCheckBoxValue = documentType.mandatoryAttachment;
		this.mandatoryAttachmentInStatesFieldSetCollapsed = !documentType.mandatoryAttachmentInStates;
		this.mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed = !documentType.mandatoryAttachmentWhenMetadataHasValue;

		if (StringUtils.isNotBlank(documentType.mandatoryAttachmentStates)) {
			this.selectedStateCodes = documentType.mandatoryAttachmentStates;
			this.selectedStateCodesAsArray = documentType.mandatoryAttachmentStates.split(DocumentTypeAttachmentsTabContentComponent.WORKFLOW_STATES_STORAGE_SEPARATOR);
			this.setMandatoryAttachmentStatesNamesByCodes();
			this.joinMandatoryAttachmentStatesNamesForUI();
		}

		this.allowedAttachmentTypes = documentType.allowedAttachmentTypes;
		this.getAllMimeTypes();
	}

	public populateDocumentType(documentType: DocumentTypeModel): void {

		documentType.mandatoryAttachment = this.mandatoryAttachmentCheckBoxValue;
		documentType.mandatoryAttachmentInStates = !this.mandatoryAttachmentInStatesFieldSetCollapsed;
		documentType.mandatoryAttachmentWhenMetadataHasValue = !this.mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed;
		documentType.allowedAttachmentTypes = this.allAttachmentTypes.filter( attachmentType => attachmentType.allowed === true );

		if (!this.mandatoryAttachmentInStatesFieldSetCollapsed) {
			documentType.mandatoryAttachmentStates = this.selectedStateCodesAsArray.join(DocumentTypeAttachmentsTabContentComponent.WORKFLOW_STATES_STORAGE_SEPARATOR);
		} else {
			documentType.mandatoryAttachmentStates = null;
		}

		if (!this.mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed) {
			documentType.metadataNameInMandatoryAttachmentCondition = this.form.value.metadataName;
			documentType.metadataValueInMandatoryAttachmentCondition = this.form.value.metadataValue;
		} else {
			documentType.metadataNameInMandatoryAttachmentCondition = null;
			documentType.metadataValueInMandatoryAttachmentCondition = null;
		}
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = true;
		if (!this.mandatoryAttachmentInStatesFieldSetCollapsed) {
			if (StringUtils.isBlank(this.mandatoryAttachmentStatesNamesJoined)) {
				this.mandatoryAttachmentStatesNamesCSSClassName = "mandatoryAttachmentStates";
				isValid = false;
			} else {
				this.mandatoryAttachmentStatesNamesCSSClassName = "";
			}
		}
		if (!this.mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed) {
			if (!this.form.valid) {
				this.metadataNameFormControl.markAsTouched();
				this.metadataValueFormControl.markAsTouched();
				isValid = false;
			}
		}
		if (!isValid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT"));
		}
		return isValid;
	}

	public getMessages(): Message[] {
		return this.messages;
	}

	public onChangeMandatoryAttachment(event: any): void {
		if (!this.mandatoryAttachmentCheckBoxValue) {
			this.mandatoryAttachmentStatesNamesJoined = "";
			this.form.controls.metadataName.reset();
			this.form.controls.metadataValue.reset();
			this.mandatoryAttachmentInStatesFieldSetCollapsed = true;
			this.mandatoryAttachmentWhenMetadataHasValueFieldSetCollapsed = true;
		}
	}

	private getAllMimeTypes(): void {
		this.mimeTypeService.getAllMimeTypes({
			onSuccess: (mimeTypeModels: MimeTypeModel[]) => {
				this.allAttachmentTypes = mimeTypeModels;
				this.findAndSetAllowedAttachmentTypes();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private findAndSetAllowedAttachmentTypes(): void {
		for (let attachmentType of this.allAttachmentTypes) {
			attachmentType.allowed = true;
			if (ArrayUtils.isNotEmpty(this.allowedAttachmentTypes)) {
				let found: boolean = false;
				for (let allowedAttachmentType of this.allowedAttachmentTypes) {
					if (allowedAttachmentType.id === attachmentType.id) {
						found = true;
						break;
					}
				}
				attachmentType.allowed = found;
			}
		}
	}

	private setMandatoryAttachmentStatesNamesByCodes(): void {
		let mandatoryAttachmentStates: WorkflowStateModel[] = [];
		for (let selectedStateCode of this.selectedStateCodesAsArray) {
			mandatoryAttachmentStates = [...mandatoryAttachmentStates, ...this.possibleWorkflowStates.filter(state => state.code === selectedStateCode)];
		}
		for (let manadatoryAttachmentState of mandatoryAttachmentStates) {
			this.mandatoryAttachmentStatesNames.push(manadatoryAttachmentState.name);
		}
	}

	private joinMandatoryAttachmentStatesNamesForUI(): void {
		this.mandatoryAttachmentStatesNamesJoined = this.mandatoryAttachmentStatesNames.join(DocumentTypeAttachmentsTabContentComponent.WORKFLOW_STATES_UI_SEPARATOR);
	}

	public onSelectState(event: any): void {
		this.workflowStatesSelectionWindowVisible = true;
	}

	public onWorkflowStatesSelectionWindowCancelled(): void {
		this.workflowStatesSelectionWindowVisible = false;
	}

	public onWorkflowStatesSelectionWindowStatesSelected(selectedStateCodes: string): void {
		this.reset();
		this.selectedStateCodes = selectedStateCodes;
		this.selectedStateCodesAsArray = this.selectedStateCodes.split(DocumentTypeAttachmentsTabContentComponent.WORKFLOW_STATES_STORAGE_SEPARATOR);
		this.setMandatoryAttachmentStatesNamesByCodes();
		this.joinMandatoryAttachmentStatesNamesForUI();
	}

	public get areMandatoryAttachmentStatesNames(): boolean {
		return ArrayUtils.isNotEmpty(this.mandatoryAttachmentStatesNames);
	}
}