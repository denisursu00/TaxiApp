package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.mail.OutOfOfficeMailService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

/**
 * 
 */
public class ReturnedReplacementProfileHelper extends ExpireOrReturnedReplacementProfileHelper {

	public ReturnedReplacementProfileHelper(ReplacementProfile replacementProfile, DocumentService documentService, WorkflowExecutionService workflowExecutionService,
			ReplacementProfileInstanceService replacementProfileInstanceService, OutOfOfficeMailService outOfOfficeMailService, UserService userService,
			ReplacementProfileDao replacementProfileDao) {
		
		super(
			
			replacementProfile,
			ReplacementProfileStatusOption.RETURNED,
			
			documentService,
			workflowExecutionService,
			replacementProfileInstanceService,
			outOfOfficeMailService,
			userService,
			
			replacementProfileDao
		);
	}
	
	public void returned() throws AppException {
		expireOrReturned();
	}
}