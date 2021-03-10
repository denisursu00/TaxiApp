import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DocumentIdentifierModel } from "./document-identifier.model ";

@JsonObject
export class ReprezentantiBancaPerFunctieSiComisieReportFilterModel {
    
	@JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;
    
	@JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;
    
	@JsonProperty("comisieId", [Number])
	public comisieId: number[] = null;
    
	@JsonProperty("institutieId", [Number])
	public institutieId: number[] = null;

	@JsonProperty("documents", [DocumentIdentifierModel])
    public documents: DocumentIdentifierModel[] = null;
}