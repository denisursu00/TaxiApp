package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ObjectUtils;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.utils.SessionAttachmentManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class UploadAttachmentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(UploadAttachmentServlet.class);
	
	private static final String FIELD_NAME_ATTACHMENT = "attachment";
	
	private static final String RESPONSE_MESSAGE_OK = "OK";
	private static final String RESPONSE_MESSAGE_ERROR = "ERROR";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		String responseMessage = RESPONSE_MESSAGE_OK;
		
		FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        try {
        	@SuppressWarnings("unchecked")
        	List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        	for (FileItem fileItem : fileItems) {
        		if (!fileItem.isFormField() && ObjectUtils.equals(fileItem.getFieldName(), FIELD_NAME_ATTACHMENT)) {
        			Attachment attachment = new Attachment(FilenameUtils.getName(fileItem.getName()), fileItem.get());
        			SessionAttachmentManager.addAttachment(request.getSession(), attachment);
        			break;
        		}
        	}
        } catch (Exception e) {
        	LOGGER.error("Exceptie in timpul adaugarii unui atasament", e,
    			"adaugarea unui atasament", userSecurity);
        	responseMessage = RESPONSE_MESSAGE_ERROR;
        }
        
        response.setContentType("text/plain");
        response.getWriter().print(responseMessage);
	}
}