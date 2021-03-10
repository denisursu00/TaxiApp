import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class TasksActivitateAttachmentViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;
}