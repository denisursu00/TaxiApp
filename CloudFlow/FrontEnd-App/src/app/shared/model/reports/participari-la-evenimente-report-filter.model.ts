import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class ParticipariLaEvenimenteReportFilterModel {

    @JsonProperty("dataInceput", JsonDateConverter)
    public dataInceput: Date = null;

    @JsonProperty("dataSfarsit", JsonDateConverter)
    public dataSfarsit: Date = null;

    @JsonProperty("idUserResponsabilTask", Number)
    public idUserResponsabilTask: number = null;

    @JsonProperty("idProiect", Number)
    public idProiect: number = null;

    @JsonProperty("subprojectIds", [Number])
    public subprojectIds: number[] = [];

    @JsonProperty("statusTask", String)
    public statusTask: string = null;

}