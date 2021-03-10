package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class HierarchicalSubalternAssignmentHandler extends BaseAssignmentHandler {

	private static final long serialVersionUID = 1L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(HierarchicalSubalternAssignmentHandler.class);
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {

		UserService userService = SpringContextUtils.getBean("userService");
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		if (userSecurity == null) {
			
			String logMessage = "NU s-au gasit datele utilizatorului curent.";
			LOGGER.error(logMessage, "asignarea utilizatorilor subalterni");
			
			throw new AppException();
		}
		Long userId = Long.valueOf(userSecurity.getUserIdAsString());
		
		Set<Long> subordonateUserIds = userService.getAllSubordinateUserIds(userId, userSecurity);
		if (CollectionUtils.isEmpty(subordonateUserIds)) {
			
			String logMessage = "NU s-au gasit utilizatori subalterni pentru utilizatorul cu ID-ul [" + userId + "].";
			LOGGER.error(logMessage, "asignarea utilizatorilor subalterni", userSecurity);
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}
		
		assignToUsers(execution, assignable, subordonateUserIds);
	}	
}