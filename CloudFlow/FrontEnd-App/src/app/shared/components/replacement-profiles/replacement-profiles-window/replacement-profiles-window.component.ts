import { Component, Input, EventEmitter, Output, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, AbstractControl, Validators } from "@angular/forms";
import { timeout } from "rxjs/operator/timeout";
import { AclService, OrganizationService, AppConstantsService, ReplacementProfilesService } from "../../../service";
import { MessageDisplayer } from "../../../message-displayer";
import { ConfirmationUtils, ArrayUtils, ObjectUtils } from "../../../utils";
import { DateConstants } from "../../../constants/date.constants";
import {
	UserModel,
	ReplacementProfileModel,
	AppError,
	SecurityManagerModel, 
	ReplacementProfilesOutOfOfficeConstantsModel,
	GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel,
	SaveReplacementProfileRequestModel
} from "../../../model";
import { ReplacementProfileValidators } from "./../replacement-profile-validators";
import { DateUtils } from "../../../utils/date-utils";
import { Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { BaseWindow } from "./../../../base-window";

@Component({
	selector: "app-replacement-profiles-window",
	templateUrl: "./replacement-profiles-window.component.html",
	styleUrls: ["./replacement-profiles-window.component.css"]
})
export class ReplacementProfilesWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public mode: "new" | "edit";

	@Input()
	public replacementProfileId: number;
	
	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public actionPerformed: EventEmitter<void>;

	private aclService: AclService;
	private organizationService: OrganizationService;
	private appConstantsService: AppConstantsService;
	private replacementProfilesService: ReplacementProfilesService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public replacementProfilesForm: FormGroup;
	private formBuilder: FormBuilder;

	public visible: boolean;

	public selectedRequesterUserProfiles: UserModel[];
	public dateFormat: string;
	public yearRange: string;

	public availableReplacementProfilesInWhichRequesterIsReplacement: ReplacementProfileModel[];
	public selectedReplacementProfilesInWhichRequesterIsReplacement: ReplacementProfileModel[];

	public constructor(aclService: AclService, organizationService: OrganizationService, appConstantsService: AppConstantsService,
			replacementProfilesService: ReplacementProfilesService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, formBuilder: FormBuilder) {
		super();
		this.aclService = aclService;
		this.organizationService = organizationService;
		this.appConstantsService = appConstantsService;
		this.replacementProfilesService = replacementProfilesService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.actionPerformed = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.visible = true;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.selectedReplacementProfilesInWhichRequesterIsReplacement = [];
		this.prepareForm();
	}

	public ngOnInit(): void {
		if (this.mode === "new") {
			this.prepareForNew();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	private prepareForm(): void {
		this.replacementProfilesForm = this.formBuilder.group({
			profileId: [null],
			requesterSelector: [""],
			replacementSelector: [""],
			selectedRequesterUserProfiles: [null],
			comments: [""],
			startDate: [null],
			endDate: [null],
			outOfOffice: false,
			outOfOfficeEmailSubjectTemplate: "",
			outOfOfficeEmailBodyTemplate: ""
		}, {
			validator: ReplacementProfileValidators.startDateGreaterThanEndDate("startDate", "endDate")
		});
	}

	private prepareForNew(): void {
		this.prepareControlsDefaultValues();
		this.prepareDefaultOutOfOfficeControlsValues();
	}

	private prepareControlsDefaultValues(): void {
		this.aclService.getSecurityManager({
			onSuccess: (securityManager: SecurityManagerModel): void => {
				this.requesterSelectorControl.setValue(securityManager.userIdAsString);
				this.prepareSelectedRequesterUserProfilesControlValuesByRequesterUsername(securityManager.userUsername);
				if (!securityManager.userAdmin) {
					this.requesterSelectorControl.disable();
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareSelectedRequesterUserProfilesControlValuesByRequesterUsername(selectedRequesterUsername: string): void {
		this.organizationService.getUsersWithUsername(selectedRequesterUsername, {
			onSuccess: (users: UserModel[]): void => {
				this.selectedRequesterUserProfiles = users;
				
				ListItemUtils.sort(this.selectedRequesterUserProfiles, "title");
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareForEdit(): void {
		this.replacementProfilesService.getReplacementProfileById(this.replacementProfileId, {
			onSuccess: (replacementProfile: ReplacementProfileModel): void => {
				this.profileIdControl.setValue(this.replacementProfileId);
				this.replacementSelectorControl.setValue(replacementProfile.replacement.userId);
				this.commentsControl.setValue(replacementProfile.comments);
				this.startDateControl.setValue(replacementProfile.startDate);
				this.endDateControl.setValue(replacementProfile.endDate);
				this.selectedRequesterUserProfilesControl.setValue(replacementProfile.selectedRequesterUserProfiles);
				
				this.outOfOfficeControl.setValue(replacementProfile.outOfOffice);
				if (replacementProfile.outOfOffice) {
					this.outOfOfficeEmailSubjectTemplateControl.setValue(replacementProfile.outOfOfficeEmailSubjectTemplate);
					this.outOfOfficeEmailBodyTemplateControl.setValue(replacementProfile.outOfOfficeEmailBodyTemplate);
				} else {
					this.prepareDefaultOutOfOfficeControlsValues();
				}

				this.prepareSelectedRequesterUserProfilesControlValuesByRequesterUsername(replacementProfile.requesterUsername);
				this.prepareRequesterSelectorForEdit(replacementProfile.requesterUsername);
				this.populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(replacementProfile);
				
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareRequesterSelectorForEdit(selectedRequesterUsername: string): void {
		this.organizationService.getUsersWithUsername(selectedRequesterUsername, {
			onSuccess: (users: UserModel[]): void => {
				users.forEach((user: UserModel) => {
					if (user.userName === selectedRequesterUsername) {
						this.requesterSelectorControl.setValue(user.userId);
					}
				});
				this.aclService.getSecurityManager({
					onSuccess: (securityManager: SecurityManagerModel): void => {
						if (!securityManager.userAdmin) {
							this.requesterSelectorControl.disable();
						}
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareDefaultOutOfOfficeControlsValues(): void {
		this.appConstantsService.getReplacementProfilesOutOfOfficeConstants({
			onSuccess: (replacementProfileConstants: ReplacementProfilesOutOfOfficeConstantsModel): void => {
				this.outOfOfficeEmailSubjectTemplateControl.setValue(replacementProfileConstants.defaultTemplateForEmailSubject);
				this.outOfOfficeEmailBodyTemplateControl.setValue(replacementProfileConstants.defaultTemplateForEmailBody);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(replacementProfile: ReplacementProfileModel): void {
		let idForRequestingReplacementProfile: number = this.profileIdControl.value;
		let idsForRequesterUserProfiles: number[] = [];
		replacementProfile.selectedRequesterUserProfiles.forEach((user: UserModel) => {
			idsForRequesterUserProfiles.push(Number(user.userId));
		});
		let startDate: Date = this.startDateControl.value;
		let endDate: Date = this.endDateControl.value;
		
		if (ArrayUtils.isEmpty(idsForRequesterUserProfiles) || ObjectUtils.isNullOrUndefined(startDate) || ObjectUtils.isNullOrUndefined(endDate)) {
			return;
		}

		let getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel: GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel = new GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel();
		getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel.idForRequestingReplacementProfile = idForRequestingReplacementProfile;
		getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel.idsForRequesterUserProfiles = idsForRequesterUserProfiles;
		getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel.startDate = startDate;
		getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel.endDate = endDate;
		
		this.replacementProfilesService.getAvailableReplacementProfilesInWhichRequesterIsReplacement(getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel, {
			onSuccess: (replacementProfiles: ReplacementProfileModel[]): void => {
				this.availableReplacementProfilesInWhichRequesterIsReplacement = replacementProfiles;
				replacementProfiles.forEach((profile: ReplacementProfileModel) => {
					replacementProfile.idsForSelectedReplacementProfilesInWhichRequesterIsReplacement.forEach((id: number) => {
						if (profile.id === id) {
							this.selectedReplacementProfilesInWhichRequesterIsReplacement.push(profile);							
						}
					});
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onReturnedAction(): void {
		this.confirmationUtils.confirm("CONFIRM_REPLACEMENT_PROFILE_RETURN_OPERATION",  {
			approve: (): void => {
				this.replacementProfilesService.returned(this.replacementProfileId, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("REPLACEMENT_PROFILE_RETURN_OPERATION_SUCCEDED");
						this.actionPerformed.emit();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public onSaveAction(): void {
		if (this.validate()) {
			this.saveProfile();
		}
	}
	
	private saveProfile() {
		let replacementProfile: SaveReplacementProfileRequestModel = new SaveReplacementProfileRequestModel();
		
		replacementProfile.id = this.profileIdControl.value;
		replacementProfile.requesterId = Number(this.requesterSelectorControl.value);
		replacementProfile.replacementUserId = Number(this.replacementSelectorControl.value);
		replacementProfile.selectedRequesterUserProfiles = this.selectedRequesterUserProfilesControl.value;
		replacementProfile.comments = this.commentsControl.value;
		replacementProfile.startDate = this.startDateControl.value;
		replacementProfile.endDate = this.endDateControl.value;
		
		this.selectedReplacementProfilesInWhichRequesterIsReplacement.forEach((profile: ReplacementProfileModel) => {
			replacementProfile.idsForSelectedReplacementProfilesInWhichRequesterIsReplacement.push(profile.id);
		});

		replacementProfile.outOfOffice = this.isOutOfOfficeChecked();
		if (this.isOutOfOfficeChecked()) {
			replacementProfile.outOfOfficeEmailSubjectTemplate = this.outOfOfficeEmailSubjectTemplateControl.value;
			replacementProfile.outOfOfficeEmailBodyTemplate = this.outOfOfficeEmailBodyTemplateControl.value;
		}
		let requesterId: string = this.requesterSelectorControl.value;
		let replacementUserId: string = this.replacementSelectorControl.value;

		this.replacementProfilesService.saveReplacementProfile(replacementProfile, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("REPLACEMENT_PROFILE_SAVED");
				this.actionPerformed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onCancelAction(): void {
		this.visible = false;
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.onCancelAction();
	}

	public onSelectedRequesterUserProfilesChanged(userId: string): void {
		this.organizationService.getUserById(userId, {
			onSuccess: (user: UserModel): void => {
				this.prepareSelectedRequesterUserProfilesControlValuesByRequesterUsername(user.userName);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private validate(): boolean {
		if (!this.replacementProfilesForm.valid) {
			if (!this.replacementSelectorControl.valid) {
				this.replacementSelectorControl.markAsTouched();
			}
			if (!this.selectedRequesterUserProfilesControl.valid) {
				this.selectedRequesterUserProfilesControl.markAsTouched();
			}
			if (!this.startDateControl.valid) {
				this.startDateControl.markAsTouched();
			}
			if (!this.endDateControl.valid) {
				this.endDateControl.markAsTouched();
			}
			if (!this.selectedRequesterUserProfilesControl.valid) {
				this.selectedRequesterUserProfilesControl.markAsTouched();
			}
			if (this.isOutOfOfficeChecked()) {
				if (!this.outOfOfficeEmailSubjectTemplateControl.valid) {
					this.outOfOfficeEmailSubjectTemplateControl.markAsTouched();
				}
				if (!this.outOfOfficeEmailSubjectTemplateControl.valid) {
					this.outOfOfficeEmailSubjectTemplateControl.markAsTouched();
				}
			}
			return false;
		}
		return true;
	}

	public isModeNew(): boolean {
		return this.mode === "new";
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.replacementProfilesForm.controls[controlName];
	}
	
	public get profileIdControl(): AbstractControl {
		return this.getControlByName("profileId");
	}
	
	public get requesterSelectorControl(): AbstractControl {
		return this.getControlByName("requesterSelector");
	}
	
	public get commentsControl(): AbstractControl {
		return this.getControlByName("comments");
	}
	
	public get replacementSelectorControl(): AbstractControl {
		return this.getControlByName("replacementSelector");
	}
	
	public get selectedRequesterUserProfilesControl(): AbstractControl {
		return this.getControlByName("selectedRequesterUserProfiles");
	}
	
	public get startDateControl(): AbstractControl {
		return this.getControlByName("startDate");
	}
	
	public get outOfOfficeEmailSubjectTemplateControl(): AbstractControl {
		return this.getControlByName("outOfOfficeEmailSubjectTemplate");
	}
	
	public get outOfOfficeEmailBodyTemplateControl(): AbstractControl {
		return this.getControlByName("outOfOfficeEmailBodyTemplate");
	}
	
	public get endDateControl(): AbstractControl {
		return this.getControlByName("endDate");
	}
	
	public get outOfOfficeControl(): AbstractControl {
		return this.getControlByName("outOfOffice");
	}

	public isOutOfOfficeChecked(): boolean {
		return this.getControlByName("outOfOffice").value;
	}
}
