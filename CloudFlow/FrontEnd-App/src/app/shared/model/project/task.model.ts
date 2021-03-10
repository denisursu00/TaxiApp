import { JsonObject, JsonProperty } from "json2typescript";
import { TaskAttachmentModel } from "./task-attachment.model";
import { JsonDateConverter } from "../../json-mapper";
import { OrganizationEntityModel } from "../organization-entity.model";

@JsonObject
export class TaskModel {
	
	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("name", String)
	public name: string = null;
	
	@JsonProperty("description", String)
	public description: string = null;
	
	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;
	
	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;
	
	@JsonProperty("finalizedDate", JsonDateConverter)
	public finalizedDate: Date = null;
	
	@JsonProperty("priority", String)
	public priority: string = null;
	
	@JsonProperty("comments", String)
	public comments: string = null;
	
	@JsonProperty("projectId", Number)
	public projectId: number = null;
	
	@JsonProperty("taskAssignments", [OrganizationEntityModel])
	public taskAssignments: OrganizationEntityModel[] = [];
	
	@JsonProperty("taskAttachments", [TaskAttachmentModel])
	public taskAttachments: TaskAttachmentModel[] = [];

	@JsonProperty("status", String)
	public status: string = null;

	@JsonProperty("permanent", Boolean)
	public permanent: boolean = null;

	@JsonProperty("participationsTo", String)
	public participationsTo: string = null;

	@JsonProperty("explications", String)
	public explications: string = null;
	
	@JsonProperty("evenimentStartDate", JsonDateConverter)
	public evenimentStartDate: Date = null;
	
	@JsonProperty("evenimentEndDate", JsonDateConverter)
	public evenimentEndDate: Date = null;

	@JsonProperty("subactivityId", Number)
	public subactivityId: number = null;
}

export enum TaskStatus {
	IN_PROGRESS = "IN_PROGRESS",
	FINALIZED = "FINALIZED",
	CANCELLED = "CANCELLED"
}

export enum TaskPriority {
	HIGH = "HIGH",
	NORMAL = "NORMAL",
	LOW = "LOW"
}

export enum TaskParticipantsTo {
	
	CONFERINTE = "Conferinte",
	COMUNICAT_DE_PRESA = "Comunicat de presa",
	EVENIMENTE_ARB = "Evenimente ARB",
	INTALNIRI = "Intalniri",
	INTALNIRE_BNR = "Intalnire BNR",
	INTALNIRE_CONSILIUL_DIRECTOR = "Intalnire Consiliul Director",
	INTALNIRE_CU_CONDUCEREA_BNR = "Intalnire cu conducerea BNR",
	SEDINTE_PARLAMENT = "Sedinte Parlament",
	SEDINTA_COMISIE_GRUPURI_DE_LUCRU = "Sedinta Comisie/Grupuri de lucru",
	SEDINTA_COMISIE_SISTEMICA = "Sedinta Comisie Sistemica",
	ALTELE = "Altele"
}