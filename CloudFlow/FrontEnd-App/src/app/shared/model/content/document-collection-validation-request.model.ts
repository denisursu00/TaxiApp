import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentValidationRequestModel } from "./document-validation-request.model";

@JsonObject
export class DocumentCollectionValidationRequestModel extends DocumentValidationRequestModel {
	
	@JsonProperty("metadataCollectionDefinitionId", Number)
	public metadataCollectionDefinitionId: number = null;
}