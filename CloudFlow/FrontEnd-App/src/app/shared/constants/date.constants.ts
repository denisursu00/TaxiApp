import { MonthEnum } from "../enums";

export class DateConstants {
	
	// TODO Aici trebuie revenit - cand va fi timp - si clarificat aceste formate scrise in 
	// diverse forme (ma refer la format de moment, de javascript, de primeng) - ca sa se 
	// inteleaga clar de-ale cui is si unde pot fi folosite.

	public static readonly DATE_FORMAT: string = "dd.MM.yyyy";
	public static readonly DATE_TIME_FORMAT: string = "dd.MM.yyyy HH:mm";
	public static readonly TIME_FORMAT: string = "HH:mm:ss";
	
	// primeng calendar formats
	public static readonly DATE_FORMAT_FOR_TYPING: string = "dd.mm.yy";	
	public static readonly DATE_TIME_FORMAT_FOR_TYPING: string = "dd.mm.yy";
	public static readonly MONTH_FORMAT_FOR_TYPING: string = "mm/yy";

	// moment.js formats
	public static readonly DATE_FORMAT_FOR_STORAGE: string = "YYYY.MM.DD";
	public static readonly DATE_FORMAT_FOR_DISPLAY: string = "DD.MM.YYYY";
	public static readonly DATE_TIME_FORMAT_FOR_STORAGE: string = "YYYY.MM.DD HH:mm";
	public static readonly DATE_TIME_FORMAT_FOR_DISPLAY: string = "DD.MM.YYYY HH:mm";

	public static readonly MONTH_YEAR_FORMAT_FOR_DISPLAY: string = "MM/YYYY";
	public static readonly MONTH_YEAR_FORMAT_FOR_STORAGE: string = "YYYY.MM";
	
	public static readonly ONE_DAY_AS_MILISECONDS: number = 24*60*60*1000;

	public static readonly PARAMETER_DATE_FORMAT_FOR_STORAGE: string = "YYYY.MM.DD";

	public static readonly MONTHS: string[] = Object.keys(MonthEnum);

	public static getMonthCodeByIndex(monthIndex: number) {		
		return DateConstants.MONTHS[monthIndex];
	}
}