package ro.cloudSoft.cloudDoc.domain.validators;

import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.INTERMEDIATE_STATE_MUST_HAVE_AT_LEAST_ONE_ARRIVING_TRANSITION;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.INTERMEDIATE_STATE_MUST_HAVE_AT_LEAST_ONE_LEAVING_TRANSITION;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.METADATA_IN_ROUTING_CONDITION_DOES_NOT_EXIST_IN_DOCUMENT_TYPES;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.START_STATE_CANNOT_HAVE_ARRIVING_TRANSITIONS;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.START_STATE_CANNOT_HAVE_MORE_THAN_ONE_LEAVING_TRANSITIONS;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.START_STATE_MUST_HAVE_A_LEAVING_TRANSITION;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.STOP_STATE_CANNOT_HAVE_LEAVING_TRANSITIONS;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.STOP_STATE_MUST_HAVE_AT_LEAST_ONE_ARRIVING_TRANSITION;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.TRANSITION_NAMES_MUST_BE_UNIQUE;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.WORKFLOW_CANNOT_HAVE_MORE_THAN_ONE_START_STATE;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.WORKFLOW_HAS_NO_ASSOCIATED_DOCUMENT_TYPES;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.WORKFLOW_MUST_HAVE_AT_LEAST_ONE_STOP_STATE;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.WORKFLOW_MUST_HAVE_A_START_STATE;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.TextWithPlaceholdersHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

/**
 * Valideaza un flux.
 * 
 * 
 */
public class WorkflowValidator {

	private final DocumentTypeService documentTypeService;
	private final Workflow workflow;
	
	private Set<WorkflowState> startStates = Sets.newHashSet();
	private Set<WorkflowState> intermediateStates = Sets.newHashSet();
	private Set<WorkflowState> stopStates = Sets.newHashSet();
	
	private SetMultimap<WorkflowState, WorkflowTransition> transitionByInitialState = HashMultimap.create();
	private SetMultimap<WorkflowState, WorkflowTransition> transitionByFinalState = HashMultimap.create();
	
	private SetMultimap<Long, String> metadataNamesByDocumentTypeId = HashMultimap.create();
	private Set<String> transitionRoutingConditions = Sets.newHashSet();
	
	public WorkflowValidator(DocumentTypeService documentTypeService, Workflow workflow) {
		this.documentTypeService = documentTypeService;
		this.workflow = workflow;
	}
	
	/**
	 * Valideaza fluxul.
	 * Va arunca exceptie (<tt>ApplicationException</tt>) daca exista probleme de validare.
	 */
	public void validate() throws AppException {
		
		buildValidationStructures();
		
		validateStatesCount();
		validateTransitionEndpoints();
		validateTransitionRoutingConditions();
		validateStateClassPath();
	}
	
	/** Construieste structurile necesare validatorului, pe baza fluxului dat. */
	private void buildValidationStructures() throws AppException {
		
		Set<String> uniqueTransitionNames = Sets.newHashSet();
		
		for (WorkflowTransition transition : workflow.getTransitions()) {
			
			String currentTransitionName = transition.getName();			
			if (uniqueTransitionNames.contains(currentTransitionName)) {
				throw new AppException(TRANSITION_NAMES_MUST_BE_UNIQUE);
			} else {
				uniqueTransitionNames.add(currentTransitionName);
			}
			
			WorkflowState initialState = transition.getStartState();
			addState(initialState);
			transitionByInitialState.put(initialState, transition);
			
			WorkflowState finalState = transition.getFinalState();
			addState(finalState);
			transitionByFinalState.put(finalState, transition);
			
			String transitionRoutingCondition = transition.getRoutingCondition();
			if (StringUtils.isNotEmpty(transitionRoutingCondition)) {
				transitionRoutingConditions.add(transitionRoutingCondition);
			}
		}
		
		if (workflow.getDocumentTypes().isEmpty()) {
			throw new AppException(WORKFLOW_HAS_NO_ASSOCIATED_DOCUMENT_TYPES);
		}
		
		Set<Long> documentTypeIds = Sets.newHashSet();
		for (DocumentType documentType : workflow.getDocumentTypes()) {
			documentTypeIds.add(documentType.getId());
		}
		
		metadataNamesByDocumentTypeId = documentTypeService.getMetadataNamesByDocumentTypeId(documentTypeIds);
	}
	
	/** Adauga o stare in seturile validatorului in functie de tipul sau. */
	private void addState(WorkflowState state) {
		switch (state.getStateType()) {
			case WorkflowState.STATETYPE_START:
				startStates.add(state);
				break;
			case WorkflowState.STATETYPE_INTERMEDIATE:
				intermediateStates.add(state);
				break;
			case WorkflowState.STATETYPE_STOP:
				stopStates.add(state);
				break;
		}
	}
	
	/** Valideaza numarul starilor de pe flux. */
	private void validateStatesCount() throws AppException {
		
		if (startStates.isEmpty()) {
			throw new AppException(WORKFLOW_MUST_HAVE_A_START_STATE);
		}
		if (startStates.size() > 1) {
			throw new AppException(WORKFLOW_CANNOT_HAVE_MORE_THAN_ONE_START_STATE);
		}
		
		if (stopStates.isEmpty()) {
			throw new AppException(WORKFLOW_MUST_HAVE_AT_LEAST_ONE_STOP_STATE);
		}
	}
	
	private void validateStateClassPath() throws AppException {
		for (WorkflowState intermediateState : intermediateStates) {
			
			if (StringUtils.isBlank(intermediateState.getClassPath())) {
				continue;
			}
			
			try {
				Class<?> clazz = Class.forName(intermediateState.getClassPath());
				if (!AutomaticTask.class.isAssignableFrom(clazz)) {
					throw new AppException(AppExceptionCodes.INTERMEDIATE_STATE_CLASS_EXTENDS_THE_WRONG_CLASS);
				}
			} catch (ClassNotFoundException e) {
				throw new AppException(AppExceptionCodes.INTERMEDIATE_STATE_CLASS_NOT_FOUND);
			}
		}
	}
	
	/** Verifica daca starile au tranzitii corespunzatoare tipului fiecareia. */
	private void validateTransitionEndpoints() throws AppException {
		
		for (WorkflowState startState : startStates) {
			
			Set<WorkflowTransition> leavingTransitions = transitionByInitialState.get(startState);
			if (leavingTransitions.isEmpty()) {
				throw new AppException(START_STATE_MUST_HAVE_A_LEAVING_TRANSITION);
			}
			if (leavingTransitions.size() > 1) {
				throw new AppException(START_STATE_CANNOT_HAVE_MORE_THAN_ONE_LEAVING_TRANSITIONS);
			}
			
			Set<WorkflowTransition> arrivingTransitions = transitionByFinalState.get(startState);
			if (!arrivingTransitions.isEmpty()) {
				throw new AppException(START_STATE_CANNOT_HAVE_ARRIVING_TRANSITIONS);
			}
		}
		
		for (WorkflowState intermediateState : intermediateStates) {
			
			Set<WorkflowTransition> leavingTransitions = transitionByInitialState.get(intermediateState);
			if (leavingTransitions.isEmpty()) {
				throw new AppException(INTERMEDIATE_STATE_MUST_HAVE_AT_LEAST_ONE_LEAVING_TRANSITION);
			}
				
			Set<WorkflowTransition> arrivingTransitions = transitionByFinalState.get(intermediateState);
			if (arrivingTransitions.isEmpty()) {
				throw new AppException(INTERMEDIATE_STATE_MUST_HAVE_AT_LEAST_ONE_ARRIVING_TRANSITION);
			}
		}
		
		for (WorkflowState stopState : stopStates) {
			
			Set<WorkflowTransition> leavingTransitions = transitionByInitialState.get(stopState);
			if (!leavingTransitions.isEmpty()) {
				throw new AppException(STOP_STATE_CANNOT_HAVE_LEAVING_TRANSITIONS);
			}
				
			Set<WorkflowTransition> arrivingTransitions = transitionByFinalState.get(stopState);
			if (arrivingTransitions.isEmpty()) {
				throw new AppException(STOP_STATE_MUST_HAVE_AT_LEAST_ONE_ARRIVING_TRANSITION);
			}
		}
	}
	
	/** Valideaza conditiile de rutare ale tranzitiilor. */
	private void validateTransitionRoutingConditions() throws AppException {
		
		Set<String> allMetadataNamesInAllConditions = Sets.newHashSet();		
		for (String transitionRoutingCondition : transitionRoutingConditions) {
			Set<String> metadataNamesInCondition = TextWithPlaceholdersHelper.getPlaceholderNames(transitionRoutingCondition);
			allMetadataNamesInAllConditions.addAll(metadataNamesInCondition);
		}
		
		for (Long documentTypeId : metadataNamesByDocumentTypeId.keySet()) {
			Set<String> metadataNamesForDocumentType = metadataNamesByDocumentTypeId.get(documentTypeId);
			if (!metadataNamesForDocumentType.containsAll(allMetadataNamesInAllConditions)) {
				throw new AppException(METADATA_IN_ROUTING_CONDITION_DOES_NOT_EXIST_IN_DOCUMENT_TYPES);
			}
		}
	}
}