import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CheltuialaArbModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("valuta", String)
	public valuta: string = null;

	@JsonProperty("cursValutar", Number)
	public cursValutar: number = null;

	@JsonProperty("modalitatePlata", String)
	public modalitatePlata: string = null;

	@JsonProperty("numarDocumentJustificativ", String)
	public numarDocumentJustificativ: string = null;

	@JsonProperty("dataDocumentJustificativ", JsonDateConverter)
	public dataDocumentJustificativ: Date = null;

	@JsonProperty("tipDocumentJustificativ", String)
	public tipDocumentJustificativ: string = null;

	@JsonProperty("valoareCheltuiala", Number)
	public valoareCheltuiala: number = null;

	@JsonProperty("explicatie", String)
	public explicatie: string = null;
	
}

export enum TipDocumentJustificativForCheltuieliArbEnum {
	CAZARE = "CAZARE",
	BILET_DE_AVION = "BILET_DE_AVION",
	ASIGURARE_MEDICALA = "ASIGURARE_MEDICALA",
	TRANSFER_AEROPORT = "TRANSFER_AEROPORT",
	ALTE_CHELTUIELI = "ALTE_CHELTUIELI"
}

export enum ValutaForCheltuieliArbEnum {
	RON = "RON",
	EUR = "EUR",
	USD = "USD",
}