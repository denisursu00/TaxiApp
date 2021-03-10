import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiARBReportFilterModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;

    @JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;

    @JsonProperty("invitatArb", String)
    public invitatiArb: string = null;
}