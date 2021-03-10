import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";
import { RaspunsuriBanciCuPropuneriEnum, MonthEnum } from "../../enums";
import { DateConstants } from "../../constants";

@JsonObject
export class RegistruIntrariViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;
	
	@JsonProperty("numeEmitent", String)
	public numeEmitent: string = null;
	
	@JsonProperty("departamentEmitent", String)
	public departamentEmitent: string = null;
	
	@JsonProperty("numarDocumentEmitent", String)
	public numarDocumentEmitent: string = null;
	
	@JsonProperty("dataDocumentEmitent", JsonDateConverter)
	public dataDocumentEmitent: Date = null;
	
	@JsonProperty("tipDocument", String)
	public tipDocument: string = null;
	
	@JsonProperty("codTipDocument", String)
	public codTipDocument: string = null;

	@JsonProperty("trimisPeMail", Boolean)
	public trimisPeMail: boolean = null;

	@JsonProperty("continut", String)
	public continut: string = null;

	@JsonProperty("numarPagini", Number)
	public numarPagini: number = null;

	@JsonProperty("numarAnexe", Number)
	public numarAnexe: number = null;

	@JsonProperty("repartizatCatre", String)
	public repartizatCatre: string = null;

	@JsonProperty("comisieSauGL", String)
	public comisieSauGL: string = null;

	@JsonProperty("proiect", String)
	public proiect: string = null;

	@JsonProperty("necesitaRaspuns", Boolean)
	public necesitaRaspuns: boolean = null;

	@JsonProperty("termenRaspuns", JsonDateConverter)
	public termenRaspuns: Date = null;

	@JsonProperty("numarInregistrareOfRegistruIesiri", String)
	public numarInregistrareOfRegistruIesiri: string = null;

	@JsonProperty("observatii", String)
	public observatii: string = null;

	@JsonProperty("raspunsuriBanciCuPropuneri", String)
	public raspunsuriBanciCuPropuneri: string = null;

	@JsonProperty("nrZileIntrareEmitent", Number)
	public nrZileIntrareEmitent: number = null;

	@JsonProperty("nrZileRaspunsIntrare", Number)
	public nrZileRaspunsIntrare: number = null;

	@JsonProperty("nrZileRaspunsEmitent", Number)
	public nrZileRaspunsEmitent: number = null;

	@JsonProperty("nrZileTermenDataRaspuns", Number)
	public nrZileTermenDataRaspuns: number = null;

	@JsonProperty("anulat", Boolean)
	public anulat: boolean = null;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;

	@JsonProperty("inchis", Boolean)
	public inchis: boolean = null;

	@JsonProperty("trebuieAnulat", Boolean, true)
	public trebuieAnulat: boolean = null;

	@JsonProperty("subactivityName", String)
	public subactivityName: string = null;

	public get dataInregistrareForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataInregistrare);
	}

	public get dataDocumentEmitentForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataDocumentEmitent);
	}

	public get trimisPeMailForDisplay(): string {
		if (this.trimisPeMail) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get necesitaRaspunsForDisplay(): string {
		if (this.necesitaRaspuns) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get termenRaspunsForDisplay(): string {
		return DateUtils.formatForDisplay(this.termenRaspuns);
	}

	public get raspunsuriBanciCuPropuneriForDisplay(): string {
		if (this.raspunsuriBanciCuPropuneri === RaspunsuriBanciCuPropuneriEnum.DA) {
			return "LABELS.YES";
		} else if (this.raspunsuriBanciCuPropuneri === RaspunsuriBanciCuPropuneriEnum.NU) {
			return "LABELS.NO";
		} else {
			return "LABELS.NA";
		}
	}

	public get anulatForDisplay(): string {
		if (this.anulat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get inchisForDisplay(): string {
		if (this.inchis) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get luna(): number {
		return this.dataInregistrare.getMonth();
	}

	public get lunaForDisplay(): string {
		return "LABELS." + DateConstants.getMonthCodeByIndex(this.dataInregistrare.getMonth());
	}

	public get numarInregistrareForDisplay(): string {
		let numarInregistrarePerAn: string = this.numarInregistrare;
		let numarInregistrareSplitArray: string[] = numarInregistrarePerAn.split("~");

		return numarInregistrareSplitArray[0];
	}

	public get numarInregistrareForFilter(): number {
		let numarInregistrarePerAn: string = this.numarInregistrare;
		let numarInregistrareSplitArray: string[] = numarInregistrarePerAn.split("~");

		return Number(numarInregistrareSplitArray[0]);
	}
	
	public get numarInregistrareOfRegistruIesiriForFilter(): number {
		if (this.numarInregistrareOfRegistruIesiri != null) {
			let numarInregistrarePerAn: string = this.numarInregistrareOfRegistruIesiri;
			let numarInregistrareSplitArray: string[] = numarInregistrarePerAn.split("~");

			return Number(numarInregistrareSplitArray[0]);
		} else {
			return -1;
		} 
	}
}
