import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiExterniReportFilterModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedintaDeLa", JsonDateConverter)
    public dataSedintaDeLa: Date = null;

    @JsonProperty("dataSedintaPanaLa", JsonDateConverter)
    public dataSedintaPanaLa: Date = null;

    @JsonProperty("institutieInvitatId", Number)
    public institutieInvitatId: number = null;

    @JsonProperty("invitatAcreditatId", Number)
    public invitatAcreditatId: number = null;

    @JsonProperty("invitatInlocuitorNume", String)
    public invitatInlocuitorNume: string = null;

    @JsonProperty("invitatInlocuitorPrenume", String)
    public invitatInlocuitorPrenume: string = null;
}