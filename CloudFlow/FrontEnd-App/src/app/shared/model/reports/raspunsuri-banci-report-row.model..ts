import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RaspunsuriBanciReportRowModel {

	@JsonProperty("denumireBanca", String)
	public denumireBanca: string = null;
    
	@JsonProperty("nrRaspunsuriFaraPropuneri", Number)
    public nrRaspunsuriFaraPropuneri: number = null;
    
	@JsonProperty("nrRaspunsuriCuPropuneri", Number)
    public nrRaspunsuriCuPropuneri: number = null;
    
	@JsonProperty("nrTotalRaspunsuriPropuneri", Number)
    public nrTotalRaspunsuriPropuneri: number = null;
    
	@JsonProperty("coeficientStructuralCalitateRaspunsuri", Number)
    public coeficientStructuralCalitateRaspunsuri: number = null;
    
	@JsonProperty("notaRaspunsuriAjustataCuCalitateaRaspunsurilor", Number)
    public notaRaspunsuriAjustataCuCalitateaRaspunsurilor: number = null;
    
	@JsonProperty("nrRaspunsuriFaraIntarziere", Number)
    public nrRaspunsuriFaraIntarziere: number = null;
    
	@JsonProperty("nrRaspunsuriCuIntarzierePesteOZi", Number)
    public nrRaspunsuriCuIntarzierePesteOZi: number = null;
    
	@JsonProperty("nrTotalRaspunsuriTermen", Number)
    public nrTotalRaspunsuriTermen: number = null;
    
	@JsonProperty("coeficientStructuralVitezaRaspunsuri", Number)
    public coeficientStructuralVitezaRaspunsuri: number = null;
    
	@JsonProperty("notaRaspunsuriAjustataCuVitezaRaspunsurilor", Number)
    public notaRaspunsuriAjustataCuVitezaRaspunsurilor: number = null;
    
	@JsonProperty("notaTotalaRaspunsuriBanci", Number)
    public notaTotalaRaspunsuriBanci: number = null;
}