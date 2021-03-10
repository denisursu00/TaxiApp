
import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class MembriiAfiliatiReportRowModel {
    
	@JsonProperty("institutie", String)
    public institutie: string = null;
    
	@JsonProperty("reprezentant", String)
    public reprezentant: string = null;
    
	@JsonProperty("comisie", String)
    public comisie: string = null;
     
    @JsonProperty("dataSedinta", JsonDateConverter)
    public dataSedinta: Date = null;
    
}