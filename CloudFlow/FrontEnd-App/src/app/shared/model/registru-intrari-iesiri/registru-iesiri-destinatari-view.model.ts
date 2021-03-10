import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class RegistruIesiriDestinatarViewModel {
	
	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("nume", String)
	public nume: String = null;
	
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
	
	@JsonProperty("comisieGl", String)
	public comisieGl: string = null;
	
	@JsonProperty("registruIesiriId", Number)
	public registruIesiriId: number = null;
	
	@JsonProperty("nrInregistrareIntrare", String)
	public nrInregistrareIntrare: string = null;
	
}