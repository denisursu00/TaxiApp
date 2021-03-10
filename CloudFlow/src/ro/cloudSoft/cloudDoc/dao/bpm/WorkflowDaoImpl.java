package ro.cloudSoft.cloudDoc.dao.bpm;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WorkflowDaoImpl extends HibernateDaoSupport implements WorkflowDao, InitializingBean {
	
	@Override
	public Workflow find(Long id) {
		String query = "FROM Workflow WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<Workflow> foundWorkflows = getHibernateTemplate().find(query, id);
		return Iterables.getOnlyElement(foundWorkflows, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Workflow> getAllWorkflows() {
		String query = "FROM Workflow ORDER BY LOWER(name), versionNumber";
		return getHibernateTemplate().find(query);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void removeWorkflow(Workflow workflow) {
		getHibernateTemplate().delete(workflow);
		deleteOrphanStates();
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void removeWorkflow(Long id) {
		Workflow workflow = find(id);
		removeWorkflow(workflow);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveWorkflow(Workflow workflow) {
		getHibernateTemplate().saveOrUpdate(workflow);
		deleteOrphanTransitions();
		deleteOrphanStates();
		deleteOrphanTransitionNotificationsForWorkflow(workflow);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Workflow saveAndGetWorkflow(Workflow workflow) {
		saveWorkflow(workflow);
		return workflow;
	}
	
	private void deleteOrphanTransitions() {
		String query =
			"FROM WorkflowTransition " +
			"WHERE id NOT IN (" +
			"	SELECT transition.id " +
			"	FROM Workflow workflow " +
			"	JOIN workflow.transitions transition" +
			")";
		@SuppressWarnings("unchecked")
		List<WorkflowTransition> orphanTransitions = getHibernateTemplate().find(query);
		getHibernateTemplate().deleteAll(orphanTransitions);
	}
	
	private void deleteOrphanStates() {
		String query =
			"FROM WorkflowState " +
			"WHERE id NOT IN (" +
			"	SELECT startState.id " +
			"	FROM Workflow workflow " +
			"	JOIN workflow.transitions transition " +
			"	JOIN transition.startState startState " +
			") AND id NOT IN (" +
			"	SELECT finalState.id " +
			"	FROM Workflow workflow " +
			"	JOIN workflow.transitions transition " +
			"	JOIN transition.finalState finalState " +
			")";
		@SuppressWarnings("unchecked")
		List<WorkflowState> orphanStates = getHibernateTemplate().find(query);
		getHibernateTemplate().deleteAll(orphanStates);
	}
	
	@SuppressWarnings("unchecked")
	private void deleteOrphanTransitionNotificationsForWorkflow(Workflow workflow) {
		
		boolean isWorkflowNew = (workflow == null);
		if (isWorkflowNew) {
			return;
		}

		final Long workflowId = workflow.getId();
		final Set<Long> idsForTransitionNotificationsToKeep = Sets.newHashSet();
		
		for (WorkflowTransition transition : workflow.getTransitions()) {
			
			boolean isTransitionNew = (transition.getId() == null);
			if (isTransitionNew) {
				continue;
			}
			
			for (TransitionNotification transitionNotification : transition.getNotifications()) {
				
				boolean isNotificationNew = (transitionNotification.getId() == null);
				if (isNotificationNew) {
					continue;
				}
				
				idsForTransitionNotificationsToKeep.add(transitionNotification.getId());
			}
		}
		
		if (idsForTransitionNotificationsToKeep.isEmpty()) {
			List<TransitionNotification> orphanTransitionNotifications = getHibernateTemplate().executeFind(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String query =
						"SELECT notification " +
						"FROM Workflow workflow " +
						"JOIN workflow.transitions transition " +
						"JOIN transition.notifications notification " +
						"WHERE workflow.id = :workflowId ";
					return session.createQuery(query)
						.setParameter("workflowId", workflowId)
						.list();
				}
			});		
			getHibernateTemplate().deleteAll(orphanTransitionNotifications);
		} else {
			List<TransitionNotification> orphanTransitionNotifications = getHibernateTemplate().executeFind(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String query =
							"SELECT notification " +
									"FROM Workflow workflow " +
									"JOIN workflow.transitions transition " +
									"JOIN transition.notifications notification " +
									"WHERE workflow.id = :workflowId " +
									"AND notification.id NOT IN (:transitionNotificationIds)";
					return session.createQuery(query)
							.setParameter("workflowId", workflowId)
							.setParameterList("transitionNotificationIds", idsForTransitionNotificationsToKeep)
							.list();
				}
			});		
			getHibernateTemplate().deleteAll(orphanTransitionNotifications);
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public WorkflowState getWorkflowState(Long workflowId, String workflowStateCode) {
		
		/*
		 * Trebuie sa iau starile de start si de stop ale tranzitiilor
		 * fluxului cu ID-ul precizat. Una din cele 2 stari trebuie sa aiba
		 * codul precizat.
		 */
		String query =
			"SELECT startState, endState " +
			"FROM Workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.startState startState " +
			"JOIN transition.finalState endState " +
			"WHERE workflow.id = ? " +
			"AND (" +
			"	startState.code = ? " +
			"	OR " +
			"	endState.code = ?" +
			")";
		Object[] queryParameters = new Object[] {
			workflowId,
			workflowStateCode,
			workflowStateCode
		};
		
		List<Object[]> results = this.getHibernateTemplate().find(query, queryParameters);		
		for (Object[] result : results) {
			
			// Iau cele 2 stari.
			WorkflowState startState = (WorkflowState) result[0];
			WorkflowState endState = (WorkflowState) result[1];
			
			/*
			 * Una din cele stari este cea care corespunde criteriilor de
			 * cautare. Voi vedea care este aceasta verificand codul starilor.
			 */
			
			if (startState.getCode().equals(workflowStateCode)) {
				return startState;
			}
			
			if (endState.getCode().equals(workflowStateCode)) {
				return endState;
			}
		}
		
		return null;
	}
	
	public WorkflowState getWorkflowStateById(Long id) {
		String query = "SELECT state FROM WorkflowState state WHERE state.id = ?";
		return (WorkflowState) getHibernateTemplate().find(query, id).get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WorkflowState> getStatesByDocumentTypeIds(final List<Long> documentTypeIds) {
		
		if (CollectionUtils.isEmpty(documentTypeIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query =
					"SELECT transition.startState, transition.finalState " +
					"FROM Workflow workflow " +
					"JOIN workflow.documentTypes documentType " +
					"JOIN workflow.transitions transition " +
					"WHERE documentType.id IN (:documentTypeIds)";
				List<Object[]> statePairs = session.createQuery(query)
					.setParameterList("documentTypeIds", documentTypeIds)
					.list();
				
				Set<WorkflowState> states = Sets.newHashSet();
				
				for (Object[] statePair : statePairs) {
					
					WorkflowState startState = (WorkflowState) statePair[0];
					WorkflowState endState = (WorkflowState) statePair[1];
					
					states.add(startState);
					states.add(endState);
				}
				
				return Lists.newArrayList(states);
			}
		});
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<WorkflowState> getWorkflowStatesByWorkflowId(final Long workflowId) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT DISTINCT state " +
					"FROM WorkflowState state " +
					"WHERE id IN (" +
					"	SELECT startState.id " +
					"	FROM Workflow workflow " +
					"	JOIN workflow.transitions transition " +
					"	JOIN transition.startState startState " +
					"	WHERE workflow.id = :workflowId " +
					") OR id IN (" +
					"	SELECT finalState.id " +
					"	FROM Workflow workflow " +
					"	JOIN workflow.transitions transition " +
					"	JOIN transition.finalState finalState " +
					"	WHERE workflow.id = :workflowId " +
					") ORDER BY code";
				return session.createQuery(query)
					.setParameter("workflowId", workflowId)
					.list();
			}
		});
	}

	@Override
	public Workflow getWorkflowByDocumentType(Long documentTypeId) {
		Long idForLatestVersionOfWorkflowForDocumentType = getIdForLatestVersionOfWorkflowForDocumentType(documentTypeId);
		if (idForLatestVersionOfWorkflowForDocumentType == null) {
			return null;
		}
		return find(idForLatestVersionOfWorkflowForDocumentType);
	}
	
	@Override
	public Workflow getWorkflowForDocument(String documentLocationRealName, String documentId) {
		
		String query =
			"SELECT workflow " +
			"FROM WorkflowInstance " +
			"WHERE workspaceName = ? " +
			"AND documentId = ?";
		Object[] queryParameters = new Object[] {
			documentLocationRealName,
			documentId
		};

		@SuppressWarnings("unchecked")
		List<Workflow> foundWorkflows = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundWorkflows, null);
	}
	
	@Override
	public Map<DocumentIdentifier, String> getWorkflowNameByDocumentIdentifier(final Collection<DocumentIdentifier> documentIdentifiers) {
		
		if (documentIdentifiers.isEmpty()) {
			return Collections.emptyMap();
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String separatorForDocumentIdentifierPair = ";;";
				String query =
					"SELECT " +
					"	workflowInstance.workspaceName, " +
					"	workflowInstance.documentId, " +
					"	workflow.name " +
					"FROM WorkflowInstance workflowInstance " +
					"JOIN workflowInstance.workflow workflow " +
					"WHERE (workflowInstance.workspaceName || '" + separatorForDocumentIdentifierPair + "' || workflowInstance.documentId) IN (:documentIdentifierPairs)";
				
				Set<String> documentIdentifierPairs = Sets.newHashSet();
				for (DocumentIdentifier documentIdentifier : documentIdentifiers) {
					String documentIdentifierPair = (documentIdentifier.getDocumentLocationRealName() + separatorForDocumentIdentifierPair + documentIdentifier.getDocumentId());
					documentIdentifierPairs.add(documentIdentifierPair);
				}
				
				return session.createQuery(query)
					.setParameterList("documentIdentifierPairs", documentIdentifierPairs)
					.list();
			}
		});
		
		Map<DocumentIdentifier, String> workflowNameByDocumentIdentifier = Maps.newHashMap();
		for (Object[] result : results) {
			
			String documentLocationRealName = (String) result[0];
			String documentId = (String) result[1];
			String workflowName = (String) result[2];
			
			DocumentIdentifier documentIdentifier = new DocumentIdentifier(documentLocationRealName, documentId);
			workflowNameByDocumentIdentifier.put(documentIdentifier, workflowName);
		}
		
		return workflowNameByDocumentIdentifier;
	}

	@Override
	@SuppressWarnings("unchecked")
	public WorkflowTransition getTransitionForDocument(String documentLocationRealName,
			String documentId, String transitionName, String endStateCode) {
		
		String query =
			"SELECT transition " +
			"FROM WorkflowInstance workflowInstance " +
			"JOIN workflowInstance.workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.finalState endState " +
			"WHERE workflowInstance.workspaceName = ? " +
			"AND workflowInstance.documentId = ? " +
			"AND transition.name = ? " +
			"AND endState.code = ?";
		Object[] queryParameters = new Object[] {
			documentLocationRealName,
			documentId,
			transitionName,
			endStateCode
		};
		
		List<WorkflowTransition> results = this.getHibernateTemplate().find(query, queryParameters);
		return (results.isEmpty()) ? null : results.get(0);
	}
	
	@Override
	public WorkflowTransition getTransitionForDocumentType(Long documentTypeId,
			String transitionName, String endStateCode) {
		
		Long idForLatestVersionOfWorkflowForDocumentType = getIdForLatestVersionOfWorkflowForDocumentType(documentTypeId);
		if (idForLatestVersionOfWorkflowForDocumentType == null) {
			throw new IllegalArgumentException("Nu s-a gasit flux pentru tipul de document cu ID-ul [" + documentTypeId + "].");
		}

		String query =
			"SELECT transition " +
			"FROM Workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.finalState endState " +
			"WHERE workflow.id = ?" +
			"AND transition.name = ? " +
			"AND endState.code = ?";
		Object[] queryParameters = new Object[] {
			idForLatestVersionOfWorkflowForDocumentType,
			transitionName,
			endStateCode
		};

		@SuppressWarnings("unchecked")
		List<WorkflowTransition> foundTransitions = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundTransitions, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public WorkflowTransition getTransition(String transitionName, Long finalStateId) {
		
		String query =
			"SELECT transition " +
			"FROM WorkflowTransition transition " +
			"JOIN transition.finalState finalState " +
			"WHERE transition.name = ? " +
			"AND finalState.id = ?";
		Object[] queryParameters = new Object[] {
			transitionName,
			finalStateId
		};
		
		List<WorkflowTransition> foundTransitions = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundTransitions);
	}

	@Override
	public WorkflowTransition getTransitionById(Long id) {
		return (WorkflowTransition)getHibernateTemplate().get(WorkflowTransition.class, id);
	}

	@Override
	public boolean hasInstances(Long workflowId) {
		String query =
			"SELECT COUNT(*) " +
			"FROM WorkflowInstance " +
			"WHERE workflow.id = ?";		
		long activeInstanceCount = DataAccessUtils.longResult(getHibernateTemplate().find(query, workflowId));		
		return (activeInstanceCount > 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getNamesForTransitionsAvailableForAutomaticActionsOnly(Long workflowId, String startStateCode) {
		
		String query =
			"SELECT transition.name " +
			"FROM Workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.startState startState " +
			"WHERE workflow.id = ? " +
			"AND transition.availableForAutomaticActionsOnly = ?" +
			"AND startState.code = ?";
		Object[] parameters = {
			workflowId,
			true,
			startStateCode
		};
		
		return getHibernateTemplate().find(query, parameters);
	}
	
	@Override
	public boolean hasState(Long workflowId, String stateCode) {
		
		String query =
			"SELECT COUNT(*) " +
			"FROM Workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.startState startState " +
			"JOIN transition.finalState finalState " +
			"WHERE workflow.id = ? " +
			"AND ((startState.code = ?) OR (finalState.code = ?))";
		Object[] queryParameters = {
			workflowId,
			stateCode,
			stateCode
		};

		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, queryParameters);
		long count = Iterables.getOnlyElement(countAsList, 0L);
	
		return (count > 0);
	}
	
	@Override
	public Map<String, WorkflowState> getFinalStateOfTypeStopByTransitionName(Long workflowId) {
		
		String query =
			"SELECT transition.name, finalState " +
			"FROM Workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.finalState finalState " +
			"WHERE workflow.id = ? " +
			"AND finalState.stateType = ?";
		Object[] parameters = { workflowId, WorkflowState.STATETYPE_STOP };

		@SuppressWarnings("unchecked")
		List<Object[]> results = getHibernateTemplate().find(query, parameters);
		Map<String, WorkflowState> finalStateOfTypeStopByTransitionName = Maps.newHashMap();
		
		for (Object[] result : results) {
			
			String transitionName = (String) result[0];
			WorkflowState finalState = (WorkflowState) result[1];
			
			finalStateOfTypeStopByTransitionName.put(transitionName, finalState);
		}
		
		return finalStateOfTypeStopByTransitionName;
	}
	
	@Override
	public Integer getLastVersionNumberOfWorkflow(final Long workflowId) {
		Integer lastVersionNumber = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT candidateWorkflow.versionNumber " +
					"FROM Workflow candidateWorkflow " +
					"WHERE " +
					"( " +
					"	( " +
					"		(candidateWorkflow.id = :workflowId) " +
					"		AND " +
					"		(candidateWorkflow.baseVersion IS NULL) " +
					"		AND " +
					"		( " +
					"			NOT EXISTS ( " +
					"				SELECT versionWhereBaseVersionIsCandidateWorkflow " +
					"				FROM Workflow versionWhereBaseVersionIsCandidateWorkflow " +
					"				WHERE versionWhereBaseVersionIsCandidateWorkflow.baseVersion = candidateWorkflow " +
					"			) " +
					"		) " +
					"	) " +
					"	OR " +
					"	( " +
					"		(candidateWorkflow.baseVersion.id = :workflowId) " +
					"		AND " +
					"		( " +
					"			candidateWorkflow.versionNumber = ( " +
					"				SELECT MAX(versionWhereWorkflowWithIdIsBaseVersion.versionNumber) " +
					"				FROM Workflow versionWhereWorkflowWithIdIsBaseVersion " +
					"				WHERE versionWhereWorkflowWithIdIsBaseVersion.baseVersion.id = :workflowId " +
					"			) " +
					"		) " +
					"	) " +
					"	OR " +
					"	( " +
					"		( " +
					"			candidateWorkflow.baseVersion = ( " +
					"				SELECT workflowWithId.baseVersion " +
					"				FROM Workflow workflowWithId " +
					"				WHERE workflowWithId.id = :workflowId " +
					"			) " +
					"		) " +
					"		AND " +
					"		( " +
					"			candidateWorkflow.versionNumber = ( " +
					"				SELECT MAX(versionWithBaseVersionThatOfWorkflowWithId.versionNumber) " +
					"				FROM Workflow versionWithBaseVersionThatOfWorkflowWithId " +
					"				WHERE versionWithBaseVersionThatOfWorkflowWithId.baseVersion = ( " +
					"					SELECT workflowWithId.baseVersion " +
					"					FROM Workflow workflowWithId " +
					"					WHERE workflowWithId.id = :workflowId " +
					"				) " +
					"			) " +
					"		) " +
					"	) " +
					")";
				return session.createQuery(query)
					.setParameter("workflowId", workflowId)
					.uniqueResult();
			}
		});
		if (lastVersionNumber == null) {
			throw new IllegalArgumentException("Nu s-a gasit ultima versiune pentru fluxul cu ID-ul [" + workflowId + "].");
		}
		return lastVersionNumber;
	}
	
	@Override
	public Long getIdForLatestVersionOfWorkflowForDocumentType(Long documentTypeId) {
		String query =
			"SELECT workflow.id " +
			"FROM Workflow workflow " +
			"JOIN workflow.documentTypes documentType " +
			"WHERE " +
			"( " +
			"	(documentType.id = ?) " +
			"	AND " +
			"	( " +
			"		( " +
			"			(workflow.baseVersion IS NULL) " +
			"			AND " +
			"			( " +
			"				NOT EXISTS ( " +
			"					SELECT versionWhereWorkflowIsBaseVersion " +
			"					FROM Workflow versionWhereWorkflowIsBaseVersion " +
			"					WHERE versionWhereWorkflowIsBaseVersion.baseVersion = workflow " +
			"				) " +
			"			) " +
			"		) " +
			"		OR " +
			"		( " +
			"			(workflow.baseVersion IS NOT NULL) " +
			"			AND " +
			"			workflow.versionNumber = ( " +
			"				SELECT MAX(versionWithBaseVersionOfWorkflow.versionNumber) " +
			"				FROM Workflow versionWithBaseVersionOfWorkflow " +
			"				WHERE versionWithBaseVersionOfWorkflow.baseVersion = workflow.baseVersion " +
			"			) " +
			"		) " +
			"	) " +
			")";
		@SuppressWarnings("unchecked")
		List<Long> foundWorkflowIds = getHibernateTemplate().find(query, documentTypeId);
		return Iterables.getOnlyElement(foundWorkflowIds, null);
	}
	
	@Override
	public String getTransitionRoutingConditionExpression(Long workflowInstanceId, String transitionName) {
		
		String query =
			"SELECT transition.routingCondition " +
			"FROM WorkflowInstance workflowInstance " +
			"JOIN workflowInstance.workflow workflow " +
			"JOIN workflow.transitions transition " +
			"WHERE workflowInstance.id = ? " +
			"AND transition.name = ?";
		Object[] queryParameters = new Object[] {
			workflowInstanceId,
			transitionName
		};

		@SuppressWarnings("unchecked")
		List<String> foundRoutingConditions = getHibernateTemplate().find(query, queryParameters);
		
		if (foundRoutingConditions.size() == 1) {
			return foundRoutingConditions.get(0);
		} else if (foundRoutingConditions.size() == 0) {
			throw new IllegalArgumentException("Nu s-a gasit tranzitia cu numele [" + transitionName + "] pentru instanta de flux cu ID-ul [" + workflowInstanceId + "].");
		} else {
			throw new IllegalStateException("S-au gasit mai multe tranzitii cu numele [" + transitionName + "] pentru instanta de flux cu ID-ul [" + workflowInstanceId + "].");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WorkflowTransition> getOutgoingTransitionsFromState(Long stateId) {
		String query = "SELECT transition FROM WorkflowTransition transition WHERE transition.startState.id = ? ";
		return getHibernateTemplate().find(query, stateId);		
	}
}