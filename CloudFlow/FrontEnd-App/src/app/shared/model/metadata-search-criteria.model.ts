import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MetadataSearchCriteriaModel {
	
	@JsonProperty("metadataDefinitionId", Number)
	public metadataDefinitionId: number = null;
	
	@JsonProperty("value", String)
	public value: string = null;
}