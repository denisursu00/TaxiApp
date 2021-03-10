import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class PrezentaSedinteMembriiReportFilterModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;

    @JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;

    @JsonProperty("institutieMembru", String)
    public institutieMembru: string = null;
}