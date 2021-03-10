import { PipeTransform, Pipe } from "@angular/core";
import { DatePipe } from "@angular/common";
import { DateConstants } from "../constants";
import { DateUtils, StringUtils } from "../utils";

@Pipe({ name: "appDateFormatterFromStorageFormat" })
export class DateFormatterPipeFromStorageFormat implements PipeTransform {

	private datePipe: DatePipe;
	public constructor(datePipe: DatePipe) {
		this.datePipe = datePipe;
	}

	public transform(value: string, type: "date" | "dateTime" | "time"): string {
		
		if (StringUtils.isBlank(value)) {
			return "";
		}
		let dateAsString: string;
		if (type === "dateTime") {
			dateAsString =  this.datePipe.transform(
				DateUtils.parseDateTimeFromStorage(value),
				DateConstants.DATE_TIME_FORMAT
			);
		} else if (type === "time") {
			dateAsString =  this.datePipe.transform(
				DateUtils.parseDateTimeFromStorage(value),
				DateConstants.TIME_FORMAT
			);
		} else {
			dateAsString = this.datePipe.transform(
				DateUtils.parseFromStorage(value),
				DateConstants.DATE_FORMAT
			);
		}
		if (StringUtils.isNotBlank(dateAsString)) {
			return dateAsString;
		} else {
			return "";
		}
	}
}