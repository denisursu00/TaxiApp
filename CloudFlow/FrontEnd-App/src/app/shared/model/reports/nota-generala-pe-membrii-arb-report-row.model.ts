import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NotaGeneralaPeMembriiArbReportRowModel {

	@JsonProperty("banca", String)
    public banca: string = null;

	@JsonProperty("comisie", String)
    public comisie: string = null;

	@JsonProperty("notaFinalaComisie", Number)
    public notaFinalaComisie: number = null;

	@JsonProperty("rankNotaComisie", Number)
    public rankNotaComisie: number = null;
 
}