package ro.cloudSoft.cloudDoc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class RedirectToDefaultModuleController implements Controller, InitializingBean {
	
	private String defaultModulePageUrl;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			defaultModulePageUrl
		);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String url = request.getContextPath() + defaultModulePageUrl;
		
		String queryString = request.getQueryString();
		if (StringUtils.isNotEmpty(queryString)) {
			url += ("?" + queryString);
		}
		
		response.sendRedirect(url);
		
		return null;
	}
	
	public void setDefaultModulePageUrl(String defaultModulePageUrl) {
		this.defaultModulePageUrl = defaultModulePageUrl;
	}
}