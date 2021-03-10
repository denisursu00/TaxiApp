import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class TasksParticipantViewModel {

	@JsonProperty("numeParticipant", String)
	public numeParticipant: string = null;
}