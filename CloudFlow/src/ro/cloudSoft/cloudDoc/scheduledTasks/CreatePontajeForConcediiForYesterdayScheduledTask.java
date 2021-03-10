package ro.cloudSoft.cloudDoc.scheduledTasks;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.services.PontajForConcediiService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class CreatePontajeForConcediiForYesterdayScheduledTask implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(CreatePontajeForConcediiForYesterdayScheduledTask.class);

	private PontajForConcediiService pontajForConcediiService;
	private boolean timesheetsForLeavesIntegrationEnabled;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			pontajForConcediiService,
			timesheetsForLeavesIntegrationEnabled
		);
	}
	
	public void run() {
		
		if (!timesheetsForLeavesIntegrationEnabled) {
			LOGGER.info("Intrucat integrarea cu pontajul pentru concedii este dezactivata, task-ul automat pentru crearea " +
				"sau actualizarea pontajelor pentru concedii NU va rula.", "crearea sau actualizarea pontajelor pentru concedii");
			return;
		}
		
		LOGGER.info("A inceput task-ul automat pentru crearea sau actualizarea pontajelor pentru concedii.",
			"crearea sau actualizarea pontajelor pentru concedii");
		
		try {
			pontajForConcediiService.createPontajeForConcediiForYesterday();
			LOGGER.info("S-a finalizat task-ul automat pentru crearea sau actualizarea pontajelor pentru concedii.",
				"crearea sau actualizarea pontajelor pentru concedii");
		} catch (Exception e) {
			LOGGER.error("Exceptie in timpul rularii task-ului automat pentru crearea sau actualizarea pontajelor pentru concedii",
				e, "crearea sau actualizarea pontajelor pentru concedii");
		}
	}
	
	public void setPontajForConcediiService(PontajForConcediiService pontajForConcediiService) {
		this.pontajForConcediiService = pontajForConcediiService;
	}
	public void setTimesheetsForLeavesIntegrationEnabled(boolean timesheetsForLeavesIntegrationEnabled) {
		this.timesheetsForLeavesIntegrationEnabled = timesheetsForLeavesIntegrationEnabled;
	}
}