import { JsonObject, JsonProperty } from "json2typescript";
import { NumarSedinteComisieGlReportRowModel } from "./numar-sedinte-comisie-gl-report-row.model";

@JsonObject
export class NumarSedinteComisieGlReportModel {
	
	@JsonProperty("rows", [NumarSedinteComisieGlReportRowModel])
	public rows: NumarSedinteComisieGlReportRowModel[] = [];
	
	@JsonProperty("totalComisie", Number)
	public totalComisie: number = null;
	
	@JsonProperty("totalGl", Number)
	public totalGl: number = null;
	
	@JsonProperty("totalGeneral", Number)
	public totalGeneral: number = null;
    
}