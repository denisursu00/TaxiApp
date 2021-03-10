package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import java.util.Collection;

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

public class HierarchicalInferiorAssignmentHandler extends BaseAssignmentHandler {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(HierarchicalInferiorAssignmentHandler.class);

	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {

		UserService userService = SpringContextUtils.getBean("userService");
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		if (userSecurity == null) {
			
			String logMessage = "NU s-au gasit datele utilizatorului curent.";
			LOGGER.error(logMessage, "asignarea utilizatorilor ierarhic inferiori");
			
			throw new AppException();
		}
		Long userId = userSecurity.getUserId();
		
		Collection<Long> idsForDirectlySubordinateManagersOrUsers = userService.getDirectlySubordinateUserIds(userId, userSecurity);
		if (CollectionUtils.isEmpty(idsForDirectlySubordinateManagersOrUsers)) {
			
			String logMessage = "NU s-au gasit utilizatori ierarhic inferiori utilizatorului cu ID-ul [" + userId + "].";
			LOGGER.error(logMessage, "asignarea utilizatorilor ierarhic inferiori", userSecurity);
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}
		
		assignToUsers(execution, assignable, idsForDirectlySubordinateManagersOrUsers);
	}
}