import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentTypeModel } from "./../content/document-type.model";
import { OrganizationEntityModel } from "./../organization-entity.model";
import { WorkflowTransitionModel } from "./workflow-transition.model";

@JsonObject
export class WorkflowModel {
	
	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("processDefinitionId", String)
	public processDefinitionId: string = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("documentTypes", [DocumentTypeModel])
	public documentTypes: DocumentTypeModel[] = [];

	@JsonProperty("displayDocumentTypes", String)
	public displayDocumentTypes: string = null;

	@JsonProperty("supervisors", [OrganizationEntityModel])
	public supervisors: OrganizationEntityModel[] = [];

	@JsonProperty("transitions", [WorkflowTransitionModel])
	public transitions: WorkflowTransitionModel[] = [];

	@JsonProperty("baseVersionWorkflowId", Number)
	public baseVersionWorkflowId: number = null;

	@JsonProperty("sourceVersionWorkflowId", Number)
	public sourceVersionWorkflowId: number = null;

	@JsonProperty("versionNumber", Number)
	public versionNumber: number = null;
}