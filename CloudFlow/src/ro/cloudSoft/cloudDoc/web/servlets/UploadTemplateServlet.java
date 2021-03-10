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
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.utils.SessionTemplateManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class UploadTemplateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final LogHelper LOGGER = LogHelper.getInstance(UploadTemplateServlet.class);
	
	private static final String FIELD_NAME_DESCRIPTION = "description";
	private static final String FIELD_NAME_TEMPLATE = "template";
	
	private static final String RESPONSE_MESSAGE_OK = "OK";
	private static final String RESPONSE_MESSAGE_ERROR = "ERROR";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		String responseMessage = RESPONSE_MESSAGE_OK;
		
		FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        
        String fileName = null;
        String description = null;
        byte[] data = null;
        
        try {
        	@SuppressWarnings("unchecked")
        	List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        	for (FileItem fileItem : fileItems) {
        		if (fileItem.isFormField() && ObjectUtils.equals(fileItem.getFieldName(), FIELD_NAME_DESCRIPTION)) {
        			description = fileItem.getString();
        		} else if (!fileItem.isFormField() && ObjectUtils.equals(fileItem.getFieldName(), FIELD_NAME_TEMPLATE)) {
        			fileName = FilenameUtils.getName(fileItem.getName());
        			data = fileItem.get();
        		}
        	}
        	
        	if (StringUtils.isBlank(fileName) || StringUtils.isBlank(description) || (data == null)) {
        		
        		String message = "Un camp necesar nu a fost completat. Numele fisierului: [" + fileName + "], " +
    				"descrierea template-ului: [" + description + "], continutul fisierului a fost luat: " +
					"[" + ((data != null) ? "DA" : "NU") + "].";
        		LOGGER.error(message, "adaugarea unui template", userSecurity);
        		
            	response.getWriter().print(RESPONSE_MESSAGE_ERROR);
            	return;
        	}
        	
        	DocumentTypeTemplate template = new DocumentTypeTemplate(fileName, description, data);
        	
			SessionTemplateManager.addTemplate(request.getSession(), template);
        } catch (Exception e) {
        	LOGGER.error("Exceptie in timpul adaugarii unui template", e,
    			"adaugarea unui template", userSecurity);
        	responseMessage = RESPONSE_MESSAGE_ERROR;
        }
        
        response.getWriter().print(responseMessage);
	}
}