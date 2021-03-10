package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.GroupConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.notifications.NotificareNrOrganismeBugetateDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.notifications.NotificareNrOrganismeBugetate;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class NotificareMailNrOrganismeBugetateScheduledTask implements InitializingBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(NotificareMailNrOrganismeBugetateScheduledTask.class);
	private static final int WEEK_DAYS_NR = 7;
	private static final int MONDAY_INDEX = 1;
	private static final int TWO_WEEKS_DAYS_NR = 14;

	private UserPersistencePlugin userPersistencePlugin;
	private ParametersDao parametersDao;
	private MailService mailService;
	private NotificareNrOrganismeBugetateDao notificareNrOrganismeBugetateDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(userPersistencePlugin, parametersDao, mailService, notificareNrOrganismeBugetateDao);
	}

	public void run() {
		String dataInceputNotificareString = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_NR_ORGANISME_BUGETATE_DATA_INCEPUT).getValue();

		EmailMessage emailMessage = null; 
		try {
			Date dataInceputNotificare = DateUtils.parse(dataInceputNotificareString, FormatConstants.PARAMETER_DATE_FOR_STORAGE);
			
			int anDeVerificat = DateUtils.getCurrentYear();
			if (isAfterHalfYear(dataInceputNotificare)) {
				anDeVerificat++;
			}
			if (isDueDate(dataInceputNotificare, anDeVerificat)) {
				emailMessage = buildMessage();
				mailService.send(emailMessage);
				NotificareNrOrganismeBugetate notificare = new NotificareNrOrganismeBugetate();
				notificare.setAn(anDeVerificat);
				notificareNrOrganismeBugetateDao.saveNotificareNrOrganismeBugetate(notificare);
			}

		} catch (RuntimeException | ParseException re) {
			String logMessage = "Nu s-a putut notifica o utilizatorii din grupul secretariat legat de data inceput sedinta AGA. "
					+ "Mail-ul pentru care s-a incercat trimiterea este: [" + emailMessage + "].";
			LOGGER.error(logMessage, re, "TrimitereMailSedintaAgaScheduledTask");
		}

	}
	private boolean isAfterHalfYear(Date data) {
		int luna = data.getMonth();
		if (luna > 6) {
			return true;
		}
		return false;
	}

	private EmailMessage buildMessage() {
		String subject = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_NR_ORGANISME_BUGETATE_SUBIECT).getValue();
		String content = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_NR_ORGANISME_BUGETATE_CONTINUT).getValue();
		
		Set<User> usersSecretariat = userPersistencePlugin.getUsersInGroupWithName(GroupConstants.GROUP_NAME_LOGISTICA);
		List<String> toAddresses = new ArrayList<>();
		usersSecretariat.forEach(user -> {
			toAddresses.add(user.getEmail());
		});
		return new EmailMessage(toAddresses, subject, content);
		
	}

	private boolean isDueDate(Date dataStartNotificare, int anDeVerificat) {
		int daysTillFirstMondey = WEEK_DAYS_NR - dataStartNotificare.getDay()  + MONDAY_INDEX;
		Date dataNotificareFirstMonday = DateUtils.addDays(dataStartNotificare, daysTillFirstMondey);
		Date today = new Date();
		today = DateUtils.maximizeHourMinutesSeconds(today);
		long nrNotificariAnCurent = notificareNrOrganismeBugetateDao.getNumarNotificariNrOrganismeBugetatePerAn(anDeVerificat);
		return (today.getTime() > dataNotificareFirstMonday.getTime() && nrNotificariAnCurent < 1) 
				|| (today.getTime() > DateUtils.addDays(dataNotificareFirstMonday, TWO_WEEKS_DAYS_NR).getTime()  && nrNotificariAnCurent < 2);
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}

	public void setParametersDao(ParametersDao parmetersDao) {
		this.parametersDao = parmetersDao;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setNotificareNrOrganismeBugetateDao(NotificareNrOrganismeBugetateDao notificareNrOrganismeBugetateDao) {
		this.notificareNrOrganismeBugetateDao = notificareNrOrganismeBugetateDao;
	}

}