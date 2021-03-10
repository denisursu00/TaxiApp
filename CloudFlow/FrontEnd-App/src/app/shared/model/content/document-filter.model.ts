import { JsonObject, JsonProperty } from "json2typescript";
import { MetadataFilterModel } from "./metadata-filter.model";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class DocumentFilterModel {
    
	@JsonProperty("sameType", Boolean)
    public sameType: boolean = null;
    
	@JsonProperty("offset", Number)
    public offset: number = null;
    
	@JsonProperty("pageSize", Number)
    public pageSize: number = null;
    
	@JsonProperty("documentLocationRealName", String)
    public documentLocationRealName: string = null;

	@JsonProperty("folderId", String)
    public folderId: string = null;

    @JsonProperty("documentTypeId", Number)
    public documentTypeId: number = null;
    
	@JsonProperty("nameFilterValue", String)
    public nameFilterValue: string = null;
   
	@JsonProperty("authorFilterValue", String)
    public authorFilterValue: string = null;
   
    @JsonProperty("createdFilterValue", JsonDateConverter)
    public createdFilterValue: Date = null;
   
	@JsonProperty("lockedFilterValue", String)
    public lockedFilterValue: string = null;
   
	@JsonProperty("statusFilterValue", String)
    public statusFilterValue: string = null;
    
	@JsonProperty("metadataFilters", [MetadataFilterModel])
    public metadataFilters: MetadataFilterModel[] = [];
}