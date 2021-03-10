import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";
import { MetadataCollectionInstanceRowModel } from "./metadata-collection-instance-row.model";

@JsonObject
export class MetadataCollectionInstanceModel {

	@JsonProperty("metadataDefinitionId", Number)
	public metadataDefinitionId: number = null;

	@JsonProperty("collectionInstanceRows", [MetadataCollectionInstanceRowModel])
	public collectionInstanceRows: MetadataCollectionInstanceRowModel[] = [];
}