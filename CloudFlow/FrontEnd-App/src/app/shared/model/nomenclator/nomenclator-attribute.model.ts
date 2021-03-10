import { JsonObject, JsonProperty } from "json2typescript";
import { NomenclatorAttributeSelectionFilterModel } from "./nomenclator-attribute-selection-filter.model";
import { ArrayUtils } from "./../../utils";

@JsonObject
export class NomenclatorAttributeModel {
	
	public static readonly AUTOCOMPLETE_TYPE_NOMENCLATOR_ATTRIBUTE: string = "NOMENCLATOR_ATTRIBUTE";
	public static readonly AUTOCOMPLETE_TYPE_COPY_VALUE: string = "COPY_VALUE";
	public static readonly AUTOCOMPLETE_TYPE_DELETE_VALUE: string = "DELETE_VALUE";

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("key", String)
	public key: string = null;

	@JsonProperty("uiOrder", Number)
	public uiOrder: number = null;

	@JsonProperty("type", String)
	public type: string = null;

	@JsonProperty("typeNomenclatorId", Number)
	public typeNomenclatorId: number = null;

	@JsonProperty("typeNomenclatorSelectionFilters", [NomenclatorAttributeSelectionFilterModel], true)
	public typeNomenclatorSelectionFilters: NomenclatorAttributeSelectionFilterModel[] = [];

	@JsonProperty("typeNomenclatorSelectionFiltersCustomClass", String, true)
	public typeNomenclatorSelectionFiltersCustomClass: string = null;

	@JsonProperty("typeNomenclatorSelectionFiltersCustomClassAttributeKeys", String, true)
	public typeNomenclatorSelectionFiltersCustomClassAttributeKeys: string = null;
	
	@JsonProperty("attributeKeyForAutocomplete", String, true)
	public attributeKeyForAutocomplete: string = null;

	@JsonProperty("autocompleteType", String, true)
	public autocompleteType: string = undefined;

	@JsonProperty("nomenclatorAttributeKeyForAutocomplete", String, true)
	public nomenclatorAttributeKeyForAutocomplete: string = null;

	@JsonProperty("required", Boolean)
	public required: boolean = false;

	@JsonProperty("requiredCheckExpression", String, true)
	public requiredCheckExpression: string = null;

	@JsonProperty("invisibleCheckExpression", String, true)
	public invisibleCheckExpression: string = null;

	@JsonProperty("defaultValue", String, true)
	public defaultValue: string = null;

	@JsonProperty("readonlyOnAdd", Boolean, true)
	public readonlyOnAdd: boolean = false;

	@JsonProperty("readonlyOnEdit", Boolean, true)
	public readonlyOnEdit: boolean = false;

	@JsonProperty("completionSuggestionNomenclatorId", Number, true)
	public completionSuggestionNomenclatorId: number = null;

	@JsonProperty("completionSuggestionNomenclatorAttributeKey", String, true)
	public completionSuggestionNomenclatorAttributeKey: string = null;

	// TODO - De adaugata si celelalte proprietati cand va fi nevoie

	public clone(): NomenclatorAttributeModel {
		let clone: NomenclatorAttributeModel = new NomenclatorAttributeModel();
		clone.id = this.id;
		clone.name = this.name;
		clone.nomenclatorId = this.nomenclatorId;
		clone.key = this.key;
		clone.uiOrder = this.uiOrder;
		clone.type = this.type;
		clone.typeNomenclatorId = this.typeNomenclatorId;
		clone.typeNomenclatorSelectionFilters = [];
		if (ArrayUtils.isNotEmpty(this.typeNomenclatorSelectionFilters)) {
			this.typeNomenclatorSelectionFilters.forEach((filter: NomenclatorAttributeSelectionFilterModel) => {
				clone.typeNomenclatorSelectionFilters.push(filter.clone());
			});
		}
		clone.typeNomenclatorSelectionFiltersCustomClass = this.typeNomenclatorSelectionFiltersCustomClass;
		clone.typeNomenclatorSelectionFiltersCustomClassAttributeKeys = this.typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
		clone.defaultValue = this.defaultValue;
		clone.required = this.required;
		clone.readonlyOnAdd = this.readonlyOnAdd;
		clone.readonlyOnEdit = this.readonlyOnEdit;
		clone.attributeKeyForAutocomplete = this.attributeKeyForAutocomplete;
		clone.autocompleteType = this.autocompleteType;
		clone.nomenclatorAttributeKeyForAutocomplete = this.nomenclatorAttributeKeyForAutocomplete;
		clone.completionSuggestionNomenclatorId = this.completionSuggestionNomenclatorId;
		clone.completionSuggestionNomenclatorAttributeKey = this.completionSuggestionNomenclatorAttributeKey;
		return clone;
	}
}

export enum AttributeTypeEnum {
	
	TEXT = "TEXT",
	NUMERIC = "NUMERIC",
	DATE = "DATE",
	BOOLEAN = "BOOLEAN",
	NOMENCLATOR = "NOMENCLATOR",
}