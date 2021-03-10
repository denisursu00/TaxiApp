package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DownloadTaskAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final LogHelper LOGGER = LogHelper.getInstance(DownloadTaskAttachmentServlet.class);

	private static final String PARAMETER_ATTACHMENT_ID = "attachmentId";
	private static final String PARAMETER_ATTACHMENT_NAME = "attachmentName";

	private static final String CONTENT_TYPE = "application/octet-stream";

	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		String attachmentId = request.getParameter(PARAMETER_ATTACHMENT_ID);
		String attachmentName = request.getParameter(PARAMETER_ATTACHMENT_NAME);
		
		if (StringUtils.isBlank(attachmentName)) {
			
			String message = "S-a incercat descarcarea unui atasament, dar nu s-a precizat numele acestuia.";
			LOGGER.error(message, "descarcarea unui atasament", userSecurity);
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		ProjectService projectService = SpringUtils.getBean("projectService");
		TaskAttachment attachment = projectService.getTaskAttachment(Long.valueOf(attachmentId));
		
		if (attachment == null) {
			String message = "Nu s-a gasit atasament pentru task-ul cu [" + attachmentId + "] si nume de fisier [" + attachmentName + "].";
			LOGGER.error(message, "descarcarea unui atasament", userSecurity);
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		try {
			response.addHeader("Content-disposition", "attachment;filename=\"" + attachmentName + "\"");
			response.setContentType(CONTENT_TYPE);
			
			byte[] data = attachment.getFileContent();
			OutputStream out = response.getOutputStream();
		  
			IOUtils.write(data, out);
		} catch (Exception e) {
			LOGGER.error("Exceptie", e, "trimiterea atasamentului");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
