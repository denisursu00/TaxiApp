import { Component, Input, OnInit } from "@angular/core";
import { AclService, AppError, ArrayUtils, BirthdayCalendarEventModel, CalendarEvent, CalendarEventModel, CalendarService, CalendarView, DateUtils, GetCalendarsEventsRequestModel, ComponentPermissionsWrapper, BaseWindow } from "@app/shared";
import { MessageDisplayer, NomenclatorConstants, NomenclatorService, NomenclatorValueModel, ObjectUtils, SecurityManagerModel, TranslateUtils, UiUtils, StringUtils, ClientPermissionEnum } from "@app/shared";
import { CalendarEventsWrapperModel } from "@app/shared/model/calendar/calendar-events-wrapper.model";
import { IntervalCalendarEvent } from "@app/shared/model/calendar/interval-calendar-event.model";
import { LangChangeEvent } from "@ngx-translate/core";
import "fullcalendar";
import * as moment from "moment";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-calendar-manager",
	templateUrl: "./calendar-manager.component.html"
})
export class CalendarManagerComponent implements OnInit, ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_CALEDAR;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_CALEDAR;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_CALEDAR;

	private static zileLibereLegale: string[] = [];
	
	private static readonly CALENDAR_DAY_BACKGROUND_COLOR_FOR_HOLIDAY_OR_WEEKEND = "#EC7063";
	
	@Input()
	public aspectRatio: number;

	private calendarService: CalendarService;
	private nomenclatorService: NomenclatorService;
	private aclService: AclService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private authManager: AuthManager;

	private view: CalendarView;
	private calendarIds: number[];

	public header: Object;
	public options: Object;
	public locale: string;
	public businessHours: Object;

	public events: CalendarEvent[];

	public loggedInUserId: number;

	public scheduleReady: boolean;

	public calendarEventWindowVisible: boolean = false;
	public calendarEventWindowMode: "add" | "edit" | "view";
	public calendarEventWindowEventId: number;

	public constructor(calendarService: CalendarService, nomenclatorService: NomenclatorService, aclService: AclService, messageDisplayer: MessageDisplayer,
			translateUtils: TranslateUtils, authManager:  AuthManager) {
		this.calendarService = calendarService;
		this.nomenclatorService = nomenclatorService;
		this.aclService = aclService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.authManager = authManager;
		this.init();
	}

	private static buildDataZiLiberaLegalaAsString(luna: number, zi: number, an: number): string {
		return luna + "-" + zi + "-" + an;
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.aspectRatio)) {
			this.aspectRatio = UiUtils.getAspectRation();
		}
		this.loggedInUserId = this.authManager.getLoggedInUser().id;
		this.loadZileLibereLegale();
	}

	private init(): void {
		this.scheduleReady = false;
		this.initCalendar();
	}

	public initCalendar(): void {
		this.header = {
			left: "today prev, next",
			center: "title",
			right: "month, agendaWeek, listWeek"
		};
		
		this.locale = this.translateUtils.getDefaultLang();

		this.translateUtils.onLangChangedHandler((event: LangChangeEvent): void => {
			this.locale = event.lang;
		});

		this.options = {
			slotLabelFormat: "HH:mm",
			nowIndicator: "true"			
		};
	}

	public onViewRender(event: any): void {
		this.view = event.view;
		this.prepareCalendarsEvents();
	}

	public calendarsSelectionChanged(event: any): void {
		this.calendarIds = event;
		this.prepareCalendarsEvents();
	}

	private prepareCalendarsEvents(): void {
		if (ObjectUtils.isNullOrUndefined(this.view)) {
			return;
		}

		let requestModel: GetCalendarsEventsRequestModel = this.prepareGetCalendarsEventsRequestModel(this.view, this.calendarIds);
		this.calendarService.getCalendarsEvents(requestModel, {
			onSuccess: (calendarEventWrapperModel: CalendarEventsWrapperModel): void => {
				calendarEventWrapperModel.calendarEvents.forEach((event: BirthdayCalendarEventModel) => {
					if (event.type === "BIRTHDAY") {
						event.birthdateEvent = new Date();
						event.birthdateEvent.setDate(event.birthdate.getDate());
						event.birthdateEvent.setMonth(event.birthdate.getMonth());
						
						if (requestModel.startDate.getMonth() > event.birthdate.getMonth()) {
							event.birthdateEvent.setFullYear(requestModel.endDate.getFullYear());
						} else {
							event.birthdateEvent.setFullYear(requestModel.startDate.getFullYear());
						}
						if (event.birthdate.getFullYear() > requestModel.startDate.getFullYear()) {
							event.birthdateEvent.setFullYear(event.birthdate.getFullYear());
						}
					}	
				});

				this.events = [...this.prepareCalendarEvents(calendarEventWrapperModel.calendarEvents)];		
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private loadZileLibereLegale(): void {
		if (ArrayUtils.isEmpty(CalendarManagerComponent.zileLibereLegale)){
			this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_ZILE_LIBERE_LEGALE, {
				onSuccess: (nomenclatorValues: NomenclatorValueModel[]): void => {
					nomenclatorValues.forEach((nomenclatorValue: NomenclatorValueModel) => {
						let an = new Date().getFullYear();
						let luna = nomenclatorValue[NomenclatorConstants.ZILE_LIBERE_LEGALE_ATTR_KEY_LUNA];
						let zi = nomenclatorValue[NomenclatorConstants.ZILE_LIBERE_LEGALE_ATTR_KEY_ZI];
						CalendarManagerComponent.zileLibereLegale.push(CalendarManagerComponent.buildDataZiLiberaLegalaAsString(Number.parseInt(luna), Number.parseInt(zi), an));
					});

					this.scheduleReady = true;
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		} else {
			this.scheduleReady = true;
		}
	}

	public onDayClick(event: any): void {
		if (this.isAddPermissionAllowed()) {
			this.calendarEventWindowEventId = null;
			this.calendarEventWindowVisible = true;
			this.calendarEventWindowMode = "add";
		}		
	}

	public onEventClick(event: any): void {
		this.calendarEventWindowEventId = event.calEvent.id;
		this.calendarEventWindowMode = "view";
		if (this.isEditPermissionAllowed()) {
			this.calendarEventWindowMode = "edit";
		}
		this.calendarEventWindowVisible = true;
	}

	public onCalendarEventWindowClosed(): void {
		this.prepareCalendarsEvents();
		this.calendarEventWindowVisible = false;
	}

	private prepareGetCalendarsEventsRequestModel(view: CalendarView, calendarIds: number[]): GetCalendarsEventsRequestModel {
		let requestModel: GetCalendarsEventsRequestModel = new GetCalendarsEventsRequestModel();

		if (ObjectUtils.isNullOrUndefined(calendarIds)) {
			requestModel.calendarIds = [];
		} else {
			requestModel.calendarIds = calendarIds;
		}

		requestModel.startDate = view.start.toDate();
		requestModel.endDate = view.end.toDate();

		return requestModel;
	}

	private prepareCalendarEvents(eventModels: CalendarEventModel[]): CalendarEvent[] {
		let events: CalendarEvent[] = [];
		eventModels.forEach((eventModel: CalendarEventModel) => {
			let calendarEvent = this.transformCalendarEventModelToCalendarEvent(eventModel);
			if (calendarEvent !== null) {
				events.push(calendarEvent);
			}
		});
		return events;
	}

	private transformCalendarEventModelToCalendarEvent(eventModel: CalendarEventModel): CalendarEvent {

		let event: CalendarEvent = new CalendarEvent();
		event.id = eventModel.id.toString();
		event.title = eventModel.subject;
		event.backgroundColor = eventModel.color;
		event.allDay = eventModel.allDay;

		if (eventModel instanceof BirthdayCalendarEventModel) {
			event.start = moment((<BirthdayCalendarEventModel>eventModel).birthdateEvent);
			event.end = moment((<BirthdayCalendarEventModel>eventModel).birthdateEvent);
		} else {
			event.start = moment((<IntervalCalendarEvent>eventModel).startDate);
			event.end = moment((<IntervalCalendarEvent>eventModel).endDate);
		}
		// if (event.start.isSame(event.end)) {
		// //	return null;  
		// }
		//TODO: de verificat ce impact are aceasta conditie, am comentat pentru ca nu afisa evenimentele BirthdayCalendarEventModel
		//si nu am inteles rolul ei

		return event;
	}

	public dayRender(dateMoment: moment.Moment, cell: HTMLElement): void {
		let date: Date = dateMoment.toDate();
		let an = date.getFullYear();
		let luna: number = date.getMonth() + 1;
		let zi: number = date.getDate();
		if (DateUtils.isWeekend(date) || CalendarManagerComponent.zileLibereLegale.includes(CalendarManagerComponent.buildDataZiLiberaLegalaAsString(luna, zi, an))) {
			cell.style.backgroundColor = CalendarManagerComponent.CALENDAR_DAY_BACKGROUND_COLOR_FOR_HOLIDAY_OR_WEEKEND;
		}
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(CalendarManagerComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(CalendarManagerComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(CalendarManagerComponent.DELETE_PERMISSION);
	}
}