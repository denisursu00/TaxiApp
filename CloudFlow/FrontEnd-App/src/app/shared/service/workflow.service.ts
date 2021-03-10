import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { WorkflowStateModel } from "./../model/bpm/workflow-state.model";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiPathConstants } from "./../constants/api-path.constants";
import { DocumentTypeIdsRequestModel, WorkflowModel } from "../model";
import { ApiPathUtils } from "./../utils";

@Injectable()
export class WorkflowService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllWorkflows(callback: AsyncCallback<WorkflowModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_WORKFLOWS, null, WorkflowModel, callback);
	}
	
	public getStatesByDocumentTypeIds(documentTypeIdsRequestModel: DocumentTypeIdsRequestModel, callback: AsyncCallback<WorkflowStateModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_STATES_BY_DOCUMENT_TYPE_IDS, documentTypeIdsRequestModel, WorkflowStateModel, callback);
	}

	public getWorkflowStatesByDocumentType(documentTypeId: number, callback: AsyncCallback<WorkflowStateModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_WORKFLOW_STATES_BY_DOCUMENT_TYPE, documentTypeId.toString());
		this.apiCaller.call(relativePath, null, WorkflowStateModel, callback);
	}

	public getWorkflowById(workflowId: number, callback: AsyncCallback<WorkflowModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_WORKFLOW_BY_ID, workflowId.toString());
		this.apiCaller.call(relativePath, null, WorkflowModel, callback);
	}

	public saveWorkflow(workflow: WorkflowModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_WORKFLOW, workflow, null, callback);
	}

	public hasInstances(workflowId: number, callback: AsyncCallback<Boolean, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.HAS_INSTANCES, workflowId.toString());
		this.apiCaller.call(relativePath, null, Boolean, callback);
	}

	public createNewVersion(workflowId: number, callback: AsyncCallback<Number, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CREATE_NEW_VERSION, workflowId.toString());
		this.apiCaller.call(relativePath, null, Number, callback);
	}

	public deleteWorkflow(workflowId: number, callback: AsyncCallback<null, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_WORKFLOW, workflowId.toString());
		this.apiCaller.call(relativePath, null, null, callback);
	}
}