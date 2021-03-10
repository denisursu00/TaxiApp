import { Moment } from "moment";

export class CalendarEvent {

	public id: string;
	public title: string;
	public allDay: boolean;
	public start: Moment;
	public end: Moment;
	public url: string;
	public className: string;
	public editable: boolean;
	public startEditable: boolean;
	public durationEditable: boolean;
	public resourceEditable: boolean;
	public rendering: "background" | "inverse-background";
	public overlap: boolean;
	public constraint: boolean;
	public source: {};
	public color: string;
	public backgroundColor: string;
	public borderColor: string;
	public textColor: string;
}