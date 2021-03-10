import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentModel } from "./../content/document.model";

@JsonObject
export class SendDocumentToWorkflowRequestModel {

	@JsonProperty("workflowId", Number)
	public workflowId: number = null;

	@JsonProperty("document", DocumentModel)
	public document: DocumentModel = null;

	@JsonProperty("transitionName", String, true)
	public transitionName: string = null;

	@JsonProperty("manualAssignmentDestinationId", String, true)
	public manualAssignmentDestinationId: string = null;

	@JsonProperty("uiSendConfirmed", Boolean)
	public uiSendConfirmed: boolean = null;
}