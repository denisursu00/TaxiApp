import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaSedinteCdPvgInvitatiARBReportRowModel } from "./prezenta-sedinte-invitati-arb-report-row.model";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiARBReportModel {

    @JsonProperty("totalInvitatiARB", Number)
    public totalInvitatiARB: number = null;

    @JsonProperty("rows", [PrezentaSedinteCdPvgInvitatiARBReportRowModel])
    public rows: PrezentaSedinteCdPvgInvitatiARBReportRowModel[] = [];
}