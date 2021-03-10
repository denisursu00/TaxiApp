import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class AttachmentModel {
	
	@JsonProperty("name", String)
	public name: string = null;
}