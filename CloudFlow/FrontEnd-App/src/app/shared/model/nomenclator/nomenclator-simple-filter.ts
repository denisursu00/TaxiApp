import { JsonProperty, JsonObject } from "json2typescript";
import { NomenclatorFilter } from "./nomenclator-filter";

@JsonObject
export class NomenclatorSimpleFilter extends NomenclatorFilter {

	@JsonProperty("value", Object)
	public value: any = undefined;
}