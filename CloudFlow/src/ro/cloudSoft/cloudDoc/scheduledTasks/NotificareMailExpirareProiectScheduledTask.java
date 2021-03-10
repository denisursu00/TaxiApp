package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.GroupConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.dao.notifications.NotificareExpirareMandatReprezentantiComisieGlDao;
import ro.cloudSoft.cloudDoc.dao.notifications.NotificareExpirareProiectDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareMandatReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareProiect;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;


public class NotificareMailExpirareProiectScheduledTask implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(NotificareMailExpirareProiectScheduledTask.class);
	
	private ParametersDao parametersDao;
	private MailService mailService;
	private ProjectDao projectDao;
	private NotificareExpirareProiectDao notificareExpirareProiectDao;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies( parametersDao, mailService, projectDao, notificareExpirareProiectDao);		
	}
	
	public void run() {

		String numarZilePanaLaExpirare = parametersDao.getByName(ParameterConstants.PARAM_PROIECT_NUMAR_ZILE_PANA_LA_EXPIRARE).getValue();
		Date endDate = new Date();
		
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, Integer.parseInt(numarZilePanaLaExpirare));
        endDate = calendar.getTime();
		
        List<Project> proiecte = projectDao.getAllByEndDate(DateUtils.nullHourMinutesSeconds(endDate));

        for (Project proiect : proiecte) {
        	if (!notificareExpirareProiectDao.existByProjectIdAndDate(proiect.getId(), endDate)) {
        		EmailMessage emailMessage = null; 
        		try {
        			emailMessage = buildMessage(proiect);
        			mailService.send(emailMessage);
        			
        			NotificareExpirareProiect notificare = new NotificareExpirareProiect();
        			notificare.setProject(proiect);
        			notificare.setProjectEndDate(proiect.getEndDate());
        			
        			notificareExpirareProiectDao.save(notificare);
        		} catch (Exception  e) {
        			String logMessage = "Nu s-a putut notifica responsabilul pe proiect. "
        					+ "Mail-ul pentru care s-a incercat trimiterea este: [" + emailMessage + "].";
        			LOGGER.error(logMessage, e, "NotificareMailExpirareProiectScheduledTask");
        		}
        	}
        }
	}
	
	private EmailMessage buildMessage(Project proiect) {
		String subject = "Expirare proiect " + proiect.getName();
		String content = "Proiectul <b>" + proiect.getName() + "</b> va expira in data de <b>" + DateFormatUtils.format(proiect.getEndDate(), FormatConstants.DATE_FOR_DISPLAY) + "</b>.";
		
		String toAddresses = proiect.getResponsibleUser().getEmail();
		
		EmailMessage emailMessage = new EmailMessage(toAddresses, subject, content);
		emailMessage.setHasHtmlContent(true);
		
		return emailMessage;
		
	}
	
	
	public void setParametersDao(ParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setNotificareExpirareProiectDao(NotificareExpirareProiectDao notificareExpirareProiectDao) {
		this.notificareExpirareProiectDao = notificareExpirareProiectDao;
	}

}
