import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NomenclatorAttributeSelectionFilterModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("filterAttributeId", Number)
	public filterAttributeId: number = null;

	@JsonProperty("filterAttributeKey", String, true)
	public filterAttributeKey: string = null;

	@JsonProperty("defaultFilterValue", String, true)
	public defaultFilterValue: string = null;

	@JsonProperty("attributeKeyForAutocompleteFilterValue", String, true)
	public attributeKeyForAutocompleteFilterValue: string = null;
	
	public clone(): NomenclatorAttributeSelectionFilterModel {
		let clone: NomenclatorAttributeSelectionFilterModel = new NomenclatorAttributeSelectionFilterModel();
		Object.keys(this).forEach((prop: string) => {
			clone[prop] = this[prop];
		});
		return clone;
	}
}