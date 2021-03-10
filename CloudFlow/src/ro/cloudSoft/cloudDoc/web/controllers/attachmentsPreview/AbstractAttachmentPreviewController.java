package ro.cloudSoft.cloudDoc.web.controllers.attachmentsPreview;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.SessionAttachmentManager;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public abstract class AbstractAttachmentPreviewController implements Controller, InitializingBean {
	
	protected static final String PARAMETER_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	protected static final String PARAMETER_DOCUMENT_ID = "documentId";
	protected static final String PARAMETER_DOCUMENT_VERSION_NUMBER = "versionNumber";
	protected static final String PARAMETER_ATTACHMENT_NAME = "attachmentName";
	
	protected static final String CONTENT_TYPE_PDF = "application.pdf";
	
	protected DocumentService documentService;

	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentService
		);
	}
	
	protected Attachment getAttachment(HttpServletRequest request) throws AppException {
		
		String documentLocationRealName = request.getParameter(PARAMETER_DOCUMENT_LOCATION_REAL_NAME);
		String documentId = request.getParameter(PARAMETER_DOCUMENT_ID);
		String versionNumberAsString = request.getParameter(PARAMETER_DOCUMENT_VERSION_NUMBER);
		String attachmentName = request.getParameter(PARAMETER_ATTACHMENT_NAME);
		
		if (StringUtils.isBlank(attachmentName)) {
			throw new RuntimeException("S-a incercat previzualizarea unui atasament, insa NU s-a precizat numele atasamentului.");
		}
		
		Attachment attachment = null;
		if (StringUtils.isNotBlank(documentLocationRealName) && StringUtils.isNotBlank(documentId)) {
			
			SecurityManager userSecurity = getUserSecurity(request);
			
			if (StringUtils.isNotBlank(versionNumberAsString)) {
				Integer versionNumber = Integer.valueOf(versionNumberAsString);
				attachment = documentService.getAttachmentFromVersion(documentId, versionNumber, attachmentName, documentLocationRealName, userSecurity);
				if (attachment == null) {
					DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
					String exceptionMessage = "Nu s-a gasit atasamentul cu numele [" + attachmentName + "] pentru versiunea " +
						"[" + versionNumber + "] a documentului cu atributele: " + documentLogAttributes + ".";
					throw new IllegalStateException(exceptionMessage);
				}
			} else {
				attachment = documentService.getAttachment(documentId, attachmentName, documentLocationRealName, userSecurity);
				if (attachment == null) {
					DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
					String exceptionMessage = "Nu s-a gasit atasamentul cu numele [" + attachmentName + "] " +
						"pentru documentul cu atributele: " + documentLogAttributes + ".";
					throw new IllegalStateException(exceptionMessage);
				}
			}
		} else {
			attachment = SessionAttachmentManager.getAttachment(request.getSession(), attachmentName);
			if (attachment == null) {
				String exceptionMessage = "Nu s-a gasit atasament temporar cu numele [" + attachmentName + "].";
				throw new IllegalStateException(exceptionMessage);
			}
		}
		return attachment;
	}
	
	protected SecurityManager getUserSecurity(HttpServletRequest request) {
		return SecurityManagerHolder.getSecurityManager();
	}
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}