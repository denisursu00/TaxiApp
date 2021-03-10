import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel {

	@JsonProperty("data", JsonDateConverter)
	public data: Date = null;

	@JsonProperty("actiune", String)
	public actiune: string = null;
    
	@JsonProperty("subiectAgenda", String)
	public subiectAgenda: string = null;
    
	@JsonProperty("detaliuSubiectAgenda", String)
	public detaliuSubiectAgenda: string = null;
    
	@JsonProperty("participanti", String)
	public participanti: string = null;
}