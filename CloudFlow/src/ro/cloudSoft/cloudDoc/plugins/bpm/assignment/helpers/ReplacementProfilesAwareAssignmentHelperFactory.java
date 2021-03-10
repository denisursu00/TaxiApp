package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;

import org.jbpm.api.task.Assignable;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.TransitionNotificationsHandlerFactory;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfileInstanceService;
import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfilesService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ReplacementProfilesAwareAssignmentHelperFactory implements AssignmentHelperFactory, InitializingBean {
	
	private ReplacementProfilesService replacementProfilesService;
	private ReplacementProfileInstanceService replacementProfileInstanceService;
	private WorkflowService workflowService;
	private DocumentService documentService;
	private MailService mailService;
	
	private UserPersistencePlugin userPersistencePlugin;
	private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	private GroupPersistencePlugin groupPersistencePlugin;
	
	private GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
	private TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			replacementProfilesService,
			replacementProfileInstanceService,
			workflowService,
			documentService,
			mailService,
			
			userPersistencePlugin,
			organizationUnitPersistencePlugin,
			groupPersistencePlugin,
			
			gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper,
			transitionNotificationsHandlerFactory
		);
	}

	@Override
	public AssignmentHelper newHelperFor(ExecutionImpl execution, Assignable assignable, SecurityManager userSecurity,
			Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnitIds, Collection<Long> idsForAssignedGroups) {
		
		return new ReplacementProfilesAwareAssignmentHelper(idsForAssignedUsers, idsForAssignedOrganizationUnitIds, idsForAssignedGroups, execution,
			assignable, gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper, replacementProfileInstanceService, workflowService,
			documentService, transitionNotificationsHandlerFactory, userSecurity);
	}

	public void setReplacementProfilesService(ReplacementProfilesService replacementProfilesService) {
		this.replacementProfilesService = replacementProfilesService;
	}
	public void setReplacementProfileInstanceService(ReplacementProfileInstanceService replacementProfileInstanceService) {
		this.replacementProfileInstanceService = replacementProfileInstanceService;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	public void setOrganizationUnitPersistencePlugin(OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
	public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
	public void setGatherReplacementProfilesForEntitiesIncludingForReplacementsHelper(GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper) {
		this.gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
	}
	public void setTransitionNotificationsHandlerFactory(TransitionNotificationsHandlerFactory transitionNotificationsHandlerFactory) {
		this.transitionNotificationsHandlerFactory = transitionNotificationsHandlerFactory;
	}
}