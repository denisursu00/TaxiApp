import { JsonObject, JsonProperty } from "json2typescript";
import { ListMetadataItemModel } from "./list-metadata-item.model";
import { ArrayUtils } from "./../../utils/array-utils";
import { NomenclatorMetadataDefinitionValueSelectionFilterModel } from "./nomenclator-metadata-definition-value-selection-filter.model";

@JsonObject
export class MetadataDefinitionModel {
	
	public static readonly TYPE_TEXT: string = "TEXT";
	public static readonly TYPE_NUMERIC: string = "NUMERIC";
	public static readonly TYPE_AUTO_NUMBER: string = "AUTO_NUMBER";
	public static readonly TYPE_DATE: string = "DATE";
	public static readonly TYPE_DATE_TIME: string = "DATE_TIME";
	public static readonly TYPE_MONTH: string = "MONTH";
	public static readonly TYPE_LIST: string = "LIST";
	public static readonly TYPE_USER: string = "USER";
	public static readonly TYPE_TEXT_AREA: string = "TEXT_AREA";
	public static readonly TYPE_METADATA_COLLECTION: string = "METADATA_COLLECTION";
	public static readonly TYPE_NOMENCLATOR: string = "NOMENCLATOR";
	public static readonly TYPE_GROUP: string = "GROUP";
	public static readonly TYPE_DOCUMENT: string = "DOCUMENT";
	public static readonly TYPE_CALENDAR: string = "CALENDAR";
	public static readonly TYPE_PROJECT: string = "PROJECT";
	
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_TITLE: string = "USER_TITLE";
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_EMAIL: string = "USER_EMAIL";
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE: string = "NOMENCLATOR_ATTRIBUTE";
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_METADATA_VALUE: string = "METADATA_VALUE";
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_DELETE_VALUE: string = "DELETE_VALUE";
	public static readonly TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS: string = "CUSTOM_CLASS";

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("label", String)
	public label: string = null;

	@JsonProperty("mandatory", Boolean)
	public mandatory: boolean = false;

	@JsonProperty("restrictedOnEdit", Boolean)
	public restrictedOnEdit: boolean = false;

	@JsonProperty("mandatoryStates", String)
	public mandatoryStates: string = null;

	@JsonProperty("restrictedOnEditStates", String)
	public restrictedOnEditStates: string = null;

	@JsonProperty("invisible", Boolean)
	public invisible: boolean = false;

	@JsonProperty("invisibleInStates", String)
	public invisibleInStates: string = null;

	@JsonProperty("representative", Boolean)
	public representative: boolean = false;

	@JsonProperty("indexed", Boolean, true)
	public indexed: boolean = undefined;

	@JsonProperty("orderNumber", Number)
	public orderNumber: number = null;

	@JsonProperty("type", String)
	public type: string = null;

	@JsonProperty("defaultValue", String, true)
	public defaultValue: string = undefined;

	@JsonProperty("metadataNameForAutoCompleteWithMetadata", String, true)
	public metadataNameForAutoCompleteWithMetadata: string = null;

	@JsonProperty("typeOfAutoCompleteWithMetadata", String, true)
	public typeOfAutoCompleteWithMetadata: string = undefined;

	@JsonProperty("nomenclatorAttributeKeyForAutoCompleteWithMetadata", String, true)
	public nomenclatorAttributeKeyForAutoCompleteWithMetadata: string = null;

	@JsonProperty("classNameForAutoCompleteWithMetadata", String, true)
	public classNameForAutoCompleteWithMetadata: string = null;

	// Date Metadata
	@JsonProperty("autoCompleteWithCurrentDate", Boolean, true)
	public autoCompleteWithCurrentDate: boolean = undefined;

	// DateTime Metadata
	@JsonProperty("autoCompleteWithCurrentDateTime", Boolean, true)
	public autoCompleteWithCurrentDateTime: boolean = undefined;

	// AutoNumber Metadata
	@JsonProperty("prefix", String, true)
	public prefix: string = undefined;

	@JsonProperty("numberLength", Number, true)
	public numberLength: number = undefined;

	// List Metadata
	@JsonProperty("multipleSelection", Boolean, true)
	public multipleSelection: boolean = undefined;

	@JsonProperty("extendable", Boolean, true)
	public extendable: boolean = undefined;

	@JsonProperty("listItems", [ListMetadataItemModel], true)
	public listItems: ListMetadataItemModel[] = undefined;

	// User Metadata
	@JsonProperty("onlyUsersFromGroup", Boolean, true)
	public onlyUsersFromGroup: boolean = undefined;

	@JsonProperty("idOfGroupOfPermittedUsers", String, true)
	public idOfGroupOfPermittedUsers: string = undefined;
	
	@JsonProperty("autoCompleteWithCurrentUser", Boolean, true)
	public autoCompleteWithCurrentUser: boolean = undefined;

	@JsonProperty("autoCompleteWithCurrentUserStateCode", String, true)
	public autoCompleteWithCurrentUserStateCode: string = undefined;

	// Collection Metadata
	@JsonProperty("metadataDefinitions", [MetadataDefinitionModel], true)
	public metadataDefinitions: MetadataDefinitionModel[] = undefined;

	// Nomenclator
	@JsonProperty("nomenclatorId", Number, true)
	public nomenclatorId: number = undefined;

	@JsonProperty("valueSelectionFilters", [NomenclatorMetadataDefinitionValueSelectionFilterModel], true)
	public valueSelectionFilters: NomenclatorMetadataDefinitionValueSelectionFilterModel[] = undefined;

	// Month
	@JsonProperty("autoCompleteWithCurrentMonth", Boolean, true)
	public autoCompleteWithCurrentMonth: boolean = undefined;

	// Document
	@JsonProperty("metadataDocumentTypeId", Number, true)
	public metadataDocumentTypeId: number = undefined;

	@JsonProperty("multipleDocumentsSelection", Boolean, true)
	public multipleDocumentsSelection: boolean = undefined;
	
	// Project
	@JsonProperty("multipleProjectsSelection", Boolean, true)
	public multipleProjectsSelection: boolean = undefined;
	
	public clone(): MetadataDefinitionModel {
		let clone: MetadataDefinitionModel = new MetadataDefinitionModel;
		clone.id = this.id;
		clone.name = this.name;
		clone.label = this.label;		
		clone.mandatory = this.mandatory;
		clone.restrictedOnEdit = this.restrictedOnEdit;
		clone.mandatoryStates = this.mandatoryStates;
		clone.restrictedOnEditStates = this.restrictedOnEditStates;
		clone.invisible = this.invisible;
		clone.invisibleInStates = this.invisibleInStates;
		clone.representative = this.representative;
		clone.indexed = this.indexed;
		clone.orderNumber = this.orderNumber;
		clone.type = this.type;
		clone.defaultValue = this.defaultValue;
		clone.metadataNameForAutoCompleteWithMetadata = this.metadataNameForAutoCompleteWithMetadata;
		clone.typeOfAutoCompleteWithMetadata = this.typeOfAutoCompleteWithMetadata;
		clone.nomenclatorAttributeKeyForAutoCompleteWithMetadata = this.nomenclatorAttributeKeyForAutoCompleteWithMetadata;
		clone.classNameForAutoCompleteWithMetadata = this.classNameForAutoCompleteWithMetadata;
		clone.autoCompleteWithCurrentDate = this.autoCompleteWithCurrentDate;
		clone.autoCompleteWithCurrentDateTime = this.autoCompleteWithCurrentDateTime;
		clone.prefix = this.prefix;
		clone.numberLength = this.numberLength;
		clone.multipleSelection = this.multipleSelection;
		clone.extendable = this.extendable;		
		if (ArrayUtils.isNotEmpty(this.listItems)) {
			clone.listItems = [];
			this.listItems.forEach((lmiModel: ListMetadataItemModel) => {
				clone.listItems.push(lmiModel.clone());
			});
		}
		clone.onlyUsersFromGroup = this.onlyUsersFromGroup;
		clone.idOfGroupOfPermittedUsers = this.idOfGroupOfPermittedUsers;
		clone.autoCompleteWithCurrentUser = this.autoCompleteWithCurrentUser;
		clone.autoCompleteWithCurrentUserStateCode = this.autoCompleteWithCurrentUserStateCode;
		if (ArrayUtils.isNotEmpty(this.metadataDefinitions)) {
			clone.metadataDefinitions = [];
			this.metadataDefinitions.forEach((mdModel: MetadataDefinitionModel) => {
				clone.metadataDefinitions.push(mdModel.clone());
			});
		}
		clone.nomenclatorId = this.nomenclatorId;
		if (ArrayUtils.isNotEmpty(this.valueSelectionFilters)) {
			clone.valueSelectionFilters = [];
			this.valueSelectionFilters.forEach((filterModel: NomenclatorMetadataDefinitionValueSelectionFilterModel) => {
				clone.valueSelectionFilters.push(filterModel.clone());
			});
		}
		clone.autoCompleteWithCurrentMonth = this.autoCompleteWithCurrentMonth;
		clone.metadataDocumentTypeId = this.metadataDocumentTypeId;
		clone.multipleDocumentsSelection = this.multipleDocumentsSelection;
		clone.multipleProjectsSelection = this.multipleProjectsSelection;
		return clone;
	}
}