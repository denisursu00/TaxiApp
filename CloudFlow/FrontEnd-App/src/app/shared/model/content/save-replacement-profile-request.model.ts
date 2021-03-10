import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { UserModel } from "../organization/user.model";

@JsonObject
export class SaveReplacementProfileRequestModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("requesterId", Number)
	public requesterId: number = null;

	@JsonProperty("replacementUserId", Number)
	public replacementUserId: number = null;

	@JsonProperty("selectedRequesterUserProfiles", [UserModel])
	public selectedRequesterUserProfiles: UserModel[] = null;

	@JsonProperty("idsForSelectedReplacementProfilesInWhichRequesterIsReplacement", [Number])
	public idsForSelectedReplacementProfilesInWhichRequesterIsReplacement: number[] = [];

	@JsonProperty("comments", String)
	public comments: string = null;

	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;

	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;

	@JsonProperty("outOfOffice", Boolean)
	public outOfOffice: boolean = false;

	@JsonProperty("outOfOfficeEmailSubjectTemplate", String)
	public outOfOfficeEmailSubjectTemplate: string = null;

	@JsonProperty("outOfOfficeEmailBodyTemplate", String)
	public outOfOfficeEmailBodyTemplate: string = null;

	@JsonProperty("status", String)
	public status: string = null;
}