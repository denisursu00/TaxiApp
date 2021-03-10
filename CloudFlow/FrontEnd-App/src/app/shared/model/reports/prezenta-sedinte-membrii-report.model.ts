import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaSedinteMembriiReportRowModel } from "./prezenta-sedinte-membrii-report-row.model";

@JsonObject
export class PrezentaSedinteMembriiReportModel {

    @JsonProperty("totalMemmbrii", Number)
    public totalMembrii: number = null;

    @JsonProperty("rows", [PrezentaSedinteMembriiReportRowModel])
    public rows: PrezentaSedinteMembriiReportRowModel[] = [];
}