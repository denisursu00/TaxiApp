package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.history.HistoryTaskQuery;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DocumentWorkflowHistoryJbpmPlugin implements DocumentWorkflowHistoryPlugin, InitializingBean {
	
	private WorkflowInstanceDao workflowInstanceDao;	
	private ProcessEngine processEngine;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowInstanceDao,
			processEngine
		);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public List<DocumentHistory> getDocumentHistory(String documentId) throws AppException {
		List<WorkflowInstance> wiList=workflowInstanceDao.findWorkflowInstances(documentId);
		
		
		List<DocumentHistory> dhList=new ArrayList<DocumentHistory>();
		
		for(WorkflowInstance wi:wiList)
		{
			
		List<HistoryActivityInstance> histActInstList=(List<HistoryActivityInstance>) processEngine.getHistoryService().
																 createHistoryActivityInstanceQuery().
																 executionId(wi.getProcessInstanceId()).orderAsc("startTime").
														         list();

		List<HistoryTask> histTaskList=processEngine.getHistoryService().createHistoryTaskQuery().
														executionId(wi.getProcessInstanceId()).orderAsc("createTime").list();
		//in tabul de istoric se vor afisa intotdeauna doar taskurile care au fost realizate
		//daca taskul a ajuns la un utilizator dar acesta nu l-a indeplinit, nu se va afisa in istoric
		if(histActInstList.size()>0)
			//daca exista date in istoric
		{ 
				//se ia numele primei activitati din tabela de istoric (ordonata crescator dupa data crearii)si se cauta tranzitia care vine in aceasta activitate; 
				//aceasta este prima tranzitie a fluxului
				String processInstanceId = wi.getProcessInstanceId();
				String finalStateCodeForSecondState = histActInstList.get(0).getActivityName();
				
				String firstTransitionName = workflowInstanceDao.getFirstTransitionName(processInstanceId, finalStateCodeForSecondState);
				Long initiatorUserId = workflowInstanceDao.getInitiatorUserId(processInstanceId);
				
				DocumentHistory dh0=new DocumentHistory();
				dh0.setTransitionName(firstTransitionName);
				dh0.setActorId(initiatorUserId);
				
				//Data primei tranzitii este data crearii primei activitati din tabela de istoric;
				//daca fluxul este in a doua activitate (s-a dat primul send) aceasta data este null
				if(histActInstList.get(0).getStartTime()!=null)
				{
					Calendar transitionDate=Calendar.getInstance();
					transitionDate.setTime(histActInstList.get(0).getStartTime());
					dh0.setTransitionDate(transitionDate);
				}
				dhList.add(dh0);
			for(HistoryTask histTask:histTaskList)
			{
			   if(histTask.getEndTime()!=null)
				   //se afiseaza in istoric doar taskurie indeplinite (endTime!=null)
			   {
					DocumentHistory dh=new DocumentHistory();
					if(histTask.getAssignee()!=null)
					dh.setActorId(Long.parseLong(histTask.getAssignee()));
					else
						throw new AppException(AppExceptionCodes.YOUR_WORKFLOW_HAS_NO_ASSIGNEE);
					//exceptand prima tranzitie, pentru celelalte, data crearii este data de sfarsit a activitatii
					Calendar c=Calendar.getInstance();
					if(histTask.getEndTime()!=null)
					{
						c.setTime(histTask.getEndTime());
						dh.setTransitionDate(c);
					}
					if (histTask.getOutcome()!=null)
					{
						dh.setTransitionName(histTask.getOutcome());
					}
					else 
							dh.setTransitionName("");
					dhList.add(dh);
			   	}
			}
		}
	}
		return dhList;
	}
	
	@Override
	public WorkflowState getStopStateOfInstance(WorkflowInstance workflowInstance, Map<String, WorkflowState> finalStateOfTypeStopByTransitionName) {
		List<HistoryTask> historyTasks = processEngine.getHistoryService().createHistoryTaskQuery()
			.executionId(workflowInstance.getProcessInstanceId())
			.list();
		for (HistoryTask historyTask : historyTasks) {
			String transitionName = historyTask.getOutcome();
			if (finalStateOfTypeStopByTransitionName.containsKey(transitionName)) {
				return finalStateOfTypeStopByTransitionName.get(transitionName);
			}
		}
		return null;
	}
	
	@Override
	public String getPreviouslyAssignedAssignee(WorkflowInstance workflowInstance) {
		
		// Lista contine, in ordine: asignarea curenta, precedentul, altii.
		List<HistoryTask> tasksOrderedByDateDescending = processEngine.getHistoryService().createHistoryTaskQuery()
			.executionId(workflowInstance.getProcessInstanceId())
			.orderDesc(HistoryTaskQuery.PROPERTY_CREATETIME)
			.list();
		
		if (tasksOrderedByDateDescending.size() < 2) {
			// Lista contine doar asignarea curenta, nu si precedentul.
			return null;
		}
		
		HistoryTask taskOfPreviouslyAssigned = tasksOrderedByDateDescending.get(1);
		return taskOfPreviouslyAssigned.getAssignee();
	}
	
	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}
	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
}