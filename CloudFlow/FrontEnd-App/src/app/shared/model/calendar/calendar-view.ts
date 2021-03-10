import { Moment } from "moment";

export class CalendarView {
	
	public name: string;
	public title: string;
	public start: Moment;
	public end: Moment;
	public intervalStart: Moment;
	public intervalEnd: Moment;
}