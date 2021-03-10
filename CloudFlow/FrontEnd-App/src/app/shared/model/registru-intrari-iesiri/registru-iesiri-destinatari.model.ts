import { JsonProperty, JsonObject } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class RegistruIesiriDestinatariModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("nume", String)
	public nume: string = null;

	@JsonProperty("institutieId", Number)
	public destinatarExistentId: number = null;

	@JsonProperty("departament", String)
	public departament: string = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;

	@JsonProperty("registruIntrariId", Number)
	public registruIntrariId: number = null;

	@JsonProperty("observatii", String)
	public observatii: string = null;

	@JsonProperty("comisieGlId", Number)
	public comisieGlId: number = null;

	@JsonProperty("registruIesiriId", Number)
	public registruIesiriId: number = null;

	public numeForView: string;

	public collapseExpandAll: boolean = true;

	public rowStyleClass: object = {};
}