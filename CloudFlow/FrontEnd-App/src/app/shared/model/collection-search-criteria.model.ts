import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CollectionSearchCriteriaModel {

	@JsonProperty("collectionDefinitionId", Number)
	public collectionDefinitionId: number = null;

	@JsonProperty("metadataDefinitionId", Number)
	public metadataDefinitionId: number = null;

	@JsonProperty("value", String)
	public value: string = null;

}