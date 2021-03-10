import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaSedinteCdPvgInvitatiExterniReportRowModel } from "./prezenta-sedinte-invitati-externi-report-row.model";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiExterniReportModel {

    @JsonProperty("totalInvitatiAcreditati", Number)
    public totalInvitatiAcreditati: number = null;

    @JsonProperty("totalInvitatiInlocuitori", Number)
    public totalInvitatiInlocuitori: number = null;

    @JsonProperty("rows", [PrezentaSedinteCdPvgInvitatiExterniReportRowModel])
    public rows: PrezentaSedinteCdPvgInvitatiExterniReportRowModel[] = [];
}