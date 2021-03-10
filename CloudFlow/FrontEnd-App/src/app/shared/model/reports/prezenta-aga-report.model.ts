import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaAgaReportRowModel } from "./prezenta-aga-report-row.model";

@JsonObject
export class PrezentaAgaReportModel {

    @JsonProperty("rows", [PrezentaAgaReportRowModel])
    public rows: PrezentaAgaReportRowModel[] = [];

    @JsonProperty("prezentaActualaPresedinte", Number)
    public prezentaActualaPresedinte: number = null;

    @JsonProperty("prezentaActualaVicepresedinte", Number)
    public prezentaActualaVicepresedinte: number = null;

    @JsonProperty("prezentaActualaInlocuitor", Number)
    public prezentaActualaInlocuitor: number = null;

    @JsonProperty("prezentaActualaTotal", Number)
    public prezentaActualaTotal: number = null;

    @JsonProperty("prezentaActualaMembriVotanti", Number)
    public prezentaActualaMembriVotanti: number = null;

    @JsonProperty("prezentaProcentPresedinte", Number)
    public prezentaProcentPresedinte: number = null;

    @JsonProperty("prezentaProcentVicepresedinte", Number)
    public prezentaProcentVicepresedinte: number = null;

    @JsonProperty("prezentaProcentInlocuitor", Number)
    public prezentaProcentInlocuitor: number = null;

    @JsonProperty("prezentaProcentMembriVotanti", Number)
    public prezentaProcentMembriVotanti: number = null;

    @JsonProperty("membriAsociatie", Number)
    public membriAsociatie: number = null;

    @JsonProperty("prezentaNecesara", Number)
    public prezentaNecesara: number = null;

    @JsonProperty("restNecesarPrezenta", Number)
    public restNecesarPrezenta: number = null;

    @JsonProperty("necesarAdoptare", Number)
    public necesarAdoptare: number = null;

    @JsonProperty("jumatePlusUnu", Number)
    public jumatePlusUnu: number = null;

    @JsonProperty("necesarAdoptareHotarare", Number)
    public necesarAdoptareHotarare: number = null;

    @JsonProperty("necesarPentruVotSecret", Number)
    public necesarPentruVotSecret: number = null;
}