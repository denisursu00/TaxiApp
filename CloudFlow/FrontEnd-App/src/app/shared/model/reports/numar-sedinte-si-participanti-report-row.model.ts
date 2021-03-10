import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class NumarSedinteSiParticipantiReportRowModel {

	@JsonProperty("dataSedinta", JsonDateConverter)
	public dataSedinta: Date = null;

	@JsonProperty("totalMembriiCD", Number)
	public totalMembriiCD: number = 0;

	@JsonProperty("totalInvitati", Number)
	public totalInvitati: number = 0;

	@JsonProperty("totalInvitatiARB", Number)
	public totalInvitatiARB: number = 0;

	@JsonProperty("totalParticipanti", Number)
	public totalParticipanti: number = 0;
}