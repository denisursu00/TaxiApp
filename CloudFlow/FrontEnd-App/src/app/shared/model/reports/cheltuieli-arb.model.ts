import { JsonObject, JsonProperty } from "json2typescript";
import { CheltuieliArbSiRePrezentantArbRowModel } from "./cheltuieli-arb-si-reprezentant-arb-row.model";

@JsonObject
export class CheltuieliArbModel {

	@JsonProperty("total", Number)
    public total: number = null;
    
	@JsonProperty("rows", [CheltuieliArbSiRePrezentantArbRowModel])
	public rows: CheltuieliArbSiRePrezentantArbRowModel[] = [];

}