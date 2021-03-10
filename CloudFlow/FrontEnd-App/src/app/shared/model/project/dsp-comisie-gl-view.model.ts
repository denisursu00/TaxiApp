import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DspComisieGlViewModel {

	@JsonProperty("denumire", String)
	public denumire: string = null;

	@JsonProperty("abreviere", String)
	public abreviere: string = null;
}