import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NumarDecizieDeplasareModel {

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("numarDecizie", String)
	public numarDecizie: string = null;
}