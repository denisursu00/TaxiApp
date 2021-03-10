import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentTypeModel } from "./document-type.model";
import { WorkflowModel, WorkflowStateModel } from "./../bpm";

@JsonObject
export class DocumentAddBundleModel {

	@JsonProperty("documentType", DocumentTypeModel)
	public documentType: DocumentTypeModel = null;

	@JsonProperty("workflow", WorkflowModel, true)
	public workflow: WorkflowModel = null;

	@JsonProperty("currentState", WorkflowStateModel, true)
	public currentState: WorkflowStateModel = null;

	@JsonProperty("canUserSend", Boolean, true)
	public canUserSend: boolean = false;
}