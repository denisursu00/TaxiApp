package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class GroupAssignmentHandler extends BaseAssignmentHandler {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(GroupAssignmentHandler.class);
	
	private String groupId;
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		
		if (StringUtils.isBlank(groupId)) {
			
			String logMessage = "S-a incercat asignarea catre un grup, insa ID-ul grupului NU este specificat " +
				"in handler. ID-ul definitiei de proces este [" + execution.getProcessDefinitionId() + "].";
			LOGGER.error(logMessage, "asignarea unui grup");
			
			throw new AppException();
		}
		Long groupIdAsLong = Long.valueOf(groupId);
		
		assignToGroup(execution, assignable, groupIdAsLong);
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}