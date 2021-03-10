package ro.cloudSoft.cloudDoc.dao.export_JR;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.export_jr.ExportDocument;

public class ExportJRDao extends HibernateDaoSupport {

	public Long save(ExportDocument entity) {
		if (entity.getId() == null) {
			return (Long) getHibernateTemplate().save(entity);
		} else {
			getHibernateTemplate().saveOrUpdate(entity);
			return entity.getId();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<ExportDocument> getAll() {
		String query = "SELECT exportDocument FROM ExportDocument exportDocument";
		return getHibernateTemplate().find(query);
	}

	@SuppressWarnings("unchecked")
	public Set<DocumentIdentifier> getAllExportedDocumentsJRIds() {
		Set<DocumentIdentifier> resultSet = new HashSet<>();
		String query = "SELECT new " + DocumentIdentifier.class.getName() + "(ed.jrDocumentLocationRealName, ed.jrId) FROM ExportDocument ed";
		List<DocumentIdentifier> resultList = getHibernateTemplate().find(query);
		resultSet.addAll(resultList);
		return resultSet;
	}
	
	
	public List<DocumentIdentifier> getAvailableDocumentsForExportBeforeWorkflowFinishedDate(Date beforeDate) {
		
		String query =
				"SELECT new " + DocumentIdentifier.class.getName() + "(wi.workspaceName, wi.documentId) " +
				"FROM WorkflowInstance wi " +
				"WHERE wi.finishedDate < ? " +
				" AND wi.status = ? " +
				" AND NOT EXISTS(select ed.jrId from ExportDocument ed where ed.jrId = wi.documentId and ed.jrDocumentLocationRealName = wi.workspaceName)";
		
		Object[] queryParameters = {
			beforeDate,
			WorkflowInstance.STATUS_FINNISHED
		};
		
		@SuppressWarnings("unchecked")
		List<DocumentIdentifier> results = getHibernateTemplate().find(query, queryParameters);
		return results;
	}
	
	public boolean existsDocument(DocumentIdentifier di) {
		String query = "SELECT ed FROM ExportDocument ed WHERE ed.jrDocumentLocationRealName = ? AND ed.jrId = ? ";
		@SuppressWarnings("unchecked")
		List<DocumentIdentifier> results = getHibernateTemplate().find(query, di.getDocumentLocationRealName(), di.getDocumentId());
		return CollectionUtils.isNotEmpty(results);
	}
	
	public List<ExportDocument> getAllArchivedAndNotDeleted() {
		String query = "SELECT ed FROM ExportDocument ed WHERE ed.archived = ? AND ed.jrDeleted = ?";
		return getHibernateTemplate().find(query, Boolean.TRUE, Boolean.FALSE);
	}
}