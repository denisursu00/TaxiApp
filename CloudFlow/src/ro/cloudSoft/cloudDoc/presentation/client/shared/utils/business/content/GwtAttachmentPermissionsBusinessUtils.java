package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

public class GwtAttachmentPermissionsBusinessUtils {
	
	/**
	 * Verifica daca pot fi adaugate atasamente documentului.
	 * @param currentState starea curenta pe flux a documentului
	 */
	public static boolean canAddAttachments(WorkflowStateModel currentState) {
		if (currentState == null) {
			return true;
		}		
		int attachmentsPermission = currentState.getAttachmentsPermission();		
		return ((attachmentsPermission == WorkflowStateModel.ATTACH_PERM_ALL) || (attachmentsPermission == WorkflowStateModel.ATTACH_PERM_ADD));
	}

	/**
	 * Verifica daca pot fi sterse atasamente ale documentului.
	 * @param currentState starea curenta pe flux a documentului
	 */
	public static boolean canDeleteAttachments(WorkflowStateModel currentState) {
		if (currentState == null) {
			return true;
		}		
		int attachmentsPermission = currentState.getAttachmentsPermission();		
		return ((attachmentsPermission == WorkflowStateModel.ATTACH_PERM_ALL) || (attachmentsPermission == WorkflowStateModel.ATTACH_PERM_DELETE));
	}
}