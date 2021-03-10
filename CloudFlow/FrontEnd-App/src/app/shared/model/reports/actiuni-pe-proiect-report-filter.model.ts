import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class  ActiuniPeProiectReportFilterModel {

	@JsonProperty("dataInceput", JsonDateConverter)
	public dataInceput: Date = null;

	@JsonProperty("dataSfarsit", JsonDateConverter)
    public dataSfarsit: Date = null;
    
	@JsonProperty("proiectId", Number)
	public proiectId: number = null;
    
	@JsonProperty("subprojectIds", [Number])
	public subprojectIds: number[] = null;
}