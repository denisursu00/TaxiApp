import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class MetadataNomenclatorUiAttributeModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("nomenclatorAttributeId", Number)
	public nomenclatorAttributeId: number = null;

	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number = null;

	public clone(): MetadataNomenclatorUiAttributeModel {
		let clone: MetadataNomenclatorUiAttributeModel = new MetadataNomenclatorUiAttributeModel();
		clone.id = this.id;
		clone.nomenclatorAttributeId = this.nomenclatorAttributeId;
		clone.nomenclatorId = this.nomenclatorId;
		return clone;
	}
}