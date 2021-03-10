import { MetadataSearchCriteriaModel } from "./metadata-search-criteria.model";
import { CollectionSearchCriteriaModel } from "./collection-search-criteria.model";
import { JsonProperty, JsonObject } from "json2typescript";
import { JsonDateConverter } from "../json-mapper";

@JsonObject
export class DocumentSearchCriteriaModel {
	
	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;
	
	@JsonProperty("createdStart", JsonDateConverter)
	public createdStart: Date = null;
	
	@JsonProperty("createdEnd", JsonDateConverter)
	public createdEnd: Date = null;
	
	@JsonProperty("searchInVersions", Boolean)
	public searchInVersions: boolean = false;
	
	@JsonProperty("documentTypeIdList", [Number])
	public documentTypeIdList: number[] = [];
	
	@JsonProperty("workflowStateIdList", [Number])
	public workflowStateIdList: number[] = [];
	
	@JsonProperty("metadataSearchCriteriaList", [MetadataSearchCriteriaModel])
	public metadataSearchCriteriaList: MetadataSearchCriteriaModel[] = [];
	
	@JsonProperty("collectionSearchCriteriaList", [CollectionSearchCriteriaModel])
	public collectionSearchCriteriaList: CollectionSearchCriteriaModel[] = [];
}