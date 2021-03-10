import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../json-mapper";
import { DocumentMetadataViewModel } from "./document-metadata-view.model";
import { WorkflowConstants } from "../constants";
import { WorkflowInstanceResponseModel } from "./bpm";

@JsonObject
export class DocumentViewModel {

	@JsonProperty("documentId", String)
	public documentId: string = null;
	
	@JsonProperty("documentName", String)
	public documentName: string = null;
	
	@JsonProperty("documentTypeName", String)
	public documentTypeName: string = null;
	
	@JsonProperty("documentAuthorName", String)
	public documentAuthorName: string = null;
	
	@JsonProperty("documentCreatedDate", JsonDateConverter)
	public documentCreatedDate: Date = null;
	
	@JsonProperty("documentLastModifiedDate", JsonDateConverter)
	public documentLastModifiedDate: Date = null;
	
	@JsonProperty("documentLockedByName", String)
	public documentLockedByName: string = null;
	
	@JsonProperty("documentRepresentativeMetadataLabels", [String])
	public documentRepresentativeMetadataLabels: string[] = [];

	@JsonProperty("documentMetadata", [DocumentMetadataViewModel])
	public documentMetadata: DocumentMetadataViewModel[] = [];

	@JsonProperty("locked", Boolean)
	public locked: boolean = null;

	@JsonProperty("documentStatus", String)
	public documentStatus: string = null;

}