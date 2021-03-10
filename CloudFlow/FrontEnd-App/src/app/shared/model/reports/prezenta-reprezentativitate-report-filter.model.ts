import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";
import { PrezentaReprezentativitateReportRowModel } from "./prezenta-reprezentativitate-report-row.model";

@JsonObject
export class PrezentaReprezentativitateReportFilterModel {

    @JsonProperty("institutieId", Number)
    public institutieId: number = null;

    @JsonProperty("dataInceputSedintaDeLa", JsonDateConverter)
    public dataInceputSedintaDeLa: Date = null;

    @JsonProperty("dataInceputSedintaPanaLa", JsonDateConverter)
    public dataInceputSedintaPanaLa: Date = null;

}