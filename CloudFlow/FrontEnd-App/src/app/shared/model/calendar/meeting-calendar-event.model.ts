import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationEntityModel } from "../organization-entity.model";
import { IntervalCalendarEvent } from "./interval-calendar-event.model";
import { StringDecoder } from "string_decoder";
import { StringUtils } from "@app/shared/utils";

@JsonObject
export class MeetingCalendarEventModel extends IntervalCalendarEvent {

	@JsonProperty("location", String)
	public location: string = null;
	
	@JsonProperty("reminderMinutes", Number)
	public reminderMinutes: number = null;
	
	@JsonProperty("repeat", String)
	public repeat: string = null;

	@JsonProperty("attendees", [OrganizationEntityModel])
	public attendees: OrganizationEntityModel[] = [];
	
	@JsonProperty("reprezentantExtern", String)
	public reprezentantExtern: string = null;
	
	@JsonProperty("documentId", String)
	public documentId: string = null;
	
	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	public get hasDocument(): boolean {
		return (StringUtils.isNotBlank(this.documentId) && StringUtils.isNotBlank(this.documentLocationRealName));
	}
}