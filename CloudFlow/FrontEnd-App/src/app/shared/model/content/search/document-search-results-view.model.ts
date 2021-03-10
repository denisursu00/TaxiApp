import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../../json-mapper";

@JsonObject
export abstract class DocumentSearchResultsViewModel {

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("documentName", String)
	public documentName: string = null;

	@JsonProperty("documentCreatedDate", JsonDateConverter)
	public documentCreatedDate: Date = null;

	@JsonProperty("documentAuthorDisplayName", String)
	public documentAuthorDisplayName: string = null;

	@JsonProperty("workflowName", String)
	public workflowName: string = null;

	@JsonProperty("workflowCurrentStateName", String)
	public workflowCurrentStateName: string = null;

	@JsonProperty("workflowSenderDisplayName", String)
	public workflowSenderDisplayName: string = null;
}