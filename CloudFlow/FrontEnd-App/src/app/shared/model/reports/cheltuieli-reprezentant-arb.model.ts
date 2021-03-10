import { JsonObject, JsonProperty } from "json2typescript";
import { CheltuieliArbSiRePrezentantArbRowModel } from "./cheltuieli-arb-si-reprezentant-arb-row.model";

@JsonObject
export class CheltuieliReprezentantArbModel {

	@JsonProperty("total", Number)
    public total: number = null;

	@JsonProperty("avans", Number)
	public avans: number = null;
	
	@JsonProperty("avansModalitatePlata", String)
    public avansModalitatePlata: string = null;
    
	@JsonProperty("diferenta", Number)
    public diferenta: number = null;
       
	@JsonProperty("rows", [CheltuieliArbSiRePrezentantArbRowModel])
	public rows: CheltuieliArbSiRePrezentantArbRowModel[] = [];

}