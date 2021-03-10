import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";

@JsonObject
export class DocumentValidationRequestModel {
	
	@JsonProperty("documentTypeId", Number)
	public documentTypeId: number = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String, true)
	public documentId: string = null;

	@JsonProperty("metatadaInstances", [MetadataInstanceModel])
	public metatadaInstances: MetadataInstanceModel[] = [];
}