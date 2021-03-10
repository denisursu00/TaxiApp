import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentTemplateModel {

	public fromDb: boolean = true;
	
	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("exportAvailabilityExpression", String, true)
	public exportAvailabilityExpression: string = null;
}