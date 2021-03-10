import { JsonObject, JsonProperty } from "json2typescript";
import { ReprezentantiBancaPerFunctieSiComisieReportRowModel } from "./reprezentanti-banca-per-functie-si-comisie-report-row.model";

@JsonObject
export class ReprezentantiBancaPerFunctieSiComisieReportModel {

	@JsonProperty("rows", [ReprezentantiBancaPerFunctieSiComisieReportRowModel])
    public rows: ReprezentantiBancaPerFunctieSiComisieReportRowModel[] = [];
    
}