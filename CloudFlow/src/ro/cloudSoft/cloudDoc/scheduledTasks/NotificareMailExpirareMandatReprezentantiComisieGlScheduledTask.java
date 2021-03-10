package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.ImmutableMap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.dao.notifications.NotificareExpirareMandatReprezentantiComisieGlDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.notifications.NotificareExpirareMandatReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.templating.TemplateEngine;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class NotificareMailExpirareMandatReprezentantiComisieGlScheduledTask implements InitializingBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(NotificareMailExpirareMandatReprezentantiComisieGlScheduledTask.class);
	private static final String PLACEHOLDER_SUBIECT_NUME_COMSIE_NAME = "nume_comisie";
	private static final String PLACEHOLDER_CONTINUT_PERSOANE_NAME = "persoane";
	private static final int TERMEN_EXPIRARE_MANDAT_NR_ZILE = 60;

	private ParametersDao parametersDao;
	private MailService mailService;
	private ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao;
	private NotificareExpirareMandatReprezentantiComisieGlDao notificareExpirareMandatReprezentantiComisieGlDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies( parametersDao, mailService, reprezentantiComisieSauGLDao, notificareExpirareMandatReprezentantiComisieGlDao);
	}

	public void run() {

		String subiectTemplateString = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_EXPIRARE_MANDAT_REPREZENTANTI_COMISIE_GL_SUBIECT).getValue();
		String continut = parametersDao.getByName(ParameterConstants.PARAM_NAME_NOTIFICARI_EMAIL_EXPIRARE_MANDAT_REPREZENTANTI_COMISIE_GL_CONTINUT).getValue();
		List<ReprezentantiComisieSauGL> reprezentanti = reprezentantiComisieSauGLDao.getAllWithExpiredMandatsSince60Days();
		
		for(ReprezentantiComisieSauGL reprezentant: reprezentanti) {
			Map<String, String> expMandatDateByPersFullName = new HashMap<>();
			List<NotificareExpirareMandatReprezentantiComisieGl> notificariDeTrimis = new ArrayList<>();
			checkDateExpiredAndAddToNotificariDeTrimis(reprezentant.getDataExpirareMandatPresedinte(),reprezentant.getPresedinte(), expMandatDateByPersFullName, reprezentant, notificariDeTrimis);
			checkDateExpiredAndAddToNotificariDeTrimis(reprezentant.getDataExpirareMandatVicepresedinte1(),reprezentant.getVicepresedinte1(), expMandatDateByPersFullName, reprezentant, notificariDeTrimis);
			checkDateExpiredAndAddToNotificariDeTrimis(reprezentant.getDataExpirareMandatVicepresedinte2(),reprezentant.getVicepresedinte2(), expMandatDateByPersFullName, reprezentant, notificariDeTrimis);
			checkDateExpiredAndAddToNotificariDeTrimis(reprezentant.getDataExpirareMandatVicepresedinte3(),reprezentant.getVicepresedinte3(), expMandatDateByPersFullName, reprezentant, notificariDeTrimis);
			if (notificariDeTrimis.isEmpty()) {
				continue;
			}
			String denumireComisieSauGl = NomenclatorValueUtils.getAttributeValueAsString(reprezentant.getComisieSauGL(), NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE);
			User responsabilARB = reprezentant.getResponsabilARB();
			EmailMessage emailMessage = null;
			try {
				emailMessage = new EmailMessage(responsabilARB.getEmail(), buildSubiect(denumireComisieSauGl, subiectTemplateString), buildContent(continut, expMandatDateByPersFullName), true);
				mailService.send(emailMessage);
				for (NotificareExpirareMandatReprezentantiComisieGl notificare: notificariDeTrimis) {
					notificareExpirareMandatReprezentantiComisieGlDao.save(notificare);
				}
			} catch (RuntimeException | AppException re) {
				String logMessage = "Nu s-a putut notifica o utilizatorii din grupul secretariat legat de data inceput sedinta AGA. "
						+ "Mail-ul pentru care s-a incercat trimiterea este: [" + emailMessage + "].";
				LOGGER.error(logMessage, re, "TrimitereMailSedintaAgaScheduledTask");
			}
		}
		
	}
	
	private String buildSubiect(String numeComsie, String subjectTemplateString) throws AppException {
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
				.put(PLACEHOLDER_SUBIECT_NUME_COMSIE_NAME, numeComsie)
				.build();
		
		return getTemplateEngine().processFromStringTemplate(subjectTemplateString , contextMap);
	}
	
	private String buildContent(String continut, Map<String, String> expMandatDateByPersFullName) throws AppException {
		StringBuilder persoaneAsHtmlTable = new StringBuilder();
		persoaneAsHtmlTable.append("<body> <table border=\"1\">");
		persoaneAsHtmlTable.append("<tr>");
		persoaneAsHtmlTable.append("<td><strong> Persoana </strong></td>");
		persoaneAsHtmlTable.append("<td><strong> Data expirare </strong></td>");
		persoaneAsHtmlTable.append("</tr>");

		expMandatDateByPersFullName.forEach((persoana, dataExpirare) -> {
			persoaneAsHtmlTable.append("<tr>");
			persoaneAsHtmlTable.append("<td>" + persoana + "</td>");
			persoaneAsHtmlTable.append("<td>" + dataExpirare + "</td>");
			persoaneAsHtmlTable.append("</tr>");
		}); 
   
		persoaneAsHtmlTable.append("</table></body>");
		
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
				.put(PLACEHOLDER_CONTINUT_PERSOANE_NAME, persoaneAsHtmlTable.toString())
				.build();
		return getTemplateEngine().processFromStringTemplate(continut , contextMap);
	}

	private void checkDateExpiredAndAddToNotificariDeTrimis(Date data, NomenclatorValue pers, Map<String, String> expMandatDateByPersFullName, ReprezentantiComisieSauGL reprezentant, List<NotificareExpirareMandatReprezentantiComisieGl> notificariDeTrimis) {
		Date today = new Date();
		Date after60DaysDate = DateUtils.addDays(today, TERMEN_EXPIRARE_MANDAT_NR_ZILE);
		after60DaysDate = DateUtils.maximizeHourMinutesSeconds(after60DaysDate);
		if ( data != null && pers != null && data.getTime() < after60DaysDate.getTime()  && !notificareExpirareMandatReprezentantiComisieGlDao.existByReprezentantComisieSauGlIdAndPersoanaId(reprezentant.getId(), pers.getId())) {
			String numePers = NomenclatorValueUtils.getAttributeValueAsString(pers, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);
			String prenumePers = NomenclatorValueUtils.getAttributeValueAsString(pers, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
			expMandatDateByPersFullName.put(numePers + " " + prenumePers, DateFormatUtils.format(data, FormatConstants.DATE_FOR_DISPLAY));
			NotificareExpirareMandatReprezentantiComisieGl notificare = new NotificareExpirareMandatReprezentantiComisieGl();
			notificare.setPersoana(pers);
			notificare.setReprezentantComisieSauGL(reprezentant);
			notificariDeTrimis.add(notificare);
		}
	}

	public void setParametersDao(ParametersDao parmetersDao) {
		this.parametersDao = parmetersDao;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setReprezentantiComisieSauGLDao(ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao) {
		this.reprezentantiComisieSauGLDao = reprezentantiComisieSauGLDao;
	}

	public void setNotificareExpirareMandatReprezentantiComisieGlDao(NotificareExpirareMandatReprezentantiComisieGlDao notificareExpirareMandatReprezentantiComisieGlDao) {
		this.notificareExpirareMandatReprezentantiComisieGlDao = notificareExpirareMandatReprezentantiComisieGlDao;
	}

	public TemplateEngine getTemplateEngine() {
		return SpringUtils.getBean("templateEngine");
	}
}