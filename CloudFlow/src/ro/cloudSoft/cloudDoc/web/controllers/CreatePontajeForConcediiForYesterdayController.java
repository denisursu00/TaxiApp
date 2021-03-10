package ro.cloudSoft.cloudDoc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.services.PontajForConcediiService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class CreatePontajeForConcediiForYesterdayController implements Controller, InitializingBean {

	private PontajForConcediiService pontajForConcediiService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			pontajForConcediiService
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		pontajForConcediiService.createPontajeForConcediiForYesterday();
		
		response.getWriter().print("OK");
		return null;
	}
	
	public void setPontajForConcediiService(PontajForConcediiService pontajForConcediiService) {
		this.pontajForConcediiService = pontajForConcediiService;
	}
}