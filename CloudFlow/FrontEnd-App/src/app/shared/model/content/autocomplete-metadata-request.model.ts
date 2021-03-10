import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";

@JsonObject
export class AutocompleteMetadataRequestModel {

	@JsonProperty("sourceMetadataValues", [String])
	public sourceMetadataValues: string[] = null;

	@JsonProperty("targetMetadataCollectionDefinitionId", Number)
	public targetMetadataCollectionDefinitionId: number = null;
}