package ro.cloudSoft.cloudDoc.dao.content;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.content.DocumentAttachmentDetail;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

public class DocumentAttachmentDetailDaoImpl extends HibernateDaoSupport implements DocumentAttachmentDetailDao {
	
	@Override
	public void save(DocumentAttachmentDetail documentAttachmentDetail) {
		if (documentAttachmentDetail.getId() == null) {
			this.getHibernateTemplate().save(documentAttachmentDetail);
		} else {
			this.getHibernateTemplate().saveOrUpdate(documentAttachmentDetail);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentAttachmentDetail> getAllOfDocument(DocumentIdentifier documentIdentifier) {
		String query = "FROM DocumentAttachmentDetail dad WHERE dad.documentLocationRealName = ? AND dad.documentId = ? ";
		return getHibernateTemplate().find(query, documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId());
	}
	
	@Override
	public void delete(DocumentIdentifier documentIdentifier, String attachmentName) {
		String query = "DELETE FROM DocumentAttachmentDetail dad WHERE dad.documentLocationRealName = ? AND dad.documentId = ? AND dad.attachmentName = ? ";
		getHibernateTemplate().bulkUpdate(query, documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId(), attachmentName);
	}
	
	@Override
	public void deleteAllOfDocument(DocumentIdentifier documentIdentifier) {
		String query = "DELETE FROM DocumentAttachmentDetail dad WHERE dad.documentLocationRealName = ? AND dad.documentId = ? ";
		getHibernateTemplate().bulkUpdate(query, documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId());
	}
}
