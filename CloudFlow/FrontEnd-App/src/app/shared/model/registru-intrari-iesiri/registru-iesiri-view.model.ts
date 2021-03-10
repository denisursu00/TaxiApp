import { JsonObject, JsonProperty } from "json2typescript";
import { RegistruIesiriDestinatarViewModel } from "./registru-iesiri-destinatari-view.model";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";
import { DateConstants } from "../../constants";
import { RegistruIesiriAtasamentModel } from "./registru-iesiri-atasamente.model";

@JsonObject
export class RegistruIesiriViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;

	@JsonProperty("tipDocument", String)
	public tipDocument: string = null;

	@JsonProperty("codTipDocument", String)
	public codTipDocument: string = null;

	@JsonProperty("numarPagini", Number)
	public numarPagini: number = null;

	@JsonProperty("numarAnexe", Number)
	public numarAnexe: number = null;

	@JsonProperty("intocmitDeUser", String)
	public intocmitDeUser: string = null;

	@JsonProperty("trimisPeMail", Boolean)
	public trimisPeMail: boolean = null;

	@JsonProperty("continut", String)
	public continut: string = null;

	@JsonProperty("asteptamRaspuns", Boolean)
	public asteptamRaspuns: boolean = null;

	@JsonProperty("termenRaspuns", JsonDateConverter)
	public termenRaspuns: Date = null;

	@JsonProperty("destinatari", [RegistruIesiriDestinatarViewModel])
	public destinatari: RegistruIesiriDestinatarViewModel[] = [];

	@JsonProperty("numeProiecteConcatenate", String)
	public numeProiecteConcatenate: string = null;

	@JsonProperty("anulat", Boolean)
	public anulat: boolean = false;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;
	
	@JsonProperty("inchis", Boolean)
	public finalizat: boolean = null;

	@JsonProperty("trebuieAnulat", Boolean, true)
	public trebuieAnulat: boolean = null;

	@JsonProperty("subactivityName", String)
	public subactivityName: string = null;

	public get dataInregistrareForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataInregistrare);
	}

	public get trimisPeMailForDisplay(): string {
		if (this.trimisPeMail) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get necesitaRaspunsForDisplay(): string {
		if (this.asteptamRaspuns) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get anulatForDisplay(): string {
		if (this.anulat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get termenRaspunsForDisplay(): string {
		return DateUtils.formatForDisplay(this.termenRaspuns);
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

	public get finalizatForDisplay(): string {
		if (this.finalizat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}
}