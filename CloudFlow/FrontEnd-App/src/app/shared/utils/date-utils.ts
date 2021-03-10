import * as moment from "moment";
import { DateConstants } from "./../constants";
import { ObjectUtils } from "./../utils/object-utils";
import { StringUtils } from "./../utils/string-utils";

export class DateUtils {

	/**
	 * Formatul trebuie sa fie de moment.js.
	 * @param dateAsString 
	 * @param format - moment.js format
	 */
	private static parse(dateAsString: string, format: string): Date {		
		if (StringUtils.isBlank(dateAsString)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			throw new Error("format cannot be empty (or null/undefined)");
		}
		return moment(dateAsString, format).toDate();
	}

	/**
	 * Formatul trebuie sa fie de moment.js.
	 * @param date 
	 * @param format - moment.js format
	 */
	private static format(date: Date, format: string): string {		
		if (ObjectUtils.isNullOrUndefined(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			throw new Error("format cannot be empty (or null/undefined)");
		}
		return moment(date).format(format);
	}


	// >>> General Date <<<

	public static formatForDisplay(date: Date): string {
		return this.format(date, DateConstants.DATE_FORMAT_FOR_DISPLAY);
	}

	public static formatForStorage(date: Date): string {
		return this.format(date, DateConstants.DATE_FORMAT_FOR_STORAGE);
	}	

	public static parseFromStorage(dateAsString: string): Date {
		return this.parse(dateAsString, DateConstants.DATE_FORMAT_FOR_STORAGE);
	}

	public static parseFromStorageFormatToDisplayFormat(dateAsStringInStorageFormat: string): string {
		let theDate: Date = this.parse(dateAsStringInStorageFormat, DateConstants.DATE_FORMAT_FOR_STORAGE);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatForDisplay(theDate);
		}
		return null;
	}
	
	public static parseFromDisplayFormatToStorageFormat(dateAsStringInDisplayFormat: string): string {
		let theDate: Date = this.parse(dateAsStringInDisplayFormat, DateConstants.DATE_FORMAT_FOR_DISPLAY);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatForStorage(theDate);
		}
		return null;
	}


	// >>> Date Time <<<

	public static formatDateTimeForDisplay(date: Date): string {
		return this.format(date, DateConstants.DATE_TIME_FORMAT_FOR_DISPLAY);
	}

	public static formatDateTimeForStorage(date: Date): string {
		return this.format(date, DateConstants.DATE_TIME_FORMAT_FOR_STORAGE);
	}

	public static parseDateTimeFromDisplayFormatToStorageFormat(dateTimeAsStringInDisplayFormat: string): string {
		let theDate: Date = this.parse(dateTimeAsStringInDisplayFormat, DateConstants.DATE_TIME_FORMAT_FOR_DISPLAY);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatDateTimeForStorage(theDate);
		}
		return null;
	}

	public static parseDateTimeFromStorage(dateTimeAsString: string): Date {
		return this.parse(dateTimeAsString, DateConstants.DATE_TIME_FORMAT_FOR_STORAGE);
	}

	public static parseDateTimeFromStorageFormatToDisplayFormat(dateTimeAsStringInStorageFormat: string): string {
		let theDate: Date = this.parse(dateTimeAsStringInStorageFormat, DateConstants.DATE_TIME_FORMAT_FOR_STORAGE);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatDateTimeForDisplay(theDate);
		}
		return null;
	}

	public static removeTimeFromDate( date: Date): Date {
		date.setHours(0, 0, 0, 0);
		return date;
	}

	// >>> Month Year >>>

	public static formatMonthYearForDisplay(date: Date): string {
		return this.format(date, DateConstants.MONTH_YEAR_FORMAT_FOR_DISPLAY);
	}

	public static formatMonthYearForStorage(date: Date): string {
		return this.format(date, DateConstants.MONTH_YEAR_FORMAT_FOR_STORAGE);
	}

	public static parseMonthYearFromStorage(monthYearAsString: string): Date {
		return this.parse(monthYearAsString, DateConstants.MONTH_YEAR_FORMAT_FOR_STORAGE);
	}

	public static parseMonthYearFromStorageFormatToDisplayFormat(monthYearAsStringInStorageFormat: string): string {
		let theDate: Date = this.parse(monthYearAsStringInStorageFormat, DateConstants.MONTH_YEAR_FORMAT_FOR_STORAGE);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatMonthYearForDisplay(theDate);
		}
		return null;
	}

	public static parseMonthYearFromDisplayFormatToStorageFormat(monthYearAsStringInDisplayFormat: string): string {
		let theDate: Date = this.parse(monthYearAsStringInDisplayFormat, DateConstants.MONTH_YEAR_FORMAT_FOR_DISPLAY);
		if (ObjectUtils.isNotNullOrUndefined(theDate)) {
			return this.formatMonthYearForStorage(theDate);
		}
		return null;
	}
	

	// >>> Misc <<<
	
	public static getDefaultYearRange(): string {
		let currentDate: Date = new Date();
		let maxYear: number = currentDate.getFullYear() + 100;
		return ("1970:" + maxYear);
	}

	public static getDefaultYearRangeForMetadata(): string {
		return this.getDefaultYearRange();
	}

	public static addDaysToDate(date: Date, numberOfDays: number): Date {
		let dateAsMoment: moment.Moment = moment(date);
		dateAsMoment.add(numberOfDays, "d");
		return dateAsMoment.toDate();
	}

	public static subtractDaysFromDate(date: Date, numberOfDays: number): Date {
		let dateAsMoment: moment.Moment = moment(date);
		dateAsMoment.subtract(numberOfDays, "d");
		return dateAsMoment.toDate();
	}

	public static addYearsToDate(date: Date, numberOfYears: number): Date {
		let dateAsMoment: moment.Moment = moment(date);
		dateAsMoment.add(numberOfYears, "years");
		return dateAsMoment.toDate();
	}

	public static isWeekend(date: Date): boolean {
		let dayNr: number  = date.getDay();
		
		if (dayNr === 0 || dayNr === 6) {
			return true;
		} else {
			return false;
		}
	}
}