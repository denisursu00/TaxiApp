import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DeplDecontCheltReprArbReportFilterModel {

	@JsonProperty("titular", String)
	public titular: string = null;
    
	@JsonProperty("dataDecont", JsonDateConverter)
    public dataDecont: Date = null;
    
	@JsonProperty("dataDecontDeLa", JsonDateConverter)
	public dataDecontDeLa: Date = null;

	@JsonProperty("dataDecontPanaLa", JsonDateConverter)
	public dataDecontPanaLa: Date = null;
}