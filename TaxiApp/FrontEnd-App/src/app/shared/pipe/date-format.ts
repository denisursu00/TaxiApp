import { PipeTransform, Pipe } from "@angular/core";
import { DatePipe } from "@angular/common";
import { DateConstants } from "../constants";
import { StringUtils, DateUtils, ObjectUtils } from "../utils";

@Pipe({ name: "appDateFormater" })
export class DateFormaterPipe implements PipeTransform {

	private datePipe: DatePipe;
	public constructor(datePipe: DatePipe) {
		this.datePipe = datePipe;
	}

	public transform(value: Date, type: "date" | "dateTime" | "time"): string {
		
		if (ObjectUtils.isNullOrUndefined(value)) {
			return "";
		}
		let dateAsString: string;
		
		if (type === "dateTime") {
			dateAsString = this.datePipe.transform(
				value,
				DateConstants.DATE_TIME_FORMAT
			);
		} else if (type === "time") {
			dateAsString = this.datePipe.transform(
				value,
				DateConstants.TIME_FORMAT
			);
		} else {
			dateAsString = this.datePipe.transform(
				value,
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