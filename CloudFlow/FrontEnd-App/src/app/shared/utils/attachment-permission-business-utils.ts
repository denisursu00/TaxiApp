import { WorkflowStateModel } from "../model/bpm";

export class AttachmentPermissionBusinessUtils {

	public static canAddAttachments(currentState: WorkflowStateModel): boolean {
		if (currentState === null || currentState === undefined) {
			return true;
		}
		let attachmentPermission: number = currentState.attachmentsPermission;
		return ((attachmentPermission === WorkflowStateModel.ATTACH_PERM_ALL) || (attachmentPermission === WorkflowStateModel.ATTACH_PERM_ADD));
	}

	public static canDeleteAttachments(currentState: WorkflowStateModel): boolean {
		if (currentState === null || currentState === undefined) {
			return true;
		}		
		let attachmentsPermission: number = currentState.attachmentsPermission;		
		return ((attachmentsPermission === WorkflowStateModel.ATTACH_PERM_ALL) || (attachmentsPermission === WorkflowStateModel.ATTACH_PERM_DELETE));
	}
}