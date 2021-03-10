import { DocumentTypeModel, WorkflowStateModel } from "@app/shared";

export interface DocumentTypeEditInfo {
	documentType: DocumentTypeModel;
	workflowStates: WorkflowStateModel[];
}