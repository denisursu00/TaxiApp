package ro.cloudSoft.cloudDoc.web.controllers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.graph.WorkflowGraphViewRepresentation;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.cloudDoc.services.bpm.DocumentWorkflowHistoryService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.utils.bpm.graph.WorkflowGraphViewGenerator;
import ro.cloudSoft.cloudDoc.webServices.client.wgvg.WorkflowGraphTrace;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class ViewWorkflowGraphController implements Controller, InitializingBean {
	
	private WorkflowService workflowService;
	private DocumentWorkflowHistoryService documentWorkflowHistoryService;
	private WorkflowGraphViewGenerator workflowGraphViewGenerator;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowService,
			documentWorkflowHistoryService,
			workflowGraphViewGenerator
		);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String documentId = request.getParameter("documentId");	
		
		String workflowIdAsString = request.getParameter("workflowId");
		if (StringUtils.isEmpty(workflowIdAsString)) {
			
			// TODO logare
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		Long workflowId = Long.valueOf(workflowIdAsString);
		
		Workflow workflow = workflowService.getWorkflowById(workflowId);
		if (workflow == null) {
			
			// TODO logare
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		
		
		String codeForCurrentState = request.getParameter("codeForCurrentState");
		if (StringUtils.isNotBlank(codeForCurrentState) && !workflowService.workflowHasState(workflowId, codeForCurrentState)) {
			
			// TODO logare
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		WorkflowGraphViewRepresentation workflowGraphView = null;
		if (StringUtils.isNotBlank(codeForCurrentState)) {
			WorkflowGraphTrace workflowGraphTrace = generateTrace(workflow, documentId, codeForCurrentState);
			workflowGraphView = workflowGraphViewGenerator.generateGraphView(workflow, workflowGraphTrace);
		} else {
			workflowGraphView = workflowGraphViewGenerator.generateGraphView(workflow);
		}
		
		response.setContentType(workflowGraphView.getMimeType());
		IOUtils.write(workflowGraphView.getContent(), response.getOutputStream());
		return null;
	}
	
	private WorkflowGraphTrace generateTrace(Workflow workflow, String documentId, String currentNodeCode) {
		
		WorkflowGraphTrace workflowGraphTrace = new WorkflowGraphTrace();
		workflowGraphTrace.setCurrentNodeCode(currentNodeCode);
		
		if (StringUtils.isBlank(documentId)) {
			return workflowGraphTrace;
		}

		LinkedList<String> transitionsCodes = new LinkedList<>();
		LinkedList<String> nodesCodes = new LinkedList<>();
		
		StringBuilder sbFinal = new StringBuilder();
		StringBuilder sbTransitions = new StringBuilder();
		StringBuilder sbNodes = new StringBuilder();
		try {
			List<DocumentHistory> history = documentWorkflowHistoryService.getDocumentHistory(documentId);
			Set<String> traceStateCodes = new HashSet<>();
			if (CollectionUtils.isNotEmpty(history)) {
				for (int i = 0; i<  history.size(); i++) {
					DocumentHistory dh = history.get(i);
					if (i > 0) {
						sbTransitions.append(",");
					}
//					sbTransitions.append(dh.getTransitionName());
					transitionsCodes.add(dh.getTransitionName());
					
					Set<WorkflowTransition> transitions = workflow.getTransitions();					
					for (WorkflowTransition wt : transitions) {
						if (dh.getTransitionName().equals(wt.getName())) {							
							if (StringUtils.isNotBlank(currentNodeCode) && !wt.getStartState().getCode().equals(currentNodeCode)) {
								nodesCodes.add(wt.getStartState().getCode());
							}
						}						
					}
				}				
				
			}
			int k = 0;
			for (String node : traceStateCodes) {
				if (k > 0) {
					sbNodes.append(",");
				}
				k++;
				sbNodes.append(node);
			}
			
			sbFinal.append(currentNodeCode + ";" + sbTransitions.toString() + ";" + sbNodes.toString());
			
		} catch (AppException e) {
			// Logare
			e.printStackTrace();
		}
		
		workflowGraphTrace.setNodesCodes(nodesCodes);
		workflowGraphTrace.setTransitionsNames(transitionsCodes);
		
		return workflowGraphTrace;
	}
	
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setWorkflowGraphViewGenerator(WorkflowGraphViewGenerator workflowGraphViewGenerator) {
		this.workflowGraphViewGenerator = workflowGraphViewGenerator;
	}	
	public void setDocumentWorkflowHistoryService(DocumentWorkflowHistoryService documentWorkflowHistoryService) {
		this.documentWorkflowHistoryService = documentWorkflowHistoryService;
	}
}