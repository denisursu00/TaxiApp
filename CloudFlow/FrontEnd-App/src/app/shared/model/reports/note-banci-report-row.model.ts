import { JsonObject, JsonProperty } from "json2typescript";
import { RaspunsuriBanciReportRowModel } from "./raspunsuri-banci-report-row.model.";

@JsonObject
export class NoteBanciReportRowModel {

    @JsonProperty("banca", String)
    public banca: string = null;

    @JsonProperty("cod", String)
    public cod: string = null;

    @JsonProperty("level0", Number)
    public level0: number = null;

    @JsonProperty("level1", Number)
    public level1: number = null;

    @JsonProperty("level2", Number)
    public level2: number = null;

    @JsonProperty("level3", Number)
    public level3: number = null;

    @JsonProperty("level3Plus", Number)
    public level3Plus: number = null;

    @JsonProperty("inAfaraNom", Number)
    public inAfaraNom: number = null;

    @JsonProperty("totalPrezenta", Number)
    public totalPrezenta: number = null;

    @JsonProperty("coeficientStructural", Number)
    public coeficientStructural: number = null;

    @JsonProperty("notaFinalaPrezenta", Number)
    public notaFinalaPrezenta: number = null;
}