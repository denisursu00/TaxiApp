package ro.cloudSoft.cloudDoc.services;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityType;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntry;
import ro.cloudSoft.cloudDoc.domain.audit.AuditSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.common.utils.PagingList;

public interface AuditService {

	void auditOperation(SecurityManager userSecurity, AuditEntityType entityType,
		Object entityIdentifier, String entityDisplayName, AuditEntityOperation operation);
	
	void auditUserOperation(SecurityManager userSecurity, User user, AuditEntityOperation operation);
	
	void auditOrganizationUnitOperation(SecurityManager userSecurity, OrganizationUnit organizationUnit, AuditEntityOperation operation);
    
    void auditGroupOperation(SecurityManager userSecurity, Group group, AuditEntityOperation operation);
    
    void auditDocumentTypeOperation(SecurityManager userSecurity, DocumentType documentType, AuditEntityOperation operation);
    
    void auditMimeTypeOperation(SecurityManager userSecurity, MimeType mimeType, AuditEntityOperation operation);
    
    void auditWorkflowOperation(SecurityManager userSecurity, Workflow workflow, AuditEntityOperation operation);
    
    void auditReplacementProfileOperation(SecurityManager userSecurity, ReplacementProfile replacementProfile, AuditEntityOperation operation);
    
    void auditDocumentOperation(SecurityManager userSecurity, Document document, AuditEntityOperation operation);
    
    PagingList<AuditEntry> searchAuditEntries(int offset, int pageSize, AuditSearchCriteria searchCriteria);
}