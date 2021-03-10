import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class AderareOioroReportFilterModel {

	@JsonProperty("aniList", [Number])
    public aniList: number[] = null;
    
    @JsonProperty("organismIdList", [Number])
    public organismIdList: number[] = null;
    
    @JsonProperty("abreviere", String)
    public abreviere: string = null;
    
    @JsonProperty("comitetIdList", [Number])
    public comitetIdList: number[] = null;
    
    @JsonProperty("institutieIdList", [Number])
    public institutieIdList: number[] = null;
    
    @JsonProperty("reprezentantIdList", [Number])
    public reprezentantIdList: number[] = null;
    
    @JsonProperty("functie", String)
    public functie: string = null;
    
    @JsonProperty("coordonatorArbIdList", [Number])
	public coordonatorArbIdList: number[] = null;

}