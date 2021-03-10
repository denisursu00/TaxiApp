import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentModel } from "./../content/document.model";

@JsonObject
export class WorkflowInstanceResponseModel {

	@JsonProperty("candidateTransitionNames", [String])
	public candidateTransitionNames: string[] = [];

	@JsonProperty("manualAssignment", Boolean)
	public manualAssignment: boolean = false;
	
	@JsonProperty("chosenTransitionName", String)
	public chosenTransitionName: string = null;
	
	@JsonProperty("workflowFinished", Boolean)
	public workflowFinished: boolean = false;

	@JsonProperty("document", DocumentModel)
	public document: DocumentModel = null;
}