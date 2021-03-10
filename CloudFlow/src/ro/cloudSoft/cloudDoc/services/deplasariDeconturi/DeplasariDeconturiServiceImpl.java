package ro.cloudSoft.cloudDoc.services.deplasariDeconturi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.CheltuieliArbDao;
import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.CheltuieliReprezentantArbDao;
import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.DeplasariDeconturiDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.DocumentDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportDateFilterModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.deplasariDeconturi.DeplasariDeconturiConverter;
import ro.cloudSoft.cloudDoc.services.arb.DecizieDeplasareService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class DeplasariDeconturiServiceImpl implements DeplasariDeconturiService {

	private DeplasariDeconturiConverter deplasariDeconturiConverter;
	private DeplasariDeconturiDao deplasariDeconturiDao;
	private CheltuieliArbDao cheltuieliArbDao;
	private CheltuieliReprezentantArbDao cheltuieliReprezentantArbDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorDao nomenclatorDao;
	private DecizieDeplasareService decizieDeplasareService;
	private DocumentService documentService;
	private DocumentTypeService documentTypeService;
	private ArbConstants arbConstants;
	
	@Override
	@Transactional
	public void saveDeplasareDecont(DeplasareDecontModel deplasareDecontModel) {
		
		DeplasareDecont deplasareDecont = deplasariDeconturiConverter.getEntityFromModel(deplasareDecontModel);

		if (deplasareDecont.getNumarInregistrare() == null) { //inseamna ca ii deplasare decont nou
						
			int year = DateUtils.getCurrentYear();
			NomenclatorValue deplasariDeconturiCuUltimulNumarInregistrare = generateDeplasariDeconturiNumarInregistrareByYear(year);
			
			deplasareDecont.setNumarInregistrare(NomenclatorValueUtils.getAttributeValueAsString(deplasariDeconturiCuUltimulNumarInregistrare, NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_ATTRIBUTE_KEY_NR_CRT) + "/" + year);
		

			int currentYear = DateUtils.getCurrentYear();
			Nomenclator detaliiNumarDeplasariBugetateNomenclator = nomenclatorDao.findByCode(NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_NOMENCLATOR_CODE);
			NomenclatorValue detaliiNumarDeplasariBugetateNomenclatorValue = nomenclatorValueDao.getDetaliiNumarDeplasariBugetateNomenclatorValueByOrganismIdAndCurrentYear(detaliiNumarDeplasariBugetateNomenclator.getId(), String.valueOf(deplasareDecont.getOrganismId()), String.valueOf(currentYear));
			
			String numarDeplasariEfectuate = NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_EFECTUATE);
			String numarDeplasariBugetateRamase = NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE);
			
			int numarDeplasariEfectuateNew = Integer.valueOf(numarDeplasariEfectuate) + 1; 
			int numarDeplasariBugetateRamaseNew = Integer.valueOf(numarDeplasariBugetateRamase) - 1; 
	
			NomenclatorValueUtils.setAttributeValue(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_EFECTUATE, String.valueOf(numarDeplasariEfectuateNew));
			NomenclatorValueUtils.setAttributeValue(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE, String.valueOf(numarDeplasariBugetateRamaseNew));
			
			nomenclatorValueDao.save(detaliiNumarDeplasariBugetateNomenclatorValue);
		
		}
		
		removeCheltuieliArbOrphans(deplasareDecont);
		removeCheltuieliReprezentantArbOrphans(deplasareDecont);
		
		deplasariDeconturiDao.saveDeplasareDecont(deplasareDecont);
	}
	
	private void removeCheltuieliArbOrphans(DeplasareDecont deplasareDecont) {
		List<Long> cheltuieliArbIds = new ArrayList<>();
		if (deplasareDecont.getId() != null) {
			List<CheltuialaArb> cheltuieliArb = deplasariDeconturiDao.getCheltuieliArbByDeplsareDecontId(deplasareDecont.getId());
			for (CheltuialaArb cheltuielaArb : cheltuieliArb) {
				cheltuieliArbIds.add(cheltuielaArb.getId());
			}
		}
		
		List<Long> cheltuieliArbToRemoveIds = new ArrayList<Long>();

		if (CollectionUtils.isNotEmpty(cheltuieliArbIds)) {
			for (Long presistedCheltuieliArbId : cheltuieliArbIds) {
				boolean found = false;
				for (CheltuialaArb newCheltuialaArb : deplasareDecont.getCheltuieliArb()) {
					if (newCheltuialaArb.getId() != null && newCheltuialaArb.getId().equals(presistedCheltuieliArbId)) {
						found = true;
					}
				}
				if (!found) {
					cheltuieliArbToRemoveIds.add(presistedCheltuieliArbId);
				}
			}
			if (CollectionUtils.isNotEmpty(cheltuieliArbToRemoveIds)) {
				deplasariDeconturiDao.removeCheltuielaArb(cheltuieliArbToRemoveIds);
			}			
		}
	}
	
	private void removeCheltuieliReprezentantArbOrphans(DeplasareDecont deplasareDecont) {
		List<Long> cheltuieliReprezentantArbIds = new ArrayList<>();
		if (deplasareDecont.getId() != null) {
			List<CheltuialaReprezentantArb> cheltuieliReprezentantArb = deplasariDeconturiDao.getCheltuieliReprezentantArbByDeplsareDecontId(deplasareDecont.getId());
			for (CheltuialaReprezentantArb cheltuielaReprezentantArb : cheltuieliReprezentantArb) {
				cheltuieliReprezentantArbIds.add(cheltuielaReprezentantArb.getId());
			}
		}
		
		List<Long> cheltuieliReprezentantArbToRemoveIds = new ArrayList<Long>();

		if (CollectionUtils.isNotEmpty(cheltuieliReprezentantArbIds)) {
			for (Long presistedCheltuieliReprezentantArbId : cheltuieliReprezentantArbIds) {
				boolean found = false;
				for (CheltuialaReprezentantArb newCheltuialaReprezentantArb : deplasareDecont.getCheltuieliReprezentantArb()) {
					if (newCheltuialaReprezentantArb.getId() != null && newCheltuialaReprezentantArb.getId().equals(presistedCheltuieliReprezentantArbId)) {
						found = true;
					}
				}
				if (!found) {
					cheltuieliReprezentantArbToRemoveIds.add(presistedCheltuieliReprezentantArbId);
				}
			}
			if (CollectionUtils.isNotEmpty(cheltuieliReprezentantArbToRemoveIds)) {
				deplasariDeconturiDao.removeCheltuielaReprezentantArb(cheltuieliReprezentantArbToRemoveIds);
			}			
		}
	}
	
	private NomenclatorValue generateDeplasariDeconturiNumarInregistrareByYear(int year) {

		Long numarInregistrareNou = null;

		Nomenclator deplasariDeconturiGenerareNumarInregistrareNomenclator = getDeplasariDeconturiGenerareNumarInregistrareNomenclator();
		NomenclatorValue deplasariDeconturiCuUltimulNumarInregistrare = getDeplasariDeconturiCuUltimulNumarInregistrareByNomenclatorAndCurrentYear(
				deplasariDeconturiGenerareNumarInregistrareNomenclator.getId(), String.valueOf(year));
		
		if (deplasariDeconturiCuUltimulNumarInregistrare != null) {
			String numarInregistrare = NomenclatorValueUtils.getAttributeValueAsString(deplasariDeconturiCuUltimulNumarInregistrare, NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_ATTRIBUTE_KEY_NR_CRT);
			numarInregistrareNou = Long.valueOf(numarInregistrare) + 1;
			NomenclatorValueUtils.setAttributeValue(deplasariDeconturiCuUltimulNumarInregistrare, NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_ATTRIBUTE_KEY_NR_CRT, String.valueOf(numarInregistrareNou));
		} else {
			numarInregistrareNou = 1L;
			
			deplasariDeconturiCuUltimulNumarInregistrare = new NomenclatorValue();
			deplasariDeconturiCuUltimulNumarInregistrare.setNomenclator(deplasariDeconturiGenerareNumarInregistrareNomenclator);
			NomenclatorValueUtils.setAttributeValue(deplasariDeconturiCuUltimulNumarInregistrare, NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_ATTRIBUTE_KEY_NR_CRT, String.valueOf(numarInregistrareNou));
			NomenclatorValueUtils.setAttributeValue(deplasariDeconturiCuUltimulNumarInregistrare, NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_ATTRIBUTE_KEY_AN, String.valueOf(year));
		}
		
		nomenclatorValueDao.save(deplasariDeconturiCuUltimulNumarInregistrare);
		
		return deplasariDeconturiCuUltimulNumarInregistrare;
	}
	
	private Nomenclator getDeplasariDeconturiGenerareNumarInregistrareNomenclator() {
		return nomenclatorDao.findByCode(NomenclatorConstants.DEPLASARI_DECONTURI_GENERARE_NR_CRT_NOMENCLATOR_CODE);
	}
	
	private NomenclatorValue getDeplasariDeconturiCuUltimulNumarInregistrareByNomenclatorAndCurrentYear(Long nomenclatorId, String year) {
		return nomenclatorValueDao.getNomenclatorValueCuUltimulNumarInregistrareByNomenclatorIdAndCurrentYear(nomenclatorId, year);
	}

	@Override
	public List<NumarDecizieDeplasareModel> getDeciziiAprobateNealocateForReprezentantForDeplasareDecont(Long reprezentantArbId, Long deplasareDecontId, SecurityManager userSecurity) {
		
		List<NumarDecizieDeplasareModel> nrDecizieDeplasareList =  decizieDeplasareService.getNumarDeciziiDeplasariAprobateByReprezentant(reprezentantArbId, userSecurity);
		
		List<String> listNrDeciziiAlocate = deplasariDeconturiDao.getListaNrDeciziiAlocateByReprezentantId(reprezentantArbId);
		
		if (deplasareDecontId != null) {
			DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
			if (deplasareDecont != null) {
				String nrDecizieAlocataDeplasareDecontCurent = deplasareDecont.getNumarDecizie();
				for (Iterator<String> iterator = listNrDeciziiAlocate.iterator(); iterator.hasNext();) {
					String nrDecizieAlocata = iterator.next();
					if (nrDecizieAlocata.equals(nrDecizieAlocataDeplasareDecontCurent)) {
						iterator.remove();
					}
				}
			}
		}
		
		// filtrare deciziile deja alocate pe reprezentantArb
		for (Iterator<NumarDecizieDeplasareModel> iterator = nrDecizieDeplasareList.iterator(); iterator.hasNext();) {
			NumarDecizieDeplasareModel nrDecizieDeplasare = iterator.next();
			if (listNrDeciziiAlocate.contains(nrDecizieDeplasare.getNumarDecizie())) {
				iterator.remove();
			}
		}

		
				
		return nrDecizieDeplasareList;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public DocumentDecizieDeplasareModel getDocumentDecizieDeplasare(String documentId,	String documentLocationRealName, 
			SecurityManager userSecurity) throws PresentationException, AppException, ParseException {
		
		DocumentDecizieDeplasareModel documentDecizieDeplasareModel = new DocumentDecizieDeplasareModel();
		
		Document document = documentService.getDocumentById(documentId, documentLocationRealName, userSecurity);
		List<MetadataInstance> metadataInstances = document.getMetadataInstanceList();
		
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId(), userSecurity);
		List<MetadataDefinition> metadataDefinitions = (List<MetadataDefinition>) documentType.getMetadataDefinitions();
		
		String institutieId = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getInstitutieMetadataName());
		NomenclatorValue institutieNomenclatorValue = nomenclatorValueDao.find(Long.valueOf(institutieId));
		
		Date dataDecizie = DocumentUtils.getMetadataValueAsDate(document, documentType, arbConstants.getDocumentDecizieDeplasare().getDataDecizieMetadataName());
		
		String organismId = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getOrganismMetadataName());
		NomenclatorValue organismNomenclatorValue = nomenclatorValueDao.find(Long.valueOf(organismId));
		
		NomenclatorValue comitetNomenclatorValue = null;
		String comitetId = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getComitetMetadataName());
		if (comitetId != null) {
			comitetNomenclatorValue = nomenclatorValueDao.find(Long.valueOf(comitetId));
		}
		
		int currentYear = DateUtils.getCurrentYear();
		Nomenclator detaliiNumarDeplasariBugetateNomenclator = nomenclatorDao.findByCode(NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_NOMENCLATOR_CODE);
		NomenclatorValue detaliiNumarDeplasariBugetateNomenclatorValue = nomenclatorValueDao.getDetaliiNumarDeplasariBugetateNomenclatorValueByOrganismIdAndCurrentYear(detaliiNumarDeplasariBugetateNomenclator.getId(), organismId, String.valueOf(currentYear));
		
		String numarDeplasariEfectuate = NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_EFECTUATE);
		String numarDeplasariBugetateRamase = calculateNrDeplasariBugetateRamase(detaliiNumarDeplasariBugetateNomenclatorValue, numarDeplasariEfectuate);
		
		String eveniment = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getEvenimentMetadataName());
		String taraNomenclatorValueId = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getTaraMetadataName());
		NomenclatorValue taraNomenclatorValue = nomenclatorValueDao.find(Long.valueOf(taraNomenclatorValueId));
		String numeTara = NomenclatorValueUtils.getAttributeValueAsString(taraNomenclatorValue, NomenclatorConstants.STATE_ATTRIBUTE_KEY_ORGANISM_NUME_STAT);
		
		String oras = getMetadataValue(metadataInstances, metadataDefinitions, arbConstants.getDocumentDecizieDeplasare().getOrasMetadataName());
		
		Date dataPlecare = DocumentUtils.getMetadataDateTimeValue(document, documentType, arbConstants.getDocumentDecizieDeplasare().getDataPlecareMetadataName());
		Date dataSosire = DocumentUtils.getMetadataDateTimeValue(document, documentType, arbConstants.getDocumentDecizieDeplasare().getDataSosireMetadataName());
		Date dataConferintaInceput = DocumentUtils.getMetadataDateTimeValue(document, documentType, arbConstants.getDocumentDecizieDeplasare().getDataConferintaInceputMetadataName());
		Date dataConferintaSfarsit = DocumentUtils.getMetadataDateTimeValue(document, documentType, arbConstants.getDocumentDecizieDeplasare().getDataConferintaSfarsitMetadataName());
		
		documentDecizieDeplasareModel.setDenumireInstitutie(NomenclatorValueUtils.getAttributeValueAsString(institutieNomenclatorValue, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE));
		documentDecizieDeplasareModel.setDataDecizie(dataDecizie);
		documentDecizieDeplasareModel.setOrganismId(Long.valueOf(organismId));
		documentDecizieDeplasareModel.setDenumireOrganism(NomenclatorValueUtils.getAttributeValueAsString(organismNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE));
		documentDecizieDeplasareModel.setAbreviereOrganism(NomenclatorValueUtils.getAttributeValueAsString(organismNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_ABREVIERE));
		
		if (comitetNomenclatorValue != null) {
			documentDecizieDeplasareModel.setDenumireComitet(NomenclatorValueUtils.getAttributeValueAsString(comitetNomenclatorValue, NomenclatorConstants.COMITETE_ATTRIBUTE_KEY_DENUMIRE));
		}

		documentDecizieDeplasareModel.setNumarDeplasariEfectuate(Integer.valueOf(numarDeplasariEfectuate));
		documentDecizieDeplasareModel.setNumarDeplasariBugetateRamase(Integer.valueOf(numarDeplasariBugetateRamase));
		documentDecizieDeplasareModel.setEveniment(eveniment);
		documentDecizieDeplasareModel.setTara(numeTara);
		documentDecizieDeplasareModel.setOras(oras);
		documentDecizieDeplasareModel.setDataPlecare(dataPlecare);
		documentDecizieDeplasareModel.setDataSosire(dataSosire);
		documentDecizieDeplasareModel.setDataConferintaInceput(dataConferintaInceput);
		documentDecizieDeplasareModel.setDataConferintaSfarsit(dataConferintaSfarsit);
		documentDecizieDeplasareModel.setDetaliiNumarDeplasariBugetateNomenclatorValueId(detaliiNumarDeplasariBugetateNomenclatorValue.getId());
		return documentDecizieDeplasareModel;
	}
	
	private String calculateNrDeplasariBugetateRamase(NomenclatorValue detaliiNumarDeplasariBugetateNomenclatorValue, String numarDeplasariEfectuate) {
		int numarDeplasariBugetate = Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE));
		int numarDeplasariBugetateSuplimentate = Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_SUPLIMENTATE));
		int numarDeplasariBugetateRelocate = Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RELOCATE));
		int numarDeplasariBugetatePrimitePrinRelocare = Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_PRIMITE_PRIN_RELOCARE));
		
		Integer numarDeplasariBugetateRamase = numarDeplasariBugetate + numarDeplasariBugetateSuplimentate + numarDeplasariBugetatePrimitePrinRelocare - numarDeplasariBugetateRelocate - Integer.parseInt(numarDeplasariEfectuate);
		
		return numarDeplasariBugetateRamase.toString();
	}
	
	private String getMetadataValue(List<MetadataInstance> metadataInstances, List<MetadataDefinition> metadataDefinitions, String metadataName) {
		
		Long metadataDefinitionId = null;
		for (MetadataDefinition metadataDefinition : metadataDefinitions) {
			if (metadataDefinition.getName().equals(metadataName)) {
				metadataDefinitionId = metadataDefinition.getId();
			}
		}
		
		if (metadataDefinitionId == null) {
			throw new RuntimeException();
		}
		
		for (MetadataInstance metadataInstance : metadataInstances) {
			if (metadataInstance.getMetadataDefinitionId() == metadataDefinitionId) {
				return metadataInstance.getValue();
			}
		}
		
		return null;
	}

	@Override
	public List<Integer> getYearsOfExistingDeplasariDeconturi() {
		return deplasariDeconturiDao.getYearsOfExistingDeconturi();
	}

	@Override
	public List<DeplasareDecontViewModel> getAllDeplasariDeconturiViewModelsByYear(Integer year) {
		List<DeplasareDecont> deplasariDeconturi = deplasariDeconturiDao.getAllDeplasariDeconturiByYear(year);
		List<DeplasareDecontViewModel> deplasariDeconturiViewModels = deplasariDeconturiConverter.getDeplasariDeconturiViewModelsFromEntities(deplasariDeconturi);
		return deplasariDeconturiViewModels;
	}

	@Override
	public boolean isDeplasareDecontCanceled(Long deplasareDecontId) {
		DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
		return deplasareDecont.isAnulat();
	}

	@Override
	public DeplasareDecontModel getDeplasareDecontById(Long deplasareDecontId) {
		DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
		DeplasareDecontModel deplasareDecontModel = deplasariDeconturiConverter.getModelFromEntity(deplasareDecont);
		return deplasareDecontModel;
	}
	
	@Override
	@Transactional
	public void cancelDeplasareDecont(Long deplasareDecontId, String motiv) {
		DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
		deplasareDecont.setAnulat(true);
		deplasareDecont.setMotivAnulare(motiv);
		
		deplasariDeconturiDao.saveDeplasareDecont(deplasareDecont);
		
		updateNumarDeplasariBugetateEfectuate(deplasareDecont);
		
	}
	
	private void updateNumarDeplasariBugetateEfectuate(DeplasareDecont deplasareDecont) {
		NomenclatorValue detaliiNumarDeplasariBugetateNomenclatorValue = nomenclatorValueDao.find(deplasareDecont.getDetaliiNumarDeplasariBugetateNomenclatorValueId());
		
		Long numarDeplasariEfectuate = NomenclatorValueUtils.getAttributeValueAsLong(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_EFECTUATE);
		numarDeplasariEfectuate--;
		NomenclatorValueUtils.setAttributeValue(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_EFECTUATE, numarDeplasariEfectuate.toString());
		
		Long numarDeplasariBugetateRamase = NomenclatorValueUtils.getAttributeValueAsLong(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE);
		numarDeplasariBugetateRamase++;
		NomenclatorValueUtils.setAttributeValue(detaliiNumarDeplasariBugetateNomenclatorValue, NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE_RAMASE, numarDeplasariBugetateRamase.toString());
		
		nomenclatorValueDao.save(detaliiNumarDeplasariBugetateNomenclatorValue);
	}
	
	@Override
	@Transactional
	public void removeDeplasareDecont(Long deplasareDecontId) throws AppException {
		DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
		
		if (deplasareDecont.isFinalizat()) {
			throw new AppException(AppExceptionCodes.DEPLASARE_DECONT_CANNOT_BE_DELETED_BECOUSE_IT_WAS_FINALIZED);
		}
		
		List<Long> cheltuieliArbIds = new ArrayList<>();
		for (CheltuialaArb cheltuialaArb : deplasareDecont.getCheltuieliArb()) {
			cheltuieliArbIds.add(cheltuialaArb.getId());
		}
		cheltuieliArbDao.deleteCheltuieliArb(cheltuieliArbIds);
		
		List<Long> cheltuialiReprezentantArbIds = new ArrayList<>();
		for (CheltuialaReprezentantArb cheltuialaReprezentantArb : deplasareDecont.getCheltuieliReprezentantArb()) {
			cheltuialiReprezentantArbIds.add(cheltuialaReprezentantArb.getId());
		}
		cheltuieliReprezentantArbDao.deleteCheltuieliReprezentatiArb(cheltuialiReprezentantArbIds);
		
		updateNumarDeplasariBugetateEfectuate(deplasareDecont);
		
		deplasariDeconturiDao.deleteById(deplasareDecontId);
	}
	
	@Override
	public void finalizeDeplasareDecont(Long deplasareDecontId) throws AppException {
		DeplasareDecont deplasareDecont = deplasariDeconturiDao.findById(deplasareDecontId);
		
		int numberOfCheltuieliArb = 0;
		if (CollectionUtils.isNotEmpty(deplasareDecont.getCheltuieliArb())) {
			numberOfCheltuieliArb = deplasareDecont.getCheltuieliArb().size();
		}
		
		int numberOfCheltuieliReprezentantiArb = 0;
		if (CollectionUtils.isNotEmpty(deplasareDecont.getCheltuieliReprezentantArb())) {
			numberOfCheltuieliReprezentantiArb = deplasareDecont.getCheltuieliReprezentantArb().size();
		}
		
		if (numberOfCheltuieliArb + numberOfCheltuieliReprezentantiArb == 0) {
			throw new AppException(AppExceptionCodes.DEPLASARE_DECONT_CANNOT_BE_FINALIZED_BECOUSE_NO_EXPENSES_WERE_ADDED);
		}
		
		deplasareDecont.setFinalizat(true);
		deplasariDeconturiDao.saveDeplasareDecont(deplasareDecont);
	}
	
	@Override
	public List<String> getAllDistinctTitulari() {
		List<String> titulari = deplasariDeconturiDao.getAllDistinctTitulariWithDecont();		
		return titulari;
	}

	@Override
	public List<String> getAllNumarDeciziiByFilter(CheltuieliArbSiReprezentantArbReportDateFilterModel filter) {
		List<String> decizii = deplasariDeconturiDao.getAllNumarDecizieByDate(DateUtils.nullHourMinutesSeconds(filter.getDataDecontDeLa()), DateUtils.maximizeHourMinutesSeconds(filter.getDataDecontPanaLa()));	
		return decizii;
	}
	
	
	public void setDeplasariDeconturiConverter(DeplasariDeconturiConverter deplasariDeconturiConverter) {
		this.deplasariDeconturiConverter = deplasariDeconturiConverter;
	}
	
	public void setDeplasariDeconturiDao(DeplasariDeconturiDao deplasariDeconturiDao) {
		this.deplasariDeconturiDao = deplasariDeconturiDao;
	}
	
	public void setCheltuieliArbDao(CheltuieliArbDao cheltuieliArbDao) {
		this.cheltuieliArbDao = cheltuieliArbDao;
	}

	public void setCheltuieliReprezentantArbDao(CheltuieliReprezentantArbDao cheltuieliReprezentantArbDao) {
		this.cheltuieliReprezentantArbDao = cheltuieliReprezentantArbDao;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setDecizieDeplasareService(DecizieDeplasareService decizieDeplasareService) {
		this.decizieDeplasareService = decizieDeplasareService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	
	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

}
