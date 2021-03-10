package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import java.util.Collection;

import org.jbpm.api.model.OpenExecution;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class TransitionNotificationsHandlerFactory implements InitializingBean {
	
	private MailService mailService;
	private DocumentService documentService;
	private UserService userService;
	
	private UserPersistencePlugin userPersistencePlugin;
	private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	private GroupPersistencePlugin groupPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			mailService,
			documentService,
			userService,
			
			userPersistencePlugin,
			organizationUnitPersistencePlugin,
			groupPersistencePlugin
		);
	}

	public AbstractTransitionNotificationsHandler forAssignedEntitiesOnly(WorkflowTransition transition, OpenExecution execution,
			Collection<Long> idsForAssignedUsers, Collection<Long> idsForAssignedOrganizationUnits, Collection<Long> idsForAssignedGroups) {
		
		return new AssignedEntitiesOnlyTransitionNotificationsHandler(transition, execution, mailService, documentService, userPersistencePlugin,
			organizationUnitPersistencePlugin, groupPersistencePlugin, idsForAssignedUsers, idsForAssignedOrganizationUnits, idsForAssignedGroups);
	}

	public AbstractTransitionNotificationsHandler forAllExceptAssignedEntities(WorkflowTransition transition, OpenExecution execution) {
		return new AllExceptAssignedEntitiesTransitionNotificationsHandler(transition, execution, mailService,
			documentService, userService, userPersistencePlugin, organizationUnitPersistencePlugin, groupPersistencePlugin);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
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
}