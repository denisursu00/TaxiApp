import { JsonObject, JsonProperty } from "json2typescript";
import { NomenclatorAttributeModel } from "./nomenclator-attribute.model";

@JsonObject
export class SaveNomenclatorValueResponseModel {
	
	public static readonly STATUS_SUCCESS: string = "SUCCESS";
	public static readonly STATUS_ERROR: string = "ERROR";

	@JsonProperty("status", String)
	public status: string = null;

	@JsonProperty("messages", [String], true)
	public messages: string[] = [];
}