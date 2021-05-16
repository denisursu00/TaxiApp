package ro.taxiApp.docs.presentation.server.services;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;
import ro.taxiApp.docs.presentation.client.shared.services.AclGxtService;
import ro.taxiApp.docs.presentation.server.converters.SecurityManagerConverter;

public class AclGxtServiceImpl extends GxtServiceImplBase implements AclGxtService, InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies();
	}
	
	@Override
	public SecurityManagerModel getSecurityManager() {
		return SecurityManagerConverter.getModelFromSecurityManager(this.getSecurity());
	}
	
	@Override
	public void invalidateSession() {
		// Nu trebuie invalidata sesiunea intrucat mai exista date utile filtrului de logout.
		// getSession().invalidate();
	}

	@Override
	public void dummy(SecurityManagerModel securityManagerModel, UserModel userModel)
			throws PresentationException {
		// TODO Auto-generated method stub
		
	}
}