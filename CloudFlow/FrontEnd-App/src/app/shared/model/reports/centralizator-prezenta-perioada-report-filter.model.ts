import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CentralizatorPrezentaPerioadaReportFilterModel {

	@JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;
    
	@JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;
    
	@JsonProperty("comisieId", Number)
	public comisieId: number = null;
    
	@JsonProperty("bancaId", Number)
	public bancaId: number = null;
    
	@JsonProperty("levelId", Number)
	public levelId: number = null;
}