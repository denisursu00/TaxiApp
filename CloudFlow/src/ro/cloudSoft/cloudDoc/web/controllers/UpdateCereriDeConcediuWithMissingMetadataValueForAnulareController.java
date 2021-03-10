package ro.cloudSoft.cloudDoc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.services.CereriDeConcediuService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class UpdateCereriDeConcediuWithMissingMetadataValueForAnulareController implements Controller, InitializingBean {

	private CereriDeConcediuService cereriDeConcediuService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			cereriDeConcediuService
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		cereriDeConcediuService.updateCereriWithMissingMetadataValueForAnulare();
		
		response.getWriter().print("OK");
		return null;
	}
	
	public void setCereriDeConcediuService(CereriDeConcediuService cereriDeConcediuService) {
		this.cereriDeConcediuService = cereriDeConcediuService;
	}
}