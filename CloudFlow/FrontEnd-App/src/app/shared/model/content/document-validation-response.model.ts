import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentValidationResponseModel {
	
	@JsonProperty("valid", Boolean)
	public valid: boolean = null;

	@JsonProperty("messages", [String], true)
	public messages: string[] = [];
}