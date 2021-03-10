import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { CalendarModel, AppError } from "../../model";
import { CalendarService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { SelectItem } from "primeng/primeng";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ObjectUtils, ArrayUtils } from "../../utils";

@Component({
	selector: "app-calendar-selector",
	templateUrl: "./calendar-selector.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: CalendarSelectorComponent, multi: true }
	]
})
export class CalendarSelectorComponent implements OnInit, ControlValueAccessor {

	private calendarService: CalendarService;
	private messageDisplayer: MessageDisplayer;

	@Input()
	public userId: number;

	@Input()
	public allCalendarsSelected: boolean = false;

	@Input()
	public multipleSelection: boolean = false;

	@Input()
	public calendarIds: number[];

	@Input()
	public readOnly: boolean;

	@Output()
	public selectionChanged: EventEmitter<number[]>;
	
	public calendars: CalendarModel[];
	public calendarsSelected: CalendarModel[];

	public calendarsSelectItems: SelectItem[];

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor(calendarService: CalendarService, messageDisplayer: MessageDisplayer) {
		this.calendarService = calendarService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<number[]>();
		this.init();
	}

	private init(): void {
		this.multipleSelection = false;
		this.calendars = [];
		this.calendarsSelected = [];
		this.calendarsSelectItems = [];
		this.allCalendarsSelected = false;
		if (ObjectUtils.isNullOrUndefined(this.readOnly)) {
			this.readOnly = false;
		}
	}

	public ngOnInit(): void {
		if (!this.multipleSelection) {
			this.calendarsSelectItems.push({
				value: null, label: ""
			});
		}
		this.loadCalendars();
	}

	private loadCalendars(): void {
		this.calendarService.getAllCalendarsForUser(this.userId, {
			onSuccess: (calendars: CalendarModel[]): void => {
				this.calendars = calendars;
				this.prepareCalendarsSelectItems(calendars);

				if (this.allCalendarsSelected) {
					this.calendarsSelected = calendars;
				} else if (ArrayUtils.isNotEmpty(this.calendarIds)) {
					this.prepareSelectedCalendars();
				}
				this.propagateValue();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	public onCalendarSelectionChanged(event: any): void {
		if (!this.multipleSelection) {
			this.propagateValue();
			return;
		}
		this.propagateValue();
	}

	private prepareCalendarsSelectItems(calendars: CalendarModel[]): void {
		calendars.forEach((calendar: CalendarModel) => {
			this.calendarsSelectItems.push({
				label: calendar.name,
				value: calendar
			});
		});
		this.calendarsSelectItems = this.calendarsSelectItems.sort(function(a,b) {
			return a.label.localeCompare(b.label);
		});
	}

	private getSelectedCalendarsIds(): number[] {
		let calendarsIds: number[] = [];
		this.calendarsSelected.forEach((calendar: CalendarModel) => {
			if (ObjectUtils.isNotNullOrUndefined(calendar)) {
				calendarsIds.push(calendar.id);
			}
		});
		return calendarsIds;
	}

	public writeValue(calendarIds: number[]): void {
		this.calendarIds = calendarIds;
		if (ArrayUtils.isNotEmpty(this.calendars)) {
			this.prepareSelectedCalendars();
		}
	}

	private prepareSelectedCalendars(): void {
		if (ArrayUtils.isNotEmpty(this.calendarIds)) {
			this.calendars.forEach((calendar: CalendarModel) => {
				this.calendarIds.forEach((calendarId: number) => {
					if (calendar.id === calendarId) {
						this.calendarsSelected.push(calendar);
					}
				});
			});
		} else {
			this.calendarsSelected = [];
		}
	}

	private propagateValue(): void {
		let calendarIds: number[] = this.getSelectedCalendarsIds();
		this.selectionChanged.emit(calendarIds);
		this.onChange(calendarIds);
		this.onTouched();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public get isReadOnly(): boolean {
		return this.readOnly;
	}
}
