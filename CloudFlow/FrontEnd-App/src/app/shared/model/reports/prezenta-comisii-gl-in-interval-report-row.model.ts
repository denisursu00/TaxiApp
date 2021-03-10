import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class PrezentaComisiiGlInIntervalReportRowModel {
    
	@JsonProperty("institutie", String)
    public institutie: string = null;
    
	@JsonProperty("prticipant", String)
    public prticipant: string = null;
    
	@JsonProperty("functie", String)
    public functie: string = null;
     
	@JsonProperty("departament", String)
    public departament: string = null;
    
	@JsonProperty("email", String)
    public email: string = null;
    
	@JsonProperty("telefon", String)
    public telefon: string = null;
    
}