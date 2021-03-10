package ro.cloudSoft.cloudDoc.services.bpm.custom.agendaSedintaPvg;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentAgendaSedintaPvgConstants;
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
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AgendaSedintaPvgInsertEventInCalendar extends AutomaticTask {

	private static final String PREFIX_EVENT_DESCRIPTION = "Sedinta PVG ";
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String agendaSedintaPvgConstantsDocumentTypeName = getDocumentAgendaSedintaPvgConstants().getDocumentTypeName();
				
		DocumentType agendaSedintaPvgConstantsDocumentType = getDocumentTypeDao().getDocumentTypeByName(agendaSedintaPvgConstantsDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateTimeValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentAgendaSedintaPvgConstants().getDataInceputMetadataName());
		Date endDate = DocumentUtils.getMetadataDateTimeValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentAgendaSedintaPvgConstants().getDataSfarsitMetadataName());
		String location = DocumentUtils.getMetadataValueAsString(document, agendaSedintaPvgConstantsDocumentType, getDocumentAgendaSedintaPvgConstants().getLocatieMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, agendaSedintaPvgConstantsDocumentType, getDocumentAgendaSedintaPvgConstants().getCalendarMetadataName());
		
		if (startDate == null || endDate == null || location == null || calendarId == null ) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}
		
		Calendar calendar = getCalendarDao().find(calendarId);
		MeetingCalendarEvent meetingCalendarEventModel = prepareMeetingCalendarEvent(startDate, endDate, location, calendar);
		meetingCalendarEventModel.setDocumentId(document.getId());
		meetingCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		
		getCalendarEventDao().saveEvent(meetingCalendarEventModel);
	}

	private MeetingCalendarEvent prepareMeetingCalendarEvent(Date startDate, Date endDate, String location, Calendar calendar) {
		MeetingCalendarEvent calendarEventModel = new MeetingCalendarEvent();
		
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);
		calendarEventModel.setLocation(location);
		
		calendarEventModel.setSubject(PREFIX_EVENT_DESCRIPTION);
		
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
	
	public DocumentAgendaSedintaPvgConstants getDocumentAgendaSedintaPvgConstants() {
		return SpringUtils.getBean("documentAgendaSedintaPvgConstants");
	}
}
