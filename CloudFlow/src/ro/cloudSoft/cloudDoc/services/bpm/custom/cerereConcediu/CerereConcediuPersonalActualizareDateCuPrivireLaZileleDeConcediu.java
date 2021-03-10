package ro.cloudSoft.cloudDoc.services.bpm.custom.cerereConcediu;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentCerereConcediuConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarEventDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
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
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class CerereConcediuPersonalActualizareDateCuPrivireLaZileleDeConcediu extends AutomaticTask {
	
	private static final String TIP_CONCEDIU_FARA_PLATA = "concediu_fara_plata";
	private static final String TIP_CONCEDIU_MEDICAL = "concediu_medical";
	private static final String TIP_CONCEDIU_SITUATII_SPECIALE = "concediu_situatii_speciale";
	private static final String TIP_CONCEDIU_ODIHNA = "concediu_odihna";
	
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
		String tipConcediu = DocumentUtils.getMetadataValueAsString(document, cerereConcediuDocumentType, getDocumentCerereConcediuConstants().getTipConcediuPersonalMetadataName());

		NomenclatorValue nomenclatorPerson = getUserService().getNomenclatorPersonByUserId(beneficiarUserId);
		if (tipConcediu.equals(TIP_CONCEDIU_FARA_PLATA)) {
			modifyNomenclatorValueForTipConcediuFaraPlata(nomenclatorPerson, startDate, endDate);
		} else if (tipConcediu.equals(TIP_CONCEDIU_MEDICAL)) {
			modifyNomenclatorValueForTipConcediuMedical(nomenclatorPerson, startDate, endDate);
		} else if (tipConcediu.equals(TIP_CONCEDIU_SITUATII_SPECIALE)) {
			modifyNomenclatorValueForTipConcediuSituatiiSpeciale(nomenclatorPerson, startDate, endDate);
		} else if (tipConcediu.equals(TIP_CONCEDIU_ODIHNA)) {
			modifyNomenclatorValueForTipConcediuOdihna(nomenclatorPerson, startDate, endDate);
		} else {
			throw new AutomaticTaskExecutionException("Tip concediu [" + tipConcediu + "] necunoscut.");
		}
	}
	
	private void modifyNomenclatorValueForTipConcediuFaraPlata(NomenclatorValue nomenclatorPersonValue, Date startDate, Date endDate) throws AutomaticTaskExecutionException {
		
		Integer year = DateUtils.getYear(startDate);
		NomenclatorValue concediuNomenclatorValue = getConcediuPersonalArbNomenclatorValue(nomenclatorPersonValue, year);
		long numarZileDeConcediu = daysBetween(startDate, endDate);
		
		Long numarZileConcediuFaraPlata = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_FARA_PLATA);
		numarZileConcediuFaraPlata += numarZileDeConcediu;
		NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_FARA_PLATA, numarZileConcediuFaraPlata.toString());
	
		getNomenclatorValueDao().save(concediuNomenclatorValue);
	}

	private void modifyNomenclatorValueForTipConcediuMedical(NomenclatorValue nomenclatorPersonValue, Date startDate, Date endDate) throws AutomaticTaskExecutionException {
		Integer year = DateUtils.getYear(startDate);
		NomenclatorValue concediuNomenclatorValue = getConcediuPersonalArbNomenclatorValue(nomenclatorPersonValue, year);
		long numarZileDeConcediu = daysBetween(startDate, endDate);
		
		Long numarZileConcediuFaraPlata = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_MEDICAL);
		numarZileConcediuFaraPlata += numarZileDeConcediu;
		NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_MEDICAL, numarZileConcediuFaraPlata.toString());
	
		getNomenclatorValueDao().save(concediuNomenclatorValue);
	}
	
	private void modifyNomenclatorValueForTipConcediuSituatiiSpeciale(NomenclatorValue nomenclatorPersonValue, Date startDate, Date endDate) throws AutomaticTaskExecutionException {
		Integer year = DateUtils.getYear(startDate);
		NomenclatorValue concediuNomenclatorValue = getConcediuPersonalArbNomenclatorValue(nomenclatorPersonValue, year);
		long numarZileDeConcediu = daysBetween(startDate, endDate);
		
		Long numarZileConcediuFaraPlata = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_SITUATII_SPECIALE);
		numarZileConcediuFaraPlata += numarZileDeConcediu;
		NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_SITUATII_SPECIALE, numarZileConcediuFaraPlata.toString());
	
		getNomenclatorValueDao().save(concediuNomenclatorValue);
	}
	
	private void modifyNomenclatorValueForTipConcediuOdihna(NomenclatorValue nomenclatorPersonValue, Date startDate, Date endDate) throws AutomaticTaskExecutionException {
		
		Integer year = DateUtils.getYear(startDate);
		NomenclatorValue concediuNomenclatorValue = getConcediuPersonalArbNomenclatorValue(nomenclatorPersonValue, year);
		long numarZileDeConcediu = daysBetween(startDate, endDate);
		
		Long numarZileConcediuNeefectuateAnAnterior = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_ANTERIOR);
		Long numarZileConcediuEfectuateAnAnterior = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_ANTERIOR);
		
		Long numarZileConcediuNeefectuateAnInCurs = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_IN_CURS);		
		Long numarZileConcediuEfectuateAnInCurs = NomenclatorValueUtils.getAttributeValueAsLong(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_IN_CURS);
		
		String numePersoana = getNumePersoana(nomenclatorPersonValue);
		
		if (numarZileConcediuNeefectuateAnAnterior + numarZileConcediuNeefectuateAnInCurs < numarZileDeConcediu) {
			throw new AutomaticTaskExecutionException("Numarul de zile de concediu ramase utilizatorului [" + numePersoana + "] sunt mai putine decat cele cerute de catre acesta.");
		}
		
		if (numarZileConcediuNeefectuateAnAnterior - numarZileDeConcediu >= 0) {
			numarZileConcediuNeefectuateAnAnterior -= numarZileDeConcediu;
			numarZileConcediuEfectuateAnAnterior += numarZileDeConcediu;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_ANTERIOR, numarZileConcediuNeefectuateAnAnterior.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_ANTERIOR, numarZileConcediuEfectuateAnAnterior.toString());
		} else {
			numarZileConcediuNeefectuateAnInCurs -= numarZileDeConcediu - numarZileConcediuNeefectuateAnAnterior;
			numarZileConcediuEfectuateAnInCurs += numarZileDeConcediu - numarZileConcediuNeefectuateAnAnterior;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_IN_CURS, numarZileConcediuNeefectuateAnInCurs.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_IN_CURS, numarZileConcediuEfectuateAnInCurs.toString());
			
			numarZileConcediuEfectuateAnAnterior += numarZileConcediuNeefectuateAnAnterior;
			numarZileConcediuNeefectuateAnAnterior = 0L;
			
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_NEEFECTUATE_AN_ANTERIOR, numarZileConcediuNeefectuateAnAnterior.toString());
			NomenclatorValueUtils.setAttributeValue(concediuNomenclatorValue, NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUMAR_ZILE_CONCEDIU_ODIHNA_EFECTUATE_AN_ANTERIOR, numarZileConcediuEfectuateAnAnterior.toString());
		}
		
		getNomenclatorValueDao().save(concediuNomenclatorValue);
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
	
	public DocumentCerereConcediuConstants getDocumentCerereConcediuConstants() {
		return SpringUtils.getBean("documentCerereConcediuConstants");
	}
}
