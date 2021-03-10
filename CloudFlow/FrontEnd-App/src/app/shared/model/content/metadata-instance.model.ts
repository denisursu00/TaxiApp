import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MetadataInstanceModel {

	@JsonProperty("metadataDefinitionId", Number)
	public metadataDefinitionId: number = null;

	@JsonProperty("values", [String])
	public values: string[] = [];
}