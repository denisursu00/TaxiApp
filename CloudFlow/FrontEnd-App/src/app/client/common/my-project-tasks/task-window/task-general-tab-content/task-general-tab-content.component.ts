import { Component, OnInit, Output, EventEmitter, Input, ViewChild } from "@angular/core";
import { TaskTabContent } from "../task-tab-content";
import { TaskModel, StringValidators, DateConstants, DateUtils, TaskPriority, FormUtils, ProjectService, MessageDisplayer, AppError, ProjectModel, 
	TaskParticipantsTo, ObjectUtils, BooleanUtils, TaskAttachmentModel, AttachmentService, AttachmentModel, ConfirmationWindowFacade, ArrayUtils, DownloadUtils, TaskEventModel, TranslateUtils, AdminPermissionEnum, TaskStatus, StringUtils } from "@app/shared";
import { FormBuilder, FormGroup, AbstractControl, Validators, ValidatorFn } from "@angular/forms";
import { SelectItem, FileUpload } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { HttpResponse } from "@angular/common/http";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-task-general-tab-content",
	templateUrl: "./task-general-tab-content.component.html",
	styleUrls: ["./task-general-tab-content.component.css"]
})
export class TaskGeneralTabContentComponent extends TaskTabContent implements OnInit {

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	@Output()
	public projectSelected: EventEmitter<number>;

	@Input()
	public perspective: "view" | "add" | "edit";

	public confirmationWindow: ConfirmationWindowFacade;

	private projectService: ProjectService;
	private attachmentService: AttachmentService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private formBuilder: FormBuilder;
	private authManager: AuthManager;

	public form: FormGroup;

	public task: TaskModel;
	public prioritySelectItems: SelectItem[];
	public participantsToSelectItems: SelectItem[];
	public subactivitySelectItems: SelectItem[];
	public taskEvents: TaskEventModel[];

	public dateFormat: string;
	public yearRange: string;
	private isExplicationMandatory: boolean = false;
	public uploadedAttachments: TaskAttachmentModel[];
	public isUpdatingAttachments: boolean;
	public displayDescriptionWindow: boolean;
	public descriptionWindowHeader: string;
	public taskEventDescription: string;
	

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder, attachmentService: AttachmentService, translateUtils: TranslateUtils, authManager: AuthManager) {
		super();
		this.projectService = projectService;
		this.attachmentService = attachmentService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.authManager = authManager;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.projectSelected = new EventEmitter<number>();
		this.init();
	}

	ngOnInit(): void {
		this.changeFormPerspective();
		this.getAllTaskEvents()
			.then((taskEvents: TaskEventModel[]) => this.taskEvents = taskEvents)
			.catch(error => console.error(error));
	}

	private init(): void {
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.uploadedAttachments = [];
		this.isUpdatingAttachments = false;
		this.displayDescriptionWindow = false;
		this.preparePrioritySelectItems();
		this.prepareParticipantsToSelectItems();
		this.prepareForm();
	}

	private prepareParticipantsToSelectItems(): void {
		this.participantsToSelectItems = [];
		Object.keys(TaskParticipantsTo).forEach((enumItem: string) => {
			let item: SelectItem = { value: TaskParticipantsTo[enumItem], label: TaskParticipantsTo[enumItem]};
			this.participantsToSelectItems.push(item);
		});

		ListItemUtils.sortByLabel(this.participantsToSelectItems);
	}

	private prepareSubactivitySelectItems(projectId: number): Promise<void> {
		return new Promise<void>((resolve, reject) => {
			this.subactivitySelectItems = [];
			this.getProjectModelById(projectId).then((project: ProjectModel) => {
				project.subactivities.forEach(subactivity => {
					this.subactivitySelectItems.push({label: subactivity.name, value: subactivity.id});
				});
				this.subactivitySelectItems.sort((a, b) => a.label.localeCompare(b.label));
				resolve();
			}).catch(error => {
				if (error instanceof AppError){
					this.messageDisplayer.displayAppError(error);
				}else{
					console.error(error);
				}
			});
		});
	}

	private getAllTaskEvents(): Promise<Array<TaskEventModel> | AppError>{
		return new Promise<Array<TaskEventModel> | AppError>((resolve, reject) => {
			this.projectService.getAllTaskEvents({
				onSuccess: (taskEvents: Array<TaskEventModel>): void => {
					resolve(taskEvents);
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject(error);
				}
			});
		});
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			"projectId": [null, Validators.required],
			"name": [null, [Validators.required, StringValidators.blank()]],
			"subactivity": [null],
			"description": [null],
			"startDate": [null, Validators.required],
			"endDate": [null, Validators.required],
			"priority": [null, Validators.required],
			"permanent": [false],
			"participantsTo": [null, [Validators.required]],
			"explications": [null],
			"comments": [null],
			"evenimentStartDate": [null],
			"evenimentEndDate": [null]
		});
	}

	private changeFormPerspective(): void {
		let participantsTo: string = this.participantsToFormControl.value;
		this.evenimentStartDateFormControl.disable();
		this.evenimentEndDateFormControl.disable();
		this.explicationsFormControl.clearValidators();
		this.evenimentStartDateFormControl.clearValidators();
		this.evenimentEndDateFormControl.clearValidators();
		this.isExplicationMandatory = false;

		if (ObjectUtils.isNotNullOrUndefined(participantsTo)) {
			this.evenimentStartDateFormControl.enable();
			this.evenimentEndDateFormControl.enable();
			this.evenimentStartDateFormControl.setValidators([Validators.required]);
			this.evenimentEndDateFormControl.setValidators([Validators.required, this.evenimentEndDateLowerThendStartDate()]);
		} 

		if (ObjectUtils.isNotNullOrUndefined(participantsTo) && participantsTo === TaskParticipantsTo.ALTELE) {
			this.isExplicationMandatory = true;
			this.explicationsFormControl.setValidators([Validators.required, StringValidators.blank()]);
			this.explicationsFormControl.updateValueAndValidity();
		}

		if (this.perspective === "edit"){
			this.permanentFormControl.disable();
			this.startDateFormControl.disable();
			this.endDateFormControl.disable();
			this.participantsToFormControl.disable();
			this.evenimentStartDateFormControl.disable();
			this.evenimentEndDateFormControl.disable();
			this.projectIdFormControl.disable();
		}
		if (this.perspective === "view"){
			this.form.disable();
		}
	}

	public onPermanentChanged(event: any): void {
		let isPermanent: boolean = this.permanentFormControl.value;
		if (isPermanent) {
			this.startDateFormControl.reset();
			this.startDateFormControl.disable();
			this.endDateFormControl.reset();
			this.endDateFormControl.disable();
		} else {
			this.startDateFormControl.validator = Validators.required;
			this.startDateFormControl.enable();
			this.endDateFormControl.validator = Validators.required;
			this.endDateFormControl.enable();
		}
	}

	private preparePrioritySelectItems(): void {
		this.prioritySelectItems = [];
		this.prioritySelectItems.push({label: "High", value: TaskPriority.HIGH});
		this.prioritySelectItems.push({label: "Normal", value: TaskPriority.NORMAL});
		this.prioritySelectItems.push({label: "Low", value: TaskPriority.LOW});
	}

	public onParticipantsToChanged(event: any): void {
		this.changeFormPerspective();
	}

	public reset(): void {
		this.form.reset();
	}
	
	public populateForSave(task: TaskModel): void {
		task.projectId = this.projectIdFormControl.value;
		task.name = this.nameFormControl.value;
		task.subactivityId = this.subactivityFormControl.value;
		task.description = this.descriptionFormControl.value;
		task.startDate = this.startDateFormControl.value;
		task.endDate = this.endDateFormControl.value;
		task.permanent = BooleanUtils.isTrue(this.permanentFormControl.value);
		task.priority = this.priorityFormControl.value;
		task.participationsTo = this.participantsToFormControl.value;
		task.explications = this.explicationsFormControl.value;
		task.comments = this.commentsFormControl.value;
		task.evenimentStartDate = this.evenimentStartDateFormControl.value;
		task.evenimentEndDate = this.evenimentEndDateFormControl.value;
		task.taskAttachments = this.uploadedAttachments;
	}

	public getProjectModelById(projectId: number): Promise<ProjectModel>{
		return new Promise<ProjectModel>((resolve, reject) => {
			this.projectService.getProjectById(projectId, {
				onSuccess: (project: ProjectModel) => {
					resolve(project);
				},onFailure: (error: AppError) => {
					reject(error);
				}
			});
		});
	}

	public onUpload(event: any): void{
		let nrOfUploads: number = event.files.length;
		if (nrOfUploads > 0) {
			this.isUpdatingAttachments = true;
		}

		for(let file of event.files) {

			if (!this.uploadedAttachmentsContainsAttachmentWithName(file.name)) {

				let formData:FormData = new FormData();
				formData.append("attachment", file, file.name);
				
				this.attachmentService.uploadFile(formData, {
					onSuccess: (attachment: AttachmentModel) => {
						let attachmentModel: TaskAttachmentModel = new TaskAttachmentModel();
						attachmentModel.name = attachment.name;
						this.uploadedAttachments.push(attachmentModel);
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.isUpdatingAttachments = false;
						}
					},
					onFailure: (appError: AppError) => {
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.isUpdatingAttachments = false;
						}
						this.messageDisplayer.displayAppError(appError);
					}
				});
			} else {
				nrOfUploads--;
				if (nrOfUploads === 0) {
					this.isUpdatingAttachments = false;
				}
			}
		}
		
		this.resetSelectedAttachments();
	}

	public onRemoveAttachment(attachment: TaskAttachmentModel): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.isUpdatingAttachments = true;
				this.attachmentService.deleteFile(attachment.name, {
					onSuccess: (): void => {
						ArrayUtils.removeElement(this.uploadedAttachments, attachment);
						this.isUpdatingAttachments = false;
					}, 
					onFailure: (error: AppError): void => {
						this.isUpdatingAttachments = false;
						this.messageDisplayer.displayAppError(error);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
		
	}

	uploadedAttachmentsContainsAttachmentWithName(name: any): boolean {
		return this.uploadedAttachments.find(attachment => {
			return attachment.name === name;
		}) != null ? true : false;	
	}

	private resetSelectedAttachments(): void {
		this.fileUpload.files = [];
	}

	public onRemoveFile(file: File): void{
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.fileUpload.files.push(file);
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
	}

	public onDownloadAttachment(attachment: TaskAttachmentModel): void {
		this.isUpdatingAttachments = true;
		if (ObjectUtils.isNullOrUndefined(attachment.id) ) {	
			this.attachmentService.downloadFile(attachment.name).subscribe(
				(response: HttpResponse<Blob>) => {
					this.isUpdatingAttachments = false;
					DownloadUtils.saveFileFromResponse(response, attachment.name);
				}, 
				(error: any) => {
					this.isUpdatingAttachments = false;
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		} else {
			this.projectService.downloadTaskAttachment(attachment.id).subscribe(
				(response: HttpResponse<Blob>) => {
					this.isUpdatingAttachments = false;
					DownloadUtils.saveFileFromResponse(response, attachment.name);
				}, 
				(error: any) => {
					this.isUpdatingAttachments = false;
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		}
	}

	public updateTaskEventDescription(taskEvent: TaskEventModel): Promise<void> {
		return new Promise<void>((resolve, reject) => {
			this.projectService.updateTaskEventDescription(taskEvent, {
				onSuccess: (): void => {
					this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
					resolve();
				}, onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}

	public hasEditEventDescriptionPermission(): boolean {
		return this.authManager.hasPermission(AdminPermissionEnum.EDIT_TASK_EVENT_DESCRIPTIONS);
	}

	public getParticipationsToDescription(item: SelectItem): string {
		let description: string = this.taskEvents.find(taskEvent => taskEvent.name === item.value).description;
		return description != null ? description : this.translateUtils.translateLabel("NO_DESCRIPTION");
	}

	public onEditTaskEventDescriptions(eventItem: SelectItem, event: MouseEvent): void {
		event.stopPropagation();
		this.descriptionWindowHeader = eventItem.label;
		let taskEventFound: TaskEventModel = this.taskEvents.find(taskEvent => taskEvent.name === eventItem.value);
		this.taskEventDescription = taskEventFound ? taskEventFound.description : null;
		this.displayDescriptionWindow = true;
	}

	public descriptionChanged(): void {
		let eventToUpdate: TaskEventModel = this.taskEvents.find(taskEvent => taskEvent.name === this.descriptionWindowHeader);
		let oldDescription = eventToUpdate.description;
		eventToUpdate.description = this.taskEventDescription;
		this.updateTaskEventDescription(eventToUpdate)
			.catch(() => eventToUpdate.description = oldDescription);
		this.displayDescriptionWindow = false;
	}

	public onProjectChanged(projectId: number): void {
		this.projectSelected.emit(projectId);
		this.prepareSubactivitySelectItems(projectId)
			.then(() => {
				if (["edit", "view"].includes(this.perspective)){
					this.subactivityFormControl.patchValue(this.subactivityFormControl.value);
				}else{
					this.subactivityFormControl.patchValue(null);
				}
			});
	}
	
	public isValid(): boolean {
		return this.isFormValid();
	}

	public isFinalized(): boolean {
		return this.task ? this.task.status === TaskStatus.FINALIZED : false;
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}
	public get explicationMandatory(): Boolean {
		return this.isExplicationMandatory;
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}

	public get projectIdFormControl(): AbstractControl {
		return this.getControlByName("projectId");
	}

	public get subactivityFormControl(): AbstractControl {
		return this.getControlByName("subactivity");
	}

	public get nameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	public get descriptionFormControl(): AbstractControl {
		return this.getControlByName("description");
	}

	public get startDateFormControl(): AbstractControl {
		return this.getControlByName("startDate");
	}

	public get endDateFormControl(): AbstractControl {
		return this.getControlByName("endDate");
	}

	public get priorityFormControl(): AbstractControl {
		return this.getControlByName("priority");
	}

	public get permanentFormControl(): AbstractControl {
		return this.getControlByName("permanent");
	}

	public get participantsToFormControl(): AbstractControl {
		return this.getControlByName("participantsTo");
	}

	public get explicationsFormControl(): AbstractControl {
		return this.getControlByName("explications");
	}
	public get evenimentStartDateFormControl(): AbstractControl {
		return this.getControlByName("evenimentStartDate");
	}

	public get evenimentEndDateFormControl(): AbstractControl {
		return this.getControlByName("evenimentEndDate");
	}

	public get commentsFormControl(): AbstractControl {
		return this.getControlByName("comments");
	}

	public get evenimentStartDate(): Date {
		return this.evenimentStartDateFormControl.value;
	}
	public get startDate(): Date {
		return this.startDateFormControl.value;
	}

	public get evenimentDatesVisible(): boolean {
		if (this.participantsToFormControl.value != null || ["view", "edit"].includes(this.perspective)) {
			return true;
		} else {
			return false;
		}
	}
	
	public get endDateVisible(): boolean {
		if (this.startDateFormControl.value != null && !["view", "edit"].includes(this.perspective)) {
			return true;
		} else {
			return false;
		}
	}
	public onEvenimentStartDateSelectEvent(dataSelected: Date): void {
		this.evenimentEndDateFormControl.setValue(null);
	}
	public onStartDateSelectEvent(dataSelected: Date): void {
		this.endDateFormControl.setValue(null);
	}

	private evenimentEndDateLowerThendStartDate(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			let endDateValue =  control.value;
			let startDateValue = this.evenimentStartDateFormControl.value;
			if (ObjectUtils.isNullOrUndefined(endDateValue) || ObjectUtils.isNullOrUndefined(startDateValue)) {
				return ;
			}
			if (endDateValue < startDateValue) {
				return { endDateLowerThendStartDate: true };				
			}
			return null;
		};
	}
}
