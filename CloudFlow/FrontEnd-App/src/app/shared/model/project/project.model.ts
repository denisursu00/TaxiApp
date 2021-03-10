import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationEntityModel } from "../organization-entity.model";
import { JsonDateConverter } from "../../json-mapper";
import { ProjectEstimationModel } from "./project-estimation.model";
import { ProjectSubactivityModel } from "./project-subactivity.model";

@JsonObject
export class ProjectModel {
	
	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("responsibleUserId", Number)
	public responsibleUserId: number = null;
	
	@JsonProperty("projectAbbreviation", String)
	public projectAbbreviation: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("participants", [OrganizationEntityModel])
	public participants: OrganizationEntityModel[] = [];

	@JsonProperty("estimations", [ProjectEstimationModel])
	public estimations: ProjectEstimationModel[] = [];

	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;

	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;
	
	@JsonProperty("status", String)
	public status: string = null;

	@JsonProperty("type", String)
	public type: string = null;

	@JsonProperty("comisiiSauGlIds", [Number])
	public comisiiSauGlIds: number[] = [];

	@JsonProperty("implementationDate", JsonDateConverter)
	public implementationDate: Date = null;

	@JsonProperty("importance", Number)
	public importance: number = null;

	@JsonProperty("subactivities", [ProjectSubactivityModel])
	public subactivities: ProjectSubactivityModel[] = [];

	public get projectStatusLabel(): string {
		if (this.status === ProjectStatus.INITIATED) {
			return "LABELS.PROJECT_STATUS_" + ProjectStatus.INITIATED;
		} else if (this.status === ProjectStatus.CLOSED) {
			return "LABELS.PROJECT_STATUS_" + ProjectStatus.CLOSED;
		} else {
			throw new Error("Unknown project status [" + this.status + "].");
		}
	}
}

export enum ProjectStatus {
	INITIATED = "INITIATED",
	CLOSED = "CLOSED"
}

export enum ProjectType {
	SIMPLE = "SIMPLE",
	DSP = "DSP"
}

export enum ArieDeCuprindere {
	INTERN = "intern",
	INTERNATIONAL = "international"
}