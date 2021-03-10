import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DocumenteTrimiseDeArbReportModel {

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;

	@JsonProperty("institutie", String)
	public institutie: string = null;

	@JsonProperty("document", String)
	public document: string = null;

	@JsonProperty("numarInregistrareIesire", String)
	public numarInregistrareIesire: string = null;

	public get numarInregistrareIesireForDisplay(): string {
		let numarInregistrarePerAn: string = this.numarInregistrareIesire;
		let numarInregistrareSplitArray: string[] = numarInregistrarePerAn.split("/");

		return numarInregistrareSplitArray[0];
	}
}