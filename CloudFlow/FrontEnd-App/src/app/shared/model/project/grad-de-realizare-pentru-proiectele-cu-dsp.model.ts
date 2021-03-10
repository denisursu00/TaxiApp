import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class GradDeRealizarePentruProiecteleCuDspModel {
	
	@JsonProperty("gradDeRealizareGeneralReprezentatAsString", String)
	public gradDeRealizareGeneralReprezentatAsString: string = null;

	@JsonProperty("gradDeRealizareGeneral", Number)
	public gradDeRealizareGeneral: number = null;

	@JsonProperty("gradDeRealizareCritic", Number)
	public gradDeRealizareCritic: number = null;

	@JsonProperty("gradDeRealizareImportant", Number)
	public gradDeRealizareImportant: number = null;

	@JsonProperty("gradDeRealizareInAsteptare", Number)
	public gradDeRealizareInAsteptare: number = null;

	@JsonProperty("gradDeRealizareStrategic", Number)
	public gradDeRealizareStrategic: number = null;
}