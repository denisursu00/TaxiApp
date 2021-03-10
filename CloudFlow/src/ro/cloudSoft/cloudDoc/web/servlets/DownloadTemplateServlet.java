package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.SessionTemplateManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DownloadTemplateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DownloadTemplateServlet.class);
	
	private static final String CONTENT_TYPE = "application/octet-stream";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		// parametrii trimisi de client
		String documentTypeId = request.getParameter("documentTypeId");
		String templateName = request.getParameter("templateName");
		
		DocumentTypeTemplate template = null;
		if (StringUtils.isNotBlank(documentTypeId)) {
			DocumentTypeService documentTypeService = SpringUtils.getBean("documentTypeService");
			template = documentTypeService.getTemplate(Long.valueOf(documentTypeId), templateName);
		} else {
			template = SessionTemplateManager.getTemplate(request.getSession(), templateName);
		}
		
		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + templateName + "\"");
            response.setContentType(CONTENT_TYPE);
            
            InputStream in = new ByteArrayInputStream(template.getData());
            OutputStream out = response.getOutputStream();
            
            IOUtils.copy(in, out);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "trimiterea template-ului", userSecurity);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
}