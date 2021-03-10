import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NotaGeneralaReportFilterModel {

    @JsonProperty("institutieId", Number)
    public institutieId: number = null;

    @JsonProperty("dataInceputSedintaDeLa", JsonDateConverter)
    public dataInceputSedintaDeLa: Date = null;

    @JsonProperty("dataInceputSedintaPanaLa", JsonDateConverter)
    public dataInceputSedintaPanaLa: Date = null;

}