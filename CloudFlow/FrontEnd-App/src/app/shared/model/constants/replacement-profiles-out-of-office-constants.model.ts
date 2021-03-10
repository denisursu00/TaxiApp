import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ReplacementProfilesOutOfOfficeConstantsModel {
	
	@JsonProperty("templatingPlaceholderForRequesterName", String)
	public templatingPlaceholderForRequesterName: string = null;
	
	@JsonProperty("templatingPlaceholderForStartDate", String)
	public templatingPlaceholderForStartDate: string = null;
	
	@JsonProperty("templatingPlaceholderForEndDate", String)
	public templatingPlaceholderForEndDate: string = null;
	
	@JsonProperty("defaultTemplateForEmailSubject", String)
	public defaultTemplateForEmailSubject: string = null;
	
	@JsonProperty("defaultTemplateForEmailBody", String)
	public defaultTemplateForEmailBody: string = null;
}