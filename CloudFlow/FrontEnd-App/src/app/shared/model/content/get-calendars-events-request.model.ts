import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class GetCalendarsEventsRequestModel {

	@JsonProperty("calendarIds", [Number])
	public calendarIds: number[] = null;

	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;

	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;
}