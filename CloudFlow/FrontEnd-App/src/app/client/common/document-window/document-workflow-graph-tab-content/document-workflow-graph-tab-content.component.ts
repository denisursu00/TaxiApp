import { Component, Input, OnInit } from "@angular/core";
import { AppError, MessageDisplayer, IconConstants, UrlBuilder, ServletPathConstants, Message } from "@app/shared";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentModel, ObjectUtils } from "@app/shared";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";
import { environment } from "@app/../environments/environment";

@Component({
	selector: "app-document-workflow-graph-tab-content",
	templateUrl: "./document-workflow-graph-tab-content.component.html"
})
export class DocumentWorkflowGraphTabContentComponent extends DocumentTabContent {

	private static readonly URL_PARAMETER_WORKFLOW_ID: string = "workflowId";
	private static readonly URL_PARAMETER_CODE_FOR_CURRENT_STATE: string = "codeForCurrentState";
	private static readonly URL_PARAMETER_DOCUMENT_ID: string = "documentId";

	public workflowGraphImageUrl: string;

	public constructor() {
		super();
	}

	protected doWhenOnInit(): void {
		// Nothing here.
	}

	protected prepareForAdd(): void {
		const addInfo: DocumentAddInfo = <DocumentAddInfo> this.inputData;
		if (ObjectUtils.isNullOrUndefined(this.inputData.workflow)) {
			return;
		}
		this.workflowGraphImageUrl = this.buildWorkflowGraphImageUrl(addInfo.workflow.id, addInfo.currentState.code, null);
	}
	
	protected prepareForViewOrEdit(): void {
		const viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;
		if (ObjectUtils.isNullOrUndefined(viewOrEditInfo.workflow)) {
			return;
		}
		let currentStateCode: string = null;
		if (ObjectUtils.isNotNullOrUndefined(viewOrEditInfo.currentState)) {
			currentStateCode = viewOrEditInfo.currentState.code;
		}
		this.workflowGraphImageUrl = this.buildWorkflowGraphImageUrl(viewOrEditInfo.workflow.id, currentStateCode, viewOrEditInfo.document.id);
	}

	private buildWorkflowGraphImageUrl(workflowId: number, documentWorkflowStateCode: string, documentId: string): string {
		let workflowGraphImageUrl = ServletPathConstants.GET_WORKFLOW_IMAGE_SERVLET;
		let urlBuilder: UrlBuilder = new UrlBuilder(
			workflowGraphImageUrl, {
				key: DocumentWorkflowGraphTabContentComponent.URL_PARAMETER_WORKFLOW_ID,
				value: workflowId 
			}, {
				key: DocumentWorkflowGraphTabContentComponent.URL_PARAMETER_CODE_FOR_CURRENT_STATE,
				value: documentWorkflowStateCode
			}, {
				key: DocumentWorkflowGraphTabContentComponent.URL_PARAMETER_DOCUMENT_ID,
				value: documentId
			}, {
				key: "random",
				value: Math.floor(Math.random() * 999999) + 1
			}
		);
		return urlBuilder.build();
	}

	public isValid(): boolean {
		return true;
	}

	public populateDocument(document: DocumentModel): void {
		// Nu se implementeaza.
	}

	public getMessages(): Message[] {
		return [];
	}
}