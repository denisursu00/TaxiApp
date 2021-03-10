import { Component, OnInit } from "@angular/core";
import { CalendarService, CalendarModel, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils } from "@app/shared";

@Component({
	selector: "app-calendar",
	templateUrl: "./calendars.component.html"
})
export class CalendarsComponent {

	private calendarService: CalendarService;
	private messageDisplyer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public calendars: CalendarModel[];
	public selectedCalendar: CalendarModel;
	public calendarId: number;
	public dataLoading: boolean;

	public isAddEnabled: boolean;
	public isEditEnabled: boolean;
	public isRemoveEnabled: boolean;

	public calendarWindowMode: "add" | "edit";
	public calendarWindowVisible: boolean;

	public scrollHeight: string;

	public constructor(calendarService: CalendarService, messageDisplyer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.calendarService = calendarService;
		this.messageDisplyer = messageDisplyer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init(): void {
		this.isAddEnabled = true;
		this.isEditEnabled = false;
		this.isRemoveEnabled = false;
		this.calendarWindowVisible = false;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadAllCalendars();
	}

	private loadAllCalendars(): void {
		this.dataLoading = true;
		this.calendarService.getAllCalendars({
			onSuccess: (calnedars: CalendarModel[]): void => {
				this.calendars = calnedars;
				this.dataLoading = false;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplyer.displayAppError(appError);
				this.dataLoading = false;
			}
		});
	}

	public onAdd(): void {
		this.calendarWindowMode = "add";
		this.calendarWindowVisible = true;
	}

	public onEdit(): void {
		this.calendarWindowMode = "edit";
		this.calendarId = this.selectedCalendar.id;
		this.calendarWindowVisible = true;
	}

	public onRemove(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_CALENDAR", {
			approve: (): void => {
				this.deleteSelectedCalendar();
			},
			reject: (): void => {}
		});
	}

	private deleteSelectedCalendar(): void {
		this.calendarService.deleteCalendar(this.selectedCalendar.id, {
			onSuccess: (): void => {
				this.messageDisplyer.displaySuccess("CALENDAR_DELETED");
				this.loadAllCalendars();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplyer.displayAppError(appError);
			}
		});
	}

	public calendarWindowClosed(): void {
		this.loadAllCalendars();
		this.calendarWindowVisible = false;
	}

	public onCalendarSelected(event: any): void {
		this.changePerspective();
	}

	public onCalendarUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.isEditEnabled = false;
		this.isRemoveEnabled = false;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedCalendar)) {
			this.isEditEnabled = true;
			this.isRemoveEnabled = true;
		}
	}
}
