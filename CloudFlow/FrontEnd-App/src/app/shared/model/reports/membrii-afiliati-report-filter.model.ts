import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DocumentIdentifierModel } from "./document-identifier.model ";

@JsonObject
export class MembriiAfiliatiReportFilterModel {

	@JsonProperty("institutieId", Number)
    public institutieId: number = null;

    @JsonProperty("comisieId", Number)
    public comisieId: number = null;

	@JsonProperty("dataSedintaDeLa", JsonDateConverter)
	public dataSedintaDeLa: Date = null;

	@JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;
    
    @JsonProperty("numeReprezentantAcreditat", String)
    public numeReprezentantAcreditat: string = null;
    
    @JsonProperty("prenumeReprezentantAcreditat", String)
    public prenumeReprezentantAcreditat: string = null;
       
	@JsonProperty("numeReprezentantInlocuitor", String)
	public numeReprezentantInlocuitor: string = null;
    
	@JsonProperty("prenumeReprezentantInlocuitor", String)
	public prenumeReprezentantInlocuitor: string = null;

    @JsonProperty("document", DocumentIdentifierModel)
    public document: DocumentIdentifierModel = null;
    
}