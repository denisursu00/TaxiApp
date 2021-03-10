import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ListaProiectelorCareAuVizatActiunileLuniiReportModel {
    
	@JsonProperty("denumireProiect", String)
	public denumireProiect: string = null;
}