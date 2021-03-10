package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.services.replacementProfiles.ReplacementProfilesService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ActivateOrExpireReplacementProfilesScheduledTask implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(ActivateOrExpireReplacementProfilesScheduledTask.class);

	private ReplacementProfilesService replacementProfilesService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			replacementProfilesService
		);
	}
	
	public void activateOrExpire() {
		
		LOGGER.info("A pornit task-ul automat pentru activarea sau expirarea profilelor de inlocuire.");
		
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		
		activate(today);
		expire(today);
		
		LOGGER.info("S-a terminat task-ul automat pentru activarea sau expirarea profilelor de inlocuire.");
	}
	
	private void activate(Date today) {
		LOGGER.info("A inceput activarea profilelor de inlocuire.");
		try {
			replacementProfilesService.activateReplacementProfilesThatBegan(today);
			LOGGER.info("S-a finalizat cu succes activarea profilelor de inlocuire.");
		} catch (Exception e) {
			LOGGER.error("Exceptie la activarea profilelor de inlocuire", e,
				"activarea profilelor de inlocuire");
		}
	}
	
	private void expire(Date today) {
		Date yersterday = DateUtils.addDays(today, -1);
		LOGGER.info("A inceput expirarea profilelor de inlocuire.");
		try {
			replacementProfilesService.expireReplacementProfilesThatEnded(yersterday);
			LOGGER.info("S-a finalizat cu succes expirarea profilelor de inlocuire.");
		} catch (Exception e) {
			LOGGER.error("Exceptie la expirarea profilelor de inlocuire", e,
				"expirarea profilelor de inlocuire");
		}
	}
	
	public void setReplacementProfilesService(ReplacementProfilesService replacementProfilesService) {
		this.replacementProfilesService = replacementProfilesService;
	}
}