import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";

@JsonObject
export class PrezentaSedinteMembriiReportRowModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedinta", JsonDateConverter)
    public dataSedinta: Date = null;

    @JsonProperty("institutieMembru", String)
    public institutieMembru: string = null;

    @JsonProperty("membru", String)
    public membru: string = null;

    public get dataSedintaForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataSedinta);
    }
}