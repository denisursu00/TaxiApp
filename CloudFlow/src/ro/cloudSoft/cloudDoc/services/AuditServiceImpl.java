package ro.cloudSoft.cloudDoc.services;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.dao.AuditEntryDao;
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
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

public class AuditServiceImpl implements AuditService, InitializingBean {
	
	private DocumentTypeService documentTypeService;
	private UserService userService;
	
	private AuditEntryDao auditEntryDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
				
			documentTypeService,
			userService,
			
			auditEntryDao
		);
	}

	@Override
	public void auditOperation(SecurityManager userSecurity, AuditEntityType entityType,
			Object entityIdentifier, String entityDisplayName, AuditEntityOperation operation) {
		
		if (!entityType.isOperationAllowed(operation)) {
			String exceptionMessage = "Operatia [" + operation + "] NU se " +
				"poate face pentru o entitate de tipul [" + entityType + "].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		if (entityIdentifier == null) {
			throw new IllegalArgumentException("Nu se poate salva in audit pentru o entitate fara ID.");
		}
		String entityIdentifierAsString = entityIdentifier.toString();
		
		AuditEntry auditEntry = new AuditEntry();
		
		auditEntry.setDateTime(new Date());
		
		auditEntry.setUserId(userSecurity.getUserId());
		auditEntry.setUserDisplayName(userSecurity.getDisplayName());
		
		auditEntry.setEntityType(entityType);
		
		auditEntry.setEntityIdentifier(entityIdentifierAsString);
		auditEntry.setEntityDisplayName(entityDisplayName);
		
		auditEntry.setOperation(operation);
		
		auditEntryDao.save(auditEntry);
	}
	
	@Override
	public void auditUserOperation(SecurityManager userSecurity, User user, AuditEntityOperation operation) {
		auditOperation(userSecurity, AuditEntityType.USER, user.getId(), user.getDisplayNameWithTitle(), operation);
	}
	
	@Override
	public void auditOrganizationUnitOperation(SecurityManager userSecurity, OrganizationUnit organizationUnit, AuditEntityOperation operation) {
		auditOperation(userSecurity, AuditEntityType.ORGANIZATION_UNIT, organizationUnit.getId(), organizationUnit.getName(), operation);
	}
	
	@Override
	public void auditGroupOperation(SecurityManager userSecurity, Group group, AuditEntityOperation operation) {
		auditOperation(userSecurity, AuditEntityType.GROUP, group.getId(), group.getName(), operation);
	}
	
	@Override
	public void auditDocumentTypeOperation(SecurityManager userSecurity, DocumentType documentType, AuditEntityOperation operation) {
		auditOperation(userSecurity, AuditEntityType.DOCUMENT_TYPE, documentType.getId(), documentType.getName(), operation);
	}
	
	@Override
	public void auditMimeTypeOperation(SecurityManager userSecurity, MimeType mimeType, AuditEntityOperation operation) {
		String mimeTypeDisplayName = getMimeTypeDisplayName(mimeType);
		auditOperation(userSecurity, AuditEntityType.MIME_TYPE, mimeType.getId(), mimeTypeDisplayName, operation);
	}
	
	private String getMimeTypeDisplayName(MimeType mimeType) {
		return new StringBuilder()
			.append(mimeType.getName())
			.append(" (").append(mimeType.getExtension()).append(")")
			.toString();
	}
	
	@Override
	public void auditWorkflowOperation(SecurityManager userSecurity, Workflow workflow, AuditEntityOperation operation) {
		String workflowDisplayName = getWorkflowDisplayName(workflow);
		auditOperation(userSecurity, AuditEntityType.WORKFLOW, workflow.getId(), workflowDisplayName, operation);
	}
	
	private String getWorkflowDisplayName(Workflow workflow) {
		return new StringBuilder()
			.append(workflow.getName())
			.append(" (").append(workflow.getVersionNumber()).append(")")
			.toString();
	}
	
	@Override
	public void auditReplacementProfileOperation(SecurityManager userSecurity, ReplacementProfile replacementProfile, AuditEntityOperation operation) {
		String replacementProfileDisplayName = getReplacementProfileDisplayName(replacementProfile);
		auditOperation(userSecurity, AuditEntityType.REPLACEMENT_PROFILE, replacementProfile.getId(), replacementProfileDisplayName, operation);
	}
	
	private String getReplacementProfileDisplayName(ReplacementProfile replacementProfile) {
		
		String startDateAsString = DateFormatUtils.format(replacementProfile.getStartDate(), FormatConstants.DATE_FOR_DISPLAY);
		String endDateAsString = DateFormatUtils.format(replacementProfile.getEndDate(), FormatConstants.DATE_FOR_DISPLAY);
		
		return new StringBuilder()
			.append(replacementProfile.getRequesterUsername())
			.append(" (").append(startDateAsString).append(" - ").append(endDateAsString).append(")")
			.toString();
	}
	
	@Override
	public void auditDocumentOperation(SecurityManager userSecurity, Document document, AuditEntityOperation operation) {
		
		String documentIdentifier = getDocumentIdentifier(document);
		String documentDisplayName = getDocumentDisplayName(document);
		
		auditOperation(userSecurity, AuditEntityType.DOCUMENT, documentIdentifier, documentDisplayName, operation);
	}
	
	private String getDocumentIdentifier(Document document) {
		return new StringBuilder()
			.append("[").append(document.getDocumentLocationRealName() + "]")
			.append(" / ")
			.append("[").append(document.getId()).append("]")
			.toString();
	}
	
	private String getDocumentDisplayName(Document document) {
		
		String documentTypeName = documentTypeService.getDocumentTypeName(document.getDocumentTypeId());
		String authorDisplayName = userService.getDisplayName(document.getAuthorUserId());
		String createdDateAsString = DateFormatUtils.format(document.getCreated(), FormatConstants.DATE_FOR_DISPLAY);
		
		return new StringBuilder()
			.append(documentTypeName)
			.append(" (").append(authorDisplayName).append(", ").append(createdDateAsString).append(")")
			.toString();
	}
	
	@Override
	public PagingList<AuditEntry> searchAuditEntries(int offset, int pageSize, AuditSearchCriteria searchCriteria) {
		return auditEntryDao.searchAuditEntries(offset, pageSize, searchCriteria);
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}
}