import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { TasksComisieGlViewModel } from "./tasks-comisie-gl-view.model";
import { TasksParticipantViewModel } from "./tasks-participant-view.model";
import { TasksActivitateViewModel } from "./tasks-activitate-view.model";

@JsonObject
export class TasksViewModel {

	@JsonProperty("projectId", Number)
	public projectId: number = null;
	
	@JsonProperty("numeProiect", String)
	public numeProiect: string = null;

	@JsonProperty("abreviereProiect", String)
	public abreviereProiect: string = null;

	@JsonProperty("descriere", String)
	public descriere: string = null;

	@JsonProperty("utilizatorResponsabil", String)
	public utilizatorResponsabil: string = null;

	@JsonProperty("dataInceputProiect", JsonDateConverter)
	public dataInceputProiect: Date = null;

	@JsonProperty("dataSfarsitProiect", JsonDateConverter)
	public dataSfarsitProiect: Date = null;

	@JsonProperty("dataImplementarii", JsonDateConverter)
	public dataImplementarii: Date = null;
		
	@JsonProperty("gradDeRealizareEstimatDeResponsabil", Number)
	public gradDeRealizareEstimatDeResponsabil: number = null;
    
	@JsonProperty("comisiiGlImplicate", [TasksComisieGlViewModel])
    public comisiiGlImplicate: TasksComisieGlViewModel[] = [];   

	@JsonProperty("participanti", [TasksParticipantViewModel])
	public participanti: TasksParticipantViewModel[] = [];

	@JsonProperty("activitati", [TasksActivitateViewModel])
	public activitati: TasksActivitateViewModel[] = [];
}