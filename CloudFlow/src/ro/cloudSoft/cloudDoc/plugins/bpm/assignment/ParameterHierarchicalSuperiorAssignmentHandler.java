package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowMetadataVariableHelper;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

import com.google.common.collect.Iterables;

public class ParameterHierarchicalSuperiorAssignmentHandler extends BaseAssignmentHandler {
	
	private static final long serialVersionUID = 4933759906077565495L;
	
	private final static LogHelper LOGGER = LogHelper.getInstance(ParameterHierarchicalSuperiorAssignmentHandler.class);
	
	private String userMetadataDefinition;
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		if (StringUtils.isBlank(userMetadataDefinition)) {
			
			String logMessage = "NU s-a specificat numele definitiei metadatei de tip user pentru asignarea de " +
				"tip parametru. ID-ul definitiei de proces este [" + execution.getProcessDefinitionId() + "].";
			LOGGER.error(logMessage, "asignarea pe baza de parametru");
			
			throw new AppException();
		}

		Collection<String> userMetadataDefinitionValues = WorkflowMetadataVariableHelper.getMetadataValues(execution, userMetadataDefinition);
		String userMetadataDefinitionValue = Iterables.getOnlyElement(userMetadataDefinitionValues);
		if (userMetadataDefinitionValue == null) {
			
			String logMessage = "NU s-a completat metadata de tip utilizator cu numele " +
				"[" + userMetadataDefinition + "]. ID-ul executiei este [" + execution.getId() + "].";
			LOGGER.error(logMessage, "asignarea pe baza de parametru");
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}

		Long userIdFromMetadataValue = MetadataValueHelper.getUserId(userMetadataDefinitionValue);
		
		User userHierarchicalSuperior = getUserService().getSuperior(userIdFromMetadataValue);
		
		assignToUser(execution, assignable, userHierarchicalSuperior.getId());
	}
	
	public void setUserMetadataDefinition(String userMetadataDefinition) {
		this.userMetadataDefinition = userMetadataDefinition;
	}
	
	private UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
}