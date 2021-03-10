import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { StringUtils, ObjectUtils, ArrayUtils } from "./../../utils";
import { CalendarService } from "./../../service";
import { AppError, CalendarModel } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";

@Component({
	selector: "app-calendar-selection-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-dropdown *ngIf="!loading"
			[options]="calendars" 
			[(ngModel)]="selectedCalendar"
			optionLabel="name"
			filter="true"
			[style]="{'width':'100%'}"
			autoDisplayFirst="false"
			showClear="true"
			[disabled]="readonly"
			[placeholder]="'LABELS.SELECT' | translate" 
			(onChange)="onSelectionChanged($event)"
			filter="true"
			(onBlur)="onSelectionBlured($event)">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: CalendarSelectionFieldComponent, multi: true }
	]
})
export class CalendarSelectionFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public readonly: boolean;

	@Output()
	public selectionChanged: EventEmitter<number>;

	public calendars: CalendarModel[];
	public selectedCalendar: CalendarModel;
	public loading: boolean;

	private calendarService: CalendarService;
	private messageDisplayer: MessageDisplayer;

	private fieldValue: number;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor(calendarService: CalendarService, messageDisplayer: MessageDisplayer) {
		this.calendarService = calendarService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter();
		this.loading = true;
	}

	public ngOnInit(): void {
		this.loadCalendars();
	}

	private loadCalendars(): void {
		this.calendarService.getAllCalendars({
			onSuccess: (rCalendars: CalendarModel[]) => {
				this.calendars = rCalendars;
				this.selectCalendar();
				this.loading = false;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	private selectCalendar(): void {
		this.selectedCalendar = null;
		if (ArrayUtils.isEmpty(this.calendars)) {
			return;
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.calendars.forEach((calendar: CalendarModel) => {
			if (this.fieldValue === calendar.id) {
				this.selectedCalendar = calendar;
			}
		});
	}

	private propagateValue(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedCalendar)) {
			this.fieldValue = null;
		} else {
			this.fieldValue = this.selectedCalendar.id;
		}
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public onSelectionChanged(event: any): void {
		this.propagateValue();
		this.selectionChanged.emit(this.fieldValue);
	}

	public onSelectionBlured(event: any): void {
		this.onTouched();
	}

	public writeValue(calendarId: number): void {
		this.fieldValue = calendarId;
		this.selectCalendar();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}