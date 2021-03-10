package ro.cloudSoft.cloudDoc.dao.content;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.TemplateType;

/**
 * 
 */
public class DocumentTypeTemplateDaoImpl extends HibernateDaoSupport implements DocumentTypeTemplateDao, InitializingBean {
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveAll(Long documentTypeId, Collection<DocumentTypeTemplate> templates) {
		for (DocumentTypeTemplate template : templates) {
			template.setDocumentTypeId(documentTypeId);
			getHibernateTemplate().save(template);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(final Long documentTypeId, final Collection<String> templateNames) {
		
		if (CollectionUtils.isEmpty(templateNames)) {
			return;
		}
		
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"DELETE FROM DocumentTypeTemplate " +
					"WHERE documentTypeId = :documentTypeId " +
					"AND name IN (:templateNames)";
				return session.createQuery(query)
					.setParameter("documentTypeId", documentTypeId)
					.setParameterList("templateNames", templateNames)
					.executeUpdate();
			}
		});
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteAll(Long documentTypeId) {
		String query = "DELETE FROM DocumentTypeTemplate WHERE documentTypeId = ?";
		getHibernateTemplate().bulkUpdate(query, documentTypeId);
	}
	
	@Override
	public List<DocumentTypeTemplate> getAll() {
		return getHibernateTemplate().find("FROM DocumentTypeTemplate");
	}

	@Override
	public DocumentTypeTemplate getByTemplateNameAndDocumentTypeId(final String templateName, final Long documentTypeId) {
		
		return getHibernateTemplate().execute(new HibernateCallback<DocumentTypeTemplate>() {
			@Override
			public DocumentTypeTemplate doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
						"FROM DocumentTypeTemplate " +
						"WHERE documentTypeId = :documentTypeId " +
						"AND name = :templateName";
				return (DocumentTypeTemplate) session.createQuery(query)
					.setParameter("documentTypeId", documentTypeId)
					.setParameter("templateName", templateName)
					.uniqueResult();
			}
		});
	}

	@Override
	public List<DocumentTypeTemplate> getAllJasperTemplates() {
		return getHibernateTemplate().find("FROM DocumentTypeTemplate WHERE type=?", TemplateType.JASPER_REPORTS);
	}
}