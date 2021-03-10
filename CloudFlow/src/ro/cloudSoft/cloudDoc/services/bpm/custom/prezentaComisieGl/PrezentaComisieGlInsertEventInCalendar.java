package ro.cloudSoft.cloudDoc.services.bpm.custom.prezentaComisieGl;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaComisieGlConstants;
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
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class PrezentaComisieGlInsertEventInCalendar extends AutomaticTask {

	private static final String PREFIX_EVENT_DESCRIPTION = "Sedinta";
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String agendaSedintaPvgConstantsDocumentTypeName = getDocumentPrezentaComisieGlConstants().getDocumentTypeName();
				
		DocumentType prezentaComisieGlDocumentType = getDocumentTypeDao().getDocumentTypeByName(agendaSedintaPvgConstantsDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateTimeValue(document, prezentaComisieGlDocumentType, getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
		Date endDate = DocumentUtils.getMetadataDateTimeValue(document, prezentaComisieGlDocumentType, getDocumentPrezentaComisieGlConstants().getDataSfarsitMetadataName());
		Long denumireComisieGlId = DocumentUtils.getMetadataNomenclatorValue(document, prezentaComisieGlDocumentType, getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, prezentaComisieGlDocumentType, getDocumentPrezentaComisieGlConstants().getCalendarMetadataName());
		
		if (startDate == null || endDate == null ||  denumireComisieGlId == null || calendarId == null) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}
		
		Calendar calendar = getCalendarDao().find(calendarId);
		
		NomenclatorValue comisieGlNomenclatorValue = getNomenclatorValueDao().find(denumireComisieGlId);
		String denumireComisieGl = NomenclatorValueUtils.getAttributeValueAsString(comisieGlNomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE);
		
		String location= DocumentUtils.getMetadataValueAsString( document, prezentaComisieGlDocumentType, getDocumentPrezentaComisieGlConstants().getLocatieMetadataName());
		
		MeetingCalendarEvent meetingCalendarEventModel = prepareMeetingCalendarEvent(startDate, endDate, denumireComisieGl, calendar);
		meetingCalendarEventModel.setDocumentId(document.getId());
		meetingCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		meetingCalendarEventModel.setLocation(location);
		getCalendarEventDao().saveEvent(meetingCalendarEventModel);
	}

	private MeetingCalendarEvent prepareMeetingCalendarEvent(Date startDate, Date endDate, String denumireComisieGl, Calendar calendar) {
		MeetingCalendarEvent calendarEventModel = new MeetingCalendarEvent();
		
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);
		
		String denumireEveniment = PREFIX_EVENT_DESCRIPTION + " - " + denumireComisieGl.trim();
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
	
	public DocumentPrezentaComisieGlConstants getDocumentPrezentaComisieGlConstants() {
		return SpringUtils.getBean("documentPrezentaComisieGlConstants");
	}
}
