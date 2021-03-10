import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils/date-utils";

@JsonObject
export class ParticipariLaEvenimenteReportRowModel {
    
    @JsonProperty("numeProiect", String)
    public numeProiect: string = null;

    @JsonProperty("numeSubproiect", String)
    public numeSubproiect: string = null;

    @JsonProperty("numeTask", String)
    public numeTask: string = null;

    @JsonProperty("dataInceput", JsonDateConverter)
    public dataInceput: Date = null;

    @JsonProperty("dataSfarsit", JsonDateConverter)
    public dataSfarsit: Date = null;

    @JsonProperty("responsabiliTask", [String])
    public responsabiliTask: string[] = [];

    @JsonProperty("participariLa", String)
    public participariLa: string = null;

    @JsonProperty("statusTask", String)
    public statusTask: string = null;
    
    public get dataInceputForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataInceput);
    }

    public get dataSfarsitForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataSfarsit);
    }
}