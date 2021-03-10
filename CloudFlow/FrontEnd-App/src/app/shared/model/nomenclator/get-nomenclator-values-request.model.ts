import { JsonObject, JsonProperty } from "json2typescript";
import { NomenclatorFilter } from "./nomenclator-filter";

@JsonObject
export class GetNomenclatorValuesRequestModel {

	@JsonProperty("nomenclatorCode", String)
	public nomenclatorCode: string = null;

	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number = null;

	@JsonProperty("filters", [NomenclatorFilter])
	public filters: NomenclatorFilter[] = [];
}