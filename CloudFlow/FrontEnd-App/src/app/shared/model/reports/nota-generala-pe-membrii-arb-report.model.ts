import { JsonObject, JsonProperty } from "json2typescript";
import { NotaGeneralaPeMembriiArbReportRowModel } from "./nota-generala-pe-membrii-arb-report-row.model";

@JsonObject
export class NotaGeneralaPeMembriiArbReportModel {

	@JsonProperty("rows", [NotaGeneralaPeMembriiArbReportRowModel])
	public rows: NotaGeneralaPeMembriiArbReportRowModel[] = [];
 
}