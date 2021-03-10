import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class PrezentaAgaReportRowModel {

    @JsonProperty("nrCrt", Number)
    public nrCrt: number = null;

    @JsonProperty("banca", String)
    public banca: string = null;

    @JsonProperty("confirmare", String)
    public confirmare: string = null;

    @JsonProperty("functie", String)
    public functie: string = null;

    @JsonProperty("necesitaImputernicire", String)
    public necesitaImputernicire: string = null;

    @JsonProperty("prezentaMembriNivelPresedinte", String)
    public prezentaMembriNivelPresedinte: string = null;

    @JsonProperty("prezentaMembriNivelVicepresedinte", String)
    public prezentaMembriNivelVicepresedinte: string = null;

    @JsonProperty("prezentaMembriNivelImputernicit", String)
    public prezentaMembriNivelImputernicit: string = null;

    @JsonProperty("totalPrezenta", Number)
    public totalPrezenta: number = null;

    @JsonProperty("membriVotanti", Number)
    public membriVotanti: number = null;

}