package ro.cloudSoft.cloudDoc.dao.bpm;

import static ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance.STATUS_RUNNING;
import static ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance.STATUS_FINNISHED;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

import com.google.common.collect.Iterables;

public class WorkflowInstanceDaoImpl extends HibernateDaoSupport implements WorkflowInstanceDao, InitializingBean
{

	@Override
	@SuppressWarnings("unchecked")
	public WorkflowInstance findCurrentWorkflowInstances(String documentId)
	{
		StringBuffer sql = new StringBuffer("from WorkflowInstance wi where wi.documentId = '");
		sql.append(documentId);
		sql.append("' and wi.status = '").append(WorkflowInstance.STATUS_RUNNING).append("'");
		List<WorkflowInstance> winstances = getHibernateTemplate().find( sql.toString() );
		return winstances != null && winstances.size() > 0 ? winstances.get( 0 ) : null;
	}

	@Override
	public WorkflowInstance findWorkflowInstanceById(Long id)
	{
		return (WorkflowInstance) getHibernateTemplate().get(WorkflowInstance.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public WorkflowInstance findWorkflowInstanceByProcessInstanceId(String processInstanceId)
	{
		StringBuffer sql = new StringBuffer("from WorkflowInstance wi where wi.processInstanceId = '");
		sql.append( processInstanceId );
		sql.append("' and wi.status = '").append(WorkflowInstance.STATUS_RUNNING).append("'");
		List<WorkflowInstance> winstances = getHibernateTemplate().find( sql.toString() );
		return winstances != null && winstances.size() > 0 ? winstances.get( 0 ) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WorkflowInstance> findWorkflowInstances(String documentId)
	{
		StringBuffer sql = new StringBuffer("from WorkflowInstance wi where wi.documentId = '");
		sql.append(documentId).append("'");
		List<WorkflowInstance> winstances = getHibernateTemplate().find( sql.toString() );
		return winstances;
	}

	@Override
	public WorkflowInstance getForDocument(String documentId) {
		String query = "FROM WorkflowInstance WHERE documentId = ?";
		@SuppressWarnings("unchecked")
		List<WorkflowInstance> workflowInstanceAsList = getHibernateTemplate().find(query, documentId);
		return Iterables.getOnlyElement(workflowInstanceAsList, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WorkflowInstance> getAllWorkflowInstances()
	{
		StringBuffer sql = new StringBuffer("from WorkflowInstance wi");
		List<WorkflowInstance> winstances = getHibernateTemplate().find( sql.toString() );
		return winstances;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WorkflowInstance> getAllWorkflowInstances(String status)
	{
		StringBuffer sql = new StringBuffer("from WorkflowInstance wi where wi.status = '");
		sql.append(status).append("'");
		List<WorkflowInstance> winstances = getHibernateTemplate().find( sql.toString() );
		return winstances;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public WorkflowInstance getWorkflowInstance(Workflow workflow, Document document) {
		if ((workflow == null) || (document == null)) {
			return null;
		}
		String query = "SELECT workflowInstance FROM WorkflowInstance workflowInstance JOIN workflowInstance.workflow workflow WHERE workflow.id = ? AND workflowInstance.workspaceName = ? AND workflowInstance.documentId = ?";
		List<WorkflowInstance> results = getHibernateTemplate().find(query, new Object[] {workflow.getId(), document.getDocumentLocationRealName(), document.getId()});
		return (results.isEmpty()) ? null : results.get(0);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveWorkflowInstance(WorkflowInstance workflowInstance)
	{
		getHibernateTemplate().saveOrUpdate(workflowInstance);
		
	}

	@Override
	public String getFirstTransitionName(String processInstanceId, String finalStateCode) {
		
		String query =
			"SELECT transition.name " +
			"FROM WorkflowInstance workflowInstance " +
			"JOIN workflowInstance.workflow workflow " +
			"JOIN workflow.transitions transition " +
			"JOIN transition.startState startState " +
			"JOIN transition.finalState finalState " +
			"WHERE workflowInstance.processInstanceId = ? " +
			"AND startState.stateType = ? " +
			"AND finalState.code = ?";
		Object[] parameters = {
			processInstanceId,
			WorkflowState.STATETYPE_START,
			finalStateCode
		};

		@SuppressWarnings("unchecked")
		List<String> foundTransitionNames = getHibernateTemplate().find(query, parameters);
		return Iterables.getOnlyElement(foundTransitionNames);
	}
	
	@Override
	public Long getInitiatorUserId(String processInstanceId) {		
		String query =
			"SELECT initiatorId " +
			"FROM WorkflowInstance " +
			"WHERE processInstanceId = ?";
		@SuppressWarnings("unchecked")
		List<Long> foundInitiatorUserIds = getHibernateTemplate().find(query, processInstanceId);
		return Iterables.getOnlyElement(foundInitiatorUserIds);
	}

	@Override
	public boolean hasActiveWorkflowInstance(String documentLocationRealName, String documentId) {
		
		String query = "SELECT COUNT(*) FROM WorkflowInstance " +
			"WHERE workspaceName = ? AND documentId = ? AND status = ?";
		Object[] parameters = new Object[] {documentLocationRealName, documentId, STATUS_RUNNING};
		@SuppressWarnings("unchecked")
		List<Long> results = getHibernateTemplate().find(query, parameters);
		
		long count = Iterables.getOnlyElement(results, 0L);
		return (count == 1);
	}

	@Override
	public boolean hasFinishedStatusWorkflowInstanceByDocumentId(String documentLocationRealName, String documentId) {

		String query = "SELECT COUNT(*) FROM WorkflowInstance " +
						"WHERE documentId = ? AND status = ?";
		Object[] parameters = new Object[] {documentId, STATUS_FINNISHED};
		@SuppressWarnings("unchecked")
		List<Long> results = getHibernateTemplate().find(query, parameters);
		
		long count = Iterables.getOnlyElement(results, 0L);
		return (count == 1);
	}
	
	public List<String> getDocumentIdsByDocTypeAndStatus(Long documentTypeId, String status, String workspaceName) {

		String query = "SELECT workflowInstance.documentId "
				+ "		FROM WorkflowInstance workflowInstance"
				+ "		INNER JOIN workflowInstance.workflow workflow "
				+ "		INNER JOIN workflow.documentTypes documentType"
				+ "		WHERE documentType.id = ? "
				+ "			AND workflowInstance.status = ?"
				+ "			AND workflowInstance.workspaceName = ?";
		Object[] parameters = new Object[] {documentTypeId, status, workspaceName};
		
		return (List<String>) getHibernateTemplate().find(query, parameters);
	}
	
	@Override
	public Set<DocumentIdentifier> getDocumentsBeforeFinishedDate(Date beforeDate) {
		Set<DocumentIdentifier> resultSet = new HashSet<>();
		String query = "SELECT new " + DocumentIdentifier.class.getName() + "(wi.workspaceName, wi.documentId) FROM WorkflowInstance wi WHERE wi.finishedDate < ? ";
		List<DocumentIdentifier> resultList = getHibernateTemplate().find(query, beforeDate);
		resultSet.addAll(resultList);
		return resultSet;
	}
}