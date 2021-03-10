package ro.cloudSoft.cloudDoc.services.bpm.custom.referatSuplimentareBuget;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentReferatSuplimentareBugetConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.StringUtils2;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class ReferatSuplimentareBugetUpdateNumarDeplasariBugetateSuplimentare extends AutomaticTask {
	
	private static final String METADATA_TIP_SUPLIMENTARE_DEPLASARE_LIST_ITEM_DEPLASARE_SUPLIMENTATA_VALUE = "deplasare_suplimentata";
	private static final String METADATA_TIP_SUPLIMENTARE_DEPLASARE_LIST_ITEM_DEPLASARE_RELOCATA_VALUE = "deplasare_relocata";
	private static final int VALOARE_DECREMENTARE_DEPLASARI_BUGETATE_RAMASE = -1;
	private static final int VALOARE_INCREMENTARE_DEPLASARI_BUGETATE_RAMASE = 1;
	
	
	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		
		String referatSuplimentareBugetDocumentTypeName = getDocumentReferatSuplimentareBugetConstants().getDocumentTypeName();
		DocumentType referatSuplimentareBugetDocumentType = getDocumentTypeDao().getDocumentTypeByName(referatSuplimentareBugetDocumentTypeName);
		
		Document document;
		
		try {
			document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		} catch (AppException e) {
			throw new AutomaticTaskExecutionException(e.getMessage(), e);
		}
		
		Long numeOrganismNomenclatorValueId = DocumentUtils.getMetadataNomenclatorValue(document, referatSuplimentareBugetDocumentType, 
				getDocumentReferatSuplimentareBugetConstants().getNumeOrganismMetadataName());
		
		String tipSuplimentareDeplasare = DocumentUtils.getMetadataValueAsString(document, referatSuplimentareBugetDocumentType,
				getDocumentReferatSuplimentareBugetConstants().getTipSuplimentareDeplasareMetadataName());
		
		if (tipSuplimentareDeplasare != null && tipSuplimentareDeplasare.equals(METADATA_TIP_SUPLIMENTARE_DEPLASARE_LIST_ITEM_DEPLASARE_SUPLIMENTATA_VALUE)) {
			NomenclatorValue nomenclatorValue = findDetaliiNumarDeplasariBugetateOrganismeCurrentNomenclatorValue(numeOrganismNomenclatorValueId);
			 
			Long numarDeplasariBugetate = NomenclatorValueUtils.getAttributeValueAsLong(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_SUPLIMENTATE);
			numarDeplasariBugetate++;
			  
			NomenclatorValueUtils.setAttributeValue(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_SUPLIMENTATE, 
					numarDeplasariBugetate.toString());
			
			actualizareDeplasariRamase(nomenclatorValue, VALOARE_INCREMENTARE_DEPLASARI_BUGETATE_RAMASE);
			
			getNomenclatorValueDao().save(nomenclatorValue);
		
		} else if (tipSuplimentareDeplasare != null && tipSuplimentareDeplasare.equals(METADATA_TIP_SUPLIMENTARE_DEPLASARE_LIST_ITEM_DEPLASARE_RELOCATA_VALUE)) {
			Long organismDeplasareRelocataNomenclatorValueId = DocumentUtils.getMetadataNomenclatorValue(document, referatSuplimentareBugetDocumentType, 
					getDocumentReferatSuplimentareBugetConstants().getOrganismDeplasareRelocata());
			
			NomenclatorValue detaliiOrganismNomenclatorValue = findDetaliiNumarDeplasariBugetateOrganismeCurrentNomenclatorValue(numeOrganismNomenclatorValueId);
			NomenclatorValue detaliiOrganismDeplasareRelocataNomenclatorValue = findDetaliiNumarDeplasariBugetateOrganismeCurrentNomenclatorValue(organismDeplasareRelocataNomenclatorValueId);
			
			Long numarDeplasariBugetatePrinRelocare = NomenclatorValueUtils.getAttributeValueAsLong(detaliiOrganismNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_PRIMITE_PRIN_RELOCARE);
			numarDeplasariBugetatePrinRelocare++;
			NomenclatorValueUtils.setAttributeValue(detaliiOrganismNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_PRIMITE_PRIN_RELOCARE, 
					numarDeplasariBugetatePrinRelocare.toString());
			
			actualizareDeplasariRamase(detaliiOrganismNomenclatorValue, VALOARE_INCREMENTARE_DEPLASARI_BUGETATE_RAMASE);
			
			String organismNumarDeplasariBugetate = NomenclatorValueUtils.getAttributeValueAsString(detaliiOrganismNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM_NUMAR_DEPLASARI_RELOCATE);
			
			String denumireOrganismDeplasariRelocate = getNumeOrganism(detaliiOrganismDeplasareRelocataNomenclatorValue);
			organismNumarDeplasariBugetate = StringUtils2.appendToStringWithSeparator(organismNumarDeplasariBugetate, denumireOrganismDeplasariRelocate, ", ");
			
			NomenclatorValueUtils.setAttributeValue(detaliiOrganismNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM_NUMAR_DEPLASARI_RELOCATE, organismNumarDeplasariBugetate);
		
			Long numarDeplasariBugetateRelocate = NomenclatorValueUtils.getAttributeValueAsLong(detaliiOrganismDeplasareRelocataNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RELOCATE);
			numarDeplasariBugetateRelocate++;
			NomenclatorValueUtils.setAttributeValue(detaliiOrganismDeplasareRelocataNomenclatorValue, 
					NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RELOCATE, numarDeplasariBugetateRelocate.toString());
			
			actualizareDeplasariRamase(detaliiOrganismDeplasareRelocataNomenclatorValue, VALOARE_DECREMENTARE_DEPLASARI_BUGETATE_RAMASE);
						
			getNomenclatorValueDao().save(detaliiOrganismNomenclatorValue);
			getNomenclatorValueDao().save(detaliiOrganismDeplasareRelocataNomenclatorValue);
		}
	}
	
	private void actualizareDeplasariRamase(NomenclatorValue nomenclatorValue, int valoareIncrementareOrDecrementare) {
		Long numarDeplasariBugetateRamase = NomenclatorValueUtils.getAttributeValueAsLong(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE);
		numarDeplasariBugetateRamase += valoareIncrementareOrDecrementare;
		NomenclatorValueUtils.setAttributeValue(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE, numarDeplasariBugetateRamase.toString());
		
	}
	
	private String getNumeOrganism(NomenclatorValue detaliiOrganismDeplasareRelocataNomenclatorValue) {
		Long organismDeplasariRelocateNomenclatorValueId = NomenclatorValueUtils.getAttributeValueAsLong(detaliiOrganismDeplasareRelocataNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM);
		NomenclatorValue organismDeplasariRelocateNomenclatorValue = getNomenclatorValueDao().find(organismDeplasariRelocateNomenclatorValueId);
		String denumireOrganismDeplasariRelocate = NomenclatorValueUtils.getAttributeValueAsString(organismDeplasariRelocateNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE);
		return denumireOrganismDeplasariRelocate;
	}
	
	private NomenclatorValue findDetaliiNumarDeplasariBugetateOrganismeCurrentNomenclatorValue(Long organismNomenclatorValueId) throws AutomaticTaskExecutionException {
		Nomenclator nomenclatorDetaliiNumarDeplasariBugetateOrganisme = NomenclatorDao().findByCode(NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME);
		List<NomenclatorValue> values = getNomenclatorValueDao().findByNomenclatorId(nomenclatorDetaliiNumarDeplasariBugetateOrganisme.getId());
		
		for (NomenclatorValue nomenclatorValue : values) {
			Date deLaData = NomenclatorValueUtils.getAttributeValueAsDate(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_DE_LA_DATA);
			Date panaLaData = NomenclatorValueUtils.getAttributeValueAsDate(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_PANA_LA_DATA);
			Long currentNomenclatorValueOrganismNomenclatorValueId = NomenclatorValueUtils.getAttributeValueAsLong(nomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM);
			
			if (deLaData.before(new Date()) && panaLaData.after(new Date()) && 
					organismNomenclatorValueId.equals(currentNomenclatorValueOrganismNomenclatorValueId)) {
				return nomenclatorValue;
			}
		}
		throw new AutomaticTaskExecutionException("No value was found, for current year, for organism with nomenclator [" + NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME + "]");
	}
	
	private DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	private DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
	
	public NomenclatorDao NomenclatorDao() {
		return SpringUtils.getBean("nomenclatorDao");
	}
	
	private DocumentReferatSuplimentareBugetConstants getDocumentReferatSuplimentareBugetConstants() {
		return SpringUtils.getBean("documentReferatSuplimentareBugetConstants");
	}
}
