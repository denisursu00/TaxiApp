import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CheltuieliArbSiRePrezentantArbRowModel {

	@JsonProperty("explicatie", String)
	public explicatie: string = null;
    
	@JsonProperty("suma", Number)
    public suma: number = null;
    
	@JsonProperty("valuta", String)
	public valuta: string = null;
    
	@JsonProperty("numerarSauCard", String)
	public numerarSauCard: string = null;
}