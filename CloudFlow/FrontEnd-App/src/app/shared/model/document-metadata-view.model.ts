import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentMetadataViewModel {
	
	@JsonProperty("name", String)
	public name: string = null;
	
	@JsonProperty("label", String)
	public label: string = null;
	
	@JsonProperty("value", String)
	public value: string = null;
}