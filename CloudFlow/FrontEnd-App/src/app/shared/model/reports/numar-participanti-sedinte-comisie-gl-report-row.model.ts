import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class NumarParticipantiSedinteComisieGlReportRowModel {
    
	@JsonProperty("numeComisie", String)
    public numeComisie: string = null;
    
	@JsonProperty("dataSedinta", JsonDateConverter)
    public dataSedinta: Date = null;
    
	@JsonProperty("numeBanca", String)
    public numeBanca: string = null;
    
	@JsonProperty("participanti", String)
    public participanti: string = null;
    
	@JsonProperty("functie", String)
    public functie: string = null;
    
	@JsonProperty("departament", String)
    public departament: string = null;
    
	@JsonProperty("calitateMembru", String)
    public calitateMembru: string = null;
    
	@JsonProperty("responsabilComisie", String)
    public responsabilComisie: string = null;
}