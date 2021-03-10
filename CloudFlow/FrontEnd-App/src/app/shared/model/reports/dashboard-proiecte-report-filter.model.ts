import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DashboardProiecteReportFilterModel {

    @JsonProperty("arieActivitateBancaraId", Number)
    public arieActivitateBancaraId: number = null;

    @JsonProperty("importantaId", Number)
    public importantaId: number = null;

    @JsonProperty("abreviereProiect", String)
    public abreviereProiect: string = null;
    
    @JsonProperty("termenFinalizareDeLa", JsonDateConverter)
    public termenFinalizareDeLa: Date = null;
    
    @JsonProperty("termenFinalizarePanaLa", JsonDateConverter)
    public termenFinalizarePanaLa: Date = null;
    
    @JsonProperty("incadrareProiectId", Number)
    public incadrareProiectId: number = null;

    @JsonProperty("gradRealizareProiectDeLa", Number)
    public gradRealizareProiectDeLa: number = null;
    
    @JsonProperty("gradRealizareProiectPanaLa", Number)
    public gradRealizareProiectPanaLa: number = null;
    
    @JsonProperty("actiuneDeUrmat", String)
    public actiuneDeUrmatId: string = null;

    @JsonProperty("deadlineActiuniDeUrmatDeLa", JsonDateConverter)
    public deadlineActiuniDeUrmatDeLa: Date = null;
    
    @JsonProperty("deadlineActiuniDeUrmatPanaLa", JsonDateConverter)
    public deadlineActiuniDeUrmatPanaLa: Date = null;
  
    @JsonProperty("idUserResponsabil", Number)
    public idUserResponsabil: number = null;

    @JsonProperty("proiectInitiatDeARB", Boolean)
    public proiectInitiatDeARB: boolean = null;
    
    @JsonProperty("ariaDeCuprindere", String)
    public ariaDeCuprindere: string = null;

    @JsonProperty("evaluareGradDeRealizare", String)
    public evaluareGradDeRealizare: string = null;
    
}
