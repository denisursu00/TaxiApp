package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;
import java.util.Set;

import org.jbpm.api.task.Assignable;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.AssigneeHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

import com.google.common.collect.Sets;

/**
 * Se ocupa cu asignarea efectiva a unui task mai multor entitati organizatorice.
 * 
 * 
 */
public class AssignTaskAssignmentHelper {

	private static final LogHelper LOGGER = LogHelper.getInstance(AssignTaskAssignmentHelper.class);

	
	private final Collection<Long> idsForAssignedUsers;
	private final Collection<Long> idsForAssignedOrganizationUnits;
	private final Collection<Long> idsForAssignedGroups;

	private final ExecutionImpl execution;
	private final Assignable assignable;
	
	private final SecurityManager userSecurity;

	
	private Set<String> assignees;
	
	
	public AssignTaskAssignmentHelper(Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnits,
			Collection<Long> idsForAssignedGroups, ExecutionImpl execution, Assignable assignable, SecurityManager userSecurity) {
		
		this.idsForAssignedUsers = idsForAssignedUsers;
		this.idsForAssignedOrganizationUnits = idsForAssignedOrganizationUnits;
		this.idsForAssignedGroups = idsForAssignedGroups;
		
		this.execution = execution;
		this.assignable = assignable;
		
		this.userSecurity = userSecurity;
	}
	
	public void assign() throws AppException {
		
		assignees = Sets.newHashSet();
		
		for (Long idForOriginallyAssignedUser : idsForAssignedUsers) {
			String assignee = AssigneeHelper.getAssigneeValueForUser(idForOriginallyAssignedUser);
			assignees.add(assignee);
		}
		
		for (Long idForOriginallyAssignedOrganizationUnit : idsForAssignedOrganizationUnits) {
			String assignee = AssigneeHelper.getAssigneeValueForOrganizationUnit(idForOriginallyAssignedOrganizationUnit);
			assignees.add(assignee);
		}
		
		for (Long idForOriginallyAssignedGroup : idsForAssignedGroups) {
			String assignee = AssigneeHelper.getAssigneeValueForGroup(idForOriginallyAssignedGroup);
			assignees.add(assignee);
		}
		
		if (assignees.isEmpty()) {

			String exceptionMessage = "NU s-a asignat nici o entitate organizatorica pentru task-ul executiei cu ID-ul [" + execution.getId() + "].";
			LOGGER.error(exceptionMessage, "asignare", userSecurity);
			
			throw new AppException(AppExceptionCodes.DOCUMENT_RECIPIENT_CANNOT_BE_DETERMINED);
		}
		
		for (String assignee : assignees) {
			assignable.addCandidateUser(assignee);
		}
	}
	
	public Set<String> getAssignees() {
		if (assignees == null) {
			throw new IllegalStateException("Trebuie rulata intai asignarea.");
		}
		return assignees;
	}
}