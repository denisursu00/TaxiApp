package ro.cloudSoft.cloudDoc.services.bpm.custom.decizieDeplasare;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDecizieDeplasareConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.StringUtils2;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DecizieDeplasareInsertEventInCalendar extends AutomaticTask {
	
	private static final String PREFIX_EVENT_NAME = "Deplasare la ";
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String decizieDeplasareDocumentTypeName = getDocumentDecizieDeplasareConstants().getDocumentTypeName();
				
		DocumentType decizieDeplasareDocumentType = getDocumentTypeDao().getDocumentTypeByName(decizieDeplasareDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateTimeValue(document, decizieDeplasareDocumentType, getDocumentDecizieDeplasareConstants().getDataPlecareMetadataName());
		Date endDate = DocumentUtils.getMetadataDateTimeValue(document, decizieDeplasareDocumentType, getDocumentDecizieDeplasareConstants().getDataSosireMetadataName());
		
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, decizieDeplasareDocumentType, getDocumentDecizieDeplasareConstants().getCalendarMetadataName());
		Calendar calendar = getCalendarDao().find(calendarId);
		
		String organismNomenclatorValueId = DocumentUtils.getMetadataValueAsString(document, decizieDeplasareDocumentType, getDocumentDecizieDeplasareConstants().getOrganismMetadataName());
		NomenclatorValue organismNomenclatorValue = getNomenclatorValueDao().find(Long.valueOf(organismNomenclatorValueId));
		String numeOrganism = NomenclatorValueUtils.getAttributeValueAsString(organismNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE);
		
		String userDelegatNomenclatorValueId = DocumentUtils.getMetadataValueAsString(document, decizieDeplasareDocumentType, getDocumentDecizieDeplasareConstants().getUserDelegatMetadataName());
		NomenclatorValue userDelegatNomenclatorValue = getNomenclatorValueDao().find(Long.valueOf(userDelegatNomenclatorValueId));
		Long responsabilUserDelegatNomenclatorPersoaneValueId = NomenclatorValueUtils.getAttributeValueAsLong(userDelegatNomenclatorValue, NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_REPREZENTANT);
		
		NomenclatorValue responsabilUserDelegatNomValue = getNomenclatorValueDao().find(responsabilUserDelegatNomenclatorPersoaneValueId);
		StringBuffer userDelegatName = new StringBuffer(NomenclatorValueUtils.getAttributeValueAsString(responsabilUserDelegatNomValue, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME));
		userDelegatName.append(" " + NomenclatorValueUtils.getAttributeValueAsString(responsabilUserDelegatNomValue, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME));
		if (startDate == null || endDate == null || organismNomenclatorValueId == null || calendarId == null || userDelegatNomenclatorValueId == null) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}
		
		MeetingCalendarEvent meetingCalendarEventModel = prepareMeetingCalendarEvent(startDate, endDate, numeOrganism, userDelegatName.toString(), calendar);
		meetingCalendarEventModel.setReprezentantExtern(responsabilUserDelegatNomValue);
		meetingCalendarEventModel.setLocation(numeOrganism);
		meetingCalendarEventModel.setDocumentId(document.getId());
		meetingCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		getCalendarEventDao().saveEvent(meetingCalendarEventModel);
	}

	private MeetingCalendarEvent prepareMeetingCalendarEvent(Date startDate, Date endDate, String numeOrganism, String userDelegatName, Calendar calendar) throws AutomaticTaskExecutionException {
		MeetingCalendarEvent calendarEventModel = new MeetingCalendarEvent();
		
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);

		String denumireEveniment = PREFIX_EVENT_NAME + numeOrganism + " - " + userDelegatName;
		calendarEventModel.setSubject(denumireEveniment);
		
		calendarEventModel.setCalendar(calendar);
		calendarEventModel.setAllDay(false);
		
		return calendarEventModel;
	}
	
	public DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	public CalendarDao getCalendarDao() {
		return SpringUtils.getBean("calendarDao");
	}
	
	public CalendarEventDao getCalendarEventDao() {
		return SpringUtils.getBean("calendarEventDao");
	}
	
	public UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	public NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
	
	public DocumentDecizieDeplasareConstants getDocumentDecizieDeplasareConstants() {
		return SpringUtils.getBean("documentDecizieDeplasareConstants");
	}
}
