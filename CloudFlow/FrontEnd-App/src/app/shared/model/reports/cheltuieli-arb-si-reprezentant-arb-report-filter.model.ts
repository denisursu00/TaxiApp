import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CheltuieliArbSiReprezentantArbReportFilterModel {

	@JsonProperty("titular", String)
	public titular: string = null;
    
	@JsonProperty("organismId", Number)
    public organismId: number = null;
    
	@JsonProperty("comitet", String)
	public comitet: string = null;
    
	@JsonProperty("dataDecontDeLa", JsonDateConverter)
	public dataDecontDeLa: Date = null;

	@JsonProperty("dataDecontPanaLa", JsonDateConverter)
	public dataDecontPanaLa: Date = null;
	
	@JsonProperty("numarDecizie", String)
	public numarDecizie: string = null;

	@JsonProperty("valuta", String)
	public valuta: string = null;
}