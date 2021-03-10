import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { AderareOioroReportRowModel } from "./aderare-oioro-report-row.model";

@JsonObject
export class AderareOioroReportModel {
	
	@JsonProperty("rows", [AderareOioroReportRowModel])
	public rows: AderareOioroReportRowModel[] = [];
}