import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NomenclatorSortedAttribute {

	@JsonProperty("attributeKey", String)
	public attributeKey: string = undefined;

	@JsonProperty("type", String)
	public type: "ASC" | "DESC" = undefined;
}