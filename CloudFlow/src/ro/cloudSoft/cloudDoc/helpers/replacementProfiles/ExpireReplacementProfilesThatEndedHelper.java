package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.util.Collection;
import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.mail.OutOfOfficeMailService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;

/**
 * 
 */
public class ExpireReplacementProfilesThatEndedHelper {
	
	private final Date referenceDate;
	
	private final DocumentService documentService;
	private final WorkflowExecutionService workflowExecutionService;
	private final ReplacementProfileInstanceService replacementProfileInstanceService;
	private final OutOfOfficeMailService outOfOfficeMailService;
	private final UserService userService;
	
	private final ReplacementProfileDao replacementProfileDao;
	
	public ExpireReplacementProfilesThatEndedHelper(Date referenceDate, DocumentService documentService, WorkflowExecutionService workflowExecutionService,
			ReplacementProfileInstanceService replacementProfileInstanceService, OutOfOfficeMailService outOfOfficeMailService, UserService userService,
			ReplacementProfileDao replacementProfileDao) {
		
		this.referenceDate = referenceDate;
		
		this.documentService = documentService;
		this.workflowExecutionService = workflowExecutionService;
		this.replacementProfileInstanceService = replacementProfileInstanceService;
		this.outOfOfficeMailService = outOfOfficeMailService;
		this.userService = userService;
		
		this.replacementProfileDao = replacementProfileDao;
	}

	public void expire() throws AppException {
		Collection<ReplacementProfile> activeReplacementProfilesThatEnded = replacementProfileDao.getActiveReplacementProfilesThatEnded(referenceDate);
		for (ReplacementProfile replacementProfile : activeReplacementProfilesThatEnded) {
			new ExpireReplacementProfileHelper(replacementProfile, documentService,
				workflowExecutionService, replacementProfileInstanceService,
				outOfOfficeMailService, userService, replacementProfileDao)
				.expire();
		}
	}
}