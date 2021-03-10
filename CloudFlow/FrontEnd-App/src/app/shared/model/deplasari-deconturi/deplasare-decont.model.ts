import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { CheltuialaArbModel } from "./cheltuiala-arb.model";
import { CheltuialaReprezentantArbModel } from "./cheltuiala-reprezentant-arb.model";

@JsonObject
export class DeplasareDecontModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("apelativ", String)
	public apelativ: string = null;

	@JsonProperty("reprezentantArbId", Number)
	public reprezentantArbId: number = null;

	@JsonProperty("numarDecizie", String)
	public numarDecizie: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("denumireInstitutie", String)
	public denumireInstitutie: string = null;

	@JsonProperty("denumireOrganism", String)
	public denumireOrganism: string = null;

	@JsonProperty("abreviereOrganism", String)
	public abreviereOrganism: string = null;

	@JsonProperty("dataDecizie", JsonDateConverter)
	public dataDecizie: Date = null;

	@JsonProperty("organismId", Number)
	public organismId: number = null;

	@JsonProperty("denumireComitet", String)
	public denumireComitet: string = null;

	@JsonProperty("numarDeplasariEfectuate", Number)
	public numarDeplasariEfectuate: number = null;

	@JsonProperty("numarDeplasariBugetateRamase", Number)
	public numarDeplasariBugetateRamase: number = null;

	@JsonProperty("eveniment", String)
	public eveniment: string = null;

	@JsonProperty("tara", String)
	public tara: string = null;

	@JsonProperty("oras", String)
	public oras: string = null;

	@JsonProperty("dataPlecare", JsonDateConverter)
	public dataPlecare: Date = null;

	@JsonProperty("dataSosire", JsonDateConverter)
	public dataSosire: Date = null;

	@JsonProperty("dataConferintaInceput", JsonDateConverter)
	public dataConferintaInceput: Date = null;

	@JsonProperty("dataConferintaSfarsit", JsonDateConverter)
	public dataConferintaSfarsit: Date = null;

	@JsonProperty("numarNopti", Number)
	public numarNopti: number = null;

	@JsonProperty("minutaIntalnireTransmisa", Boolean)
	public minutaIntalnireTransmisa: boolean = null;

	@JsonProperty("observatii", String)
	public observatii: string = null;

	@JsonProperty("cheltuieliArbTitularDecont", String)
	public cheltuieliArbTitularDecont: string = null;

	@JsonProperty("cheltuieliArbTipDecont", String)
	public cheltuieliArbTipDecont: string = null;

	@JsonProperty("cheltuieliArbDataDecont", JsonDateConverter)
	public cheltuieliArbDataDecont: Date = null;

	@JsonProperty("cheltuieliArb", [CheltuialaArbModel])
	public cheltuieliArb: CheltuialaArbModel[] = null;

	@JsonProperty("cheltuieliReprezentantArbDiurnaZilnica", Number)
	public cheltuieliReprezentantArbDiurnaZilnica: number = null;

	@JsonProperty("cheltuieliReprezentantArbDiurnaZilnicaValuta", String)
	public cheltuieliReprezentantArbDiurnaZilnicaValuta: string = null;

	@JsonProperty("cheltuieliReprezentantArbDiurnaZilnicaCursValutar", Number)
	public cheltuieliReprezentantArbDiurnaZilnicaCursValutar: number = null;
	
	@JsonProperty("cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata", String)
	public cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata: string = null;

	@JsonProperty("cheltuieliReprezentantArbNumarZile", Number)
	public cheltuieliReprezentantArbNumarZile: number = null;

	@JsonProperty("cheltuieliReprezentantArbTotalDiurna", Number)
	public cheltuieliReprezentantArbTotalDiurna: number = null;

	@JsonProperty("cheltuieliReprezentantArbAvansPrimitSuma", Number)
	public cheltuieliReprezentantArbAvansPrimitSuma: number = null;

	@JsonProperty("cheltuieliReprezentantArbAvansPrimitSumaValuta", String)
	public cheltuieliReprezentantArbAvansPrimitSumaValuta: string = null;

	@JsonProperty("cheltuieliReprezentantArbAvansPrimitSumaCursValutar", Number)
	public cheltuieliReprezentantArbAvansPrimitSumaCursValutar: number = null;

	@JsonProperty("cheltuieliReprezentantArbAvansPrimitCardSauNumerar", String)
	public cheltuieliReprezentantArbAvansPrimitCardSauNumerar: string = null;

	@JsonProperty("cheltuieliReprezentantArb", [CheltuialaReprezentantArbModel])
	public cheltuieliReprezentantArb: CheltuialaReprezentantArbModel = null;
	
	@JsonProperty("detaliiNumarDeplasariBugetateNomenclatorValueId", Number)
	public detaliiNumarDeplasariBugetateNomenclatorValueId: number = null;

}

export enum ApelativReprezentantArbDeplasareDecontEnum {
	DOMNISOARA = "DOMNISOARA",
	DOAMNA = "DOAMNA",
	DOMNUL = "DOMNUL"
}

export enum ModalitatePlataForDecontEnum {
	CARD = "CARD",
	NUMERAR = "NUMERAR"
}