import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export abstract class NomenclatorFilter {

	@JsonProperty("attributeKey", String)
	public attributeKey: string = undefined;
}