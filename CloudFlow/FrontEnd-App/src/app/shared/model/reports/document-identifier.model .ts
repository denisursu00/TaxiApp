import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentIdentifierModel {

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;
  
}