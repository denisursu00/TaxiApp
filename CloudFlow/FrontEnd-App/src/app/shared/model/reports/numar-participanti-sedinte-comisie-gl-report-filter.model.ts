import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class NumarParticipantiSedinteComisieGlReportFilterModel {
    
	@JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;
    
	@JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;
    
	@JsonProperty("comisieId", Number)
	public comisieId: number = null;
    
	@JsonProperty("bancaId", Number)
	public bancaId: number = null;
    
	@JsonProperty("functie", String)
	public functie: string = null;
    
	@JsonProperty("departament", String)
	public departament: string = null;
    
	@JsonProperty("calitateMembru", String)
	public calitateMembru: string = null;
    
	@JsonProperty("responsabilId", Number)
	public responsabilId: number = null;
}