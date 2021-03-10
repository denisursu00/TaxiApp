import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";
import { NivelReprezentareComisiiReportRowModel } from "./nivel-reprezentare-comisii-report-row.model";

@JsonObject
export class NivelReprezentareComisiiReportModel {

    @JsonProperty("rows", [NivelReprezentareComisiiReportRowModel])
    public rows: NivelReprezentareComisiiReportRowModel[] = [];

    @JsonProperty("totalPresedintiComisii", Number)
    public totalPresedintiComisii: number = null;

    @JsonProperty("totalVicepresedintiComisii", Number)
    public totalVicepresedintiComisii: number = null;

    @JsonProperty("totalReprezentareOrganismeInterne", Number)
    public totalReprezentareOrganismeInterne: number = null;

    @JsonProperty("totalReprezentareOrganismeInternationale", Number)
    public totalReprezentareOrganismeInternationale: number = null;

    @JsonProperty("sumaTotalReprezentare", Number)
    public sumaTotalReprezentare: number = null;

    @JsonProperty("totalProcentReprezentare", Number)
    public totalProcentReprezentare: number = null;
    
}