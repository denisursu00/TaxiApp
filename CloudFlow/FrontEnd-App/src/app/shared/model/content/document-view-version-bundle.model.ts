import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentTypeModel } from "./document-type.model";
import { DocumentModel } from "./document.model";
import { WorkflowModel } from "./../bpm/workflow.model";
import { WorkflowStateModel } from "./../bpm/workflow-state.model";

@JsonObject
export class DocumentViewVersionBundleModel {
	
	@JsonProperty("document", DocumentModel)
	public document: DocumentModel = null;

	@JsonProperty("documentType", DocumentTypeModel)
	public documentType: DocumentTypeModel = null;

	@JsonProperty("workflow", WorkflowModel, true)
	public workflow: WorkflowModel = null;

	@JsonProperty("workflowState", WorkflowStateModel, true)
	public workflowState: WorkflowStateModel = null;
}