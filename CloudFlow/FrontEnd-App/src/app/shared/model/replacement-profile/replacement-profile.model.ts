import { JsonObject, JsonProperty } from "json2typescript";
import { UserModel } from "../../model/organization/user.model";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils, StringUtils, ObjectUtils } from "../../utils";

@JsonObject
export class ReplacementProfileModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("requesterUsername", String)
	public requesterUsername: string = null;

	@JsonProperty("replacement", UserModel)
	public replacement: UserModel = null;

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

	public get startDateForDisplay(): string {
		return DateUtils.formatForDisplay(this.startDate);
	}

	public get endDateForDisplay(): string {
		return DateUtils.formatForDisplay(this.endDate);
	}

	public get replacementName(): string {
		if (ObjectUtils.isNotNullOrUndefined(this.replacement)) {
			if (StringUtils.isNotBlank(this.replacement.title)) {
				return this.replacement.name + " - " + this.replacement.title;
			} else {
				return this.replacement.name;
			}
		}
	}
}