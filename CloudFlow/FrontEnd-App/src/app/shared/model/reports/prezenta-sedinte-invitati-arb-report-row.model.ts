import { JsonProperty, JsonObject } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils/date-utils";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiARBReportRowModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedinta", JsonDateConverter)
    public dataSedinta: Date = null;

    @JsonProperty("invitatArb", String)
    public invitatArb: string = null;

    public get dataSedintaForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataSedinta);
    }

}