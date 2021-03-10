import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel {

	@JsonProperty("idForRequestingReplacementProfile", Number)
	public idForRequestingReplacementProfile: number = null;

	@JsonProperty("idsForRequesterUserProfiles", [Number])
	public idsForRequesterUserProfiles: number[] = null;

	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;

	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;
}