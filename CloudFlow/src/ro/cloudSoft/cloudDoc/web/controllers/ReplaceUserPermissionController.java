package ro.cloudSoft.cloudDoc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ReplaceUserPermissionController implements Controller, InitializingBean {
	
	private DocumentService documentService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentService
		);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String documentLocationRealName = request.getParameter("documentLocationRealName");
		String documentTypeIdAsString = request.getParameter("documentTypeId");
		String oldUserIdAsString = request.getParameter("oldUserId");
		String newUserIdAsString = request.getParameter("newUserId");
		
		if (StringUtils.isBlank(documentLocationRealName)
				|| StringUtils.isBlank(documentTypeIdAsString)
				|| StringUtils.isBlank(oldUserIdAsString)
				|| StringUtils.isBlank(newUserIdAsString)) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		Long documentTypeId = null;
		Long oldUserId = null;
		Long newUserId = null;
		
		try {
			documentTypeId = Long.valueOf(documentTypeIdAsString);
			oldUserId = Long.valueOf(oldUserIdAsString);
			newUserId = Long.valueOf(newUserIdAsString);
		} catch (NumberFormatException nfe) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		documentService.replaceUserPermission(documentLocationRealName, documentTypeId, oldUserId, newUserId);
		
		response.getWriter().print("OK");		
		return null;
	}
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}