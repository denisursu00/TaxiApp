package ro.cloudSoft.cloudDoc.services.bpm.custom;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.MeetingCalendarEventModel;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class InsertEventInCalendar extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {
		Document document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		
		Date startDate = null;
		Date endDate = null;
		String location = null;
		String denumireEveniment = null;
		
		List<MetadataInstance> documentMetadataInstanceList = document.getMetadataInstanceList();
		for (MetadataInstance metadataInstance : documentMetadataInstanceList) {
			metadataInstance.getMetadataDefinitionId();
			MetadataDefinition metadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
			
			if (metadataDefinition.getName().equals("data_inceput")) {
				startDate = MetadataValueHelper.getDateTime(metadataInstance.getValue());
			}
			if (metadataDefinition.getName().equals("data_sfarsit")) {
				endDate = MetadataValueHelper.getDateTime(metadataInstance.getValue());
			}
			if (metadataDefinition.getName().equals("locatie")) {
				location = metadataInstance.getValue();
			}
			if (metadataDefinition.getName().equals("denumire_eveniment")) {
				denumireEveniment = metadataInstance.getValue();
			}
		}
		
		if (startDate == null || endDate == null || location == null || denumireEveniment == null) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}
		// TODO: Trebuie scrhimbata implementarea
//		getCalendarService().saveCalendarEvent(prepareCalendarEventModel(startDate, endDate, location, denumireEveniment));
	}
	
	private MeetingCalendarEventModel prepareCalendarEventModel(Date startDate, Date endDate, String location, String denumireEveniment) {
		MeetingCalendarEventModel calendarEventModel = new MeetingCalendarEventModel();
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);
		calendarEventModel.setLocation(location);
		calendarEventModel.setSubject(denumireEveniment);
		calendarEventModel.setCalendarId(new Long(19035));
		return calendarEventModel;
	}
	
	public DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	public CalendarService getCalendarService() {
		return SpringUtils.getBean("calendarService");
	}
}
