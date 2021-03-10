import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ProjectComisieSauGlViewModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("denumire", String)
	public denumire: string = null;
}