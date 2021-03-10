package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;

import org.jbpm.api.task.Assignable;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * 
 */
public interface AssignmentHelperFactory {

	public AssignmentHelper newHelperFor(ExecutionImpl execution, Assignable assignable, SecurityManager userSecurity,
		Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnitIds, Collection<Long> idsForAssignedGroups);
}