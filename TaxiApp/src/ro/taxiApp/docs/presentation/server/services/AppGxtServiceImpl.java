package ro.taxiApp.docs.presentation.server.services;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.presentation.client.shared.services.AppGxtService;

public class AppGxtServiceImpl extends GxtServiceImplBase implements AppGxtService, InitializingBean {

	
	public AppGxtServiceImpl() {}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies();
	}
	
	@Override
	public Integer getSessionTimeoutInSeconds() {
		return getSession().getMaxInactiveInterval();
	}
	
	@Override
	public void keepSessionAlive() {
		getSession().getAttribute("dummy");
	}
	
}