import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataInstanceModel } from "./metadata-instance.model";

@JsonObject
export class MetadataCollectionInstanceRowModel {

	@JsonProperty("id", String)
	public id: string = null;

	@JsonProperty("metadataInstanceList", [MetadataInstanceModel])
	public metadataInstanceList: MetadataInstanceModel[] = [];
}