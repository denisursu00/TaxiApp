import { JsonObject, JsonProperty } from "json2typescript";
import { ArrayUtils } from "./../../utils";

@JsonObject
export class NomenclatorRunExpressionsResponseModel {
	
	@JsonProperty("resultsByAttributeKey", Object)
	public resultsByAttributeKey: object = null;
}