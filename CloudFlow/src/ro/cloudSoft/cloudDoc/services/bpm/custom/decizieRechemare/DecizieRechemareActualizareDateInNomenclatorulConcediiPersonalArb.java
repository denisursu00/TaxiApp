package ro.cloudSoft.cloudDoc.services.bpm.custom.decizieRechemare;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDecizieRechemareConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.HolidayCalendarEvent;
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

public class DecizieRechemareActualizareDateInNomenclatorulConcediiPersonalArb extends AutomaticTask {
	
	@Override
	public void execute(WorkflowInstance workflowInstance)  throws AutomaticTaskExecutionException, AppException {
		executeUpdateEventInCalendar(workflowInstance);
		executeActualizareDateInNomenclatorConcediiPersonal(workflowInstance);
	}

	
	private void executeUpdateEventInCalendar(WorkflowInstance workflowInstance)  throws AutomaticTaskExecutionException, AppException {
		String cerereRechemareDocumentTypeName = getDocumentDecizieRechemareConstants().getDocumentTypeName();
		
		DocumentType cerereRechemareDocumentType = getDocumentTypeDao().getDocumentTypeByName(cerereRechemareDocumentTypeName);
		Document documentCerereRechemare;
		
		try {
			documentCerereRechemare = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date dataInceputConcediuAprobat = DocumentUtils.getMetadataDateValue(documentCerereRechemare, cerereRechemareDocumentType, getDocumentDecizieRechemareConstants().getDataInceputConcediuAprobatMetadataName());
		Date dataSfarsitConcediuAprobat = DocumentUtils.getMetadataDateValue(documentCerereRechemare, cerereRechemareDocumentType, getDocumentDecizieRechemareConstants().getDataSfarsitConcediuAprobatMetadataName());
		Date dataIntrairiInVigoareARechemarii = DocumentUtils.getMetadataDateValue(documentCerereRechemare, cerereRechemareDocumentType, getDocumentDecizieRechemareConstants().getDataIntraiiInVigoareARechemariiMetadataName());
		
		Long userRechematId = DocumentUtils.getMetadataUserValue(documentCerereRechemare, cerereRechemareDocumentType, getDocumentDecizieRechemareConstants().getUserRechematMetadataName());
		Long calendarId = DocumentUtils.getMetadataCalendarValue(documentCerereRechemare, cerereRechemareDocumentType, getDocumentDecizieRechemareConstants().getCalendarMetadataName());
		
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
		event.setDocumentId(documentCerereRechemare.getId());
		event.setDocumentLocationRealName(documentCerereRechemare.getDocumentLocationRealName());
		
		getCalendarEventDao().saveEvent(event);
	}
	
	
	private void executeActualizareDateInNomenclatorConcediiPersonal(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {
		
		String decizieConcediuDocumentTypeName = getDocumentDecizieRechemareConstants().getDocumentTypeName();
		
		DocumentType cerereConcediuDocumentType = getDocumentTypeDao().getDocumentTypeByName(decizieConcediuDocumentTypeName);
		Document documentCerereConcediu;
		
		try {
			documentCerereConcediu = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Date dataInceputConcediu = DocumentUtils.getMetadataDateValue(documentCerereConcediu, cerereConcediuDocumentType, getDocumentDecizieRechemareConstants().getDataInceputConcediuAprobatMetadataName());
		Date dataSfarsitConcediu = DocumentUtils.getMetadataDateValue(documentCerereConcediu, cerereConcediuDocumentType, getDocumentDecizieRechemareConstants().getDataSfarsitConcediuAprobatMetadataName());
		Date dataIntrariiInViguareARechemarii = DocumentUtils.getMetadataDateValue(documentCerereConcediu, cerereConcediuDocumentType, getDocumentDecizieRechemareConstants().getDataIntraiiInVigoareARechemariiMetadataName());
		
		Long userRechematId = DocumentUtils.getMetadataUserValue(documentCerereConcediu, cerereConcediuDocumentType, getDocumentDecizieRechemareConstants().getUserRechematMetadataName());
		NomenclatorValue personNomenclatorValue = getUserService().getNomenclatorPersonByUserId(userRechematId);
		
		updateNomenclatorValues(dataInceputConcediu, dataSfarsitConcediu, dataIntrariiInViguareARechemarii, personNomenclatorValue);
	}
	
	private void updateNomenclatorValues(Date dataInceputConcediu, Date dataSfarsitConcediu, Date dataIntrariiInViguareARechemarii, NomenclatorValue personNomenclatorValue) throws AutomaticTaskExecutionException {
		
		Integer year = DateUtils.getYear(dataIntrariiInViguareARechemarii);
		NomenclatorValue concediuNomenclatorValue = getConcediuPersonalArbNomenclatorValue(personNomenclatorValue, year);
		
		long numarZileDeConcediuRestituite = daysBetween(dataIntrariiInViguareARechemarii, dataSfarsitConcediu);
		
		Long numarZileConcediuNeefectuateAnAnterior = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_ANTERIOR);
		Long numarZileConcediuEfectuateAnAnterior = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_ANTERIOR);
		
		Long numarZileConcediuNeefectuateAnInCurs = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_IN_CURS);		
		Long numarZileConcediuEfectuateAnInCurs = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_IN_CURS);
	
		Long numarZileConcediuConformContract = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_CONFORM_CONTRACT);

		if (numarZileConcediuConformContract < numarZileConcediuNeefectuateAnInCurs + numarZileDeConcediuRestituite) {
			Long numarZileCeTrebuieRestituitePentruAnulCurent = numarZileConcediuConformContract - numarZileConcediuNeefectuateAnInCurs;
			
			numarZileConcediuEfectuateAnInCurs -= numarZileCeTrebuieRestituitePentruAnulCurent;
			numarZileConcediuNeefectuateAnInCurs += numarZileCeTrebuieRestituitePentruAnulCurent;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_IN_CURS, numarZileConcediuNeefectuateAnInCurs.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_IN_CURS, numarZileConcediuEfectuateAnInCurs.toString());
			
			numarZileConcediuEfectuateAnAnterior -=  numarZileDeConcediuRestituite - numarZileCeTrebuieRestituitePentruAnulCurent;
			numarZileConcediuNeefectuateAnAnterior += numarZileDeConcediuRestituite - numarZileCeTrebuieRestituitePentruAnulCurent;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_ANTERIOR, numarZileConcediuNeefectuateAnAnterior.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_ANTERIOR, numarZileConcediuEfectuateAnAnterior.toString());
		} else {
			numarZileConcediuEfectuateAnInCurs -= numarZileDeConcediuRestituite;
			numarZileConcediuNeefectuateAnInCurs += numarZileDeConcediuRestituite;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_IN_CURS, numarZileConcediuNeefectuateAnInCurs.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_IN_CURS, numarZileConcediuEfectuateAnInCurs.toString());
		}
		
		Long numarZileRechemare = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_NUMAR_ZILE_RECHEMARE);
		numarZileRechemare += numarZileDeConcediuRestituite;
		NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_NUMAR_ZILE_RECHEMARE, numarZileRechemare.toString());
		
		getNomenclatorValueDao().save(concediuNomenclatorValue);
	}
	
	private NomenclatorValue getConcediuPersonalArbNomenclatorValue(NomenclatorValue nomenclatorPerson, Integer year) throws AutomaticTaskExecutionException {

		String yearAsString = year.toString();
		
		List<NomenclatorValue> concediiPersoana = getNomenclatorValueDao().findByNomenclatorCodeAndAttribute(
				NomenclatorConstants.CONCEDII_PERSONAL_ARB,
				NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUME,
				nomenclatorPerson.getId().toString());

		NomenclatorValue concediu = null;
		for (NomenclatorValue nomenclatorValue : concediiPersoana) {
			String anConcediu = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_AN);
			if (anConcediu.equals(yearAsString)) {
				concediu = nomenclatorValue;
			}
		}
		
		if (concediu == null) {
			String numePersoana = getNumePersoana(nomenclatorPerson);
			throw new AutomaticTaskExecutionException("No record was found in nomenclator Concecdii Personal Arb for year [" + yearAsString + "] and for person with name [" + numePersoana + "]");
		}
		
		return concediu;
	}

	private String getNumePersoana(NomenclatorValue nomenclatorPerson) {
		String numePersoana = 
				NomenclatorValueUtils.getAttributeValueAsString(nomenclatorPerson, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME) + " " +
				NomenclatorValueUtils.getAttributeValueAsString(nomenclatorPerson, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
		return numePersoana;
	}
	
	private long daysBetween(Date dataInceputConcediu, Date dataSfarsitConcediu) {
		
		Calendar dataInceputConcediuAsCalendar = DateUtils.toCalendar(dataInceputConcediu);
		Calendar dataSfarsitConcediuAsCalendar = DateUtils.toCalendar(dataSfarsitConcediu);
		
		int numarZile = 0;
		while (DateUtils.isBeforeDate(dataInceputConcediuAsCalendar.getTime(), dataSfarsitConcediuAsCalendar.getTime())
				|| DateUtils.isSameDate(dataInceputConcediuAsCalendar.getTime(), dataSfarsitConcediuAsCalendar.getTime())) {
			
			if (DateUtils.isWeekend(dataInceputConcediuAsCalendar.getTime()) || isZiLiberaLegala(dataInceputConcediuAsCalendar.getTime())) {
				dataInceputConcediuAsCalendar.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}
			
			dataInceputConcediuAsCalendar.add(Calendar.DAY_OF_MONTH, 1);
			numarZile++;
		}
		return numarZile;
	}
	
	private boolean isZiLiberaLegala(Date date) {
		
		List<NomenclatorValue> zileLibereLegale = getNomenclatorValueDao().findByNomenclatorCode(NomenclatorConstants.ZILE_LIBERE_LEGALE);
		for (NomenclatorValue ziLiberaLegala : zileLibereLegale) {
			Long zi = NomenclatorValueUtils.getAttributeValueAsLong(ziLiberaLegala, NomenclatorConstants.ZILE_LIBERE_LEGALE_ATTRIBUTE_KEY_ZI);
			Long luna = NomenclatorValueUtils.getAttributeValueAsLong(ziLiberaLegala, NomenclatorConstants.ZILE_LIBERE_LEGALE_ATTRIBUTE_KEY_LUNA);
			
			if (DateUtils.isSameMonthAndDay(date, luna, zi)) {
				return true;
			}
		}
		return false;
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
	
	public DocumentDecizieRechemareConstants getDocumentDecizieRechemareConstants() {
		return SpringUtils.getBean("documentDecizieRechemareConstants");
	}
	
	public NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
}
