import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { AlteDeconturiCheltuieliViewModel } from "./alte-deconturi-cheltuieli-view.model";
import { DateUtils } from "../../utils";

@JsonObject
export class AlteDeconturiViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("month", String)
	public month: string = null;

	@JsonProperty("titularDecont", String)
	public titularDecont: string = null;

	@JsonProperty("numarDecont", String)
	public numarDecont: string = null;

	@JsonProperty("dataDecont", JsonDateConverter)
	public dataDecont: Date = null;

	@JsonProperty("avansPrimit", Number)
	public avansPrimit: number = null;

	@JsonProperty("tipAvansPrimit", String)
	public tipAvansPrimit: string = null;

	@JsonProperty("cheltuieli", [AlteDeconturiCheltuieliViewModel])
	public cheltuieli: AlteDeconturiCheltuieliViewModel[] = null;

	@JsonProperty("totalCheltuieli", Number)
	public totalCheltuieli: number = null;

	@JsonProperty("totalDeIncasatRestituit", Number)
	public totalDeIncasatRestituit: number = null;

	@JsonProperty("anulat", Boolean)
	public anulat: boolean = null;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;

	@JsonProperty("finalizat", Boolean)
	public finalizat: boolean = null;

	public get monthForDisplay(): string {
		return "LABELS." + this.month;
	}

	public get numarDecontForDisplay(): string {
		return this.numarDecont.split("/", 1)[0];
	}

	public get dataDecontForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataDecont);
	}

	public get tipAvansPrimitForDisplay(): string {
		if (this.tipAvansPrimit === TipAvansPrimitEnum.CARD) {
			return "LABELS.CARD";
		} else if (this.tipAvansPrimit === TipAvansPrimitEnum.NUMERAR) {
			return "LABELS.NUMERAR";
		}
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

export enum TipAvansPrimitEnum {
	CARD = "CARD", 
	NUMERAR = "NUMERAR"
}