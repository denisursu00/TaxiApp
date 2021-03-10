import { Component, OnInit, ViewChild } from "@angular/core";
import { AppError, Message, MessageDisplayer, IconConstants, DocumentAttachmentModel, MimeTypeModel, WorkflowStateModel, ArrayUtils, StringUtils, ObjectUtils } from "@app/shared";
import { DocumentAttachmentComponent } from "@app/shared/components/document-attachment/document-attachment.component";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentModel } from "@app/shared";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";

@Component({
	selector: "app-document-attachments-tab-content",
	templateUrl: "./document-attachments-tab-content.component.html"
})
export class DocumentAttachmentsTabContentComponent extends DocumentTabContent {

	@ViewChild(DocumentAttachmentComponent)
	private documentAttachmentComponent: DocumentAttachmentComponent;

	public documentAttachments: DocumentAttachmentModel[];
	public documentWorkflowState: WorkflowStateModel;
	public allowedAttachmentTypes: MimeTypeModel[];
	
	private messages: Message[];

	public constructor() {
		super();
		this.messages = [];
	}

	public doWhenOnInit(): void {
		this.documentWorkflowState = this.inputData.currentState;
		this.allowedAttachmentTypes = this.inputData.documentType.allowedAttachmentTypes;
	}

	protected prepareForAdd(): void {
		// Nothing here.
	}

	protected prepareForViewOrEdit(): void {
		this.documentAttachments = [];
		let viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;
		if (ArrayUtils.isNotEmpty(viewOrEditInfo.document.attachments)) {
			this.documentAttachments = [...viewOrEditInfo.document.attachments];
		}
		this.documentAttachments.forEach((documentAttachment: DocumentAttachmentModel) => {
			documentAttachment.documentId = viewOrEditInfo.document.id;
			documentAttachment.documentLocationRealName = viewOrEditInfo.document.documentLocationRealName;
			documentAttachment.versionNumber = viewOrEditInfo.document.versionNumber;	
		});
	}

	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		this.messages = [];
		if (this.isAttachmentMandatoryInCurrentState()) {
			let isValid: boolean = false;
			let attachments: DocumentAttachmentModel[] = this.documentAttachmentComponent.getUploadedAttachments();
			if (ArrayUtils.isNotEmpty(attachments)) {
				attachments.forEach((attachment: DocumentAttachmentModel) => {
					if (attachment.documentWorkflowStateCode === this.documentWorkflowState.code) {
						isValid = true;
					}
				});
			}
			if (!isValid) {
				this.messages.push(Message.createForError("AT_LEAST_ONE_ATTACHMENT_REQUIRED"));
			}
		}
		return ArrayUtils.isEmpty(this.messages);
	}

	private isAttachmentMandatoryInCurrentState(): boolean {
		
		if (ObjectUtils.isNullOrUndefined(this.documentWorkflowState)) {
			return false;
		}

		let attachmentMandatory: boolean = false;

		let mandatoryAttachmentInStates: boolean = this.inputData.documentType.mandatoryAttachmentInStates;
		if (mandatoryAttachmentInStates) {
			
			let currentStateCode: string = this.documentWorkflowState.code;

			let mandatoryAttachmentStatesAsString: string = this.inputData.documentType.mandatoryAttachmentStates;
			let statesCodes: string[] = StringUtils.splitString(mandatoryAttachmentStatesAsString, ";");
			
			statesCodes.forEach((stateCode: string) => {
				if (stateCode === currentStateCode) {
					attachmentMandatory = true;
				}
			});
		}

		return attachmentMandatory;
	}

	public populateDocument(document: DocumentModel): void {
		document.attachments = this.documentAttachmentComponent.getUploadedAttachments();
		document.namesForAttachmentsToDelete = this.documentAttachmentComponent.getNamesForAttachmentsToDelete();
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}