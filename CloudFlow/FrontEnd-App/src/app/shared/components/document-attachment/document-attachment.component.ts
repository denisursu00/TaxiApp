import { Component, Input, OnInit, ViewEncapsulation, ViewChild } from "@angular/core";
import { FileUpload } from "primeng/primeng";
import { environment } from "@app/../environments/environment";
import { ArrayUtils, AttachmentPermissionBusinessUtils, StringUtils, ObjectUtils } from "../../utils";
import { DocumentAttachmentModel, WorkflowStateModel, MimeTypeModel, AppError } from "../../model";
import { AttachmentService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { DownloadUtils } from "./../../utils";
import { AttachmentModel } from "./../../model";
import { DocumentService } from "./../../service";
import { HttpResponse } from "@angular/common/http";
import { ConfirmationWindowFacade } from "./../confirmation-window";

@Component({
	selector: "app-document-attachment",
	templateUrl: "./document-attachment.component.html",
	styleUrls: ["./document-attachment.component.css"],
	encapsulation: ViewEncapsulation.None
})
export class DocumentAttachmentComponent implements OnInit {

	private static readonly FIELD_NAME_ATTACHMENT: string = "attachment";

	@Input("documentAttachments")
	public uploadedAttachments: DocumentAttachmentModel[];

	@Input()
	public documentWorkflowState: WorkflowStateModel;

	@Input()
	public allowedAttachmentTypes: MimeTypeModel[] = [];

	@Input()
	public readonly: boolean;

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	private documentAttachmentService: AttachmentService;
	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;

	private namesForAttachmentsToDelete: String[];	
	public downloadUrl: string;
	public isUploadDisabled: boolean;
	public allowedAttachmentTypesAsString: string;
	public loading: boolean = false;

	public deleteAttachmentEnabled: boolean = false;

	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(documentAttachmentService: AttachmentService, messageDisplayer: MessageDisplayer, 
			documentService: DocumentService) {
		this.documentAttachmentService = documentAttachmentService;
		this.messageDisplayer = messageDisplayer;
		this.documentService = documentService;
		this.confirmationWindow = new ConfirmationWindowFacade();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		
		this.resetSelectedAttachments();
		this.resetNamesForAttachmentsToDelete();

		if (ArrayUtils.isEmpty(this.uploadedAttachments)) {
			this.uploadedAttachments = [];
		}
		if (!this.readonly) {
			this.deleteAttachmentEnabled = AttachmentPermissionBusinessUtils.canDeleteAttachments(this.documentWorkflowState);
		}
		if (!this.readonly) {
			this.readonly = !AttachmentPermissionBusinessUtils.canAddAttachments(this.documentWorkflowState);
		}


		let allowedAttachmentTypes = [];
		if (ArrayUtils.isNotEmpty(this.allowedAttachmentTypes)) {
			this.allowedAttachmentTypes.forEach(allowedAttachmentType => {
				if (ObjectUtils.isNotNullOrUndefined(allowedAttachmentType)) {
					allowedAttachmentTypes.push("." + allowedAttachmentType.extension);
				}
			});
			this.allowedAttachmentTypesAsString = allowedAttachmentTypes.join(",");
		}
	}

	private resetSelectedAttachments(): void {
		this.fileUpload.files = [];
	}

	private resetNamesForAttachmentsToDelete(): void {
		this.namesForAttachmentsToDelete = [];
	}

	public getUploadedAttachments(): DocumentAttachmentModel[] {
		return this.uploadedAttachments;
	}

	public getNamesForAttachmentsToDelete(): String[] {
		return this.namesForAttachmentsToDelete;
	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	public onUpload(event): void {
				
		let nrOfUploads: number = event.files.length;
		if (nrOfUploads > 0) {
			this.lock();
		}

		for(let file of event.files) {

			if (!this.uploadedAttachmentsContainsAttachmentWithName(file.name)) {

				let formData:FormData = new FormData();
				formData.append("attachment", file, file.name);
				
				this.documentAttachmentService.uploadFile(formData, {
					onSuccess: (attachment: AttachmentModel) => {
						let attachmentModel: DocumentAttachmentModel = new DocumentAttachmentModel();
						attachmentModel.name = attachment.name;
						attachmentModel.documentWorkflowStateCode = this.documentWorkflowState.code;
						attachmentModel.isNew = true;
						this.uploadedAttachments.push(attachmentModel);
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
					},
					onFailure: (appError: AppError) => {
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
						this.messageDisplayer.displayAppError(appError);
					}
				});
			} else {
				nrOfUploads--;
				if (nrOfUploads === 0) {
					this.unlock();
				}
			}
		}
		
		this.resetSelectedAttachments();
	}

	public onDownload(attachment: DocumentAttachmentModel): void {
		this.lock();
		if (ObjectUtils.isNullOrUndefined(attachment.documentId) && ObjectUtils.isNullOrUndefined(attachment.documentLocationRealName) ) {	
			this.documentAttachmentService.downloadFile(attachment.name).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.name);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		} else {
			this.documentService.downloadDocumentAttachment(attachment.documentLocationRealName, attachment.documentId, attachment.name, attachment.versionNumber).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.name);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		}
	}

	public onRemove(uploadedAttachment: DocumentAttachmentModel): void {
		this.confirmationWindow.confirm({
			approve: () => {
				if (!uploadedAttachment.isNew) {
					this.namesForAttachmentsToDelete.push(uploadedAttachment.name);
				}
				ArrayUtils.removeElement(this.uploadedAttachments, uploadedAttachment);
				this.confirmationWindow.hide();
			},
			reject: () => {
				this.confirmationWindow.hide();
			}
		}, "DELETE_CONFIRM");
		
	}

	private uploadedAttachmentsContainsAttachmentWithName(name: string): boolean {
		return ArrayUtils.isNotEmpty(
			this.uploadedAttachments.filter(
				attachment => attachment.name === name
			)
		);
	}
	
	public get hasAttachments() {
		return ArrayUtils.isNotEmpty(this.uploadedAttachments);
	}
}