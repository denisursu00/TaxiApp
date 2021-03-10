import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";
import { PrezentaSedinteCdPvgInvitatiARBReportFilterModel } from "../reports";
import { DateUtils } from "@app/shared/utils";

@JsonObject
export class DocumentPrezentaOnlineModel {

	@JsonProperty("comisieGl", String)
	public comisieGl: string = null;

	@JsonProperty("comisieGlId", Number)
	public comisieGlId: number = null;
	
	@JsonProperty("numeDocument", String)
	public numeDocument: string = null;

	@JsonProperty("dataInceput", JsonDateConverter)
	public dataInceput: Date = null;

	@JsonProperty("dataSfarsit", JsonDateConverter)
	public dataSfarsit: Date = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("finalizata", Boolean)
	public finalizata: boolean = false;

	public get dataInceputForDisplay(): string {
        return DateUtils.formatDateTimeForDisplay(this.dataInceput);
	}
	
	public get dataSfarsitForDisplay(): string {
        return DateUtils.formatDateTimeForDisplay(this.dataSfarsit);
	}
	
} 