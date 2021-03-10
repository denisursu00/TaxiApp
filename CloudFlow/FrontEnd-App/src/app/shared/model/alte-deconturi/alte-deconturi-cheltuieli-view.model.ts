import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class AlteDeconturiCheltuieliViewModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("tipDocumentJustificativ", String)
	public tipDocumentJustificativ: string = null;
	
	@JsonProperty("explicatie", String)
	public explicatie: string = null;
	
	@JsonProperty("numarDocumentJustificativ", String)
	public numarDocumentJustificativ: string = null;
	
	@JsonProperty("dataDocumentJustificativ", JsonDateConverter)
	public dataDocumentJustificativ: Date = null;
	
	@JsonProperty("valoareCheltuiala", Number)
	public valoareCheltuiala: number = null;

}

export enum TipDocumentJustificativForCheltuieliEnum {
	BON_FISCAL = "BON_FISCAL",
	FACTURA = "FACTURA",
	CHITANTA = "CHITANTA",
	ALTELE = "ALTELE"
}