import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class PrezentaMembriiReprezentantiComisieGl {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("institutieId", Number)
	public institutieId: number = null;
	
	@JsonProperty("numeInstitutie", String)
	public numeInstitutie: string = null;

	@JsonProperty("membruInstitutieId", Number)
	public membruInstitutieId: number = null;
	
	@JsonProperty("nume", String)
	public nume: string = null;

	@JsonProperty("prenume", String)
	public prenume: string = null;

	@JsonProperty("functie", String)
	public functie: string = null;

	@JsonProperty("departament", String)
	public departament: string = null;

	@JsonProperty("telefon", String)
	public telefon: string = null;

	@JsonProperty("email", String)
	public email: string = null;

	@JsonProperty("calitate", String)
	public calitate: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	public isPrezent: boolean = false;

} 