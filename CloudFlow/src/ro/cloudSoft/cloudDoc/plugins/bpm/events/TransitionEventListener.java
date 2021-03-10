package ro.cloudSoft.cloudDoc.plugins.bpm.events;

import java.util.Set;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.AbstractTransitionNotificationsHandler;
import ro.cloudSoft.cloudDoc.plugins.bpm.notifications.TransitionNotificationsHandlerFactory;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;

import com.google.common.collect.Sets;

/**
 * Aceasta clasa este un listener care se executa la trecerea printr-o tranzitie.
 *
 */
public class TransitionEventListener implements EventListener {

	private static final long serialVersionUID = 1L;
	
	private Long transitionId;
	
	@Override
	public void notify(EventListenerExecution execution) throws Exception {		
		
		WorkflowService workflowService = SpringContextUtils.getBean("workflowService");
		
		WorkflowTransition transition = workflowService.getTransitionById(transitionId);
		
		if (transition == null) {
			throw new IllegalStateException("Nu s-a gasit tranzitia cu ID-ul [" + transitionId + "].");
		}
			
		handleExtraViewers(transition, execution);
		
		TransitionNotificationsHandlerFactory notificationsHandlerFactory = getNotificationsHandlerFactory();
		AbstractTransitionNotificationsHandler notificationHandler = notificationsHandlerFactory.forAllExceptAssignedEntities(transition, execution);
		notificationHandler.handleNotifications();
	}
	
	private void handleExtraViewers(WorkflowTransition transition, OpenExecution execution) throws AppException {
		
		Set<Long> userIds = Sets.newHashSet();
		Set<Long> organizationUnitIds = Sets.newHashSet();
		Set<Long> groupIds = Sets.newHashSet();
		
		for (OrganizationEntity organizationEntity : transition.getExtraViewers()) {
			if (organizationEntity instanceof User) {
				userIds.add(organizationEntity.getId());
			} else if (organizationEntity instanceof OrganizationUnit) {
				organizationUnitIds.add(organizationEntity.getId());
			} else if (organizationEntity instanceof Group) {
				groupIds.add(organizationEntity.getId());
			} 
		}
		
		String documentId = WorkflowVariableHelper.getDocumentId(execution);
		String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
		
		DocumentService documentService = SpringContextUtils.getBean("documentService");
		documentService.addExtraViewers(documentId, documentLocationRealName, userIds, organizationUnitIds, groupIds);
	}
	
	private TransitionNotificationsHandlerFactory getNotificationsHandlerFactory() {
		return SpringContextUtils.getBean("transitionNotificationsHandlerFactory");
	}
	
	public void setTransitionId(Long transitionId) {
		this.transitionId = transitionId;
	}	
	public Long getTransitionId() {
		return transitionId;
	}
}