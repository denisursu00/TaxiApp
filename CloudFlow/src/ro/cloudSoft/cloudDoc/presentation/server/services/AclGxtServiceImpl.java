package ro.cloudSoft.cloudDoc.presentation.server.services;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AclGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.SecurityManagerConverter;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class AclGxtServiceImpl extends GxtServiceImplBase implements AclGxtService, InitializingBean {
	
	private BusinessConstants businessConstants;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies();
	}
	
	@Override
	public SecurityManagerModel getSecurityManager() {
		return SecurityManagerConverter.getModelFromSecurityManager(this.getSecurity(), this.businessConstants);
	}
	
	@Override
	public void invalidateSession() {
		// Nu trebuie invalidata sesiunea intrucat mai exista date utile filtrului de logout.
		// getSession().invalidate();
	}
	
	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}
}