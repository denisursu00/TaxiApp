import { JsonProperty, JsonObject } from "json2typescript";
import { NomenclatorFilter } from "./nomenclator-filter";

@JsonObject
export class NomenclatorMultipleFilter extends NomenclatorFilter {

	@JsonProperty("values", [])
	public values: any[] = undefined;
}