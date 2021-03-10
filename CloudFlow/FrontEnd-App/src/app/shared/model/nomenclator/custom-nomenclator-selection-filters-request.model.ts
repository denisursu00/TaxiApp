
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CustomNomenclatorSelectionFiltersRequestModel {
	
	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number;

	@JsonProperty("attributeId", Number)
	public attributeId: number;

	@JsonProperty("attributeValueByKey", Object)
	public attributeValueByKey: object = null;
}