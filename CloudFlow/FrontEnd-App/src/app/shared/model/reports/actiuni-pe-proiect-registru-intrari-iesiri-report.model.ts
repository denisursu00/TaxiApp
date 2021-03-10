import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "@app/shared/utils";

@JsonObject
export class ActiuniPeProiectRegistruIntrariIesiriReportModel {

	@JsonProperty("numeProiect", String)
	public numeProiect: string = null;

	@JsonProperty("numeSubproiect", String)
	public numeSubproiect: string = null;

	@JsonProperty("tipDocument", String)
	public tipDocument: string = null;

	@JsonProperty("document", String)
	public document: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;

	public get dataInregistrareForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataInregistrare);
	}

	@JsonProperty("numarDeInregistrare", String)
	public numarDeInregistrare: string = null;
}