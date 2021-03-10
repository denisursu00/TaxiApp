package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class HierarchicalSuperiorAssignmentHandler extends BaseAssignmentHandler {
	
	private static final long serialVersionUID = 1L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(HierarchicalSuperiorAssignmentHandler.class);

	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {

		UserService userService = SpringContextUtils.getBean("userService");
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		if (userSecurity == null) {
			
			String logMessage = "NU s-au gasit datele utilizatorului curent.";
			LOGGER.error(logMessage, "asignarea utilizatorului ierarhic superior");
			
			throw new AppException();
		}
		Long userId = userSecurity.getUserId();
		
		User superior = userService.getSuperior(userId);
		if (superior == null) {
			
			String logMessage = "NU s-a gasit superiorul pentru utilizatorul cu ID-ul [" + userId + "].";
			LOGGER.error(logMessage, "asignarea utilizatorului ierarhic superior", userSecurity);
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}
		
		Long superiorUserId = superior.getId();
		assignToUser(execution, assignable, superiorUserId);
	}	
}