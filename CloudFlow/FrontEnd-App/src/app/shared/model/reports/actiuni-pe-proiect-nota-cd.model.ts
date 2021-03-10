import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "./../../utils/date-utils";

@JsonObject
export class ActiuniPeProiectNotaCDReportModel {

	@JsonProperty("numeProiect", String)
	public numeProiect: string = null;

	@JsonProperty("tipNota", String)
	public tipNota: string = null;	
	
	@JsonProperty("subiectNota", String)
	public subiectNota: string = null;

	@JsonProperty("dataCdArb", JsonDateConverter)
	public dataCdArb: Date = null;

	public get dataCdArbForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataCdArb);
	}	
}