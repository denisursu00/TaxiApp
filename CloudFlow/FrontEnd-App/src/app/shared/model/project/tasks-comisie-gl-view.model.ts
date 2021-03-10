import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class TasksComisieGlViewModel {

	@JsonProperty("denumire", String)
	public denumire: string = null;

	@JsonProperty("abreviere", String)
	public abreviere: string = null;
}