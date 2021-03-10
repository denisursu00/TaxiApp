package ro.cloudSoft.cloudDoc.dao.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.DocumentAttachmentDetail;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

public interface DocumentAttachmentDetailDao {
	
	void save(DocumentAttachmentDetail documentAttachmentDetail);
	
	List<DocumentAttachmentDetail> getAllOfDocument(DocumentIdentifier documentIdentifier);
	
	void delete(DocumentIdentifier documentIdentifier, String attachmentName);
	
	void deleteAllOfDocument(DocumentIdentifier documentIdentifier);
}
