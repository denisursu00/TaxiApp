import { JsonObject, JsonProperty } from "json2typescript";
import { CentralizatorPrezentaPerioadaReportRowModel } from "./centralizator-prezenta-perioada-report-row.model";

@JsonObject
export class CentralizatorPrezentaPerioadaReportModel {
	
	@JsonProperty("rows", [CentralizatorPrezentaPerioadaReportRowModel])
    public rows: CentralizatorPrezentaPerioadaReportRowModel[] = [];
    
	@JsonProperty("totalParticipariLevel0", Number)
	public totalParticipariLevel0: number = null;
    
	@JsonProperty("totalParticipariLevel1", Number)
	public totalParticipariLevel1: number = null;
    
	@JsonProperty("totalParticipariLevel2", Number)
	public totalParticipariLevel2: number = null;
    
	@JsonProperty("totalParticipariLevel3", Number)
	public totalParticipariLevel3: number = null;
    
	@JsonProperty("totalParticipariLevel3Plus", Number)
	public totalParticipariLevel3Plus: number = null;
    
	@JsonProperty("totalParticipariInAfaraNom", Number)
	public totalParticipariInAfaraNom: number = null;
}