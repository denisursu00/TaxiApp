import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class CursValutarModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("eur", Number)
	public eur: number = null;

	@JsonProperty("usd", Number)
	public usd: number = null;

	@JsonProperty("data", JsonDateConverter)
	public data: Date = null;
}