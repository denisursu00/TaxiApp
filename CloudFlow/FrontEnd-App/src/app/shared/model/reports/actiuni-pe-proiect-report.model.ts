import { JsonObject, JsonProperty } from "json2typescript";
import { ActiuniPeProiectTaskReportModel } from "./actiuni-pe-proiect-task-report.model";
import { ActiuniPeProiectRegistruIntrariIesiriReportModel } from "./actiuni-pe-proiect-registru-intrari-iesiri-report.model";
import { ActiuniPeProiectNotaCDReportModel } from "./actiuni-pe-proiect-nota-cd.model";

@JsonObject
export class ActiuniPeProiectReportModel {

	@JsonProperty("actiuniPeProiectTask", [ActiuniPeProiectTaskReportModel])
	public actiuniPeProiectTask: ActiuniPeProiectTaskReportModel[] = [];

	@JsonProperty("actiuniPeProiectRegistruIntrariIesiri", [ActiuniPeProiectRegistruIntrariIesiriReportModel])
	public actiuniPeProiectRegistruIntrariIesiri: ActiuniPeProiectRegistruIntrariIesiriReportModel[] = [];

	@JsonProperty("actiuniPeProiectNotaCD", [ActiuniPeProiectNotaCDReportModel])
	public actiuniPeProiectNotaCD: ActiuniPeProiectNotaCDReportModel[] = [];
}