import { Component, OnInit, Input, Output, EventEmitter, SystemJsNgModuleLoader } from "@angular/core";
import { FormGroup, FormBuilder, Validators, AbstractControl } from "@angular/forms";
import { StringValidators, UiUtils, ObjectUtils, AclService, MessageDisplayer, SecurityManagerModel, AppError, CalendarRepeatEvent, CalendarEventType, 
		FormUtils, CalendarService, CalendarEventModel, BirthdayCalendarEventModel, MeetingCalendarEventModel, HolidayCalendarEventModel,
		ConfirmationUtils, 
		CalendarModel,
		CalendarUserRightsModel,
		ArrayUtils,
		StringUtils,
		DateConstants,
		ConfirmationWindowFacade,
		TranslateUtils,
		BaseWindow } from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";
import { CalendarEventWrapperModel } from "@app/shared/model/calendar/calendar-event-wrapper.model";
import { AuditCalendarEventModel } from "@app/shared/model/calendar/audit-calendar-event.model";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-calendar-event-window",
	templateUrl: "./calendar-event-window.component.html"
})
export class CalendarEventWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public eventId: number;

	@Input()
	public mode: "add" | "edit" | "view";

	@Output()
	public windowClosed: EventEmitter<void>;

	private activeEvents: Map<string, string>;

	private calendarService: CalendarService;
	private aclService: AclService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;
	
	public form: FormGroup;
	public formBuilder: FormBuilder;

	public loggedInUserId: number;
	public calendarSelectorVisible: boolean = false;

	public showTimeForStartDateAndEndDate: boolean = true;
	
	public selectedEventType: String;

	public eventTypeSelectItems: SelectItem[];
	public reminderSelectItems: SelectItem[];
	public repeatSelectItems: SelectItem[];

	public saveButtonEnabled: boolean;
	public deleteButtonEnabled: boolean;

	public minStartDate: Date;
	public minEndDate: Date;
	private isReadOnly: boolean = false;
	public isLocationFormControlVisible: boolean = false;
	public isAllDayFormControlVisible: boolean = false;
	public isStartDateFormControlVisible: boolean = false;
	public isEndDateFormControlVisible: boolean = false;
	public isReprezentantExternFormControlVisible: boolean = false;
	public isBirthdateFormControlVisible: boolean = false;
	public isReminderInMinutesFormControlVisible: boolean = false;
	public isRepeatFormControlVisible: boolean = false;
	public isAttendeesFormControlVisible: boolean = false;
	public isUserIdFormControlVisible: boolean = false;
	public dateFormat: string;

	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(calendarService: CalendarService, aclService: AclService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, 
			formBuilder: FormBuilder) {
		super();
		this.calendarService = calendarService;
		this.aclService = aclService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.init();
	}

	public init(): void {

		this.saveButtonEnabled = false;
		this.deleteButtonEnabled = false;

		this.activeEvents = new Map();
		
		this.minStartDate = new Date();
		this.minEndDate = new Date();

		this.prepareForm();
		this.prepareEventTypeSelectItems();
		this.prepareReminderSelectItems();
		this.prepareRepeatSelectItems();
		this.getLoggedInUserForLoadingCalendars();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			calendarIds: [[], Validators.required],
			subject: [null, [Validators.required, StringValidators.blank()]],
			location: [null],
			description: [null],
			startDate: [null],
			endDate: [null],
			birthdate: [null],
			allDay: [false],
			attendees: [null],
			userId: [null],
			reminderInMinutes: [null],
			repeat: [null],
			reprezentantExtern: [null]
		});
	}

	private prepareEventTypeSelectItems(): void {
		this.eventTypeSelectItems = [
			{label: this.translateUtils.translateLabel("EVENT_TYPE_MEETING"), value: CalendarEventType.MEETING},
			{label: this.translateUtils.translateLabel("EVENT_TYPE_BIRTHDAY"), value: CalendarEventType.BIRTHDAY}

		];

		ListItemUtils.sortByLabel(this.eventTypeSelectItems);
	}

	private prepareRepeatSelectItems(): void {
		this.repeatSelectItems = [
			{label: "", value: null},
			{label: this.translateUtils.translateLabel("EVENT_REPEAT_DAILY"), value: CalendarRepeatEvent.DAY},
			{label: this.translateUtils.translateLabel("EVENT_REPEAT_WEEKLY"), value: CalendarRepeatEvent.WEEK},
			{label: this.translateUtils.translateLabel("EVENT_REPEAT_MONTHLY"), value: CalendarRepeatEvent.MOUNTH},
			{label: this.translateUtils.translateLabel("EVENT_REPEAT_YEARLY"), value: CalendarRepeatEvent.YEAR},
		];

		ListItemUtils.sortByLabel(this.repeatSelectItems);
	}

	private prepareReminderSelectItems(): void {
		this.reminderSelectItems = [
			{label: "", value: null},
			{label: "10 min", value: 10},
			{label: "20 min", value: 20},
			{label: "50 min", value: 50},
			{label: "1 h", value: 60},
			{label: "2 h", value: 120},
			{label: "5 h", value: 300}
		];

		ListItemUtils.sortByValue(this.reminderSelectItems, false, true);
	}
	
	public getLoggedInUserForLoadingCalendars() {
		this.aclService.getSecurityManager({
			onSuccess: (userSecurity: SecurityManagerModel): void => {
				this.loggedInUserId = Number(userSecurity.userIdAsString);
				this.calendarSelectorVisible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.mode)) {
			throw new Error("Input property [mode] cannot be null or undefined.");
		}
		
		this.windowVisible = false;
		if (this.isAdd()) {
			this.prepareForAdd();
			this.onCalendarIdsFormControlChanges();
		} else if (this.isEdit() || this.isView()) {
			this.prepareForViewOrEdit();
		}
	}

	private prepareForAdd(): void {
		this.reset();
		this.selectedEventType = CalendarEventType.MEETING;
		this.changeFormPerspectiveForMeetingEvent();
		this.unlock();
		this.windowVisible = true;
		this.isReprezentantExternFormControlVisible = false;
	}

	private prepareForViewOrEdit(): void {
		this.reset();
		this.prepareCalendarEvent();
	}

	private prepareCalendarEvent(): void {
		this.calendarService.getCalendarEvent(this.eventId, {
			onSuccess: (calendarEventWrapperModel: CalendarEventWrapperModel): void => {
				this.prepareFormFromCalendarEventModel(calendarEventWrapperModel.calendarEvent);
				this.minEndDate = this.startDateFormControl.value;
				this.changeButtonsPerspectiveByCalendarEvent(calendarEventWrapperModel.calendarEvent);
				if (this.isReadOnly) {
					this.setReadOlnyComponents();
				}
				this.unlock();
				this.windowVisible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.windowVisible = true;
			}
		});
	}

	private changeButtonsPerspectiveByCalendarEvent(calendarEvent: CalendarEventModel): void {
		
		if (this.isView()) {
			this.saveButtonEnabled = false;
			this.deleteButtonEnabled = false;
			this.isReadOnly = true;
			return;
		}

		let hasDocument: boolean = false;
		if (calendarEvent.type === CalendarEventType.HOLIDAY) {
			hasDocument = (<HolidayCalendarEventModel> calendarEvent).hasDocument;
		} else if (calendarEvent.type === CalendarEventType.AUDIT) {
			hasDocument = (<AuditCalendarEventModel> calendarEvent).hasDocument;
		} else if (calendarEvent.type === CalendarEventType.MEETING) {
			hasDocument = (<MeetingCalendarEventModel> calendarEvent).hasDocument;
		}  
		if (hasDocument) {
			this.saveButtonEnabled = false;
			this.deleteButtonEnabled = true;
			this.isReadOnly = true;
		} else 	if (calendarEvent.type === CalendarEventType.HOLIDAY) {
			this.saveButtonEnabled = false;
			this.deleteButtonEnabled = false;
		} else if (calendarEvent.type === CalendarEventType.BIRTHDAY || calendarEvent.type === CalendarEventType.MEETING ||
				calendarEvent.type === CalendarEventType.AUDIT) {
			this.prepareButtonsPerspectiveByCalendarUserRights(calendarEvent.calendarId);
		}
		
	}

	public onCalendarIdsFormControlChanges(): void {
		this.calendarIdsFormControl.valueChanges.subscribe((calendarIds: number[]) => {
			if (ArrayUtils.isEmpty(calendarIds)) {
				return;
			}
			this.prepareButtonsPerspectiveByCalendarUserRights(calendarIds[0]);
		});
	}

	private prepareButtonsPerspectiveByCalendarUserRights(calendarId: number): void {
		
		if (this.isView()) {
			this.saveButtonEnabled = false;
			this.deleteButtonEnabled = false;
			this.isReadOnly = true;
			return;
		}

		if (this.selectedEventType === CalendarEventType.HOLIDAY) {
			this.saveButtonEnabled = false;
			this.deleteButtonEnabled = false;
			return;
		}

		this.deleteButtonEnabled = false;
		this.saveButtonEnabled = false;

		this.calendarService.getCalendar(calendarId, {
			onSuccess: (calendar: CalendarModel): void => {
				if (calendar.permitAll) {
					this.saveButtonEnabled = true;
					if (this.isEdit()) {
						this.deleteButtonEnabled = true;
					}
				}
				calendar.usersRights.forEach((userRights: CalendarUserRightsModel) => {
					if (userRights.user.id === this.loggedInUserId) {
						if (this.mode === "add") {
							this.deleteButtonEnabled = false;
							if (userRights.add) {
								this.saveButtonEnabled = true;
							}
						} else if (this.mode === "edit") {
							if (userRights.edit) {
								this.saveButtonEnabled = true;
							}
							if (userRights.delete) {
								this.deleteButtonEnabled = true;
							}
						}
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	private setReadOlnyComponents(): void {
		this.calendarIdsFormControl.disable();
		this.subjectFormControl.disable();
		this.descriptionFormControl.disable();
		this.birthdateFormControl.disable();
		this.userIdFormControl.disable();
		this.attendeesFormControl.disable();
		this.startDateFormControl.disable();
		this.endDateFormControl.disable();
		this.locationFormControl.disable();
		this.allDayFormControl.disable();
		this.reminderInMinutesFormControl.disable();
		this.repeatFormControl.disable();
		this.reprezentantExternFormControl.disable();
	}

	private  prepareFormFromCalendarEventModel(calendarEventModel: CalendarEventModel): void {
		if (calendarEventModel instanceof BirthdayCalendarEventModel) {
			this.selectedEventType = CalendarEventType.BIRTHDAY;
			this.subjectFormControl.setValue(calendarEventModel.subject);
			this.descriptionFormControl.setValue(calendarEventModel.description);
			this.birthdateFormControl.setValue(calendarEventModel.birthdate);
			this.calendarIdsFormControl.setValue([calendarEventModel.calendarId]);
			this.changeFormPerspectiveForBirthdayEvent();
		} else if (calendarEventModel instanceof MeetingCalendarEventModel) {
			this.selectedEventType = CalendarEventType.MEETING;
			this.descriptionFormControl.setValue(calendarEventModel.description);
			this.subjectFormControl.setValue(calendarEventModel.subject);
			this.startDateFormControl.setValue(calendarEventModel.startDate);
			this.endDateFormControl.setValue(calendarEventModel.endDate);
			this.attendeesFormControl.setValue(calendarEventModel.attendees);
			this.calendarIdsFormControl.setValue([calendarEventModel.calendarId]);
			this.locationFormControl.setValue(calendarEventModel.location);
			this.reminderInMinutesFormControl.setValue(calendarEventModel.reminderMinutes);
			this.repeatFormControl.setValue(calendarEventModel.repeat);
			
			this.allDayFormControl.setValue(calendarEventModel.allDay);
			this.showTimeForStartDateAndEndDate = true;
			if (calendarEventModel.allDay) {
				this.showTimeForStartDateAndEndDate = false;
			}

			this.changeFormPerspectiveForMeetingEvent();
			
			if (StringUtils.isNotBlank(calendarEventModel.reprezentantExtern)) {
				this.isReprezentantExternFormControlVisible = true;
				this.reprezentantExternFormControl.setValue(calendarEventModel.reprezentantExtern);
			}
		} else if (calendarEventModel instanceof AuditCalendarEventModel) {
			this.selectedEventType = CalendarEventType.AUDIT;
			this.descriptionFormControl.setValue(calendarEventModel.description);
			this.subjectFormControl.setValue(calendarEventModel.subject);
			this.startDateFormControl.setValue(calendarEventModel.startDate);
			this.endDateFormControl.setValue(calendarEventModel.endDate);
			this.attendeesFormControl.setValue(calendarEventModel.attendees);
			this.calendarIdsFormControl.setValue([calendarEventModel.calendarId]);
			this.locationFormControl.setValue(calendarEventModel.location);
			
			this.allDayFormControl.setValue(calendarEventModel.allDay);
			this.showTimeForStartDateAndEndDate = true;
			if (calendarEventModel.allDay) {
				this.showTimeForStartDateAndEndDate = false;
			}

			this.changeFormPerspectiveForAuditEvent();
		} else if (calendarEventModel instanceof HolidayCalendarEventModel) {
			this.selectedEventType = CalendarEventType.HOLIDAY;
			this.subjectFormControl.setValue(calendarEventModel.subject);
			this.descriptionFormControl.setValue(calendarEventModel.description);
			this.startDateFormControl.setValue(calendarEventModel.startDate);
			this.endDateFormControl.setValue(calendarEventModel.endDate);
			this.allDayFormControl.setValue(calendarEventModel.allDay);
			this.calendarIdsFormControl.setValue([calendarEventModel.calendarId]);
			this.userIdFormControl.setValue(calendarEventModel.userId);
			this.changeFormPerspectiveForHolidayEvent();
		}
	}

	public onEventTypeChanged(): void {

		this.reset();
		if (this.isBirthday()) {
			this.changeFormPerspectiveForBirthdayEvent();
		} else if (this.isHoliday()) {
			this.changeFormPerspectiveForHolidayEvent();
		} else if (this.isMeeting()) {
			this.changeFormPerspectiveForMeetingEvent();
		} else if (this.isAudit()) {
			this.changeFormPerspectiveForAuditEvent();
		}
	}

	private reset(): void {

		this.form.reset();
		this.isBirthdateFormControlVisible = false;
		this.birthdateFormControl.clearValidators();
		this.birthdateFormControl.updateValueAndValidity();
		this.isUserIdFormControlVisible = false;
		this.userIdFormControl.clearValidators();
		this.userIdFormControl.updateValueAndValidity();
		this.isAttendeesFormControlVisible = false;
		this.attendeesFormControl.clearValidators();
		this.attendeesFormControl.updateValueAndValidity();
		this.isStartDateFormControlVisible = false;
		this.startDateFormControl.clearValidators();
		this.startDateFormControl.updateValueAndValidity();
		this.isEndDateFormControlVisible = false;
		this.endDateFormControl.clearValidators();
		this.endDateFormControl.updateValueAndValidity();
		this.isLocationFormControlVisible = false;
		this.locationFormControl.clearValidators();
		this.locationFormControl.updateValueAndValidity();
		this.isAllDayFormControlVisible = false;
		this.allDayFormControl.clearValidators();
		this.allDayFormControl.updateValueAndValidity();
		this.isReminderInMinutesFormControlVisible = false;
		this.reminderInMinutesFormControl.clearValidators();
		this.reminderInMinutesFormControl.updateValueAndValidity();
		this.isRepeatFormControlVisible = false;
		this.repeatFormControl.clearValidators();
		this.repeatFormControl.updateValueAndValidity();
	}

	private changeFormPerspectiveForBirthdayEvent(): void {
		this.isBirthdateFormControlVisible = true;
		this.birthdateFormControl.setValidators(Validators.required);
	}

	private changeFormPerspectiveForHolidayEvent(): void {
		this.isStartDateFormControlVisible = true;
		this.startDateFormControl.setValidators(Validators.required);
		this.isEndDateFormControlVisible = true;
		this.endDateFormControl.setValidators(Validators.required);
		this.isUserIdFormControlVisible = true;
		this.userIdFormControl.setValidators(Validators.required);
	}

	private changeFormPerspectiveForMeetingEvent(): void {
		this.isAttendeesFormControlVisible = true;
		this.attendeesFormControl.setValidators(Validators.required);
		this.isStartDateFormControlVisible = true;
		this.startDateFormControl.setValidators(Validators.required);
		this.isEndDateFormControlVisible = true;
		this.endDateFormControl.setValidators(Validators.required);
		this.isLocationFormControlVisible = true;
		this.locationFormControl.setValidators([Validators.required, StringValidators.blank]);
		this.isAllDayFormControlVisible = true;
		this.isReminderInMinutesFormControlVisible = true;
		this.isRepeatFormControlVisible = true;
	}

	private changeFormPerspectiveForAuditEvent(): void {
		this.isAttendeesFormControlVisible = true;
		this.attendeesFormControl.setValidators(Validators.required);
		this.isStartDateFormControlVisible = true;
		this.startDateFormControl.setValidators(Validators.required);
		this.isEndDateFormControlVisible = true;
		this.endDateFormControl.setValidators(Validators.required);
		this.isLocationFormControlVisible = true;
		this.locationFormControl.setValidators([Validators.required, StringValidators.blank]);
		this.isAllDayFormControlVisible = true;
	}

	public onAllDayChanged(allDay: boolean): void {
		this.showTimeForStartDateAndEndDate = true;
		if (allDay) {
			this.showTimeForStartDateAndEndDate = false;
		}
	}

	public onSaveAction(): void {
		if (!this.isFormValid()) {
			return;
		}
		this.calendarService.saveEvent(this.prepareCalendarEventWrapperModelFromForm(), {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("CALENDAR_EVENT_SAVED");
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public prepareCalendarEventWrapperModelFromForm(): CalendarEventWrapperModel {
		let calendarEventWrapperModel: CalendarEventWrapperModel = new CalendarEventWrapperModel();
		if (this.isBirthday()) {
			let birthdayCalendarEventModel: BirthdayCalendarEventModel = new BirthdayCalendarEventModel();
			birthdayCalendarEventModel.id = this.eventId;
			birthdayCalendarEventModel.description = this.descriptionFormControl.value;
			birthdayCalendarEventModel.subject = this.subjectFormControl.value;
			birthdayCalendarEventModel.allDay = true;
			birthdayCalendarEventModel.birthdate = this.birthdateFormControl.value;
			birthdayCalendarEventModel.calendarId = this.calendarIdsFormControl.value[0];
			birthdayCalendarEventModel.type = CalendarEventType.BIRTHDAY;
			calendarEventWrapperModel.calendarEvent = birthdayCalendarEventModel;
		} else if (this.isMeeting()) {
			let meetingCalendarEventModel: MeetingCalendarEventModel = new MeetingCalendarEventModel();
			meetingCalendarEventModel.id = this.eventId;
			meetingCalendarEventModel.description = this.descriptionFormControl.value;
			meetingCalendarEventModel.subject = this.subjectFormControl.value;
			meetingCalendarEventModel.endDate = this.endDateFormControl.value;
			meetingCalendarEventModel.startDate = this.startDateFormControl.value;
			meetingCalendarEventModel.allDay = this.allDayFormControl.value;
			meetingCalendarEventModel.attendees = this.attendeesFormControl.value;
			meetingCalendarEventModel.calendarId = this.calendarIdsFormControl.value[0];
			meetingCalendarEventModel.location = this.locationFormControl.value;
			meetingCalendarEventModel.reminderMinutes = this.reminderInMinutesFormControl.value;
			meetingCalendarEventModel.repeat = this.repeatFormControl.value;
			meetingCalendarEventModel.type = CalendarEventType.MEETING;
			calendarEventWrapperModel.calendarEvent = meetingCalendarEventModel;
		}

		return calendarEventWrapperModel;
	}

	public onDeleteAction(): void {
		this.confirmationWindow.confirm({			
			approve: (): void => {
				this.lock();
				this.calendarService.deleteEvent(this.eventId, {
					onSuccess: (): void => {
						this.unlock();
						this.messageDisplayer.displaySuccess("EVENT_DELETED");
						this.windowClosed.emit();
					},
					onFailure: (appError: AppError): void => {
						this.unlock();
						this.messageDisplayer.displayAppError(appError);
					}
				});
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "CONFIRM_DELETE_EVENT");
	}

	public onStartDateSelected(): void {
		this.endDateFormControl.setValue(null);
		this.minEndDate = this.startDateFormControl.value;
	}

	public onCancelAction(): void {
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public isValid(): boolean {
		return this.isFormValid();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	public isBirthday(): boolean {
		return this.selectedEventType === CalendarEventType.BIRTHDAY;
	}

	public isMeeting(): boolean {
		return this.selectedEventType === CalendarEventType.MEETING;
	}

	public isAudit(): boolean {
		return this.selectedEventType === CalendarEventType.AUDIT;
	}

	public isHoliday(): boolean {
		return this.selectedEventType === CalendarEventType.HOLIDAY;
	}

	public isAdd(): boolean {
		return this.mode === "add";
	}

	public isEdit(): boolean {
		return this.mode === "edit";
	}

	public isView(): boolean {
		return this.mode === "view";
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}

	public get subjectFormControl(): AbstractControl {
		return this.getControlByName("subject");
	}

	public get locationFormControl(): AbstractControl {
		return this.getControlByName("location");
	}

	public get descriptionFormControl(): AbstractControl {
		return this.getControlByName("description");
	}

	public get calendarIdsFormControl(): AbstractControl {
		return this.getControlByName("calendarIds");
	}

	public get startDateFormControl(): AbstractControl {
		return this.getControlByName("startDate");
	}

	public get endDateFormControl(): AbstractControl {
		return this.getControlByName("endDate");
	}

	public get birthdateFormControl(): AbstractControl {
		return this.getControlByName("birthdate");
	}

	public get attendeesFormControl(): AbstractControl {
		return this.getControlByName("attendees");
	}

	public get userIdFormControl(): AbstractControl {
		return this.getControlByName("userId");
	}

	public get reminderInMinutesFormControl(): AbstractControl {
		return this.getControlByName("reminderInMinutes");
	}

	public get repeatFormControl(): AbstractControl {
		return this.getControlByName("repeat");
	}

	public get allDayFormControl(): AbstractControl {
		return this.getControlByName("allDay");
	}

	public get reprezentantExternFormControl(): AbstractControl {
		return this.getControlByName("reprezentantExtern");
	}

	public get readOnly(): boolean {
		return this.isReadOnly;
	}
}
