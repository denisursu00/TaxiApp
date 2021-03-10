import { JsonObject, JsonProperty } from "json2typescript";
import { ActiuniOrganizateDeArbReportRowModel } from "./actiuni-organizate-de-arb-report-row.model";

@JsonObject
export class ActiuniOrganizateDeArbReportModel {

	@JsonProperty("rows", [ActiuniOrganizateDeArbReportRowModel])
	public rows: ActiuniOrganizateDeArbReportRowModel[] = [];
}