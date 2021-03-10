import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DecontCheltuieliAlteDeconturiReportRowModel {

	@JsonProperty("titular", String)
	public titular: string = null;

	@JsonProperty("dataDocumentJustificativ", JsonDateConverter)
	public dataDocumentJustificativ: Date = null;

	@JsonProperty("numarDocumentJustificativ", String)
	public numarDocumentJustificativ: string = null;
    
	@JsonProperty("explicatie", String)
	public explicatie: string = null;

	@JsonProperty("suma", Number)
	public suma: number = null;
}