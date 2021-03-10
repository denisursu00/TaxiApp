import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MetadataFilterModel {
    
	@JsonProperty("metadataId", Number)
    public metadataId: number = null;
    
	@JsonProperty("value", String)
    public value: string = null;
}