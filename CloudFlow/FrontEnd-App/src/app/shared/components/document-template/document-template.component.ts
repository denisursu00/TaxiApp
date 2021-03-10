import { Component, Input, ViewEncapsulation, ViewChild, Output, EventEmitter, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators, AbstractControl, FormControl } from "@angular/forms";
import { FileUpload } from "primeng/primeng";
import { environment } from "@app/../environments/environment";
import { ArrayUtils, StringUtils, ObjectUtils, DownloadUtils, FormUtils } from "../../utils";
import { WorkflowStateModel, MimeTypeModel, AppError, DocumentTemplateModel } from "../../model";
import { DocumentTemplateService, AttachmentService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { HttpResponse } from "@angular/common/http";

@Component({
	selector: "app-document-template",
	templateUrl: "./document-template.component.html",
	styleUrls: ["./document-template.component.css"],
	encapsulation: ViewEncapsulation.None
})
export class DocumentTemplateComponent implements OnInit {

	private static readonly FIELD_NAME_TEMPLATE: string = "template";
	private static readonly FIELD_NAME_TEMPLATE_DESCRIPTION: string = "description";

	@Input("documentTemplates")
	public uploadedDocumentTemplates: DocumentTemplateModel[];

	@Input()
	public documentTypeId: string;

	@Input()
	public allowedDocumentTemplateTypes: MimeTypeModel[] = [];

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	private documentTemplateService: DocumentTemplateService;
	private attachmentService: AttachmentService;
	private messageDisplayer: MessageDisplayer;

	private namesForTemplatesToDelete: string[];

	public isUploadDisabled: boolean;
	public allowedDocumentTemplateTypesAsString: string;
	public fileNameWithDescription: string;

	public loading: boolean;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public constructor(documentTemplateService: DocumentTemplateService, 
			attachmentService: AttachmentService, messageDisplayer: MessageDisplayer,
			formBuilder: FormBuilder) {
		this.documentTemplateService = documentTemplateService;
		this.attachmentService = attachmentService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.prepareForm();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		
		this.resetSelectedTemplates();
		this.resetNamesForTemplatesToDelete();

		if (ArrayUtils.isEmpty(this.uploadedDocumentTemplates)) {
			this.uploadedDocumentTemplates = [];
		}

		let allowedDocumentTemplateTypes = [];
		if (ArrayUtils.isNotEmpty(this.allowedDocumentTemplateTypes)) {
			this.allowedDocumentTemplateTypes.forEach(allowedDocumentTemplateType => {
				if (ObjectUtils.isNotNullOrUndefined(allowedDocumentTemplateType)) {
					allowedDocumentTemplateTypes.push("." + allowedDocumentTemplateType.extension);
				}
			});
			this.allowedDocumentTemplateTypesAsString = allowedDocumentTemplateTypes.join(",");
		}
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group([]);
		this.form.addControl("description", new FormControl(null, [Validators.required]));
		this.form.addControl("exportAvailabilityExpression", new FormControl(null, []));
	}

	public get descriptionFormControl(): AbstractControl {
		return this.form.controls["description"];
	}

	public get exportAvailabilityExpressionFormControl(): AbstractControl {
		return this.form.controls["exportAvailabilityExpression"];
	}

	private resetSelectedTemplates(): void {
		this.fileUpload.files = [];
	}

	private resetNamesForTemplatesToDelete(): void {
		this.namesForTemplatesToDelete = [];
	}

	public getUploadedDocumentTemplates(): DocumentTemplateModel[] {
		return this.uploadedDocumentTemplates;
	}

	public getNamesForTemplatesToDelete(): string[] {
		return this.namesForTemplatesToDelete;
	}

	public onSelect(event: any): void {
		// nothing now
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	public onUpload(event: any): void {

		if (!this.validate()) {
			return;
		}
		this.lock();

		let uploadCounter: number = event.files.length;
		for(let file of event.files) {
			if (!this.uploadedDocumentTemplatesContainsTemplateWithName(file.name)) {
				let formData: FormData = new FormData();
				formData.append(DocumentTemplateComponent.FIELD_NAME_TEMPLATE, file, file.name);
				formData.append(DocumentTemplateComponent.FIELD_NAME_TEMPLATE_DESCRIPTION, file, this.descriptionFormControl.value);
				this.documentTemplateService.uploadTemplateFile(formData, {
					onSuccess: (documentTemplateModel: DocumentTemplateModel) => {
						
						documentTemplateModel.fromDb = false;
						documentTemplateModel.description = this.descriptionFormControl.value;
						documentTemplateModel.exportAvailabilityExpression = this.exportAvailabilityExpressionFormControl.value;
						this.uploadedDocumentTemplates.push(documentTemplateModel);
						
						uploadCounter--;
						if (uploadCounter === 0) {
							
							this.descriptionFormControl.reset();
							this.exportAvailabilityExpressionFormControl.reset();
							
							this.unlock();
						}						
					},
					onFailure: (appError: AppError) => {
						uploadCounter--;
						if (uploadCounter === 0) {
							this.unlock();
							this.messageDisplayer.displayAppError(appError);
						}						
					}
				});
			} else {
				uploadCounter--;
				if (uploadCounter === 0) {
					this.unlock();
				}
			}
		}
		this.resetSelectedTemplates();		
	}

	public onRemoveFile(file: any): void {
		this.fileUpload.files.splice(this.fileUpload.files.indexOf(file), 1);
	}

	public onRemoveTemplate(uploadedTemplate: DocumentTemplateModel): void {
		this.namesForTemplatesToDelete.push(uploadedTemplate.name);
		this.uploadedDocumentTemplates.splice(this.uploadedDocumentTemplates.indexOf(uploadedTemplate), 1);
	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	private uploadedDocumentTemplatesContainsTemplateWithName(name: string): boolean {
		return ArrayUtils.isNotEmpty(
			this.uploadedDocumentTemplates.filter(template => template.name === name)
		);
	}

	public onDownloadAttachment(event: any, attachment: DocumentTemplateModel): void {
		if (event) {
			event.preventDefault();
		}
		this.lock();
		if (attachment.fromDb) {	
			this.documentTemplateService.downloadTemplateFile(Number(this.documentTypeId), attachment.name).subscribe(
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
			this.attachmentService.downloadFile(attachment.name).subscribe(
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
	
	public get hasTemplates() {
		return ArrayUtils.isNotEmpty(this.uploadedDocumentTemplates);
	}
}