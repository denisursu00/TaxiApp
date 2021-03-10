package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.EndStateStartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.StartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.StateStartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.TransitionEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableNames;
import ro.cloudSoft.cloudDoc.utils.log.LogConstants;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * @deprecated Am inlocuit generatorul cu un template bazat pe XML-ul rezultat care este MULT mai usor de urmarit.
 * De asemenea, generatorul acesta avea un bug (vezi Mantis 0003026) prin care nu tinea cont de tranzitii pentru asignarea unui task.
 */
@Deprecated
public class Generator implements InitializingBean {
	
	private static final LogHelper log = LogHelper.getInstance(Generator.class);

	private Map<String, String> assigmentHandlerClassesMap;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		DependencyInjectionUtils.checkRequiredDependencies(
			assigmentHandlerClassesMap
		);
		
		for (String className : assigmentHandlerClassesMap.values()) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException cnfe) {
				throw new IllegalStateException("Nu exista clasa [" + className + "], necesara pentru handler-ele de asignare.", cnfe);
			}
		}
	}
	
	public byte[] createProcessDefinition(Workflow workflow, SecurityManager securityManager) throws AppException {
		byte[] result = null;

		if (workflow == null || workflow.getTransitions() == null) {
			log.error("The workflow is null or has no transitions defined", LogConstants.MODULE_BPM, "", securityManager);
			throw new AppException(AppExceptionCodes.WORKFLOW_NULL_OR_NO_TRANSITION_ADDED);
		}

		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dom = dbf.newDocumentBuilder();
			Document document = dom.newDocument();
			HashMap<String, Element> tasksMap = new HashMap<String, Element>();
			Set<String> codesForStatesWithManualAssignment = new HashSet<String>();
			
			Element rootElement = document.createElement("process");
			rootElement.setAttribute("name", workflow.getName());
			rootElement.setAttribute("xmlns", "http://jbpm.org/4.0/jpdl");

			createTaskNodes(workflow.getTransitions(), tasksMap, codesForStatesWithManualAssignment, document, rootElement);
			createTransitions(workflow.getTransitions(), tasksMap, document, rootElement);

			/*
			 * Pun un eveniment de start in definitia procesului. Acest eveniment se
			 * executa primul.
			 */
			Element onStartEventElement = document.createElement("on");
			onStartEventElement.setAttribute("event", "start");
			Element eventListenerElement = document.createElement("event-listener");
			eventListenerElement.setAttribute("class", StartEventListener.class.getCanonicalName());
			onStartEventElement.appendChild(eventListenerElement);
			
			Element fieldElement = document.createElement("field");
			fieldElement.setAttribute("name", "manualAssignmentVariables");
			eventListenerElement.appendChild(fieldElement);
			
			Element listElement = document.createElement("list");
			fieldElement.appendChild(listElement);
			for (String var : codesForStatesWithManualAssignment) {
				Element stringElement = document.createElement("string");
				stringElement.setAttribute("value", var);
				listElement.appendChild(stringElement);
			}
			rootElement.insertBefore(onStartEventElement, rootElement.getFirstChild());
			
			document.appendChild(rootElement);

			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(byteArrayOutputStream,
					format);
			serializer.serialize(document);
			result = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		} catch (Exception e) {
			log.debug(e.getMessage(), LogConstants.MODULE_BPM, "Generator.createProcessDefinition", securityManager);
			throw new AppException();
		}

		if (log.isDebugEnabled()) {
			log.debug("S-a generat urmatorul XML: " + new String(result),
				"generarea XML-ului pt. definitia de proces jBPM");
		}
		
		return result;
	}

	private void createTransitions(Set<WorkflowTransition> transitions,
			HashMap<String, Element> tasksMap, Document document,
			Element rootElement) {
		Iterator<WorkflowTransition> iterator = transitions.iterator();
		WorkflowTransition transition;
		Element startElement;
		Element transitionElement;

		while (iterator.hasNext()) {
			transition = iterator.next();
			
			if ( transition.getStartState().getStateType().intValue() != WorkflowState.STATETYPE_START ) {
				
				transitionElement = createTransitionElement(document, transition);
				
				/*
				if ( transition.getRoutingType() != null && !transition.getRoutingType().equals(""))
				{
					createScriptRoutingType(document, transition, transitionElement);
					createScriptRoutingDestinationId(document, transition,
							transitionElement);
				}
				*/
				
				/*
				if ( transition.getRoutingType() != null && !transition.getRoutingType().equals(""))
				{
					createScriptRoutingDestinationId(document, transition,
							transitionElement);
				}
				*/
	
				/*if (transition.isNotifyDestination()
						|| (transition.getInfoRecipients() != null && transition
								.getInfoRecipients().size() > 0)) {
					createMailElement(document, transition, transitionElement);
				}*/
	
				startElement = tasksMap.get(transition.getStartState().getCode());
				startElement.appendChild(transitionElement);
				
				if ((transition.getRoutingCondition() != null) && (transition.getRoutingCondition().length() > 0)) {
					addCondition(document, transition, startElement);
				}
			}
		}
	}
	
	private void createTaskNodes(Set<WorkflowTransition> transitions,
			HashMap<String, Element> tasksMap, Set<String> codesForStatesWithManualAssignment, Document document,
			Element rootElement) throws AppException {
		Iterator<WorkflowTransition> iterator = transitions.iterator();
		WorkflowTransition transition;

		Element startElement = null;
		
		Map<String, WorkflowState> finalStateMap = new HashMap<String, WorkflowState>();
		String firstTaskCode = null;
		
		WorkflowTransition firstTransition = null;

		while (iterator.hasNext()) 
		{
			transition = iterator.next();

			if (transition.getFinalState().getStateType() != WorkflowState.STATETYPE_STOP ) 
			{
				addTask(tasksMap, codesForStatesWithManualAssignment, document, transition);
			}
			
			/*
			if (transition.getFinalState().getStateType() != WorkflowState.STATETYPE_STOP) 
			{
				addTask(tasksMap, document, transition.getFinalState());
			}
			*/
			
			if (transition.getStartState().getStateType() == WorkflowState.STATETYPE_START) {
				firstTaskCode = transition.getFinalState().getCode();
				firstTransition = transition;
			}
			

			if (transition.getFinalState().getStateType() == WorkflowState.STATETYPE_STOP) {
				finalStateMap.put(transition.getFinalState().getCode(), transition.getFinalState());
			}
		}

		if (firstTaskCode == null || finalStateMap.size() == 0) {
			throw new AppException(AppExceptionCodes.NO_TRANSITION_START_STOP_STATE_ADDED);
		}

		startElement = tasksMap.get( firstTaskCode );
		
		
		if (startElement == null || finalStateMap.size() == 0) {
			throw new AppException(AppExceptionCodes.NO_TRANSITION_START_STOP_STATE_ADDED);
		}
		
		createStartElement(document, rootElement, startElement.getAttribute("name"), firstTransition );
		
		Iterator<Element> iteratorElements = tasksMap.values().iterator();
		while (iteratorElements.hasNext()) {
			rootElement.appendChild(iteratorElements.next());
		}
		createEndElement(document, rootElement,  finalStateMap );
	}

	private void createStartElement(Document document, Element rootElement, 
			String firstElement, WorkflowTransition firstTransition) {
		Element startElement = document.createElement("start");
		startElement.setAttribute("name", firstTransition.getStartState().getCode());		

		this.addStateStartEventListener(document, startElement, firstTransition.getStartState());
		
		Element transitionElement = createTransitionElement(document, firstTransition);
		/*
		if ( firstTransition.getRoutingType() != null && !firstTransition.getRoutingType().equals(""))
		{
			createScriptRoutingType(document, firstTransition, transitionElement);
			createScriptRoutingDestinationId(document, firstTransition,	transitionElement);
		}
		*/
		startElement.appendChild(transitionElement);

		rootElement.appendChild(startElement);
	}

	private void createEndElement(Document document, Element rootElement, Map<String, WorkflowState> finalStateMap ) {
		for (String elementName : finalStateMap.keySet()) {
			Element createElement = document.createElement("end");
			createElement.setAttribute("name", elementName );
			rootElement.appendChild(createElement);
			
			this.addStateStartEventListener(document, createElement, finalStateMap.get(elementName));
		}
	}
	
	/**
	 * Creeaza un element 'transition' in definitia procesului.
	 * @param document
	 * @param transition
	 * @return {@link Element} - element 'transition' din definitia procesului
	 */
	private Element createTransitionElement(Document document, WorkflowTransition transition) {
		Element transitionElement = document.createElement("transition");
		
		transitionElement.setAttribute("to", transition.getFinalState().getCode());
		transitionElement.setAttribute("name", transition.getName());
		
		/*
		 * Adauga tranzitiei un script ce actualizeaza numele ultimei tranzitii
		 * prin care s-a trecut pe flux.
		 */
		Element scriptElement = document.createElement("script");
		
		scriptElement.setAttribute("expr", transition.getName());
		scriptElement.setAttribute("var", WorkflowVariableNames.LAST_TRANSITION_NAME);
		
		/*
		 * Adauga tranzitiei un venet-listener care va face ceva cand trece prin
		 * tranzitie, de exemplu trimiterea unui email pentru destinatari informare.
		 */
		Element listenerElement = document.createElement("event-listener");
		listenerElement.setAttribute("class", TransitionEventListener.class.getCanonicalName());
		
		Element fieldElement = document.createElement("field");
		fieldElement.setAttribute("name", "transitionId");
		listenerElement.appendChild(fieldElement);
		
		Element longElement = document.createElement("long");
		longElement.setAttribute("value", transition.getId().toString());
		fieldElement.appendChild(longElement);
		
		transitionElement.appendChild(scriptElement);
		transitionElement.appendChild(listenerElement);
		
		return transitionElement;
	}
	
	/**
	 * Adauga un element 'task' in definitia procesului.
	 * @param tasksMap
	 * @param document
	 * @param transition
	 */
	private void addTask(HashMap<String, Element> tasksMap, Set<String> codesForStatesWithManualAssignment,
			Document document, WorkflowTransition transition) {
		WorkflowState state = transition.getFinalState();
		if (tasksMap.get(state.getName()) == null) {
			// elementul task
			Element element = document.createElement("task");
			element.setAttribute("name", state.getCode());
			element.setAttribute("description", state.getName());
			
			// elementul assignment-handler
			Element assignmentHandlerElement = document.createElement("assignment-handler");
			
			if (transition.getRoutingType() != null) {
				// daca e tip de rutare
				if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_PARAMETER)) {
					assignmentHandlerElement.setAttribute("class", assigmentHandlerClassesMap.get(transition.getRoutingType()));
					
					// id-ul metadatei de tip user
					Element userMetadataDefinitionIdElement = document.createElement("field");
					userMetadataDefinitionIdElement.setAttribute("name", "userMetadataDefinition");
					
					// valoarea id-ului metadatei de tip user
					Element userMetadataDefinitionIdValueElement = document.createElement("string");
					userMetadataDefinitionIdValueElement.setAttribute("value", transition.getRoutingDestinationParameter());
					
					userMetadataDefinitionIdElement.appendChild(userMetadataDefinitionIdValueElement);
					assignmentHandlerElement.appendChild(userMetadataDefinitionIdElement);
				}else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_MANUAL)){
					codesForStatesWithManualAssignment.add(state.getCode());
					assignmentHandlerElement.setAttribute("class", assigmentHandlerClassesMap.get(transition.getRoutingType()));
				}else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_GROUP)){
					assignmentHandlerElement.setAttribute("class", assigmentHandlerClassesMap.get(transition.getRoutingType()));
					
					// id-ul grupului
					Element groupIdElement = document.createElement("field");
					groupIdElement.setAttribute("name", "groupId");
					
					// valoarea id-ului
					Element groupIdValueElement = document.createElement("string");
					groupIdValueElement.setAttribute("value", transition.getRoutingDestinationId().toString());
					
					groupIdElement.appendChild(groupIdValueElement);
					assignmentHandlerElement.appendChild(groupIdElement);
				}else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_OU)){
					assignmentHandlerElement.setAttribute("class", assigmentHandlerClassesMap.get(transition.getRoutingType()));
					
					// id-ul ou
					Element ouIdElement = document.createElement("field");
					ouIdElement.setAttribute("name", "organizationUnitId");
					
					// valoarea id-ului
					Element ouIdValueElement = document.createElement("string");
					ouIdValueElement.setAttribute("value", transition.getRoutingDestinationId().toString());
					
					ouIdElement.appendChild(ouIdValueElement);
					assignmentHandlerElement.appendChild(ouIdElement);
				}else {
					assignmentHandlerElement.setAttribute("class", assigmentHandlerClassesMap.get(transition.getRoutingType()));
				}
			}
			
			element.appendChild(assignmentHandlerElement);
			
			this.addStateStartEventListener(document, element, state);
			
			tasksMap.put(state.getCode(), element);
		}
	}
	
	private void addCondition(Document document, WorkflowTransition transition, Element taskElement) {
		Element eventElement = null;

		NodeList eventElementList = taskElement.getElementsByTagName("on");
		if (eventElementList.getLength() == 0) {
			eventElement = document.createElement("on");
			eventElement.setAttribute("event", "start");
			if (taskElement.hasChildNodes()) {
				// prioritatea o are <assignment-handler>, deci daca exista element
				// assignment-handler in <task> atunci elementul <on> il adaugam dupa el
				NodeList assignmentHandlers = taskElement.getElementsByTagName("assignment-handler");
				if (assignmentHandlers.getLength() > 0) {
					// daca mai exista si alte elemente dupa <assignment-handler> 
					// (deocamdata pentru <transiton> nu ne complicam)
					NodeList transitions = taskElement.getElementsByTagName("transition");
					if (transitions.getLength() == 0)
						taskElement.appendChild(eventElement);
					else {
						// iau primul element si inserez inaintea lui pe <on>
						taskElement.insertBefore(eventElement, transitions.item(0));
					}						
				}else
					taskElement.insertBefore(eventElement, taskElement.getFirstChild());
			} else {
				taskElement.appendChild(eventElement);
			}
		} else {
			eventElement = (Element) eventElementList.item(0);
		}
		
		Element conditionElement = document.createElement("script");
		conditionElement.setAttribute("expr", "'" + transition.getRoutingCondition() + "'");
		//conditionElement.setAttribute("var", WorkflowVariableHelper.getTransitionRoutingConditionExpressionVariableName(transition));
		eventElement.appendChild(conditionElement);
	}	
	
	/**
	 * Adauga un listener pentru evenimentul de "intrare" in starea precizata.
	 */
	private void addStateStartEventListener(Document document, Element stateElement, WorkflowState state) {
		
		Element onElement = document.createElement("on");
		onElement.setAttribute("event", "start");
		stateElement.appendChild(onElement);
		
		Element listenerElement = document.createElement("event-listener");
		Class<?> eventListenerClass = (state.getStateType().intValue() == WorkflowState.STATETYPE_STOP) ? EndStateStartEventListener.class : StateStartEventListener.class;
		listenerElement.setAttribute("class", eventListenerClass.getCanonicalName());
		onElement.appendChild(listenerElement);
		
		Element fieldElement = document.createElement("field");
		fieldElement.setAttribute("name", "stateId");
		listenerElement.appendChild(fieldElement);
		
		Element longElement = document.createElement("long");
		longElement.setAttribute("value", state.getId().toString());
		fieldElement.appendChild(longElement);
	}
		
	public void setAssigmentHandlerClassesMap(Map<String, String> assigmentHandlerClassesMap) {
		this.assigmentHandlerClassesMap = assigmentHandlerClassesMap;
	}
	public Map<String, String> getAssigmentHandlerClassesMap() {
		return assigmentHandlerClassesMap;
	}		
}