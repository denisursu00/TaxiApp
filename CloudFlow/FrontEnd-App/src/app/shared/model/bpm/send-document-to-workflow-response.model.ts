import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentModel } from "./../content/document.model";
import { WorkflowInstanceResponseModel } from "./workflow-instance-response.model";

@JsonObject
export class SendDocumentToWorkflowResponseModel {

	@JsonProperty("workflowInstanceResponse", WorkflowInstanceResponseModel, true)
	public workflowInstanceResponse: WorkflowInstanceResponseModel = null;

	@JsonProperty("needUiSendConfirmation", Boolean, true)
	public needUiSendConfirmation: boolean = null;
}