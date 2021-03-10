import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NotaGeneralaReportRowModel {

    @JsonProperty("banca", String)
    public banca: string = null;

    @JsonProperty("notaFinalaBanca", Number)
    public notaFinalaBanca: number = null;

}