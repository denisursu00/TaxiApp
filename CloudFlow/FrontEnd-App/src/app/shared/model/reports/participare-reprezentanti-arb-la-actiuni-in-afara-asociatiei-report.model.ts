import { JsonObject, JsonProperty } from "json2typescript";
import { ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel } from "./participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report-row.model";

@JsonObject
export class ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel {

	@JsonProperty("rows", [ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel])
	public rows: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel[] = [];
}