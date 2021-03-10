import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { TaskModel, AppError, CompleteTaskRequestModel, ProjectModel, TaskViewModel, UserModel, PageRequest,
		GetPagedProjectTaskViewModelsRequestModel, PagingList, ProjectWithDspViewModel, GradDeRealizarePentruProiecteleCuDspModel, DspViewModel, ProjectViewModel, TasksViewModel, TaskEventModel} from "../model";
import { ApiPathUtils } from "../utils";
import { ApiPathConstants } from "../constants";
import { AsyncCallback } from "../async-callback";
import { Observable } from "rxjs/Observable";
import { StringUtils } from "../utils/string-utils";
import { ExportType } from "../enums/export-type";
import { HttpResponse } from "@angular/common/http";
import { ProjectSubactivityModel } from "../model/project/project-subactivity.model";

@Injectable()
export class ProjectService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getUserTasks(userId: number, callback: AsyncCallback<TaskModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USER_TASKS, userId.toString()), null, TaskModel, callback);
	}

	public getTask(taskId: number, callback: AsyncCallback<TaskModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_TASK, taskId.toString()), null, TaskModel, callback);
	}

	public saveTask(task: TaskModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_TASK, task, null, callback);
	}

	public completeTask(requestModel: CompleteTaskRequestModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.COMPLETE_TASK, requestModel, null, callback);
	}

	public cancelTask(taskId: number, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CANCEL_TASK, taskId.toString()), null, null, callback);
	}

	public getAllProjects(callback: AsyncCallback<ProjectModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_PROJECTS, null, ProjectModel, callback);
	}

	public getUserInProgressTasksModels(userId: number, callback: AsyncCallback<TaskViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USER_IN_PROGRESS_TASKS_MODELS, userId.toString()), null, TaskViewModel, callback);
	}

	public getTaskViewModel(taskId: number, callback: AsyncCallback<TaskViewModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_TASK_VIEW_MODEL, taskId.toString()), null, TaskViewModel, callback);
	}

	public getUserProjects(callback: AsyncCallback<ProjectModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_USER_PROJECTS, null, ProjectModel, callback);
	}

	public saveProject(project: ProjectModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_PROJECT, project, null, callback);
	}

	public getProjectParticipants(projectId: number, callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_PROJECT_PARTICIPANTS, projectId.toString()), null, UserModel, callback);
	}

	public getProjectById(projectId: number, callback: AsyncCallback<ProjectModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_PROJECT_BY_ID, projectId.toString()), null, ProjectModel, callback);
	}

	public closeProject(projectId: number, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CLOSE_PROJECT, projectId.toString()), null, null, callback);
	}
	
	public getPagedProjectTaskViewModels(pageRequest: PageRequest<GetPagedProjectTaskViewModelsRequestModel>, callback: AsyncCallback<PagingList<TaskViewModel>, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_PAGED_PROJECT_TASK_VIEW_MODELS, pageRequest, PagingList, callback);
	}
	
	public getProjectWithDspViewModels(allProjects: boolean, callback: AsyncCallback<ProjectWithDspViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_PROJECT_WITH_DSP_VIEW_MODELS, allProjects.toString()), null, ProjectWithDspViewModel, callback);
	}

	public getGradDeRealizarePentruProiecteleCuDspModel(callback: AsyncCallback<GradDeRealizarePentruProiecteleCuDspModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_GRAD_DE_REALIZARE_PENTRU_PROIECTELE_CU_DSPMODEL, null, GradDeRealizarePentruProiecteleCuDspModel, callback);
	}
	
	public getAllOpenedProjectsWithDsp(callback: AsyncCallback<ProjectModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_OPENED_PROJECTS_WITH_DSP, null, ProjectModel, callback);
	}

	public getDspViewModelByProiectId(projectId: number, callback: AsyncCallback<DspViewModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DSP_VIEW_MODEL, projectId.toString());
		this.apiCaller.call(relativePath, null, DspViewModel, callback);
	}

	public getTasksViewModelByProjectId(projectId: number, callback: AsyncCallback<TasksViewModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_TASKS_VIEW_MODEL, projectId.toString());
		this.apiCaller.call(relativePath, null, TasksViewModel, callback);
	}

	public downloadTaskAttachment(taskAttachmentId: number): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_TASK_ATTACHMENT, taskAttachmentId.toString());
		return this.apiCaller.download(relativePath);
	}

	public getProjectViewModelById(projectId: number, callback: AsyncCallback<ProjectViewModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_PROJECT_VIEW_MODEL_BY_ID, projectId.toString()), null, ProjectViewModel, callback);
	}

	public exportDsp(projectId: number, exportType: ExportType): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.EXPORT_DSP, String(projectId), exportType);
		return this.apiCaller.download(relativePath);
	}

	public getAllInProgressTaskNamesByProjectAbbreviation(projectAbbreviation: string, callback: AsyncCallback<String[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_IN_PROGRESS_TASK_NAMES_BY_PROJECT_ABREVIATION, projectAbbreviation), null, String, callback);
	}

	public existsAbbreviation(projectAbbreviation: string, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.EXISTS_PROJECT_BY_ABBREVIATION, projectAbbreviation), null, Boolean, callback);
	}

	public existsName(projectName: string, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.EXISTS_PROJECT_BY_NAME, projectName), null, Boolean, callback);
	}

	public getAllTaskEvents(callback: AsyncCallback<TaskEventModel[], AppError>): void{
		this.apiCaller.call(ApiPathConstants.GET_ALL_TASK_EVENTS, null, TaskEventModel, callback);
	}

	public updateTaskEventDescription(taskEvent: TaskEventModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.UPDATE_TASK_EVENT_DESCRIPTION, taskEvent, null, callback);
	}

	public getProjectSubactivities(projectId: number, callback: AsyncCallback<ProjectSubactivityModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_PROJECT_SUBACTIVITIES, projectId.toString()), null, ProjectSubactivityModel, callback);
	}

	public isSubactivityUsedInAnyTask(subactivityId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_SUBACTIVITY_USED_IN_ANY_TASK, subactivityId.toString()), null, Boolean, callback);
	}
}