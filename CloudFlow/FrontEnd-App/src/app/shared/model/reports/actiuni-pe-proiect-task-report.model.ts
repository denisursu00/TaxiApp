import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { ObjectUtils } from "@app/shared/utils";
import { ReportConstants } from "@app/client/reports/constants/report.constants";

@JsonObject
export class ActiuniPeProiectTaskReportModel {

	@JsonProperty("numeProiect", String)
	public numeProiect: string = null;

	@JsonProperty("tipActiune", String)
	public tipActiune: string = null;
	
	public get tipActiuneForGrouping(): string {
		if (ObjectUtils.isNullOrUndefined(this.tipActiune)) {
			return ReportConstants.ACTIUN_PE_PROIECT_TIP_ACTIUNE_NULL_VALUE_FOR_GROUPING;
		}
		return this.tipActiune;
	}

	@JsonProperty("actiuni", String)
	public actiuni: string = null;

	@JsonProperty("dataInceputTask", JsonDateConverter)
	public dataInceputTask: Date = null;

	@JsonProperty("status", String)
	public status: string = null;

	@JsonProperty("numeSubproiect", String)
	public numeSubproiect: string = null;
}