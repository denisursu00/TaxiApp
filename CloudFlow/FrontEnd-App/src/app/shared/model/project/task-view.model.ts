import { JsonObject, JsonProperty } from "json2typescript";
import { TaskAttachmentModel } from "./task-attachment.model";
import { JsonDateConverter } from "../../json-mapper";
import { ProjectModel } from "./project.model";
import { UserModel } from "../organization";
import { ProjectSubactivityModel } from "./project-subactivity.model";

@JsonObject
export class TaskViewModel {
	
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

	@JsonProperty("finalizedDate", JsonDateConverter, true)
	public finalizedDate: Date = null;
	
	@JsonProperty("priority", String)
	public priority: string = null;
	
	@JsonProperty("comments", String)
	public comments: string = null;
	
	@JsonProperty("project", ProjectModel)
	public project: ProjectModel = null;
	
	@JsonProperty("assignedUsers", [UserModel])
	public taskAssignments: UserModel[] = [];
	
	@JsonProperty("taskAttachments", [TaskAttachmentModel])
	public taskAttachments: TaskAttachmentModel[] = [];

	@JsonProperty("status", String)
	public status: string = null;
	
	@JsonProperty("initiator", UserModel)
	public initiator: UserModel = null;

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

	@JsonProperty("subactivity", ProjectSubactivityModel)
	public subactivity: ProjectSubactivityModel = null;
	
	public style = {};
}