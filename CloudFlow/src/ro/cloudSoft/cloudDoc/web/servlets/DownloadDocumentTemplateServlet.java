package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.FileSystemDocumentTemplateManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DownloadDocumentTemplateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DownloadDocumentTemplateServlet.class);
	
	private static final String PARAMETER_USERNAME = "username";
	private static final String PARAMETER_DOCUMENT_TYPE_ID = "documentTypeId";
	private static final String PARAMETER_TEMPLATE_NAME = "templateName";
	private static final String PARAMETER_DESCRIPTION = "description";
	
	private static final String CONTENT_TYPE = "application/octet-stream";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		String username = request.getParameter(PARAMETER_USERNAME);
		if(userSecurity != null) {
			username = userSecurity.getUserUsername();
		}
		
		// parametrii trimisi de client
		String documentTypeId = request.getParameter(PARAMETER_DOCUMENT_TYPE_ID);
		String templateName = request.getParameter(PARAMETER_TEMPLATE_NAME);
		String description = request.getParameter(PARAMETER_DESCRIPTION);
		
		DocumentTypeTemplate template = null;
		if (StringUtils.isNotBlank(documentTypeId)) {
			DocumentTypeService documentTypeService = SpringUtils.getBean("documentTypeService");
			template = documentTypeService.getTemplate(Long.valueOf(documentTypeId), templateName);
		}
		
		if (template == null) {
			FileSystemDocumentTemplateManager fileSystemDocumentTemplateManager = SpringUtils.getBean("fileSystemDocumentTemplateManager");
			byte[] data = fileSystemDocumentTemplateManager.getTemplateAsByteArray(templateName, username);
			template = new DocumentTypeTemplate(templateName, description, data);
		}
		
		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + templateName + "\"");
            response.setContentType(CONTENT_TYPE);
            
            InputStream in = new ByteArrayInputStream(template.getData());
            OutputStream out = response.getOutputStream();
            
            IOUtils.copy(in, out);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "descarcarea template-ului", userSecurity);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}

}
