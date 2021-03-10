import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";


@JsonObject
export class DeplDecontCheltReprArbReportRowModel {

	@JsonProperty("titular", String)
	public titular: string = null;

	@JsonProperty("dataDocumentJustificativ", JsonDateConverter)
	public dataDocumentJustificativ: Date = null;

	@JsonProperty("nrDocumentJustificativ", String)
	public nrDocumentJustificativ: string = null;
    
	@JsonProperty("explicatie", String)
	public explicatie: string = null;

	@JsonProperty("suma", Number)
	public suma: Number = null;
}