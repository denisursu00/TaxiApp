package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import java.util.Collection;
import java.util.Collections;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers.AssignmentHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers.AssignmentHelperFactory;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

/**
 * 
 */
public abstract class BaseAssignmentHandler implements AssignmentHandler {
	
	private static final long serialVersionUID = 1L;
	
	protected void assignToAssignee(OpenExecution execution, Assignable assignable, String assignee) throws AppException {
		if (AssigneeHelper.isUser(assignee)) {
			Long idForAssignedUser = AssigneeHelper.getUserId(assignee);
			assignToUser(execution, assignable, idForAssignedUser);
		} else if (AssigneeHelper.isOrganizationUnit(assignee)) {
			Long idForAssignedOrganizationUnit = AssigneeHelper.getOrganizationUnitId(assignee);
			assignToOrganizationUnit(execution, assignable, idForAssignedOrganizationUnit);
		} else if (AssigneeHelper.isGroup(assignee)) {
			Long idForAssignedGroup = AssigneeHelper.getGroupId(assignee);
			assignToGroup(execution, assignable, idForAssignedGroup);
		} else {
			
			LogHelper logger = LogHelper.getInstance(getClass());
			
			String logMessage = "Asignarea [" + assignee + "] NU este " +
				"suportata de aplicatie. ID-ul executiei este [" + execution.getId() + "].";
			logger.error(logMessage, "asignarea persoanei precedente");
			
			throw new AppException();
		}
	}
	
	protected void assignToUsers(OpenExecution execution, Assignable assignable, Collection<Long> userIds) throws AppException {
		assignTo(execution, assignable, userIds, Collections.<Long> emptySet(), Collections.<Long> emptySet());
	}
	
	protected void assignToUser(OpenExecution execution, Assignable assignable, Long userId) throws AppException {
		assignToUsers(execution, assignable, Collections.singleton(userId));
	}
	
	protected void assignToOrganizationUnit(OpenExecution execution, Assignable assignable, Long organizationUnitId) throws AppException {
		assignTo(execution, assignable, Collections.<Long> emptySet(), Collections.singleton(organizationUnitId), Collections.<Long> emptySet());
	}
	
	protected void assignToGroup(OpenExecution execution, Assignable assignable, Long groupId) throws AppException {
		assignTo(execution, assignable, Collections.<Long> emptySet(), Collections.<Long> emptySet(), Collections.singleton(groupId));
	}
	
	protected void assignTo(OpenExecution execution, Assignable assignable, Collection<Long> idsForAssignedUsers,
			Collection<Long> idsForAssignedOrganizationUnitIds, Collection<Long> idsForAssignedGroups) throws AppException {
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		AssignmentHelperFactory assignmentHelperFactory = SpringContextUtils.getBean("assignmentHelperFactory");
		AssignmentHelper assignmentHelper = assignmentHelperFactory.newHelperFor((ExecutionImpl) execution, assignable,
			userSecurity, idsForAssignedUsers, idsForAssignedOrganizationUnitIds, idsForAssignedGroups);
		
		assignmentHelper.handleAssignment();
	}
}