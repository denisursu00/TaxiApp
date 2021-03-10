import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CereriConcediuReportFilterModel {

	@JsonProperty("deLaData", JsonDateConverter)
    public deLaData: Date = null;
    
	@JsonProperty("panaLaData", JsonDateConverter)
    public panaLaData: Date = null;
    
	@JsonProperty("tipConcediu", String)
	public tipConcediu: string = null;
    
	@JsonProperty("angajatId", Number)
	public angajatId:  number = null;
    
	@JsonProperty("status", String)
	public status: string = null;
}