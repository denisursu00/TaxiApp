import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CustomNomenclatorSelectionFiltersResponseModel {

	@JsonProperty("selectable", Boolean)
	public selectable: boolean = null;

	@JsonProperty("valueIds", [Number])
	public valueIds: number[] = null;

	@JsonProperty("attributeValuesByKey", Object)
	public attributeValuesByKey: object = null;
}