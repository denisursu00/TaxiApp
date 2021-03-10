import { JsonObject, JsonProperty } from "json2typescript";
import { ArrayUtils } from "./../../utils";

@JsonObject
export class NomenclatorRunExpressionsRequestModel {

	@JsonProperty("nomenclatorId", Number)
	public nomenclatorId: number = null;

	@JsonProperty("attributeValueByKey", Object)
	public attributeValueByKey: object = null;
}