import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel {

	@JsonProperty("dataInceput", JsonDateConverter)
    public dataInceput: Date = null;
    
	@JsonProperty("dataSfarsit", JsonDateConverter)
	public dataSfarsit: Date = null;
}