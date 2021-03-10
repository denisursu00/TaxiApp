import { JsonObject, JsonProperty } from "json2typescript";
import { NumarParticipantiSedinteComisieGlReportRowModel } from "./numar-participanti-sedinte-comisie-gl-report-row.model";

@JsonObject
export class NumarParticipantiSedinteComisieGlReportModel {
    	
	@JsonProperty("totalGeneral", Number)
	public totalGeneral: Number = null;
	
	@JsonProperty("rows", [NumarParticipantiSedinteComisieGlReportRowModel])
	public rows: NumarParticipantiSedinteComisieGlReportRowModel[] = [];
}