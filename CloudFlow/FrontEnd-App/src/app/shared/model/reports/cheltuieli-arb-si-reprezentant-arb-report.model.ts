import { JsonObject, JsonProperty } from "json2typescript";
import { CheltuieliArbModel } from "./cheltuieli-arb.model";
import { CheltuieliReprezentantArbModel } from "./cheltuieli-reprezentant-arb.model";

@JsonObject
export class CheltuieliArbSiReprezentantArbReportModel {

	@JsonProperty("cheltuieliArb", CheltuieliArbModel)
	public cheltuieliArb: CheltuieliArbModel = null;

	@JsonProperty("cheltuieliReprezentantArb", CheltuieliReprezentantArbModel)
	public cheltuieliReprezentantArb: CheltuieliReprezentantArbModel = null;

}