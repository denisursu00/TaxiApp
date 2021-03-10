import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CereriConcediuReportRowModel {

	@JsonProperty("angajat", String)
    public angajat: string = null;

	@JsonProperty("tipConcediu", String)
    public tipConcediu: string = null;
    
	@JsonProperty("dataInitiere", JsonDateConverter)
	public dataInitiere: Date = null;
    
	@JsonProperty("dataInceput", JsonDateConverter)
	public dataInceput: Date = null;
    
	@JsonProperty("dataSfarsit", JsonDateConverter)
	public dataSfarsit: Date = null;

	@JsonProperty("status", String)
	public status: string = null;
    
	@JsonProperty("motivRespingere", String)
	public motivRespingere: string = null;
}