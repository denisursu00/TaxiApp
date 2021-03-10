import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationEntityModel } from "../organization-entity.model";
import { IntervalCalendarEvent } from "./interval-calendar-event.model";
import { StringUtils } from "@app/shared/utils";

@JsonObject
export class AuditCalendarEventModel extends IntervalCalendarEvent {

	@JsonProperty("location", String)
	public location: string = null;

	@JsonProperty("attendees", [OrganizationEntityModel])
	public attendees: OrganizationEntityModel[] = [];
	
	@JsonProperty("documentId", String)
	public documentId: string = null;
	
	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	public get hasDocument(): boolean {
		return (StringUtils.isNotBlank(this.documentId) && StringUtils.isNotBlank(this.documentLocationRealName));
	}
}