package ro.cloudSoft.cloudDoc.services.bpm.custom.cerereConcediu;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentCerereConcediuConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.HolidayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CerereConcediuPersonalInsertHolidayEventInCalendar extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String cerereConcediuDocumentTypeName = getDocumentCerereConcediuConstants().getDocumentTypeName();
		
		DocumentType cerereConcediuDocumentType = getDocumentTypeDao().getDocumentTypeByName(cerereConcediuDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getDataInceputMetadataName());
		Date endDate = DocumentUtils.getMetadataDateValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getDataSfarsitMetadataName());
		Long beneficiarUserId = DocumentUtils.getMetadataUserValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getBeneficiarConcediuMetadataName());
		String tipConcediu = DocumentUtils.getMetadataListValueAsLabel(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getTipConcediuPersonalMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getCalendarMetadataName());
		String numeBeneficiarConcediu = getUserService().getDisplayName(beneficiarUserId);
		
		User beneficiar = getUserService().getUserById(beneficiarUserId);
		Calendar calendar = getCalendarDao().find(calendarId);
		HolidayCalendarEvent prepareCalendarEvent = prepareCalendarEvent(startDate, endDate, beneficiar, tipConcediu, numeBeneficiarConcediu, calendar);
		prepareCalendarEvent.setDocumentId(document.getId());
		prepareCalendarEvent.setDocumentLocationRealName(document.getDocumentLocationRealName());
		
		getCalendarEventDao().saveEvent(prepareCalendarEvent);
	}
	
	private HolidayCalendarEvent prepareCalendarEvent(Date startDate, Date endDate, User beneficiar, String tipConcediu, String numeBeneficiarConcediu, Calendar calendar) {
		HolidayCalendarEvent calendarEvent = new HolidayCalendarEvent();
		calendarEvent.setStartDate(DateUtils.nullHourMinutesSeconds(startDate));
		calendarEvent.setEndDate(DateUtils.maximizeHourMinutesSeconds(endDate));
		calendarEvent.setSubject(tipConcediu + " - " + numeBeneficiarConcediu);
		calendarEvent.setUser(beneficiar);
		calendarEvent.setCalendar(calendar);
		
		return calendarEvent;
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
	
	public DocumentCerereConcediuConstants getDocumentCerereConcediuConstants() {
		return SpringUtils.getBean("documentCerereConcediuConstants");
	}
}
