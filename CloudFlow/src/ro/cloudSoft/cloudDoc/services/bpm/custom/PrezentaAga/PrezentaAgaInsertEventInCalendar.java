package ro.cloudSoft.cloudDoc.services.bpm.custom.PrezentaAga;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaAgaConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.templating.TemplateEngine;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class PrezentaAgaInsertEventInCalendar extends AutomaticTask {

	private static final String TITLE_TEMPLATE_RELATIVE_PATH = "/bpm/automaticTasks/prezentaAgaInsertEventInCalendarEventTitle.ftl";
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String agendaSedintaPvgConstantsDocumentTypeName = getDocumentPrezentaAgaConstants().getDocumentTypeName();
				
		DocumentType agendaSedintaPvgConstantsDocumentType = getDocumentTypeDao().getDocumentTypeByName(agendaSedintaPvgConstantsDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateTimeValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentPrezentaAgaConstants().getDataInceputMetadataName());
		Date endDate = DocumentUtils.getMetadataDateTimeValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentPrezentaAgaConstants().getDataSfarsitMetadataName());
		String location = DocumentUtils.getMetadataValueAsString(document, agendaSedintaPvgConstantsDocumentType, getDocumentPrezentaAgaConstants().getLocatieMetadataName());
		String sedinta = DocumentUtils.getMetadataValueAsString(document, agendaSedintaPvgConstantsDocumentType, getDocumentPrezentaAgaConstants().getSedintaMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentPrezentaAgaConstants().getCalendarMetadataName());
		
		if (startDate == null || endDate == null || location == null || calendarId == null) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}
		
		Calendar calendar = getCalendarDao().find(calendarId);
		MeetingCalendarEvent meetingCalendarEventModel = prepareMeetingCalendarEvent(startDate, endDate, location, sedinta, calendar);
		meetingCalendarEventModel.setDocumentId(document.getId());
		meetingCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		
		getCalendarEventDao().saveEvent(meetingCalendarEventModel);
	}

	private MeetingCalendarEvent prepareMeetingCalendarEvent(Date startDate, Date endDate, String locatie, String sedinta, Calendar calendar) throws AutomaticTaskExecutionException {
		MeetingCalendarEvent calendarEventModel = new MeetingCalendarEvent();
		
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);
		calendarEventModel.setLocation(locatie);

		String denumireEveniment = getEventName(locatie, sedinta);
		calendarEventModel.setSubject(denumireEveniment);
		
		calendarEventModel.setCalendar(calendar);
		calendarEventModel.setAllDay(false);
		
		return calendarEventModel;
	}
	
	private String getEventName(String locatie, String sedinta) throws AutomaticTaskExecutionException {
		
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
				.put("sedinta", sedinta)
				.put("locatie", locatie)
				.build();
		
		try {
			return getTemplateEngine().processFromFileTemplate(TITLE_TEMPLATE_RELATIVE_PATH, contextMap);
		} catch (AppException ae) {
			throw new AutomaticTaskExecutionException("Process engine couldn't process the template with name [" + TITLE_TEMPLATE_RELATIVE_PATH + "]", ae);
		}
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
	
	public TemplateEngine getTemplateEngine() {
		return SpringUtils.getBean("templateEngine");
	}
	
	public DocumentPrezentaAgaConstants getDocumentPrezentaAgaConstants() {
		return SpringUtils.getBean("documentPrezentaAgaConstants");
	}
}
