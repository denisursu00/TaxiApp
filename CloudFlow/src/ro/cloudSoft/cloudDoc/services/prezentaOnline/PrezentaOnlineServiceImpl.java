package ro.cloudSoft.cloudDoc.services.prezentaOnline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.prezentaOnline.PrezentaOnlineFinalizataDao;
import ro.cloudSoft.cloudDoc.dao.prezentaOnline.PrezentaOnlineParticipantiDao;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Stare;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineFinalizata;
import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineParticipanti;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.arb.PrezentaComisieGlDocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.DocumentPrezentaOnlineModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.ParticipantiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.PrezentaMembriiReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.presentation.server.converters.prezentaOnline.PrezentaOnlineConverter;
import ro.cloudSoft.cloudDoc.services.arb.ComisieSauGLService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class PrezentaOnlineServiceImpl implements PrezentaOnlineService {
	
	private PrezentaComisieGlDocumentPlugin prezentaComisieGlDocumentPlugin;
	private PrezentaOnlineFinalizataDao prezentaOnlineFinalizataDao;
	private PrezentaOnlineParticipantiDao prezentaOnlineParticipantiDao;
	private ComisieSauGLService comisieSauGLService;
	private NomenclatorService nomenclatorService;
	private DocumentService documentService;
	private DocumentTypeService documentTypeService;
	private WorkflowService workflowService;
	private PrezentaOnlineConverter prezentaOnlineConverter;
	private ArbConstants arbConstants;
	
	@Override
	public List<DocumentPrezentaOnlineModel> getAllDocumentsPrezenta(SecurityManager userSecurity) {
		List<DocumentPrezentaOnlineModel> documente = prezentaComisieGlDocumentPlugin.getPrezentaComisieGlDocumentsForPrezentaOnline(userSecurity);
	
		documente.removeIf(document -> prezentaOnlineFinalizataDao.isImportedByDocument(document.getDocumentId(), document.getDocumentLocationRealName()));		
		documente.sort((d1, d2) -> d2.getDataInceput().compareTo(d1.getDataInceput()));
		
		documente.forEach(document -> {
			document.setFinalizata(prezentaOnlineFinalizataDao.existByDocument(document.getDocumentId(), document.getDocumentLocationRealName()));
		});
		return documente;
	}

	@Override
	public List<PrezentaMembriiReprezentantiComisieGl> getAllMembriiReprezentantiByComisieGlId(Long comisieGlId) throws AppException {
		
		ReprezentantiComisieSauGLModel reprezentant = comisieSauGLService.getReprezentantiByComisieSauGLId(comisieGlId);
		
		List<MembruReprezentantiComisieSauGLModel> membriiReprezentanti = reprezentant.getMembri();
		membriiReprezentanti.removeIf(membru -> 
			(
				membru.getStare().equals(Stare.INACTIV.name()) ||
				NomenclatorValueUtils.getAttributeValueAsBoolean(nomenclatorService.getNomenclatorValue(membru.getInstitutieId()), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT) ||
				(membru.getMembruInstitutieId() == null &&  StringUtils.isBlank(membru.getNume()) &&  StringUtils.isBlank(membru.getPrenume()))
			)
		);
		
		List<PrezentaMembriiReprezentantiComisieGl> membrii = prezentaOnlineConverter.fromModelToModel(reprezentant.getMembri());
		if (CollectionUtils.isNotEmpty(membrii)) {
			membrii.sort(Comparator.comparing(PrezentaMembriiReprezentantiComisieGl :: getNumeInstitutie, String.CASE_INSENSITIVE_ORDER));
		} 
		return membrii;
	}

	@Override
	public void saveParticipant(PrezentaMembriiReprezentantiComisieGl prezentaMembriiReprezentantiComisieGl) {
		PrezentaOnlineParticipanti entity = prezentaOnlineConverter.toEntity(prezentaMembriiReprezentantiComisieGl);
		
		if (prezentaMembriiReprezentantiComisieGl.getMembruInstitutieId() == null) {
			prezentaOnlineParticipantiDao.save(entity);
		} else if (!prezentaOnlineParticipantiDao.existByDocument(prezentaMembriiReprezentantiComisieGl.getDocumentId(), 
				prezentaMembriiReprezentantiComisieGl.getDocumentLocationRealName(), prezentaMembriiReprezentantiComisieGl.getMembruInstitutieId())) {
			prezentaOnlineParticipantiDao.save(entity);
		}
		
	}	

	@Override
	public ParticipantiModel getAllParticipantiByDocument(String documentId, String documentLocationRealName) throws AppException {

		List<PrezentaOnlineParticipanti> participanti = prezentaOnlineParticipantiDao.getAllByDocument(documentId, documentLocationRealName);	
		ParticipantiModel model = new ParticipantiModel();
		List<PrezentaMembriiReprezentantiComisieGl> rows = new ArrayList<>();
		List<NomenclatorValueModel> tipInstitutii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.TIP_INSTITUTII_NOMENCLATOR_CODE);

		String intitutiiArbId = null;
		String institutieAfiliateId = null;
		String alteInstitutiiId = null;
		
		for (NomenclatorValueModel tip : tipInstitutii) {
			String tipInstitutie = NomenclatorValueUtils.getAttributeValueAsString(tip, NomenclatorConstants.TIP_INSTITUTIE_DENUMIRE_ATTRIBUTE_KEY_COD);
			if (tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB)) {
				intitutiiArbId = tip.getId().toString();
			}
			if (tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_AFILIAT)) {
				institutieAfiliateId = tip.getId().toString();
			}
			if (tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_ALTE_INSTITUTII)) {
				alteInstitutiiId = tip.getId().toString();
			}
		}
		
		int totalParticipanti = 0;
		int totalMembriiArb = 0;
		int totalMembriiAfiliati = 0;
		int totalMembriiAlteInstitutii = 0;

		for (PrezentaOnlineParticipanti participant : participanti) {
			PrezentaMembriiReprezentantiComisieGl row = new PrezentaMembriiReprezentantiComisieGl();
			row = prezentaOnlineConverter.toModel(participant);
			rows.add(row);

			if (intitutiiArbId.equals(NomenclatorValueUtils.getAttributeValueAsString(participant.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE))) {
				totalMembriiArb++;
			}
			if (institutieAfiliateId.equals(NomenclatorValueUtils.getAttributeValueAsString(participant.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE))) {
				totalMembriiAfiliati++;
			}
			if (alteInstitutiiId.equals(NomenclatorValueUtils.getAttributeValueAsString(participant.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE))) {
				totalMembriiAlteInstitutii++;
			}
			totalParticipanti++;			
		};
		
		rows.sort(Comparator.comparing(PrezentaMembriiReprezentantiComisieGl :: getNumeInstitutie, String.CASE_INSENSITIVE_ORDER));		

		model.setRows(rows);
		model.setTotalMembriiAfiliati(totalMembriiAfiliati);
		model.setTotalMembriiAlteInstitutii(totalMembriiAlteInstitutii);
		model.setTotalMembriiArb(totalMembriiArb);
		model.setTotalParticipanti(totalParticipanti);
		
		return model;
	}
	
	@Override
	public void deleteParticipant(Long id) {
		prezentaOnlineParticipantiDao.deleteById(id);
	}
	
	@Override
	public void finalizarePrezentaByDocument(String documentId, String documentLocationRealName) {
		
		if (!prezentaOnlineFinalizataDao.existByDocument(documentId, documentLocationRealName)) {
			PrezentaOnlineFinalizata entity = new PrezentaOnlineFinalizata();
			
			entity.setDocumentId(documentId);
			entity.setDocumentLocationRealName(documentLocationRealName);
			entity.setHasImported(false);
			
			prezentaOnlineFinalizataDao.save(entity);	
		}
	}
	
	@Override
	public boolean isPrezentaFinalizataByDocument(String documentId, String documentLocationRealName) {
		return prezentaOnlineFinalizataDao.existByDocument(documentId, documentLocationRealName);
	}
	
	@Override
	public void importaPrezentaOnlineByDocument(String documentId, String documentLocationRealName, SecurityManager userSecurity) throws AppException {
		Document document = documentService.getDocumentById(documentId, documentLocationRealName);
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());		
		Workflow workflow = workflowService.getWorkflowByDocumentType(document.getDocumentTypeId());
		WorkflowState prezentaComisieGlCompletarePrezentaWorkflowState = workflowService.getWorkflowState(workflow.getId(), arbConstants.getDocumentPrezentaComisieGlConstants().getCompletareMetadatePrezentaWorkflowStateCode());
		
		if ((document.getWorkflowStateId()).equals(prezentaComisieGlCompletarePrezentaWorkflowState.getId())) {
			if (!document.isLocked() || (document.getLockedByUserId()).equals(userSecurity.getUserId())) {
				Collection<Attachment> attachments = new ArrayList<Attachment>();

				if (!document.isLocked()) {
					documentService.checkout(document.getId(), document.getDocumentLocationRealName(), userSecurity);
				}
				
				try {
					addParticipantiToDocument(document, documentType);
					
					documentService.save(document, attachments, null, documentLocationRealName, null, userSecurity);
				} catch (AppException e) {
					e.printStackTrace();
					throw new AppException(AppExceptionCodes.IMPORT_PREZENTA_COMISIE_GL_ERROR);
				}
				
				PrezentaOnlineFinalizata prezentaOnlineFinalizata = prezentaOnlineFinalizataDao.getByDocument(document.getId(), document.getDocumentLocationRealName());
				prezentaOnlineFinalizata.setHasImported(true);
				prezentaOnlineFinalizataDao.save(prezentaOnlineFinalizata);
				
				List<PrezentaOnlineParticipanti> participanti = prezentaOnlineParticipantiDao.getAllByDocument(document.getId(), document.getDocumentLocationRealName());
				for (PrezentaOnlineParticipanti participant : participanti) {
					prezentaOnlineParticipantiDao.deleteById(participant.getId());
				}	
				
				if (!document.isLocked()) {
					Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = new ArrayList<Long>();
					List<String> namesForAttachmentsToDelete = new ArrayList<String>();
					documentService.checkin(document, attachments, documentType.isKeepAllVersions(), null, documentLocationRealName, definitionIdsForAutoNumberMetadataValuesToGenerate, namesForAttachmentsToDelete, userSecurity);
				}
			}
			if (document.isLocked() && !(document.getLockedByUserId()).equals(userSecurity.getUserId())) {
				throw new AppException(AppExceptionCodes.DOCUMENT_BLOKED_FROM_ANOTHER_USER);
			}
		} else {
			throw new AppException(AppExceptionCodes.DOCUMENT_IS_IN_ANOTHER_STEEP);
		}
	}
	
	private void addParticipantiToDocument(Document document, DocumentType documentType) throws AppException {
		List<PrezentaOnlineParticipanti> participanti = prezentaOnlineParticipantiDao.getAllByDocument(document.getId(), document.getDocumentLocationRealName());
		Map<Long, String> persoaneUiAttrValuesAsMap = nomenclatorService.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);
		Map<Long, List<CollectionInstance>> collectionInstanceListMap = document.getCollectionInstanceListMap();
		List<CollectionInstance> colectionInstanceList = new ArrayList<>();
		
		Long informatiiParticipantiMetadataId = DocumentTypeUtils.getMetadataCollectionDefinitionIdByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());
		Long membruAcriditatMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName()).getId();
		Long numeInlocuitorMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName()).getId();
		Long prenumeInlocuitorMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getPrenumeParticipantInlocuitorOfInformatiiParticipantiMetadataName()).getId();
		Long functieMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName()).getId();
		Long departamentMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getDepartamentOfInformatiiParticipantiMetadataName()).getId();
		Long emailtMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getEmailOfInformatiiParticipantiMetadataName()).getId();
		Long telefonMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getTelefonOfInformatiiParticipantiMetadataName()).getId();
		Long institutieMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName()).getId();
		Long calitateMetadataId = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(), arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName()).getId();
		
		if (CollectionUtils.isEmpty(collectionInstanceListMap.get(informatiiParticipantiMetadataId))) {
			collectionInstanceListMap.put(informatiiParticipantiMetadataId, colectionInstanceList);
		} else {
			colectionInstanceList = collectionInstanceListMap.get(informatiiParticipantiMetadataId);
		}
		
		for (PrezentaOnlineParticipanti participant : participanti) {
			CollectionInstance collectionInstance = new CollectionInstance();
			List<MetadataInstance> metadataInstanceList = new ArrayList<MetadataInstance>();
			
			MetadataInstance institutieMetadataInstance = new MetadataInstance();
			institutieMetadataInstance.setMetadataDefinitionId(institutieMetadataId);
			institutieMetadataInstance.setValue(participant.getInstitutie().getId().toString());
			metadataInstanceList.add(institutieMetadataInstance);
			
			if (participant.getMembruInstitutie() != null) {
				MetadataInstance membruAcreditatMetadataInstance = new MetadataInstance();
				membruAcreditatMetadataInstance.setMetadataDefinitionId(membruAcriditatMetadataId);
				membruAcreditatMetadataInstance.setValue(persoaneUiAttrValuesAsMap.get(participant.getMembruInstitutie().getId()));
				metadataInstanceList.add(membruAcreditatMetadataInstance);
			} else {
				if (StringUtils.isNotEmpty(participant.getNume())) {
					MetadataInstance numeMetadataInstance = new MetadataInstance();
					numeMetadataInstance.setMetadataDefinitionId(numeInlocuitorMetadataId);
					numeMetadataInstance.setValue(participant.getNume());
					metadataInstanceList.add(numeMetadataInstance);			
				}
				if (StringUtils.isNotEmpty(participant.getPrenume())) {
					MetadataInstance prenumeMetadataInstance = new MetadataInstance();
					prenumeMetadataInstance.setMetadataDefinitionId(prenumeInlocuitorMetadataId);
					prenumeMetadataInstance.setValue(participant.getPrenume());
					metadataInstanceList.add(prenumeMetadataInstance);
				}
			}
			if (StringUtils.isNotEmpty(participant.getFunctie())) {
				MetadataInstance functieMetadataInstance = new MetadataInstance();
				functieMetadataInstance.setMetadataDefinitionId(functieMetadataId);
				functieMetadataInstance.setValue(participant.getFunctie());
				metadataInstanceList.add(functieMetadataInstance);
			}
			if (StringUtils.isNotEmpty(participant.getDepartament())) {
				MetadataInstance departamentMetadataInstance = new MetadataInstance();
				departamentMetadataInstance.setMetadataDefinitionId(departamentMetadataId);
				departamentMetadataInstance.setValue(participant.getDepartament());
				metadataInstanceList.add(departamentMetadataInstance);
			}
			if (StringUtils.isNotEmpty(participant.getEmail())) {		
				MetadataInstance emailMetadataInstance = new MetadataInstance();
				emailMetadataInstance.setMetadataDefinitionId(emailtMetadataId);
				emailMetadataInstance.setValue(participant.getEmail());
				metadataInstanceList.add(emailMetadataInstance);	
			}
			if (StringUtils.isNotEmpty(participant.getTelefon())) {	
				MetadataInstance telefonMetadataInstance = new MetadataInstance();
				telefonMetadataInstance.setMetadataDefinitionId(telefonMetadataId);
				telefonMetadataInstance.setValue(participant.getTelefon());
				metadataInstanceList.add(telefonMetadataInstance);
			}
			if (StringUtils.isNotEmpty(participant.getCalitate())) {	
				MetadataInstance calitateMetadataInstance = new MetadataInstance();
				calitateMetadataInstance.setMetadataDefinitionId(calitateMetadataId);
				calitateMetadataInstance.setValue(participant.getCalitate().toLowerCase());
				metadataInstanceList.add(calitateMetadataInstance);
			}
			
			collectionInstance.setMetadataInstanceList(metadataInstanceList);
			colectionInstanceList.add(collectionInstance);
		}
		
		document.setCollectionInstanceListMap(collectionInstanceListMap);		
	}
	
	
	
	public void setPrezentaComisieGlDocumentPlugin(PrezentaComisieGlDocumentPlugin prezentaComisieGlDocumentPlugin) {
		this.prezentaComisieGlDocumentPlugin = prezentaComisieGlDocumentPlugin;
	}

	public void setPrezentaOnlineFinalizataDao(PrezentaOnlineFinalizataDao prezentaOnlineFinalizataDao) {
		this.prezentaOnlineFinalizataDao = prezentaOnlineFinalizataDao;
	}

	public void setPrezentaOnlineParticipantiDao(PrezentaOnlineParticipantiDao prezentaOnlineParticipantiDao) {
		this.prezentaOnlineParticipantiDao = prezentaOnlineParticipantiDao;
	}

	public void setComisieSauGLService(ComisieSauGLService comisieSauGLService) {
		this.comisieSauGLService = comisieSauGLService;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setPrezentaOnlineConverter(PrezentaOnlineConverter prezentaOnlineConverter) {
		this.prezentaOnlineConverter = prezentaOnlineConverter;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}



}
