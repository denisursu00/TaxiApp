import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

JsonObject
export class AderareOioroReportRowModel {
   
    @JsonProperty("organism", String)
    public organism: string = null;
    
    @JsonProperty("an", Number)
    public an: number = 0;
    
    @JsonProperty("abreviere", String)
    public abreviere: string = null;
    
    @JsonProperty("comitet", String)
    public comitet: string = null;
    
    @JsonProperty("institutie", String)
    public institutie: string = null;
    
    @JsonProperty("reprezentant", String)
    public reprezentant: string = null;
    
    @JsonProperty("functie", String)
    public functie: string = null;
    
    @JsonProperty("coordonatorArb", String)
    public coordonatorArb: string = null;
    
    @JsonProperty("nrDeplasariBugetate", Number)
	public nrDeplasariBugetate: number = 0;
}