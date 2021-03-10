import { DocumentModel, DocumentTypeModel, WorkflowModel, WorkflowStateModel } from "@app/shared";

export interface DocumentViewOrEditInfo {
	documentType: DocumentTypeModel;
	document: DocumentModel;
	workflow: WorkflowModel;
	currentState?: WorkflowStateModel;	
	canUserSend?: boolean;
	canUserEdit?: boolean;
	canUserAccessLockedDocument?: boolean;
}