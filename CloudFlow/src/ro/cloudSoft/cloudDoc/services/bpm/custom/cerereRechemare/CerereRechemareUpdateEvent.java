package ro.cloudSoft.cloudDoc.services.bpm.custom.cerereRechemare;

import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentCerereRechemareConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.HolidayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.StringUtils2;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CerereRechemareUpdateEvent extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String cerereRechemareDocumentTypeName = getDocumentCerereRechemareConstants().getDocumentTypeName();
		
		DocumentType cerereRechemareDocumentType = getDocumentTypeDao().getDocumentTypeByName(cerereRechemareDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date dataInceputConcediuAprobat = DocumentUtils.getMetadataDateValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getDataInceputConcediuAprobatMetadataName());
		Date dataSfarsitConcediuAprobat = DocumentUtils.getMetadataDateValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getDataSfarsitConcediuAprobatMetadataName());
		Date dataIntrairiInVigoareARechemarii = DocumentUtils.getMetadataDateValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getDataIntraiiInVigoareARechemariiMetadataName());
		
		Long userRechematId = DocumentUtils.getMetadataUserValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getUserRechematMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(document, cerereRechemareDocumentType, getDocumentCerereRechemareConstants().getCalendarMetadataName());
		
		HolidayCalendarEvent event = (HolidayCalendarEvent) getCalendarEventDao().getEvent(calendarId, DateUtils.nullHourMinutesSeconds(dataInceputConcediuAprobat), DateUtils.maximizeHourMinutesSeconds(dataSfarsitConcediuAprobat), userRechematId);
		
		if (event == null) {
			throw new AutomaticTaskExecutionException("[HolidayCalendarEvent] Cannot fine event with start date [" + dataInceputConcediuAprobat + "] and end date [" + dataSfarsitConcediuAprobat + "] on calendar "
					+ " with id [" + calendarId + "] for user with id [" + userRechematId + "].");
		}
		
		if (DateUtils.isAfterDate(dataInceputConcediuAprobat, dataIntrairiInVigoareARechemarii)) {
			throw new  AutomaticTaskExecutionException("[HolidayCalendarEvent] dataIntrairiInVigoareARechemarii [" + dataIntrairiInVigoareARechemarii + "] cannot be lower "
					+ "than dataInceputConcediuAprobat [" + dataInceputConcediuAprobat + "].");
		}
		
		if (DateUtils.isSameDay(dataInceputConcediuAprobat, dataIntrairiInVigoareARechemarii)) {
			event.setEndDate(DateUtils.nullHourMinutesSeconds(dataIntrairiInVigoareARechemarii));
			event.setSubject(StringUtils2.appendToStringWithSeparator(event.getSubject(), "Anulat", " - "));
		} else {
			dataIntrairiInVigoareARechemarii = DateUtils.addDays(dataIntrairiInVigoareARechemarii, -1);
			event.setEndDate(DateUtils.maximizeHourMinutesSeconds(dataIntrairiInVigoareARechemarii));
		}
		event.setDocumentId(document.getId());
		event.setDocumentLocationRealName(document.getDocumentLocationRealName());

		getCalendarEventDao().saveEvent(event);
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
	
	public DocumentCerereRechemareConstants getDocumentCerereRechemareConstants() {
		return SpringUtils.getBean("documentCerereRechemareConstants");
	}
}
