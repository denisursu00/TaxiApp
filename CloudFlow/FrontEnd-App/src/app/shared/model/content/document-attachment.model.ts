import { JsonObject, JsonProperty } from "json2typescript";
import { AttachmentModel } from "../attachment.model";
import { BooleanUtils } from "@app/shared";

@JsonObject
export class DocumentAttachmentModel extends AttachmentModel {
	
	@JsonProperty("isNew", Boolean)
	public isNew: boolean = false;

	@JsonProperty("documentWorkflowStateCode", String, true)
	public documentWorkflowStateCode: string = null;
	
	public documentLocationRealName: string = null;
	public documentId: string = null;
	public versionNumber: number = null;
	
	
}