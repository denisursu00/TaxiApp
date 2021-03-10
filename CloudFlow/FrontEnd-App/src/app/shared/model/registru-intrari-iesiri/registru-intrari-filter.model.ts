import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class RegistruIntrariFilterModel {

	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("pageSize", Number)
	public pageSize: number = undefined;

	@JsonProperty("year", Number)
	public year: number = null;
	
	@JsonProperty("selectedMonths", [Number], true)
	public selectedMonths: number[] = null;
	
	@JsonProperty("registrationNumber", String, true)
	public registrationNumber: string = null;
	
	@JsonProperty("registrationDate", JsonDateConverter, true)
	public registrationDate: Date = null;

	@JsonProperty("senderIds", [Number], true)
	public senderIds: number[] = null;
	
	@JsonProperty("senderDepartment", String, true)
	public senderDepartment: string = null;
	
	@JsonProperty("senderDocumentNr", String, true)
	public senderDocumentNr: string = null;
	
	@JsonProperty("senderDocumentDate", JsonDateConverter, true)
	public senderDocumentDate: Date = null;

	@JsonProperty("documentTypeIds", [Number], true)
	public documentTypeIds: number[] = null;
	
	@JsonProperty("documentTypeCode", String, true)
	public documentTypeCode: string = null;
	
	@JsonProperty("isMailed", Boolean, true)
	public isMailed: boolean = null;
	
	@JsonProperty("content", String, true)
	public content: string = null;
	
	@JsonProperty("numberOfPages", Number, true)
	public numberOfPages: number = null;

	@JsonProperty("numberOfAnnexes", Number, true)
	public numberOfAnnexes: number = null;
	
	@JsonProperty("assignedUser", String, true)
	public assignedUser: string = null;
	
	@JsonProperty("committeeWgName", String, true)
	public committeeWgName: string = null;
	
	@JsonProperty("projectName", String, true)
	public projectName: string = null;
	
	@JsonProperty("subactivityName", String, true)
	public subactivityName: string = null;
	
	@JsonProperty("isAwaitingResponse", Boolean, true)
	public isAwaitingResponse: boolean = null;
	
	@JsonProperty("responseDeadline", JsonDateConverter, true)
	public responseDeadline: Date = null;
	
	@JsonProperty("iesireRegistrationNumber", String, true)
	public iesireRegistrationNumber: string = null;
	
	@JsonProperty("remarks", String, true)
	public remarks: string = null;
	
	@JsonProperty("bankResponseProposal", [String], true)
	public bankResponseProposal: string[] = null;
	
	@JsonProperty("nrZileIntrareEmitent", Number, true)
	public nrZileIntrareEmitent: number = null;
	
	@JsonProperty("nrZileRaspunsIntrare", Number, true)
	public nrZileRaspunsIntrare: number = null;
	
	@JsonProperty("nrZileRaspunsEmitent", Number, true)
	public nrZileRaspunsEmitent: number = null;
	
	@JsonProperty("nrZileTermenDataRaspuns", Number, true)
	public nrZileTermenDataRaspuns: number = null;
	
	@JsonProperty("isCanceled", Boolean, true)
	public isCanceled: boolean = null;
	
	@JsonProperty("cancellationReason", String, true)
	public cancellationReason: string = null;
	
	@JsonProperty("isFinished", Boolean, true)
	public isFinished: boolean = null;
	
	@JsonProperty("sortField", String, true)
	public sortField: string = null;

	@JsonProperty("isAscendingOrder", Boolean, true)
	public isAscendingOrder: boolean = null;
	
	// // specific filtrarii pentru cautarea din iesiri
	
	@JsonProperty("destinatarId", Number, true)
	public destinatarId: number = null;
	
	@JsonProperty("numeDestinatar", String, true)
	public numeDestinatar: string = null;
	
	@JsonProperty("tipDocumentIdDestinatar", Number, true)
	public tipDocumentIdDestinatar: number = null;
	
}