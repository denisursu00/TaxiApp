import { JsonObject, JsonProperty } from "json2typescript";
import { TaskAttachmentModel } from "./project";

@JsonObject
export class CompleteTaskRequestModel {

	@JsonProperty("taskId", Number)
	public taskId: number = null;

	@JsonProperty("comments", String)
	public comments: string = null;

	@JsonProperty("attachments", [TaskAttachmentModel])
	public attachments: TaskAttachmentModel[] = [];
}