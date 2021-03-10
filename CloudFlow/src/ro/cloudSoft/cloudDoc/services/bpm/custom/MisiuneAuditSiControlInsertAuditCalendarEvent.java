package ro.cloudSoft.cloudDoc.services.bpm.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentMisiuneAuditSiControlConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.AuditCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class MisiuneAuditSiControlInsertAuditCalendarEvent extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String misiuneAuditSiControlDocumentTypeName = getDocumentMisiuneAuditSiControlConstants().getDocumentTypeName();
				
		DocumentType misiuneAuditSiControlDocumentType = getDocumentTypeDao().getDocumentTypeByName(misiuneAuditSiControlDocumentTypeName);
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date startDate = DocumentUtils.getMetadataDateValue(document, misiuneAuditSiControlDocumentType, getDocumentMisiuneAuditSiControlConstants().getDataInceputMetadataName());
		Date endDate = DocumentUtils.getMetadataDateValue(document, misiuneAuditSiControlDocumentType, getDocumentMisiuneAuditSiControlConstants().getDataSfarsitMetadataName());
		Long institutiiNomenclaorId = DocumentUtils.getMetadataNomenclatorValue(document, misiuneAuditSiControlDocumentType, getDocumentMisiuneAuditSiControlConstants().getLocatieMetadataName());
		Long calendarId = DocumentUtils.getMetadataValueAsLong(document, misiuneAuditSiControlDocumentType, getDocumentMisiuneAuditSiControlConstants().getCalendarMetadataName());
		
		if (startDate == null || endDate == null || institutiiNomenclaorId == null || calendarId == null) {
			throw new RuntimeException("Datele pentru creeare evenimentului in calendar nu pot sa fie null.");
		}

		List<OrganizationEntity> participantiEveniment = getParticipantiEveniment(document);
		Calendar calendar = getCalendarDao().find(calendarId);
		
		String location = getLocationFromInstitutie(institutiiNomenclaorId);
		AuditCalendarEvent auditCalendarEventModel = prepareAuditCalendarEvent(startDate, endDate, location, calendar, new HashSet<>(participantiEveniment));
		auditCalendarEventModel.setDocumentId(document.getId());
		auditCalendarEventModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		
		getCalendarEventDao().saveEvent(auditCalendarEventModel);
	}
	
	private String getLocationFromInstitutie(Long institutiiNomenclaorId) {
		NomenclatorValue institutie = getNomenclatorValueDao().find(institutiiNomenclaorId);
		return NomenclatorValueUtils.getAttributeValueAsString(institutie, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE);
	}

	private List<OrganizationEntity> getParticipantiEveniment(Document document) {
		List<OrganizationEntity> participantiEveniment = new ArrayList<OrganizationEntity>();
		Map<Long, List<CollectionInstance>> documentCollectionMetadataInstanceList = document.getCollectionInstanceListMap();
		for (Entry<Long, List<CollectionInstance>> entry : documentCollectionMetadataInstanceList.entrySet()) {
			MetadataCollection metadataDefinition = getDocumentTypeDao().getMetadataCollectionDefinition(entry.getKey());
			if (metadataDefinition.getName().equals(getDocumentMisiuneAuditSiControlConstants().getEchipaArbMetadataName())) {
				List<CollectionInstance> collectionInstances = entry.getValue();
				for (CollectionInstance collectionInstance : collectionInstances) {
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
						MetadataDefinition collectionInstanceMetadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
						if (collectionInstanceMetadataDefinition.getName().equals(getDocumentMisiuneAuditSiControlConstants().getMembruEchipaArbMetadataName())) {
							Long participantUserId = MetadataValueHelper.getUserId(metadataInstance.getValue());
							participantiEveniment.add(getUserService().getUserById(participantUserId));
						}
					}
				}
			}
		}
		return participantiEveniment;
	}
	
	private AuditCalendarEvent prepareAuditCalendarEvent(Date startDate, Date endDate, String location, 
			Calendar calendar, Set<OrganizationEntity> attendees) {
		AuditCalendarEvent calendarEventModel = new AuditCalendarEvent();
		
		calendarEventModel.setStartDate(DateUtils.nullHourMinutesSeconds(startDate));
		calendarEventModel.setEndDate(DateUtils.maximizeHourMinutesSeconds(endDate));
		calendarEventModel.setLocation(location);
		
		String denumireEveniment = "Misiune la " + location;
		calendarEventModel.setSubject(denumireEveniment);
		
		calendarEventModel.setCalendar(calendar);
		calendarEventModel.setAttendees(attendees);
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
	
	public DocumentMisiuneAuditSiControlConstants getDocumentMisiuneAuditSiControlConstants() {
		return SpringUtils.getBean("documentMisiuneAuditSiControlConstants");
	}
	
	public NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
}
