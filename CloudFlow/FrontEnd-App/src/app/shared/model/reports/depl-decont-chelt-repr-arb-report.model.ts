import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DeplDecontCheltReprArbReportRowModel } from "./depl-decont-chelt-repr-arb-report-row.model";
@JsonObject
export class DeplDecontCheltReprArbReportModel {

	@JsonProperty("titular", String)
	public titular: string = null;

	@JsonProperty("numarInregistrareDecont", String)
	public numarInregistrareDecont: string = null;

	@JsonProperty("dataDecont", JsonDateConverter)
    public dataDecont: Date = null;
    
	@JsonProperty("avansPrimitCard", Number)
	public avansPrimitCard: Number = null;

	@JsonProperty("avansPrimitNumerar", Number)
	public avansPrimitNumerar: Number = null;

	@JsonProperty("diferentaDeIncasat", Number)
	public diferentaDeIncasat: Number = null;
	
	@JsonProperty("diferentaDeRestituit", Number)
	public diferentaDeRestituit: Number = null;
	
	@JsonProperty("rows", [DeplDecontCheltReprArbReportRowModel])
	public rows: DeplDecontCheltReprArbReportRowModel[] = [];
	
	@JsonProperty("totalCheltuieli", Number)
	public totalCheltuieli: Number = null;
    
}