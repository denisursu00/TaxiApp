import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NomenclatorMetadataDefinitionValueSelectionFilterModel {
	
	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("filterAttributeId", Number)
	public filterAttributeId: number = null;

	@JsonProperty("filterAttributeKey", String, true)
	public filterAttributeKey: string = null;

	@JsonProperty("defaultFilterValue", String, true)
	public defaultFilterValue: string = null;

	@JsonProperty("metadataNameForAutocompleteFilterValue", String, true)
	public metadataNameForAutocompleteFilterValue: string = null;
	
	public clone(): NomenclatorMetadataDefinitionValueSelectionFilterModel {
		let clone: NomenclatorMetadataDefinitionValueSelectionFilterModel = new NomenclatorMetadataDefinitionValueSelectionFilterModel();
		Object.keys(this).forEach((prop: string) => {
			clone[prop] = this[prop];
		});
		return clone;
	}
}