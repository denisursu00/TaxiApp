import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class TaskuriCumulateReportRowModel {

    @JsonProperty("zonaTask", String)
    public zonaTask: string = null;

    @JsonProperty("denumireTask", String)
    public denumireTask: string = null;

    @JsonProperty("userAsignat", String)
    public userAsignat: string = null;

    @JsonProperty("status", String)
    public status: string = null;

}