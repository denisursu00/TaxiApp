package ro.cloudSoft.cloudDoc.presentation.server.services;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironment;
import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ApplicationInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

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
	
	@Override
	public ApplicationInfoModel getApplicationInfo() {
		ApplicationInfoModel appInfo = new ApplicationInfoModel();
		appInfo.setEnvironmentName(AppEnvironmentConfig.getCurrentEnvironment().name());
		appInfo.setProduction(AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.PRODUCTION));
		return appInfo;
	}
}