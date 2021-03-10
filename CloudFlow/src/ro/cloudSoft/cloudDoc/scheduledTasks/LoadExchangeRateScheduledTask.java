package ro.cloudSoft.cloudDoc.scheduledTasks;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.services.cursValutar.CursValutarService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class LoadExchangeRateScheduledTask implements InitializingBean {

	private CursValutarService cursValutarService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
				cursValutarService
		);
	}
	
	public void loadExchangeRate() throws Exception {
		cursValutarService.loadCursValutar();
	}
	
	public void setCursValutarService(CursValutarService cursValutarService) {
		this.cursValutarService = cursValutarService;
	}
}
