import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class RegistruIesiriFilterModel {
	
	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("pageSize", Number)
	public pageSize: number = undefined;

	@JsonProperty("year", Number)
	public year: number = null;
	
	@JsonProperty("documentTypeIds", [Number])
	public documentTypeIds: number[] = null;

	@JsonProperty("nrInregistrare", String)
	public nrInregistrare: string = null;

	@JsonProperty("selectedMonths", [Number])
	public selectedMonths: number[] = null;
	
	@JsonProperty("isMailed", Boolean)
	public isMailed: boolean = null;

	@JsonProperty("developingUser", String)
	public developingUser: string = null;

	@JsonProperty("isAwaitingResponse", Boolean)
	public isAwaitingResponse: boolean = null;

	@JsonProperty("isCanceled", Boolean)
	public isCanceled: boolean = null;

	@JsonProperty("isFinished", Boolean)
	public isFinished: boolean = null;

	@JsonProperty("registrationDate", JsonDateConverter)
	public registrationDate: Date = null;

	@JsonProperty("documentTypeCode", String)
	public documentTypeCode: string = null;

	@JsonProperty("content", String)
	public content: string = null;

	@JsonProperty("numberOfPages", Number)
	public numberOfPages: number = null;

	@JsonProperty("numberOfAnnexes", Number)
	public numberOfAnnexes: number = null;

	@JsonProperty("projectName", String)
	public projectName: string = null;
	
	@JsonProperty("subactivityName", String, true)
	public subactivityName: string = null;

	@JsonProperty("responseDeadline", JsonDateConverter)
	public responseDeadline: Date = null;

	@JsonProperty("cancellationReason", String)
	public cancellationReason: string = null;
	
	@JsonProperty("sortField", String)
	public sortField: string = null;

	@JsonProperty("isAscendingOrder", Boolean)
	public isAscendingOrder: boolean = null;
	
	//proprietati pentru cautarea iesirilor echivalente intrarii
	@JsonProperty("emitentId", Number, true)
	public emitentId: number = null;
	
	@JsonProperty("numeEmitent", String, true)
	public numeEmitent: string = null;

	@JsonProperty("tipDocumentIntrareId", Number, true)
	public tipDocumentIntrareId: number = null;

	//proprietati pentru filtrarea pe destinatari
	@JsonProperty("departamentDestinatar", String, true)
	public departamentDestinatar: string = null;

	@JsonProperty("numarInregistrareDestinatar", String, true)
	public numarInregistrareDestinatar: string = null;

	@JsonProperty("dataInregistrareDestinatar", JsonDateConverter, true)
	public dataInregistrareDestinatar: Date = null;

	@JsonProperty("comisieGlDestinatar", Number, true)
	public comisieGlDestinatar: number = null;

	@JsonProperty("observatiiDestinatar", String, true)
	public observatiiDestinatar: string = null;

	@JsonProperty("institutiiIdsDestinatar", [Number], true)
	public institutiiIdsDestinatar: number[] = null;

	@JsonProperty("numeDestinatarNou", String, true)
	public destinatarNou: string = null;
}