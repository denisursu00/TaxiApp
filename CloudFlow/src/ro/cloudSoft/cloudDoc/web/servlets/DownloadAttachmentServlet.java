package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.SessionAttachmentManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class DownloadAttachmentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DownloadAttachmentServlet.class);

	private static final String PARAMETER_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	private static final String PARAMETER_DOCUMENT_ID = "documentId";
	private static final String PARAMETER_DOCUMENT_VERSION_NUMBER = "versionNumber";
	private static final String PARAMETER_ATTACHMENT_NAME = "attachmentName";
	
	private static final String CONTENT_TYPE = "application/octet-stream";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		// parametrii trimisi de client
		String documentLocationRealName = request.getParameter(PARAMETER_DOCUMENT_LOCATION_REAL_NAME);
		String documentId = request.getParameter(PARAMETER_DOCUMENT_ID);
		String versionNumberAsString = request.getParameter(PARAMETER_DOCUMENT_VERSION_NUMBER);
		String attachmentName = request.getParameter(PARAMETER_ATTACHMENT_NAME);
		
		if (StringUtils.isBlank(attachmentName)) {
			
			String message = "S-a incercat descarcarea unui atasament, dar nu s-a precizat numele acestuia.";
			LOGGER.error(message, "descarcarea unui atasament", userSecurity);
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Attachment attachment = null;
		if (StringUtils.isNotBlank(documentLocationRealName) && StringUtils.isNotBlank(documentId)) {
			// serviciul pentru documente
			DocumentService documentService = (DocumentService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("documentService");
			try {

				if (StringUtils.isNotBlank(versionNumberAsString)) {
					Integer versionNumber = Integer.valueOf(versionNumberAsString);
					attachment = documentService.getAttachmentFromVersion(documentId, versionNumber, attachmentName, documentLocationRealName, userSecurity);
				} else {
					attachment = documentService.getAttachment(documentId, attachmentName, documentLocationRealName, userSecurity);
				}
				
				if (attachment == null) {
					
					DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
					
					String message = "S-a incercat luarea din baza de date a atasamentului cu numele: [" + attachmentName + "], " +
						"pentru documentul cu atributele: " + documentLogAttributes + ", numarul versiunii [" + versionNumberAsString + "], " +
						"insa acesta nu a fost gasit.";
					LOGGER.error(message, "luarea atasamentului din baza de date", userSecurity);
				
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return;
				}
			} catch (AppException ae) {
				
				DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
				
				String message = "Exceptie la luarea atasamentului cu numele: [" + attachmentName + "], " +
					"pentru documentul cu atributele: " + documentLogAttributes;
				LOGGER.error(message, ae, "luarea atasamentului din baza de date", userSecurity);
				
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		} else {
			attachment = SessionAttachmentManager.getAttachment(request.getSession(), attachmentName);
			
			if (attachment == null) {
				
				String message = "S-a incercat luarea din spatiul temporar a atasamentului " +
					"cu numele [" + attachmentName + "], insa acesta nu a fost gasit.";
				LOGGER.error(message, "luarea atasamentului din spatiul temporar", userSecurity);
			
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}
		
		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getName() + "\"");
            response.setContentType(CONTENT_TYPE);
            
            byte[] data = attachment.getData();
            OutputStream out = response.getOutputStream();
            
            IOUtils.write(data, out);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "trimiterea atasamentului", userSecurity);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}