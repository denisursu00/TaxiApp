package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.ImmutableMap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.GroupConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.calendar.IntervalCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.templating.TemplateEngine;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class NotificareMailSedintaAgaScheduledTask implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(NotificareMailSedintaAgaScheduledTask.class);

	private CalendarEventDao calendareEventDao;
	private UserPersistencePlugin userPersistencePlugin;
	private ParametersDao parametersDao;
	private MailService mailService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
				calendareEventDao,
				userPersistencePlugin,
				parametersDao,
				mailService
		);
	}
	
	public void run() {
		 
		String subjectTemplateString = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_SEDINTA_AGA_SUBIECT).getValue();
		String contentTemplateString = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_SEDINTA_AGA_CONTINUT).getValue();
		List<MeetingCalendarEvent> events = calendareEventDao.getCalendarEventsForSedintaAgaNotification();
		Set<User> usersSecretariat = userPersistencePlugin.getUsersInGroupWithName(GroupConstants.GROUP_NAME_SECRETARIAT);
		List<String> toAddresses = new ArrayList<>();
		usersSecretariat.forEach(user -> {
			toAddresses.add(user.getEmail());
		});
		for (MeetingCalendarEvent event: events) {
			EmailMessage emailMessage = null;;
			try {
				emailMessage = new EmailMessage(toAddresses, buildSubiect(event.getStartDate(), subjectTemplateString), buildContinut(event.getStartDate(), contentTemplateString));
				mailService.send(emailMessage);
				event.setNotificat(true);
				calendareEventDao.saveEvent(event);
			} catch (RuntimeException | AppException re) {
				String logMessage = "Nu s-a putut notifica responsabil comisie sau gl legat de expirare mandat. " +
						"Mail-ul pentru care s-a incercat trimiterea este: [" + emailMessage + "].";
				LOGGER.error(logMessage, re, "TrimitereMailSedintaAgaScheduledTask");
			}
		}
	}

	private String buildSubiect(Date dataSedinta, String subjectTemplateString) throws AppException {
		String dataSedintaAsString = DateFormatUtils.format(dataSedinta, FormatConstants.DATE_FOR_DISPLAY);
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
				.put("data", dataSedintaAsString)
				.build();
		
		return getTemplateEngine().processFromStringTemplate(subjectTemplateString , contextMap);
	}

	private String buildContinut(Date dataSedinta, String continutTemplateString) throws AppException {
		String dataSedintaAsString = DateFormatUtils.format(dataSedinta, FormatConstants.DATE_FOR_DISPLAY);
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
				.put("data", dataSedintaAsString)
				.build();
		
		return getTemplateEngine().processFromStringTemplate(continutTemplateString , contextMap);
	}
	
	public void setCalendareEventDao(CalendarEventDao calendareEventDao) {
		this.calendareEventDao = calendareEventDao;
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

	public TemplateEngine getTemplateEngine() {
		return SpringUtils.getBean("templateEngine");
	}
	
}