import { JsonObject, JsonProperty } from "json2typescript";
import { PrezentaMembriiReprezentantiComisieGl } from "./prezenta-membrii-reprezentanti-comisie-gl.model";

@JsonObject
export class ParticipantiModel {

    @JsonProperty("totalParticipanti", Number)
    public totalParticipanti: number = null;
    
    @JsonProperty("totalMembriiArb", Number)
    public totalMembriiArb: number = null;
    
    @JsonProperty("totalMembriiAfiliati", Number)
    public totalMembriiAfiliati: number = null;
    
    @JsonProperty("totalMembriiAlteInstitutii", Number)
    public totalMembriiAlteInstitutii: number = null;

	@JsonProperty("rows", [PrezentaMembriiReprezentantiComisieGl])
	public rows: PrezentaMembriiReprezentantiComisieGl[] = [];
}