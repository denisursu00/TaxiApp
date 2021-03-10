import { JsonObject, JsonProperty } from "json2typescript";
import { NomenclatorFilter } from "./nomenclator-filter";
import { NomenclatorSortedAttribute } from "./nomenclator-sorted-attribute";

@JsonObject
export class NomenclatorValueAsViewSearchRequestModel {

	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("pageSize", Number)
	public pageSize: number = undefined;

	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number = undefined;

	@JsonProperty("valueIds", [Number], true)
	public valueIds: number[] = [];

	@JsonProperty("filters", [NomenclatorFilter], true)
	public filters: NomenclatorFilter[] = [];

	@JsonProperty("sortedAttributes", [NomenclatorSortedAttribute])
	public sortedAttributes: NomenclatorSortedAttribute[] = [];
}