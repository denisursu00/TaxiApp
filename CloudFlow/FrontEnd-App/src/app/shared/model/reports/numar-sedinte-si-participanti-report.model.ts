import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { NumarSedinteSiParticipantiReportRowModel } from "./numar-sedinte-si-participanti-report-row.model";

@JsonObject
export class NumarSedinteSiParticipantiReportModel {
	
	@JsonProperty("totalOfTotalMembriiCD", Number)
	public totalOfTotalMembriiCD: number = 0;

	@JsonProperty("totalOfTotalInvitati", Number)
	public totalOfTotalInvitati: number = 0;

	@JsonProperty("totalOfTotalInvitatiARB", Number)
	public totalOfTotalInvitatiARB: number = 0;

	@JsonProperty("totalOfTotalParticipanti", Number)
	public totalOfTotalParticipanti: number = 0;

	@JsonProperty("rows", [NumarSedinteSiParticipantiReportRowModel])
	public rows: NumarSedinteSiParticipantiReportRowModel[] = [];
}