import { JsonObject, JsonProperty } from "json2typescript";
import { CereriConcediuReportRowModel } from "./cereri-concediu-report-row.model";

@JsonObject
export class CereriConcediuReportModel {

	@JsonProperty("rows", [CereriConcediuReportRowModel])
	public rows: CereriConcediuReportRowModel[] = [];
}