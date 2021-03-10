import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CentralizatorPrezentaPerioadaReportRowModel {
    
	@JsonProperty("banca", String)
	public banca: string = null;
    
	@JsonProperty("comisie", String)
	public comisie: string = null;
    
	@JsonProperty("participariLevel0", Number)
	public participariLevel0: number = null;
    
	@JsonProperty("participariLevel1", Number)
	public participariLevel1: number = null;
    
	@JsonProperty("participariLevel2", Number)
	public participariLevel2: number = null;
    
	@JsonProperty("participariLevel3", Number)
	public participariLevel3: number = null;
    
	@JsonProperty("participariLevel3Plus", Number)
	public participariLevel3Plus: number = null;
    
	@JsonProperty("participariInAfaraNom", Number)
	public participariInAfaraNom: number = null;
}