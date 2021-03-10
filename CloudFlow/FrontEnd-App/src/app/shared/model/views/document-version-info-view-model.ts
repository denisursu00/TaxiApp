import { JsonObject, JsonProperty } from "json2typescript";
import { DateConstants } from "../../constants/date.constants";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DocumentVersionInfoViewModel {
	
	@JsonProperty("versionNumber", String)
	public versionNumber: string = null;
	
	@JsonProperty("versionAuthor", String)
	public versionAuthor: string = null;
	
	@JsonProperty("versionCreationDate", JsonDateConverter)
	public versionCreationDate: Date = null;

	public versionCreationDateForView: string;
}