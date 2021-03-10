import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { TasksActivitateAttachmentViewModel } from "./tasks-activitate-attachment-view.model";

@JsonObject
export class TasksActivitateViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("denumire", String)
	public denumire: string = null;

	@JsonProperty("descriere", String)
	public descriere: string = null;

	@JsonProperty("dataInceput", JsonDateConverter)
	public dataInceput: Date = null;

	@JsonProperty("dataSfarsit", JsonDateConverter)
	public dataSfarsit: Date = null;

	@JsonProperty("prioritate", String)
	public prioritate: string = null;

	@JsonProperty("responsabil", String)
	public responsabil: string = null;
	
	@JsonProperty("participareLa", String)
	public participareLa: string = null;
	
	@JsonProperty("explicatii", String)
	public explicatii: string = null;

	@JsonProperty("comentarii", String)
	public comentarii: string = null;

	@JsonProperty("status", String)
	public status: string = null;

	@JsonProperty("attachments", [TasksActivitateAttachmentViewModel])
	public attachments: TasksActivitateAttachmentViewModel[] = [];
}