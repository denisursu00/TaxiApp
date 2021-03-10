import { JsonObject, JsonProperty } from "json2typescript";
import { AttachmentModel } from "../attachment.model";

@JsonObject
export class TaskAttachmentModel extends AttachmentModel {
	
	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("taskId", Number)
	public taskId: number = null;
}