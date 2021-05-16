package ro.taxiApp.docs.presentation.server.services;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;
import ro.taxiApp.docs.presentation.client.shared.services.AclGxtService;
import ro.taxiApp.docs.presentation.server.converters.SecurityManagerConverter;
import ro.taxiApp.docs.security.SecurityManagerHolder;

public class AclGxtServiceImpl implements AclGxtService, InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies();
	}
	
	@Override
	public SecurityManagerModel getSecurityManager(SecurityManager securityManager) {
		return SecurityManagerConverter.getModelFromSecurityManager(securityManager);
	}

}