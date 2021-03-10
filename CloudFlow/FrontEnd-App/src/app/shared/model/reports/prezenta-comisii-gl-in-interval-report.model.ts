import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaComisiiGlInIntervalReportRowModel } from "./prezenta-comisii-gl-in-interval-report-row.model";

@JsonObject
export class PrezentaComisiiGlInIntervalReportModel {

	@JsonProperty("rows", [PrezentaComisiiGlInIntervalReportRowModel])
	public rows: PrezentaComisiiGlInIntervalReportRowModel[] = [];
}