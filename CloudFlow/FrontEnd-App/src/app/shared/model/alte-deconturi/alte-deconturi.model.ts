import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { AlteDeconturiCheltuieliModel } from "./alte-deconturi-cheltuieli.model";

@JsonObject
export class AlteDeconturiModel {

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

	@JsonProperty("cheltuieli", [AlteDeconturiCheltuieliModel])
	public cheltuieli: AlteDeconturiCheltuieliModel[] = null;

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

}