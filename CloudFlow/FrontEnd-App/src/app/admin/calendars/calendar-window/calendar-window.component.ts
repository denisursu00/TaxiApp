import { Component, Input, Output, EventEmitter, ViewChild, OnInit } from "@angular/core";
import { StringValidators, CalendarModel, UiUtils, CalendarService, MessageDisplayer, AppError, BaseWindow } from "@app/shared";
import { CalendarGeneralTabContentComponent } from "./calendar-general-tab-content/calendar-general-tab-content.component";
import { CalendarUsersRightsTabContentComponent } from "./calendar-users-rights-tab-content/calendar-users-rights-tab-content.component";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-calendar-window",
	templateUrl: "./calendar-window.component.html"
})
export class CalendarWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public calendarId: number;

	@Input()
	public mode: "add" | "edit";
	
	@Output()
	public windowClosed: EventEmitter<void>;

	@ViewChild(CalendarGeneralTabContentComponent)
	public generalTab: CalendarGeneralTabContentComponent;

	@ViewChild(CalendarUsersRightsTabContentComponent)
	public usersRightsTab: CalendarGeneralTabContentComponent;

	public calendarService: CalendarService;
	public messageDisplayer: MessageDisplayer;

	public windowVisible: boolean = false;
	
	public tabContentVisible: boolean = false;
	public calendarModel: CalendarModel;

	public constructor(calendarService: CalendarService, messageDisplayer: MessageDisplayer) {
		super();
		this.calendarService = calendarService;
		this.messageDisplayer = messageDisplayer;
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	public ngOnInit(): void {
		this.tabContentVisible = false;
		if (this.mode === "add") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	public init(): void {
	}

	private prepareForAdd(): void {
		this.unlock();
		this.tabContentVisible = true;
		this.windowVisible = true;
	}

	private prepareForEdit(): void {
		this.calendarService.getCalendar(this.calendarId, {
			onSuccess: (calendar: CalendarModel): void => {
				this.calendarModel = calendar;
				this.unlock();
				this.tabContentVisible = true;
				this.windowVisible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onSaveAction(event: any): void {
		if (!this.areTabsValid()) {
			return;
		}
		let calendar: CalendarModel = new CalendarModel();
		this.generalTab.populateForSave(calendar);
		this.usersRightsTab.populateForSave(calendar);

		if (this.mode === "edit") {
			calendar.id = this.calendarId;
		}
		this.lock();
		this.calendarService.saveCalendar(calendar, {
			onSuccess: (): void => {
				this.unlock();
				this.messageDisplayer.displaySuccess("CALENDAR_SUCCESSFULLY_SAVED");
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public areTabsValid(): boolean {
		if (!this.usersRightsTab.isValid()) {
			this.messageDisplayer.displayError("CALENDAR_USERS_RIGHTS_NOT_ADDED");
		}
		return this.generalTab.isValid() && this.usersRightsTab.isValid();
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}
}