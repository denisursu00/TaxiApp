import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DocumentIdentifierModel } from "./document-identifier.model ";

@JsonObject
export class  PrezentaComisiiGlInIntervalReportFilterModel {

    @JsonProperty("document", DocumentIdentifierModel)
    public document: DocumentIdentifierModel = null;

    @JsonProperty("institutieId", Number)
    public institutieId: number = null;

    @JsonProperty("participantAcreditat", String)
    public participantAcreditat: string = null;
       
	@JsonProperty("numeParticipantInlocuitor", String)
	public numeParticipantInlocuitor: string = null;
    
	@JsonProperty("prenumeParticipantInlocuitor", String)
	public prenumeParticipantInlocuitor: string = null;
    
	@JsonProperty("functie", String)
	public functie: string = null;

}