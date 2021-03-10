import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentTypeModel } from "./document-type.model";
import { DocumentModel } from "./document.model";
import { WorkflowModel, WorkflowStateModel } from "./../bpm";

@JsonObject
export class DocumentViewOrEditBundleModel {
	
	@JsonProperty("documentModel", DocumentModel)
	public document: DocumentModel = null;

	@JsonProperty("documentTypeModel", DocumentTypeModel)
	public documentType: DocumentTypeModel = null;

	@JsonProperty("workflowModel", WorkflowModel, true)
	public workflow: WorkflowModel = null;

	@JsonProperty("workflowStateModel", WorkflowStateModel, true)
	public workflowState: WorkflowStateModel = null;

	@JsonProperty("canUserSend", Boolean, true)
	public canUserSend: boolean = false;

	@JsonProperty("canUserEdit", Boolean, true)
	public canUserEdit: boolean = false;

	@JsonProperty("canUserAccessLockedDocument", Boolean, true)
	public canUserAccessLockedDocument: boolean = false;
}