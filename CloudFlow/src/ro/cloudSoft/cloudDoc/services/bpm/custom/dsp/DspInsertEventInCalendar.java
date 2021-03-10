package ro.cloudSoft.cloudDoc.services.bpm.custom.dsp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.TaskConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDspConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.MeetingCalendarEventModel;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DspInsertEventInCalendar extends AutomaticTask {

	private static final String PREFIX_EVENT_DESCRIPTION = "Participare la";
	
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String dspDocumentTypeName = getDocumentDspConstants().getDocumentTypeName();
		DocumentType dspDocumentType = getDocumentTypeDao().getDocumentTypeByName(dspDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		String activitatiMetadataName = getDocumentDspConstants().getActivitatiProiectMetadataName();
		List<CollectionInstance> activitatiMetadataCollectionInstance = DocumentUtils.getMetadataCollectionInstance(document, dspDocumentType, activitatiMetadataName);
		MetadataDefinition metadataDefinitionParticipareLa = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getParticipareLaActivitateOfActivitatiMetadataName());
		MetadataDefinition metadataDefinitionExplicatii = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getExplicatiiActivitateOfActivitatiMetadataName());
		MetadataDefinition metadataDefinitionStartEveniment = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getDataInceputEvenimentOfActivitatiMetadataName());
		MetadataDefinition metadataDefinitionEndEveniment = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getDataSfarsitEvenimentOfActivitatiMetadataName());
		MetadataDefinition metadataDefinitionDescriereActivitate = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getDescriereActivitateOfActivitatiMetadataName());
		MetadataDefinition metadataDefinitionUserAsignat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(dspDocumentType,
				activitatiMetadataName, getDocumentDspConstants().getUserAsignatActivitateOfActivitatiMetadataName());
		
		for (CollectionInstance activitateCollectionInstance : activitatiMetadataCollectionInstance) {
			String participareLa = null;
			String explicatii = null;
			Date startDate = null;
			Date endDate = null;
			String descriere = null;
			Long userAsignatId = null;
			
			for ( MetadataInstance activitateMetadataInstance : activitateCollectionInstance.getMetadataInstanceList()) {
				if (activitateMetadataInstance.getValue() == null) {
					continue;
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionParticipareLa.getId())) {
					participareLa = activitateMetadataInstance.getValue();
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionExplicatii.getId())) {
					explicatii = activitateMetadataInstance.getValue();
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionStartEveniment.getId())) {
					startDate = DateUtils.parseDate(activitateMetadataInstance.getValue(), FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionEndEveniment.getId())) {
					endDate = DateUtils.parseDate(activitateMetadataInstance.getValue(), FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionDescriereActivitate.getId())) {
					descriere = activitateMetadataInstance.getValue();
				}
				if (activitateMetadataInstance.getMetadataDefinitionId().equals(metadataDefinitionUserAsignat.getId())) {
					userAsignatId = Long.parseLong(activitateMetadataInstance.getValue());
				}
			}
			
			// Nu se marcheaza in calendar.
			if (StringUtils.isEmpty(participareLa) 
					|| participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_COMUNICAT_DE_PRESA)
					|| participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_GRUPURI_DE_LUCRU)
					|| participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_CONSILIUL_DIRECTOR)
					|| participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_SISTEMICA)
					|| participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_ALTELE)) {
				continue;
			}
			
			String denumireEveniment = participareLa;
			if (!participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_BNR) && !participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_CU_CONDUCEREA_BNR)) {
				if (StringUtils.isNotEmpty(explicatii)) {
					denumireEveniment +=  " - " + explicatii;
				}
			}
			Long calendarId;
			if (participareLa.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTE_PARLAMENT)) {
				calendarId = DocumentUtils.getMetadataCalendarValue(document, dspDocumentType, getDocumentDspConstants().getCalendarSedinteParlamentMetadataName());
			} else {
				calendarId = DocumentUtils.getMetadataCalendarValue(document, dspDocumentType, getDocumentDspConstants().getCalendarEvenimenteIntalniriMetadataName());
			}
			if (startDate == null || endDate == null || calendarId == null) {
				throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
			}
			Calendar calendar = getCalendarDao().find(calendarId);
			MeetingCalendarEventModel meetingCalendarEventModel = prepareMeetingCalendarEvent(startDate, endDate, calendar, denumireEveniment, descriere);
			meetingCalendarEventModel.setDocumentId(document.getId());
			meetingCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
			meetingCalendarEventModel.setLocation(participareLa);
			if (userAsignatId != null) {
				List<OrganizationEntityModel> attendees = new ArrayList<OrganizationEntityModel>();
				OrganizationEntityModel userAsignat = new OrganizationEntityModel();
				userAsignat.setId(userAsignatId);
				attendees.add(userAsignat);
				meetingCalendarEventModel.setAttendees(attendees );
			}
			try {
				getCalendarService().saveCalendarEvent(meetingCalendarEventModel, getUserSecurity(), false);
			} catch (AppException e) {
				throw new AutomaticTaskExecutionException(e.getMessage());
			}
			
		}
	}

	private MeetingCalendarEventModel prepareMeetingCalendarEvent(Date startDate, Date endDate, Calendar calendar, String denumireEveniment, String descriere) {
		MeetingCalendarEventModel calendarEventModel = new MeetingCalendarEventModel();
		
		calendarEventModel.setStartDate(startDate);
		calendarEventModel.setEndDate(endDate);
		
		String denumireEvenimentWithPrefix = PREFIX_EVENT_DESCRIPTION + " " + denumireEveniment;
		calendarEventModel.setSubject(denumireEvenimentWithPrefix);
		if (StringUtils.isNotEmpty(descriere)) {
			calendarEventModel.setDescription(descriere);
		}
		
		calendarEventModel.setCalendarId(calendar.getId());
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
	
	public CalendarService getCalendarService() {
		return SpringUtils.getBean("calendarService");
	}
	
	public DocumentDspConstants getDocumentDspConstants() {
		return SpringUtils.getBean("documentDspConstants");
	}
}
