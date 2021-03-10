import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CheltuialaReprezentantArbModel {

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

export enum TipDocumentJustificativForCheltuieliReprezentantArbEnum {
	CAZARE = "CAZARE",
	BILET_DE_AVION = "BILET_DE_AVION",
	TAXI_TREN_METROU = "TAXI_TREN_METROU",
	COMISION_UTILIZARE_CARD = "COMISION_UTILIZARE_CARD",
	ALTE_CHELTUIELI = "ALTE_CHELTUIELI"
}

export enum ValutaForCheltuieliReprezentantArbEnum {
	RON = "RON",
	EUR = "EUR",
	USD = "USD",
}