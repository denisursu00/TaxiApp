import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CheltuieliArbSiReprezentantArbReportDateFilterModel {
    
	@JsonProperty("dataDecontDeLa", JsonDateConverter)
	public dataDecontDeLa: Date = null;

	@JsonProperty("dataDecontPanaLa", JsonDateConverter)
    public dataDecontPanaLa: Date = null;
    
}