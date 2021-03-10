import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { ObjectUtils, DateUtils } from "./../../utils";
import { DiplomaMembruReprezentantiComisieSauGLModel } from "./diploma-membru-reprezentanti-comisie-sau-gl.model";

@JsonObject
export class MembruReprezentantiComisieSauGLModel {

	public static readonly CALITATE_TITULAR: string = "TITULAR";
	public static readonly CALITATE_SUPLEANT: string = "SUPLEANT";
	public static readonly CALITATE_INLOCUITOR: string = "INLOCUITOR";

	public static readonly STARE_ACTIV: string = "ACTIV";
	public static readonly STARE_INACTIV: string = "INACTIV";

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("reprezentantiComisieSauGLId", Number)
	public reprezentantiComisieSauGLId: number = null;

	@JsonProperty("institutieId", Number)
	public institutieId: number = null;

	@JsonProperty("membruInstitutieId", Number, true)
	public membruInstitutieId: number = null;

	@JsonProperty("nume", String, true)
	public nume: string = null;

	@JsonProperty("prenume", String, true)
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

	@JsonProperty("stare", String)
	public stare: string = null;

	@JsonProperty("diplome", [DiplomaMembruReprezentantiComisieSauGLModel], true)
	public diplome: DiplomaMembruReprezentantiComisieSauGLModel[] = [];

	public buildKeyFromInstitutieIdAndMembruInstitieId(): string {
		return this.institutieId + "-" + this.membruInstitutieId;
	}
}