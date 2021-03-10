import { JsonObject, JsonProperty } from "json2typescript";
import { MembriiAfiliatiReportRowModel } from "./membrii-afiliati-report-row.model";

@JsonObject
export class MembriiAfiliatiReportModel {

	@JsonProperty("rows", [MembriiAfiliatiReportRowModel])
	public rows: MembriiAfiliatiReportRowModel[] = [];
}