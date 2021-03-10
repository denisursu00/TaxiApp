import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";
import { DateUtils } from "../utils/date-utils";

@JsonObject
export class MyActivitiesModel {

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentName", String)
	public documentName: string = null;

	@JsonProperty("workflowSender", String)
	public workflowSender: string = null;

	@JsonProperty("workflowCurrentStatus", String)
	public workflowCurrentStatus: string = null;

	@JsonProperty("workflowName", String)
	public workflowName: string = null;

	@JsonProperty("documentTypeName", String)
	public documentTypeName: string = null;

	@JsonProperty("documentCreatedDate", JsonDateConverter)
	public documentCreatedDate: Date = null;

	@JsonProperty("documentAuthor", String)
	public documentAuthor: string = null;

	public get documentCreatedDateForDisplay(): string {
		return DateUtils.formatForDisplay(this.documentCreatedDate);
	}
	
}