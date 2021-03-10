import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DecontCheltuieliAlteDeconturiReportRowModel } from "./decont-cheltuieli-alte-deconturi-report-row.model";

@JsonObject
export class DecontCheltuieliAlteDeconturiReportModel {
    
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
	
	@JsonProperty("rows", [DecontCheltuieliAlteDeconturiReportRowModel])
	public rows: DecontCheltuieliAlteDeconturiReportRowModel[] = [];
	
	@JsonProperty("totalCheltuieli", Number)
	public totalCheltuieli: Number = null;
}