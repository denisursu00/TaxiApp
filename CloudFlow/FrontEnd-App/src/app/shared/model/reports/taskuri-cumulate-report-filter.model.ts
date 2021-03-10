import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class TaskuriCumulateReportFilterModel {

    @JsonProperty("zonaTask", String)
	public zonaTask: string = null;

	@JsonProperty("deLaData", JsonDateConverter)
	public deLaData: Date = null;

	@JsonProperty("panaLaData", JsonDateConverter)
    public panaLaData: Date = null;
    
    @JsonProperty("userAsignat", Number)
    public userAsignat: number = null;
    
    @JsonProperty("status", String)
    public status: string = null;
}