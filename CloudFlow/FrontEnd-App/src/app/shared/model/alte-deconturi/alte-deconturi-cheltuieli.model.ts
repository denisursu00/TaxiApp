import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { TipDocumentJustificativForCheltuieliEnum } from "./alte-deconturi-cheltuieli-view.model";
import { AlteDeconturiCheltuialaAtasamentModel } from "./alte-deconturi-cheltuiala-atasament.model";

@JsonObject
export class AlteDeconturiCheltuieliModel {

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

	@JsonProperty("atasamente", [AlteDeconturiCheltuialaAtasamentModel])
	public atasamente:  AlteDeconturiCheltuialaAtasamentModel[] = [];
}