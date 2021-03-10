import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NivelReprezentareComisiiReportFilterModel {

    @JsonProperty("institutieId", Number)
    public institutieId: number = null;

    @JsonProperty("dataExpirareMandatDeLa", JsonDateConverter)
    public dataExpirareMandatDeLa: Date = null;

    @JsonProperty("dataExpirareMandatPanaLa", JsonDateConverter)
    public dataExpirareMandatPanaLa: Date = null;

}