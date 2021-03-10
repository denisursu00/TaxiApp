package ro.cloudSoft.cloudDoc.dao.content;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.ImportedDocument;

public class ImportedDocumentDaoImpl extends HibernateDaoSupport implements ImportedDocumentDao {

	@Override
	public void save(ImportedDocument importedDocument) {
		if (importedDocument.getId() == null) {
			getHibernateTemplate().save(importedDocument);
		} else {
			getHibernateTemplate().saveOrUpdate(importedDocument);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean isDocumentImported(DocumentIdentifier documentIdentifier) {
		String query = "select a from ImportedDocument a where a.documentId = ? and a.documentLocationRealName = ?";		
		List results = getHibernateTemplate().find(query.toString(), documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName());
		return CollectionUtils.isNotEmpty(results);
	}

	@Override
	public List<String> getDocumentIdsByDocumentLocationRealName(String documentLocationRealName) {
		String query = "SELECT a.documentId "
				+ "		FROM ImportedDocument a "
				+ "		WHERE a.documentLocationRealName = ?";		
		List<String> results = (List<String>) getHibernateTemplate().find(query.toString(), documentLocationRealName);
		return results;
	}
}
