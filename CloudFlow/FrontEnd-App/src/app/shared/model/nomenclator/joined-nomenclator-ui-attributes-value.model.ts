import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class JoinedNomenclatorUiAttributesValueModel {

	@JsonProperty("id", Number)
	public id: number = undefined;

	@JsonProperty("value", String)
	public value: string = undefined;
}