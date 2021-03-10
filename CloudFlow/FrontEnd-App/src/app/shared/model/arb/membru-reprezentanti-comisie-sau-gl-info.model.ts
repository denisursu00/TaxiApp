import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MembruReprezentantiComisieSauGLInfoModel {

	@JsonProperty("nume", String)
	public nume: string = null;

	@JsonProperty("prenume", String)
	public prenume: string = null;

	@JsonProperty("functie", String, true)
	public functie: string = null;

	@JsonProperty("departament", String, true)
	public departament: string = null;

	@JsonProperty("email", String, true)
	public email: string = null;

	@JsonProperty("telefon", String, true)
	public telefon: string = null;

	@JsonProperty("calitate", String)
	public calitate: string = null;
}