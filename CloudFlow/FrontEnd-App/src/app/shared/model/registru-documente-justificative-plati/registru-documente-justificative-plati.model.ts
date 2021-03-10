import { JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";
import { NomenclatorValueModel } from "../nomenclator";
import { RegistruDocumenteJustificativePlatiAtasamentModel } from "./registru-documente-justificative-plati-atasamente.model";

export class RegistruDocumenteJustificativePlatiModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("lunaInregistrare", String)
	public lunaInregistrare: string = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;
	
	@JsonProperty("emitent", String)
	public emitent: string = null;
	
	@JsonProperty("tipDocument", NomenclatorValueModel)
	public tipDocument: NomenclatorValueModel = null;
	
	@JsonProperty("numarDocument", String)
	public numarDocument: string= null;
	
	@JsonProperty("dataDocument", JsonDateConverter)
	public dataDocument: Date = null;
	
	@JsonProperty("modLivrare", String)
	public modLivrare: string = null;
	
	@JsonProperty("detalii", String)
	public detalii: string = null;
	
	@JsonProperty("valoare", Number)
	public valoare: number = null;
	
	@JsonProperty("moneda", NomenclatorValueModel)
	public moneda: NomenclatorValueModel = null;
	
	@JsonProperty("dataScadenta", JsonDateConverter)
	public dataScadenta: Date = null;
	
	@JsonProperty("modalitatePlata", String)
	public modalitatePlata: string = null;
	
	@JsonProperty("reconciliereCuExtrasBanca", Boolean)
	public reconciliereCuExtrasBanca: boolean = false;
	
	@JsonProperty("platit", Boolean)
	public platit: boolean = false;
	
	@JsonProperty("dataPlatii", JsonDateConverter)
	public dataPlatii: Date = null;
	
	@JsonProperty("incadrareConformBVC", String)
	public incadrareConformBVC: string = null;
	
	@JsonProperty("intrareEmitere", Number)
	public intrareEmitere: number = null;
	
	@JsonProperty("plataScadenta", Number)
	public plataScadenta: number = null;
	
	@JsonProperty("scadentaEmitere", Number)
	public scadentaEmitere: number = null;
	
	@JsonProperty("anulat", Boolean)
	public anulat: boolean = false;
	
	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;

	@JsonProperty("atasamente", [RegistruDocumenteJustificativePlatiAtasamentModel])
	public atasamente: RegistruDocumenteJustificativePlatiAtasamentModel[] = null;

	public get lunaInregistrareForDisplay(): string {
		return "LABELS." + this.lunaInregistrare;
	}

	public get lunaInregistrareForFilter(): number {
		return this.dataInregistrare.getMonth();
	}

	public get yearForDisplay(): string {
		return this.dataInregistrare.getFullYear().toString();
	}

	public get numarInregistrareForDisplay(): string {
		return this.numarInregistrare.split("/")[0];
	}

	public get numarInregistrareForFilter(): number {
		return Number(this.numarInregistrare.split("/")[0].substr(1));
	}

	public get tipDocumentForDisplay(): string {
		return this.tipDocument.attribute1;
	}

	public get dataInregistrareForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataInregistrare);
	}

	public get dataDocumentForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataDocument);
	}

	public get modLivrareForDisplay(): string {
		return "LABELS." + this.modLivrare;
	}

	public get valoareForDisplay(): string {
		return this.valoare.toFixed(2);
	}

	public get monedaForDisplay(): string {
		return this.moneda.attribute1;
	}

	public get dataScadentaForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataScadenta);
	}

	public get modalitatePlataForDisplay(): string {
		return "LABELS." + this.modalitatePlata;
	}

	public get reconciliereCuExtrasBancaForDisplay(): string {
		if (this.reconciliereCuExtrasBanca) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get platitForDisplay(): string {
		if (this.platit) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

	public get dataPlatiiForDisplay(): string {
		return DateUtils.formatForDisplay(this.dataPlatii);
	}

	public get anulatForDisplay(): string {
		if (this.anulat) {
			return "LABELS.YES";
		}
		return "LABELS.NO";
	}

}

export enum TipDocumentEnum {
	FACTURA_FISCALA = "Factura",
	PROFORMA = "Proforma",
	INSTIINTARE_DE_PLATA = "Instiintare de plata",
	CHITANTA = "Chitanta",
	BON_FISCAL = "Bon fiscal"
}

export enum ModLivrareEnum {
	POSTA = "POSTA",
	EMAIL = "EMAIL",
	CURIER = "CURIER",
	LIVRARE_EMITENT = "LIVRARE_EMITENT"
}

export enum ModalitatePlataEnum {
	INTERNET_BANKING = "INTERNET_BANKING",
	ORDIN_DE_PLATA = "ORDIN_DE_PLATA",
	NUMERAR = "NUMERAR",
	CARD = "CARD",
	DIRECT_DEBIT = "DIRECT_DEBIT"
}

export enum MonthsEnum {
	JANUARY = "JANUARY",
	FEBRUARY = "FEBRUARY",
	MARCH = "MARCH",
	APRIL = "APRIL",
	MAY = "MAY",
	JUNE = "JUNE",
	JULY = "JULY",
	AUGUST = "AUGUST",
	SEPTEMBER = "SEPTEMBER",
	OCTOBER = "OCTOBER",
	NOVEMBER = "NOVEMBER",
	DECEMBER = "DECEMBER"
}