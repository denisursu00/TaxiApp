import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";
import { NivelReprezentareComisiiReportRowModel } from "./nivel-reprezentare-comisii-report-row.model";
import { NoteBanciReportRowModel } from "./note-banci-report-row.model";

@JsonObject
export class NoteBanciReportModel {

    @JsonProperty("rows", [NoteBanciReportRowModel])
    public rows: NoteBanciReportRowModel[] = [];

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
    
}