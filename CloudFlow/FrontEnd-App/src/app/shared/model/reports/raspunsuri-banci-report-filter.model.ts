import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class RaspunsuriBanciReportFilterModel {

    @JsonProperty("denumireBanca", String)
    public denumireBanca: string = null;

    @JsonProperty("proiectId", Number)
    public proiectId: number = null;

    
    @JsonProperty("termenRaspunsDela", JsonDateConverter)
    public termenRaspunsDela: Date = null;
    
    @JsonProperty("termenRaspunsPanaLa", JsonDateConverter)
    public termenRaspunsPanaLa: Date = null;
}