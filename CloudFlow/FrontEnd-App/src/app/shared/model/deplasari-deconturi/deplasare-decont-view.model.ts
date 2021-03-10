import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";
import { CheltuialaArbModel } from "./cheltuiala-arb.model";
import { CheltuialaReprezentantArbModel } from "./cheltuiala-reprezentant-arb.model";

@JsonObject
export class DeplasareDecontViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("month", String)
	public month: string = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("apelativ", String)
	public apelativ: string = null;

	@JsonProperty("denumireReprezentantArb", String)
	public denumireReprezentantArb: string = null;

	@JsonProperty("numarDecizie", String)
	public numarDecizie: string = null;

	@JsonProperty("denumireInstitutie", String)
	public denumireInstitutie: string = null;

	@JsonProperty("functie", String)
	public functie: string = null;

	@JsonProperty("dataDecizie", JsonDateConverter)
	public dataDecizie: Date = null;

	@JsonProperty("denumireOrganism", String)
	public denumireOrganism: string = null;

	@JsonProperty("abreviereOrganism", String)
	public abreviereOrganism: string = null;

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

	@JsonProperty("totalCheltuieliArbValutaEur", Number)
	public totalCheltuieliArbValutaEur: number = null;

	@JsonProperty("totalCheltuieliArbValutaUsd", Number)
	public totalCheltuieliArbValutaUsd: number = null;

	@JsonProperty("totalCheltuieliArbValutaRon", Number)
	public totalCheltuieliArbValutaRon: number = null;

	@JsonProperty("totalCheltuieliArbRon", Number)
	public totalCheltuieliArbRon: number = null;

	@JsonProperty("cheltuieliReprezentantArb", [CheltuialaReprezentantArbModel])
	public cheltuieliReprezentantArb: CheltuialaReprezentantArbModel = null;

	@JsonProperty("totalCheltuieliReprezentantArbValutaEur", Number)
	public totalCheltuieliReprezentantArbValutaEur: number = null;

	@JsonProperty("totalCheltuieliReprezentantArbValutaUsd", Number)
	public totalCheltuieliReprezentantArbValutaUsd: number = null;

	@JsonProperty("totalCheltuieliReprezentantArbValutaRon", Number)
	public totalCheltuieliReprezentantArbValutaRon: number = null;

	@JsonProperty("totalDiurnaRon", Number)
	public totalDiurnaRon: number = null;

	@JsonProperty("totalCheltuieliReprezentantArbRon", Number)
	public totalCheltuieliReprezentantArbRon: number = null;

	@JsonProperty("avansPrimitRon", Number)
	public avansPrimitRon: number = null;

	@JsonProperty("totalDeIncasat", Number)
	public totalDeIncasat: number = null;

	@JsonProperty("anulat", Boolean)
	public anulat: boolean = false;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;

	@JsonProperty("finalizat", Boolean)
	public finalizat: boolean = false;

	public get monthForDisplay(): string {
		return "LABELS." + this.month;
	}

	public get numarInregistrareForDisplay(): string {
		return this.numarInregistrare.split("/", 1)[0];
	}

	public get apelativForDisplay(): string {
		return "LABELS." + this.apelativ;
	}

	public get dataDecizieForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataDecizie);
	}

	public get dataPlecareForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataPlecare);
	}

	public get dataSosireForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataSosire);
	}

	public get dataConferintaInceputForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataConferintaInceput);
	}

	public get dataConferintaSfarsitForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataConferintaSfarsit);
	}

	public get minutaIntalnireTransmisaForDisplay(): string {
		if (this.minutaIntalnireTransmisa) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get cheltuieliArbDataDecontForDisplay(): string {
		return DateUtils.formatForDisplay(this.cheltuieliArbDataDecont);
	}

	public get anulatForDisplay(): string {
		if (this.anulat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get finalizatForDisplay(): string {
		if (this.finalizat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}
}