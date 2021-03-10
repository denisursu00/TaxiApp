package ro.cloudSoft.cloudDoc.builders.content.search;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.search.AbstractDocumentSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class AbstractDocumentSearchResultsViewsBuilder<V extends AbstractDocumentSearchResultsView> {
	

	private final List<Document> documents;
	private final SecurityManager userSecurity;
	
	
	private final WorkflowService workflowService;
	private final WorkflowExecutionService workflowExecutionService;
	private final UserService userService;
	
	
	private Map<String, TaskInstance> taskInstanceByDocumentId;
	
	private Set<Long> userIds;
	private Map<DocumentIdentifier, Workflow> workflowByDocumentIdentifier;
	private Map<DocumentIdentifier, WorkflowState> currentStateByDocumentIdentifier;
	
	private Map<Long, String> userDisplayNameById;
	
	protected AbstractDocumentSearchResultsViewsBuilder(List<Document> documents, SecurityManager userSecurity,
			WorkflowService workflowService, WorkflowExecutionService workflowExecutionService, UserService userService) {
		
		this.documents = documents;
		this.userSecurity = userSecurity;
		
		this.workflowService = workflowService;
		this.workflowExecutionService = workflowExecutionService;
		this.userService = userService;
	}

	/**
	 * Initializeaza colectiile intermediare necesare construirii view-urilor.
	 */
	private void init() {
		
		taskInstanceByDocumentId = Maps.newHashMap();
		
		userIds = Sets.newHashSet();
		workflowByDocumentIdentifier = Maps.newHashMap();
		currentStateByDocumentIdentifier = Maps.newHashMap();
		
		userDisplayNameById = Maps.newHashMap();
		
		additionalInit();
	}

	/**
	 * Initializeaza colectiile intermediare necesare construirii view-urilor.
	 */
	protected void additionalInit() {}
	
	/**
	 * Aduna elemente (id-uri, obiecte) necesare pentru construirea view-urilor.
	 */
	private void gatherItems() throws AppException {
		taskInstanceByDocumentId = workflowExecutionService.getCurrentTaskInstanceMap(documents);
		gatherAdditionalItems();
		gatherItemsFromEachDocument();
	}

	/**
	 * Aduna elemente (id-uri, obiecte) necesare pentru construirea view-urilor.
	 */
	protected void gatherAdditionalItems() {}
	
	/**
	 * Aduna elemente (id-uri, obiecte) din fiecare document, necesare pentru construirea view-urilor.
	 */
	private void gatherItemsFromEachDocument() throws AppException {
		
		for (Document document : documents) {
			
			userIds.add(document.getAuthorUserId());

			DocumentIdentifier documentIdentifier = new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId());
			
			Workflow workflow = workflowService.getWorkflowForDocument(document.getDocumentLocationRealName(), document.getId(), document.getDocumentTypeId());
			workflowByDocumentIdentifier.put(documentIdentifier, workflow);
			
			WorkflowState currentState = workflowExecutionService.getCurrentState(workflow, document, userSecurity);
			currentStateByDocumentIdentifier.put(documentIdentifier, currentState);
			
			TaskInstance taskInstance = taskInstanceByDocumentId.get(document.getId());
			if (taskInstance != null) {
				userIds.add(taskInstance.getSenderUserId());
			}
			
			gatherAdditionalItemsForDocument(document);
		}
		
	}

	/**
	 * Aduna elemente (id-uri, obiecte) din document, necesare pentru construirea view-urilor.
	 */
	protected void gatherAdditionalItemsForDocument(Document document) {}
	
	/**
	 * Aduna elemente (id-uri, obiecte) bazate pe cele adunate dinainte (de la fiecare document).
	 */
	private void gatherItemsBasedOnPreviouslyGatheredOnes() {
		userDisplayNameById = userService.getUsersNameMap(userIds, userSecurity);
		gatherAdditionalItemsBasedOnPreviouslyGatheredOnes();
	}

	/**
	 * Aduna elemente (id-uri, obiecte) bazate pe cele adunate dinainte (de la fiecare document).
	 */
	protected void gatherAdditionalItemsBasedOnPreviouslyGatheredOnes() {}
	
	public List<V> buildViews() throws AppException {
		
		init();
		gatherItems();
		gatherItemsBasedOnPreviouslyGatheredOnes();
		
		List<V> views = Lists.newArrayList();
		
		for (Document document : documents) {
			
			V view = createNewViewInstance();
			
			DocumentIdentifier documentIdentifier = new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId());
			
			view.setDocumentId(document.getId());
			view.setDocumentName(document.getName());
			view.setDocumentCreatedDate(document.getCreated().getTime());
			
			String authorDisplayName = userDisplayNameById.get(document.getAuthorUserId());
			view.setDocumentAuthorDisplayName(authorDisplayName);
			
			view.setWorkflowName(getWorkflowName(documentIdentifier));
			view.setWorkflowCurrentStateName(getCurrentStateName(documentIdentifier));
			view.setWorkflowSenderDisplayName(getSenderDisplayName(document.getId()));
			
			setAdditionalPropertiesForView(view, document);
			
			views.add(view);
		}
		
		return views;
	}
	
	protected abstract V createNewViewInstance();
	
	protected void setAdditionalPropertiesForView(V view, Document document) {}
	
	private String getWorkflowName(DocumentIdentifier documentIdentifier) {
		Workflow workflow = workflowByDocumentIdentifier.get(documentIdentifier);
		if (workflow == null) {
			return null;
		}
		return workflow.getName();
	}
	
	private String getCurrentStateName(DocumentIdentifier documentIdentifier) {
		WorkflowState currentState = currentStateByDocumentIdentifier.get(documentIdentifier);
		if (currentState == null) {
			return null;
		}
		return currentState.getName();
	}
	
	private String getSenderDisplayName(String documentId) {
		TaskInstance taskInstance = taskInstanceByDocumentId.get(documentId);
		if (taskInstance == null) {
			return null;
		}
		Long senderUserId = taskInstance.getSenderUserId();
		return userDisplayNameById.get(senderUserId);
	}
}