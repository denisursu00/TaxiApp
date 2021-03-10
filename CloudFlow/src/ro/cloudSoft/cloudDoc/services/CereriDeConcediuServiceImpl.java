package ro.cloudSoft.cloudDoc.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.CerereDeConcediuConstants;
import ro.cloudSoft.cloudDoc.core.constants.CereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.dao.ConcediuDao;
import ro.cloudSoft.cloudDoc.domain.Concediu;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.CereriDeConcediuBusinessUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * , Dan Cirja
 */
public class CereriDeConcediuServiceImpl implements CereriDeConcediuService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(CereriDeConcediuServiceImpl.class);
	
	private CereriDeConcediuConstants cereriDeConcediuConstants;
	private boolean timesheetsForLeavesIntegrationEnabled;
	
	private DocumentService documentService;
	private DocumentTypeService documentTypeService;
	private WorkflowService workflowService;
	private WorkflowExecutionService workflowExecutionService;
	private PontajForConcediiService pontajForConcediiService;
	
	private ConcediuDao concediuDao;
	private UserPersistencePlugin userPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			cereriDeConcediuConstants,
			timesheetsForLeavesIntegrationEnabled,
			
			documentService,
			documentTypeService,
			workflowService,
			workflowExecutionService,
			pontajForConcediiService,
			
			concediuDao,
			userPersistencePlugin
		);
	}
	
	/**
	 * Verifica daca tipul de document are metadatele necesare pentru anularea unei cereri de concediu.
	 */
	private boolean documentTypeHasRequiredMetadataForAnulare(DocumentType documentType, SecurityManager userSecurity) {
		
		CerereDeConcediuConstants constantsForCerere = cereriDeConcediuConstants.getFor(documentType.getId());
		
		Long anulataMetadataId = constantsForCerere.getAnulataMetadataId();
		String anulataDaMetadataValue = constantsForCerere.getAnulataMetadataPositiveValue();
		
		for (MetadataDefinition metadata : documentType.getMetadataDefinitions()) {
			
			if (!metadata.getId().equals(anulataMetadataId)) {
				continue;
			}
			
			ListMetadataDefinition metadataAsList = (ListMetadataDefinition) metadata;				
			
			boolean isRequiredMetadataValueFound = false;
			for (ListMetadataItem listItem : metadataAsList.getListItems()) {
				if (listItem.getValue().equals(anulataDaMetadataValue)) {
					isRequiredMetadataValueFound = true;
					break;
				}
			}
			
			if (isRequiredMetadataValueFound) {
				return true;
			} else {
				
				String logMessage = "Pentru tipul de document cu ID-ul [" + documentType.getId() + "] " +
					"si numele [" + documentType.getName() + "], s-a gasit metadata 'anulata', necesara " +
					"anularii unei cereri de concediu, insa aceasta nu are ca valoare posibila " +
					"[" + anulataDaMetadataValue + "].";
				LOGGER.error(logMessage, "documentTypeHasRequiredMetadataForAnulare", userSecurity);
				
				return false;
			}
				
		}
		
		return false;
	}
	
	/**
	 * Verifica daca fluxul are tranzitiile necesare pentru anularea unei cereri de concediu.
	 */
	private boolean workflowHasRequiredTransitionsForAnulare(Workflow workflow, Long documentTypeId, SecurityManager userSecurity) {
		
		Multimap<WorkflowState, WorkflowTransition> transitionsByIntermediateState = HashMultimap.create();
		for (WorkflowTransition transition : workflow.getTransitions()) {
			WorkflowState startState = transition.getStartState();
			if (startState.getStateType().equals(WorkflowState.STATETYPE_INTERMEDIATE)) {
				transitionsByIntermediateState.put(startState, transition);
			}
		}
		
		for (WorkflowState intermediateState : transitionsByIntermediateState.keySet()) {
			
			Collection<WorkflowTransition> leavingTransitions = transitionsByIntermediateState.get(intermediateState);
			
			boolean isRequiredTransitionFoundForState = false;
			for (WorkflowTransition leavingTransition : leavingTransitions) {
				boolean transitionNameHasPrefix = leavingTransition.getName().startsWith(cereriDeConcediuConstants.getFor(documentTypeId).getAnulareTransitionNamePrefix());
				boolean isFinalStateStop = leavingTransition.getFinalState().getStateType().equals(WorkflowState.STATETYPE_STOP);
				if (transitionNameHasPrefix) {
					if (isFinalStateStop) {
						isRequiredTransitionFoundForState = true;
						break;
					} else {
						String logMessage = "Pentru fluxul cu ID-ul [" + workflow.getId() + "] si numele " +
							"[" + workflow.getName() + "], s-a gasit tranzitia cu numele [" + leavingTransition.getName() + "]," +
							"necesara anularii cererii de concediu, insa aceasta NU duce spre o stare de tip STOP.";
						LOGGER.error(logMessage, "workflowHasRequiredTransitionsForAnulare", userSecurity);
					}
				}
			}
			if (!isRequiredTransitionFoundForState) {
				
				String logMessage = "Pentru fluxul cu ID-ul [" + workflow.getId() + "] si numele " +
					"[" + workflow.getName() + "], s-a gasit o stare care NU are o tranzitie necesara " +
					"anularii cererii de concediu. Starea are codul [" + intermediateState.getCode() + "] " +
					"si numele [" + intermediateState.getName() + "].";
				LOGGER.error(logMessage, "workflowHasRequiredTransitionsForAnulare", userSecurity);
				
				return false;
			}
		}
		
		return true;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void anuleaza(String documentLocationRealName, String documentId,
			SecurityManager userSecurity) throws AppException {
		
		Document document = documentService.getDocumentForAutomaticAction(documentLocationRealName, documentId);
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
		Workflow workflow = workflowService.getWorkflowByDocumentType(documentType.getId());
		
		if (!cereriDeConcediuConstants.isCerereDeConcediu(documentType.getId())) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "S-a incercat anularea documentului cu atributele: " + documentLogAttributes + 
				", insa acesta NU este de tip cerere de concediu, ci de tipul [" + documentType.getName() + "].";
			LOGGER.error(logMessage, "anulare", userSecurity);
			
			throw new AppException();
		}
		
		if (!documentTypeHasRequiredMetadataForAnulare(documentType, userSecurity)) {
			
			String logMessage = "Tipul de document cu ID-ul [" + documentType.getId() + "] " +
				"si numele [" + documentType.getName() + "] NU are metadata 'anulata', " +
				"necesara pentru anularea cererii de concediu.";
			LOGGER.error(logMessage, "anuleaza", userSecurity);
			
			throw new AppException(AppExceptionCodes.CERERI_DE_CONCEDIU_DOCUMENT_TYPE_NOT_PROPERLY_CONFIGURED);
		}
		
		if (!workflowHasRequiredTransitionsForAnulare(workflow, documentType.getId(), userSecurity)) {
			
			String logMessage = "Fluxul cu ID-ul [" + workflow.getId() + "] si numele " +
				"[" + workflow.getName() + "] NU are toate tranzitiile necesare " +
				"anularii cererii de concediu.";
			LOGGER.error(logMessage, "anulare", userSecurity);
			
			throw new AppException(AppExceptionCodes.CERERI_DE_CONCEDIU_WORKFLOW_NOT_PROPERLY_CONFIGURED);
		}
		
		if (!CereriDeConcediuBusinessUtils.hasPermissionForAnulare(userSecurity, document)) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "S-a incercat anularea cererii de concediu reprezentata prin documentul " +
				"cu atributele: " + documentLogAttributes + ", insa utilizatorul curent NU are acest drept.";
			LOGGER.error(logMessage, "anulare", userSecurity);
			
			throw new AppException();
		}
		
		if (timesheetsForLeavesIntegrationEnabled) {
			Concediu concediu = getConcediuForCerere(document);
			if ((concediu != null) && pontajForConcediiService.hasPontajeAprobate(concediu)) {
				
				String logMessage = "S-a incercat ANULAREA cererii de concediu pentru solicitantul cu ID-ul " +
					"[" + concediu.getSolicitantId() + "], data inceput [" + LogHelper.formatDateForLog(concediu.getDataInceput()) + "], " +
					"data sfarsit [" + LogHelper.formatDateForLog(concediu.getDataSfarsit()) + "], insa exista " +
					"deja pontaje aprobate pentru acel concediu.";
				LOGGER.error(logMessage, "anulare", userSecurity);
				
				throw new AppException(AppExceptionCodes.CERERI_DE_CONCEDIU_ANULARE_NOT_POSSIBLE_PONTAJE_APROBATE_EXIST);
			}
		}
		
		CerereDeConcediuConstants constantsForCerere = cereriDeConcediuConstants.getFor(documentType.getId());
		
		boolean foundAnulataMetadataValueInDocument = false;
		for (MetadataInstance metadata : document.getMetadataInstanceList()) {
			if (metadata.getMetadataDefinitionId().equals(constantsForCerere.getAnulataMetadataId())) {
				
				metadata.setValue(constantsForCerere.getAnulataMetadataPositiveValue());
				
				foundAnulataMetadataValueInDocument = true;
				break;
			}
		}
		if (!foundAnulataMetadataValueInDocument) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "Documentul cu atributele: " + documentLogAttributes + ", NU are " +
				"completata metadata 'anulata', necesara pentru anularea cererii de concediu.";
			LOGGER.error(logMessage, "anulare", userSecurity);
			
			throw new AppException();
		}
		
		documentService.checkinForAutomaticAction(document, documentType.isKeepAllVersions(), userSecurity);
		
		workflowExecutionService.endWorkflowInstanceIfActive(documentLocationRealName, documentId,
				constantsForCerere.getAnulareTransitionNamePrefix(), userSecurity);
		
		deleteConcediuForCerere(documentId);
	}
	
	@Override
	public boolean isDocumentCerereDeConcediu(Long documentTypeId) {
		return (cereriDeConcediuConstants.isCerereDeConcediu(documentTypeId));
   	}
	
	@Override
	public void saveOrUpdateConcediuForCerere(Document document, String documentId, SecurityManager userSecurity) throws AppException {
		
		CerereDeConcediuConstants metadateCerereDeConcediu = cereriDeConcediuConstants.getFor(document.getDocumentTypeId());
		
		Long solicitantMetadataId = metadateCerereDeConcediu.getSolicitantMetadataId();
    	String solicitantMetadataValue = null;
    	
    	Long dataInceputMetadataId = metadateCerereDeConcediu.getDataInceputMetadataId();
    	String dataInceputMetadataValue = null;
    	
    	Long dataSfarsitMetadataId = metadateCerereDeConcediu.getDataSfarsitMetadataId();
    	String dataSfarsitMetadataValue = null;
    	
    	Long aprobareDecisivaMetadataId = metadateCerereDeConcediu.getAprobareDecisivaMetadataId();
    	String aprobareDecisivaMetadataValue = null;
    	
    	List<MetadataInstance> metadateDocument = document.getMetadataInstanceList();
    	for (MetadataInstance metadata : metadateDocument) {
    		if (metadata.getMetadataDefinitionId().equals(solicitantMetadataId)) {
    			solicitantMetadataValue = metadata.getValue();
    		} else if (metadata.getMetadataDefinitionId().equals(dataInceputMetadataId)) {
    			dataInceputMetadataValue = metadata.getValue();
    		} else if (metadata.getMetadataDefinitionId().equals(dataSfarsitMetadataId)) {
    			dataSfarsitMetadataValue = metadata.getValue();
    		} else if (metadata.getMetadataDefinitionId().equals(aprobareDecisivaMetadataId)) {
    			aprobareDecisivaMetadataValue = metadata.getValue();
    		}
    	}

    	Long solicitantId = null;
    	Date dataInceput = null;
    	Date dataSfarsit = null;
    	
    	try {    		
    		solicitantId = MetadataValueHelper.getUserId(solicitantMetadataValue);    		
    	} catch (Exception e) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
    		
    		String logMessage = "A aparut o EXCEPTIE la incercarea de convertire a ID-ului solicitantului ce are ca valoare [ " + 
				solicitantMetadataValue + "], insa pentru acest document cu atributele: " + documentLogAttributes + ", valoarea NU este corecta.";
    		LOGGER.error(logMessage, e, "saveOrUpdateConcediuForCerere", "incercarea de parsare a ID-ului solicitantului din String in Long", userSecurity);
		
    		throw new AppException();
    	}
    	
    	try {
    		dataInceput = MetadataValueHelper.getDate(dataInceputMetadataValue);
    	} catch (Exception e) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
    		
    		String logMessage = "A aparut o EXCEPTIE la incercarea de parsare a datei de inceput, aceasta avand valoarea " +
				"[" + dataInceputMetadataValue + "], pentru documentul cu atributele: " + documentLogAttributes + ".";
    		LOGGER.error(logMessage, e, "saveOrUpdateConcediuForCerere", "incercarea de parsare a datei de inceput din String in Date", userSecurity);
    		
    		throw new AppException();
    	}

    	try {
    		dataSfarsit = MetadataValueHelper.getDate(dataSfarsitMetadataValue);    		
    	} catch (Exception e) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
    		
    		String logMessage = "A aparut o EXCEPTIE la incercare de parsare a datei de sfarsit, aceasta avand valoarea " +
				"[ " + dataSfarsitMetadataValue + "], pentru documentul cu atributele: " + documentLogAttributes + ".";
    		LOGGER.error(logMessage, e, "saveOrUpdateConcediuForCerere", "incercarea de parsare a datei de sfarsit din String in Date", userSecurity);
    		
    		throw new AppException();
    	}
    	
    	String aprobareDecisivaMetadataNeededValue = metadateCerereDeConcediu.getAprobareDecisivaMetadataPositiveValue();
    	boolean isCerereAprobata = StringUtils.equals(aprobareDecisivaMetadataValue, aprobareDecisivaMetadataNeededValue);

    	Concediu concediu = new Concediu();
   	
   		concediu.setDocumentId(documentId);
   		concediu.setSolicitantId(solicitantId);
   		concediu.setDataInceput(dataInceput);
   		concediu.setDataSfarsit(dataSfarsit);
   		concediu.setCerereAprobata(isCerereAprobata);
    	
   		concediuDao.save(concediu);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteConcediuForCerere(String documentId) {
		Concediu concediu = concediuDao.getForDocument(documentId);
		if (concediu != null) {
			pontajForConcediiService.deletePendingPontajeForConcediu(concediu);
			concediuDao.deleteByDocumentId(documentId);
		}
	}
	
	@Override
	public Concediu getConcediuForCerere(Document document) {
		return concediuDao.getForDocument(document.getId());
	}
	
	@Override
	public boolean isCerereRespinsa(Document document) {
		
		Long documentTypeId = document.getDocumentTypeId();		
		CerereDeConcediuConstants constantsForCerere = cereriDeConcediuConstants.getFor(documentTypeId);
		
		for (MetadataInstance metadata : document.getMetadataInstanceList()) {
			if (metadata.getMetadataDefinitionId().equals(constantsForCerere.getAprobareDecisivaMetadataId())) {
				String value = metadata.getValue();
				if ((value != null) && value.equals(constantsForCerere.getAprobareDecisivaMetadataNegativeValue())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isCerereAnulata(Document document) {
		
		Long documentTypeId = document.getDocumentTypeId();		
		CerereDeConcediuConstants constantsForCerere = cereriDeConcediuConstants.getFor(documentTypeId);
		
		for (MetadataInstance metadata : document.getMetadataInstanceList()) {
			if (metadata.getMetadataDefinitionId().equals(constantsForCerere.getAnulataMetadataId())) {
				String value = metadata.getValue();
				if ((value != null) && value.equals(constantsForCerere.getAnulataMetadataPositiveValue())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Collection<Long> getIdsForUsersInConcediu(Date dataInceput, Date dataSfarsit) {
		// Iau ID-urile de utilizatori extrase din cererile de concediu.
		Collection<Long> idsForUsersInConcediu = concediuDao.getSolicitantsIdsByPeriod(dataInceput, dataSfarsit);
		// Iau ID-urile de utilizatori pentru persoanele care au concediu (o persoana poate avea mai multe posturi, deci mai multe ID-uri).
		Collection<Long> idsForUsersInConcediuNormalizedByUsername = userPersistencePlugin.getIdsForUsersMatchedByUsernameInLowercase(idsForUsersInConcediu);
		return idsForUsersInConcediuNormalizedByUsername;
	}
	
	@Override
	public void updateCereriWithMissingMetadataValueForAnulare() throws AppException {
		
		Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId = cereriDeConcediuConstants.getConstantsByDocumentTypeId();
		Map<Long, Map<Long, String>> metadataValueByMetadataIdByDocumentTypeId = Maps.newHashMap();
		
		for (Entry<Long, CerereDeConcediuConstants> constantsByDocumentTypeIdEntry : constantsByDocumentTypeId.entrySet()) {
			
			Long documentTypeId = constantsByDocumentTypeIdEntry.getKey();
			CerereDeConcediuConstants constants = constantsByDocumentTypeIdEntry.getValue();
			
			Map<Long, String> metadataValueByMetadataId = ImmutableMap.<Long, String> builder()
				.put(constants.getAnulataMetadataId(), constants.getAnulataMetadataNegativeValue())
				.build();
			metadataValueByMetadataIdByDocumentTypeId.put(documentTypeId, metadataValueByMetadataId);
		}
		// COMENTAT MIGRARE JR
//		documentService.setMetadataValues(metadataValueByMetadataIdByDocumentTypeId);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void extractAndUpdateConcediiFromCereri() throws AppException {
		
		Multimap<Long, Long> neededMetadataIdsByDocumentTypeId = HashMultimap.create();
		Map<Long, Map<Long, String>> usedForExclusionMetadataValueByMetadataIdByDocumentTypeId = Maps.newHashMap();
		
		Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId = cereriDeConcediuConstants.getConstantsByDocumentTypeId();
		for (Entry<Long, CerereDeConcediuConstants> constantsByDocumentTypeIdEntry : constantsByDocumentTypeId.entrySet()) {
			
			Long documentTypeId = constantsByDocumentTypeIdEntry.getKey();
			CerereDeConcediuConstants constants = constantsByDocumentTypeIdEntry.getValue();
			
			Long solicitantMetadataId = constants.getSolicitantMetadataId();
			Long dataInceputMetadataId = constants.getDataInceputMetadataId();
			Long dataSfarsitMetadataId = constants.getDataSfarsitMetadataId();
			Long aprobareDecisivaMetadataId = constants.getAprobareDecisivaMetadataId();
			
			Collection<Long> neededMetadataIds = ImmutableSet.of(
				solicitantMetadataId,
				dataInceputMetadataId,
				dataSfarsitMetadataId,
				aprobareDecisivaMetadataId
			);
			neededMetadataIdsByDocumentTypeId.putAll(documentTypeId, neededMetadataIds);
			
			String aprobareDecisivaMetadataNegativeValue = constants.getAprobareDecisivaMetadataNegativeValue();
			
			Long anulataMetadataId = constants.getAnulataMetadataId();
			String anulataMetadataPositiveValue = constants.getAnulataMetadataPositiveValue();
			
			Map<Long, String> usedForExclusionMetadataValueByMetadataId = ImmutableMap.<Long, String> builder()
				.put(aprobareDecisivaMetadataId, aprobareDecisivaMetadataNegativeValue)
				.put(anulataMetadataId, anulataMetadataPositiveValue)
				.build();
			usedForExclusionMetadataValueByMetadataIdByDocumentTypeId.put(documentTypeId, usedForExclusionMetadataValueByMetadataId);
		}
		
		// COMENTAT MIGRARE JR
		/*
		Map<Long, Map<String, Map<Long, String>>> metadataValueByMetadataIdByDocumentIdByDocumentTypeId =
			documentService.getMetadataValueByMetadataIdByDocumentIdByDocumentTypeId(neededMetadataIdsByDocumentTypeId,
			usedForExclusionMetadataValueByMetadataIdByDocumentTypeId);
		Collection<Concediu> concediiToSave = Lists.newLinkedList();
		
		for (Entry<Long, Map<String, Map<Long, String>>> metadataValueByMetadataIdByDocumentIdByDocumentTypeIdEntry : metadataValueByMetadataIdByDocumentIdByDocumentTypeId.entrySet()) {
			
			Long documentTypeId = metadataValueByMetadataIdByDocumentIdByDocumentTypeIdEntry.getKey();
			Map<String, Map<Long, String>> metadataValueByMetadataIdByDocumentId = metadataValueByMetadataIdByDocumentIdByDocumentTypeIdEntry.getValue();
			
			CerereDeConcediuConstants constants = cereriDeConcediuConstants.getFor(documentTypeId);

			Long solicitantMetadataId = constants.getSolicitantMetadataId();
			Long dataInceputMetadataId = constants.getDataInceputMetadataId();
			Long dataSfarsitMetadataId = constants.getDataSfarsitMetadataId();
			Long aprobareDecisivaMetadataId = constants.getAprobareDecisivaMetadataId();
			
			for (Entry<String, Map<Long, String>> metadataValueByMetadataIdByDocumentIdEntry : metadataValueByMetadataIdByDocumentId.entrySet()) {
				
				String documentId = metadataValueByMetadataIdByDocumentIdEntry.getKey();
				Map<Long, String> metadataValueByMetadataId = metadataValueByMetadataIdByDocumentIdEntry.getValue();
				
				String solicitantMetadataValue = metadataValueByMetadataId.get(solicitantMetadataId);
				Long solicitantUserId = MetadataValueHelper.getUserId(solicitantMetadataValue);
				
				String dataInceputMetadataValue = metadataValueByMetadataId.get(dataInceputMetadataId);
				Date dataInceput = MetadataValueHelper.getDate(dataInceputMetadataValue);
				
				String dataSfarsitMetadataValue = metadataValueByMetadataId.get(dataSfarsitMetadataId);
				Date dataSfarsit = MetadataValueHelper.getDate(dataSfarsitMetadataValue);
				
				if ((solicitantUserId == null) || (dataInceput == null) || (dataSfarsit == null)) {
					
					String logMessage = "Cererea de concediu reprezentata prin documentul cu ID-ul [" + documentId + "] " +
						"NU are completate solicitantul, data inceput si / sau data sfarsit. ID solicitant: " +
						"[" + solicitantUserId + "], data inceput: [" + LogHelper.formatDateForLog(dataInceput) + "], " +
						"data sfarsit: [" + LogHelper.formatDateForLog(dataSfarsit) + "].";
					LOGGER.warn(logMessage, "extragerea concediilor din cereri");
					
					continue;
				}
				
				String aprobareDecisivaMetadataValue = metadataValueByMetadataId.get(aprobareDecisivaMetadataId);
				String aprobareDecisivaMetadataNeededValue = constants.getAprobareDecisivaMetadataPositiveValue();
		    	boolean isCerereAprobata = StringUtils.equals(aprobareDecisivaMetadataValue, aprobareDecisivaMetadataNeededValue);
				
				Concediu concediu = new Concediu();
				
				concediu.setDocumentId(documentId);
				concediu.setSolicitantId(solicitantUserId);
				concediu.setDataInceput(dataInceput);
				concediu.setDataSfarsit(dataSfarsit);
				concediu.setCerereAprobata(isCerereAprobata);
				
				concediiToSave.add(concediu);
			}
		}
		
		concediuDao.saveAll(concediiToSave);
		*/
	}

	public void setCereriDeConcediuConstants(CereriDeConcediuConstants cereriDeConcediuConstants) {
		this.cereriDeConcediuConstants = cereriDeConcediuConstants;
	}
	public void setTimesheetsForLeavesIntegrationEnabled(boolean timesheetsForLeavesIntegrationEnabled) {
		this.timesheetsForLeavesIntegrationEnabled = timesheetsForLeavesIntegrationEnabled;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setPontajForConcediiService(PontajForConcediiService pontajForConcediiService) {
		this.pontajForConcediiService = pontajForConcediiService;
	}
	public void setConcediuDao(ConcediuDao concediuDao) {
		this.concediuDao = concediuDao;
	}
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
}