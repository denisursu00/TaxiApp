import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class NotaGeneralaPeMembriiArbReportFilterModel {

	@JsonProperty("bancaId", Number)
    public bancaId: number = null;

	@JsonProperty("dataSedintaDeLa", JsonDateConverter)
	public dataSedintaDeLa: Date = null;

	@JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;
    
}