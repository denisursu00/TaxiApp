import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "./../../../json-mapper";

@JsonObject
export class DocumentSelectionSearchFilterModel {

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string;

	@JsonProperty("documentTypeId", Number)
	public documentTypeId: number;

	@JsonProperty("documentName", String)
	public documentName: string;
}