import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class SimpleListItemModel {

	@JsonProperty("itemValue", String)
	public value: string = null;

	@JsonProperty("itemLabel", String)
	public label: string = null;

}