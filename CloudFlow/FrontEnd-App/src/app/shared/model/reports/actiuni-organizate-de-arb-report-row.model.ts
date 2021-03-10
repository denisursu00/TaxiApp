import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "@app/shared/utils";

@JsonObject
export class ActiuniOrganizateDeArbReportRowModel {

	@JsonProperty("data", JsonDateConverter)
	public data: Date = null;

	public get dataForDisplay(): string {
		return DateUtils.formatForDisplay(this.data);
	}

	@JsonProperty("actiune", String)
	public actiune: string = null;
    
	@JsonProperty("subiectAgenda", String)
	public subiectAgenda: string = null;
    
	@JsonProperty("detaliuSubiectAgenda", String)
	public detaliuSubiectAgenda: string = null;

	@JsonProperty("participanti", String)
	public participanti: string = null;
}