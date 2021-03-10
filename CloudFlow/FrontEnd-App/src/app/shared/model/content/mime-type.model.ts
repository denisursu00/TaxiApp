import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MimeTypeModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("extension", String)
	public extension: string = null;

	@JsonProperty("allowed", Boolean)
	public allowed: boolean = false;	
}