import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";
import { PrezentaReprezentativitateReportRowModel } from "./prezenta-reprezentativitate-report-row.model";

@JsonObject
export class PrezentaReprezentativitateReportModel {

    @JsonProperty("rows", [PrezentaReprezentativitateReportRowModel])
    public rows: PrezentaReprezentativitateReportRowModel[] = [];

    @JsonProperty("level0Percentage", Number)
    public level0Percentage: number = null;

    @JsonProperty("level1Percentage", Number)
    public level1Percentage: number = null;

    @JsonProperty("level2Percentage", Number)
    public level2Percentage: number = null;

    @JsonProperty("level3Percentage", Number)
    public level3Percentage: number = null;

    @JsonProperty("level3PlusPercentage", Number)
    public level3PlusPercentage: number = null;

    @JsonProperty("inAfaraNomPercentage", Number)
    public inAfaraNomPercentage: number = null;

    @JsonProperty("raspFaraPropPercentage", Number)
    public raspFaraPropPercentage: number = null;

    @JsonProperty("raspCuPropPercentage", Number)
    public raspCuPropPercentage: number = null;

    @JsonProperty("nrRaspFaraIntarzPercentage", Number)
    public nrRaspFaraIntarzPercentage: number = null;

    @JsonProperty("nrRaspCuIntarzPercentage", Number)
    public nrRaspCuIntarzPercentage: number = null;


    @JsonProperty("level0Total", Number)
    public level0Total: number = null;

    @JsonProperty("level1Total", Number)
    public level1Total: number = null;

    @JsonProperty("level2Total", Number)
    public level2Total: number = null;

    @JsonProperty("level3Total", Number)
    public level3Total: number = null;

    @JsonProperty("level3PlusTotal", Number)
    public level3PlusTotal: number = null;

    @JsonProperty("inAfaraNomTotal", Number)
    public inAfaraNomTotal: number = null;

    @JsonProperty("totalPrezentaTotal", Number)
    public totalPrezentaTotal: number = null;

    @JsonProperty("coeficientStructuralTotal", Number)
    public coeficientStructuralTotal: number = null;

    @JsonProperty("notaFinalaPrezentaTotal", Number)
    public notaFinalaPrezentaTotal: number = null;


    @JsonProperty("nrRaspunsuriFaraPropuneriTotal", Number)
    public nrRaspunsuriFaraPropuneriTotal: number = null;

    @JsonProperty("nrRaspunsuriCuPropuneriTotal", Number)
    public nrRaspunsuriCuPropuneriTotal: number = null;

    @JsonProperty("nrTotalRaspunsuriPropuneriTotal", Number)
    public nrTotalRaspunsuriPropuneriTotal: number = null;

    @JsonProperty("coeficientStructuralCalitateRaspunsuriTotal", Number)
    public coeficientStructuralCalitateRaspunsuriTotal: number = null;

    @JsonProperty("notaRaspunsuriAjustataCuCalitateaRaspunsurilorTotal", Number)
    public notaRaspunsuriAjustataCuCalitateaRaspunsurilorTotal: number = null;

    @JsonProperty("nrRaspunsuriFaraIntarziereTotal", Number)
    public nrRaspunsuriFaraIntarziereTotal: number = null;

    @JsonProperty("nrRaspunsuriCuIntarzierePesteOZiTotal", Number)
    public nrRaspunsuriCuIntarzierePesteOZiTotal: number = null;

    @JsonProperty("nrTotalRaspunsuriTermenTotal", Number)
    public nrTotalRaspunsuriTermenTotal: number = null;

    @JsonProperty("coeficientStructuralVitezaRaspunsuriTotal", Number)
    public coeficientStructuralVitezaRaspunsuriTotal: number = null;

    @JsonProperty("notaRaspunsuriAjustataCuVitezaRaspunsurilorTotal", Number)
    public notaRaspunsuriAjustataCuVitezaRaspunsurilorTotal: number = null;

    @JsonProperty("notaTotalaRaspunsuriBanciTotal", Number)
    public notaTotalaRaspunsuriBanciTotal: number = null;

    @JsonProperty("notaFinalaRaspunsuriBanciTotal", Number)
    public notaFinalaRaspunsuriBanciTotal: number = null;

    @JsonProperty("notaFinalaBancaTotal", Number)
    public notaFinalaBancaTotal: number = null;

    @JsonProperty("rankNotaBancaTotal", Number)
    public rankNotaBancaTotal: number = null;



    @JsonProperty("level0Avg", Number)
    public level0Avg: number = null;

    @JsonProperty("level1Avg", Number)
    public level1Avg: number = null;

    @JsonProperty("level2Avg", Number)
    public level2Avg: number = null;

    @JsonProperty("level3Avg", Number)
    public level3Avg: number = null;

    @JsonProperty("level3PlusAvg", Number)
    public level3PlusAvg: number = null;

    @JsonProperty("inAfaraNomAvg", Number)
    public inAfaraNomAvg: number = null;

    @JsonProperty("totalPrezentaAvg", Number)
    public totalPrezentaAvg: number = null;

    @JsonProperty("coeficientStructuralAvg", Number)
    public coeficientStructuralAvg: number = null;

    @JsonProperty("notaFinalaPrezentaAvg", Number)
    public notaFinalaPrezentaAvg: number = null;


    @JsonProperty("nrRaspunsuriFaraPropuneriAvg", Number)
    public nrRaspunsuriFaraPropuneriAvg: number = null;

    @JsonProperty("nrRaspunsuriCuPropuneriAvg", Number)
    public nrRaspunsuriCuPropuneriAvg: number = null;

    @JsonProperty("nrTotalRaspunsuriPropuneriAvg", Number)
    public nrTotalRaspunsuriPropuneriAvg: number = null;

    @JsonProperty("coeficientStructuralCalitateRaspunsuriAvg", Number)
    public coeficientStructuralCalitateRaspunsuriAvg: number = null;

    @JsonProperty("notaRaspunsuriAjustataCuCalitateaRaspunsurilorAvg", Number)
    public notaRaspunsuriAjustataCuCalitateaRaspunsurilorAvg: number = null;

    @JsonProperty("nrRaspunsuriFaraIntarziereAvg", Number)
    public nrRaspunsuriFaraIntarziereAvg: number = null;

    @JsonProperty("nrRaspunsuriCuIntarzierePesteOZiAvg", Number)
    public nrRaspunsuriCuIntarzierePesteOZiAvg: number = null;

    @JsonProperty("nrTotalRaspunsuriTermenAvg", Number)
    public nrTotalRaspunsuriTermenAvg: number = null;

    @JsonProperty("coeficientStructuralVitezaRaspunsuriAvg", Number)
    public coeficientStructuralVitezaRaspunsuriAvg: number = null;

    @JsonProperty("notaRaspunsuriAjustataCuVitezaRaspunsurilorAvg", Number)
    public notaRaspunsuriAjustataCuVitezaRaspunsurilorAvg: number = null;

    @JsonProperty("notaTotalaRaspunsuriBanciAvg", Number)
    public notaTotalaRaspunsuriBanciAvg: number = null;

    @JsonProperty("notaFinalaRaspunsuriBanciAvg", Number)
    public notaFinalaRaspunsuriBanciAvg: number = null;

    @JsonProperty("notaFinalaBancaAvg", Number)
    public notaFinalaBancaAvg: number = null;

    @JsonProperty("rankNotaBancaAvg", Number)
    public rankNotaBancaAvg: number = null;

}