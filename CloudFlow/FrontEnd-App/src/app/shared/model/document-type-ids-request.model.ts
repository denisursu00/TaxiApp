import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentTypeIdsRequestModel {

	@JsonProperty("ids", [Number])
	public ids: number[] = [];
}