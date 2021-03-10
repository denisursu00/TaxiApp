import { JsonObject, JsonProperty } from "json2typescript";
import { TaskuriCumulateReportRowModel } from "./taskuri-cumulate-report-row.model";

@JsonObject
export class TaskuriCumulateReportModel {

    @JsonProperty("rows", [TaskuriCumulateReportRowModel])
    public rows: TaskuriCumulateReportRowModel[] = [];
}