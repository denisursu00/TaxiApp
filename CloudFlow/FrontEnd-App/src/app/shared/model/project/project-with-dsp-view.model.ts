import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ProjectWithDspViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("responsibleUserName", String)
	public responsibleUserName: string = null;

	@JsonProperty("degreeOfAchievement", String)
	public degreeOfAchievement: string = null;

	@JsonProperty("importanceDegreeColor", String)
	public importanceDegreeColor: string = null;

	@JsonProperty("subprojects", [String])
	public subprojects: string[] = [];

	public importanceDegreeColorForSort: number;

	public get degreeOfAchievementForView(): string {
		if (this.degreeOfAchievement === ProjectDegreeOfAchievement.NESATISFACATOR) {
			return "nesatisfacator";
		} else if (this.degreeOfAchievement === ProjectDegreeOfAchievement.SATISFACATOR) {
			return "satisfacator";
		} else if (this.degreeOfAchievement === ProjectDegreeOfAchievement.BINE) {
			return "bine";
		} else if (this.degreeOfAchievement === ProjectDegreeOfAchievement.FOARTE_BINE) {
			return "foarte bine";
		}
	}
}

export enum ProjectDegreeOfAchievement {
	
	NESATISFACATOR = "NESATISFACATOR",
	SATISFACATOR = "SATISFACATOR",
	BINE = "BINE",
	FOARTE_BINE = "FOARTE_BINE"
}
