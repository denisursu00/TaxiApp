import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ProjectSubactivityModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("name", String)
	public name: string = null;
}