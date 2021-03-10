import { DocumentModel, DocumentTypeModel, WorkflowModel, WorkflowStateModel } from "@app/shared";

export interface DocumentAddInfo {
	documentType: DocumentTypeModel;
	documentLocationRealName: string;
	parentFolderId: string;
	workflow: WorkflowModel;
	currentState: WorkflowStateModel;
	
	canUserSend: boolean;
}

