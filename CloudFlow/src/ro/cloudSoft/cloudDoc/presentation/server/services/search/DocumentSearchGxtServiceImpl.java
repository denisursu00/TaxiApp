package ro.cloudSoft.cloudDoc.presentation.server.services.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.search.DocumentSearchGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentSearchCriteriaConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.search.DocumentAdvancedSearchResultsViewsWrapperConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.search.DocumentSimpleSearchResultsViewConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.content.SearchService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Sets;

public class DocumentSearchGxtServiceImpl extends GxtServiceImplBase implements DocumentSearchGxtService, InitializingBean {
	
	private SearchService searchService;
	private DocumentTypeService documentTypeService;
	private UserService userService;
	private WorkflowService workflowService;
	private WorkflowExecutionService workflowExecutionService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			searchService,
			documentTypeService,
			userService,
			workflowService,
			workflowExecutionService
		);
	}
	
	@Override
	public List<DocumentSimpleSearchResultsViewModel> findDocumentsUsingSimpleSearch(
			DocumentSearchCriteriaModel documentSearchCriteriaModel)
			throws PresentationException {
		
		DocumentSearchCriteria documentSearchCriteria = DocumentSearchCriteriaConverter.getDocumentSearchCriteriaFromModel(documentSearchCriteriaModel);
		
		List<DocumentSimpleSearchResultsView> searchResultsViews = null;
		try {
			searchResultsViews = searchService.findDocumentsUsingSimpleSearch(documentSearchCriteria, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		List<DocumentSimpleSearchResultsViewModel> searchResultsViewModels = DocumentSimpleSearchResultsViewConverter.getModels(searchResultsViews);
		return searchResultsViewModels;
	}
	
	@Override
	public DocumentAdvancedSearchResultsViewsWrapperModel findDocumentsUsingAdvancedSearch(
			DocumentSearchCriteriaModel documentSearchCriteriaModel)
			throws PresentationException {
		
		DocumentSearchCriteria documentSearchCriteria = DocumentSearchCriteriaConverter.getDocumentSearchCriteriaFromModel(documentSearchCriteriaModel);
		
		DocumentAdvancedSearchResultsViewsWrapper searchResultsViewsWrapper = null;
		try {
			searchResultsViewsWrapper = searchService.findDocumentsUsingAdvancedSearch(documentSearchCriteria, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		DocumentAdvancedSearchResultsViewsWrapperModel searchResultsViewsWrapperModel = DocumentAdvancedSearchResultsViewsWrapperConverter.getModel(searchResultsViewsWrapper);
		return searchResultsViewsWrapperModel;
	}
	
	@Override
	public List<MyActivitiesViewModel> getMyActivites() throws PresentationException {
		
		List<MyActivitiesViewModel> result = new ArrayList<MyActivitiesViewModel>();
		try {
			List<TaskInstance> currentTasks = workflowExecutionService.getCurrentTasks(getSecurity());
			if (currentTasks != null){
				
				Set<Long> documentTypeIds = Sets.newHashSet();
				Set<Long> authorAndSenderUserIds = Sets.newHashSet();
				
				for (TaskInstance taskInstance : currentTasks) {
					documentTypeIds.add(taskInstance.getDocumentTypeId());
					authorAndSenderUserIds.add(taskInstance.getDocumentAuthorId());
					authorAndSenderUserIds.add(taskInstance.getSenderUserId());
				}
				
				Map<Long, String> documentTypeNames = documentTypeService.getDocumentTypesNameMap(documentTypeIds);
				Map<Long, String> authorAndSenderUserNamesById = userService.getUsersNameMap(authorAndSenderUserIds, getSecurity());
				
				currentTasks.sort(Comparator. comparing(TaskInstance::getDocumentCreatedDate, Comparator.reverseOrder()));
				
				for (TaskInstance taskInstance : currentTasks) {
					
					MyActivitiesViewModel model = new MyActivitiesViewModel();
					
					model.setDocumentId(taskInstance.getDocumentId());					
					model.setDocumentLocationRealName(taskInstance.getDocumentLocationRealName());
					model.setDocumentName(taskInstance.getDocumentName());
					model.setWorkflowSender(authorAndSenderUserNamesById.get(taskInstance.getSenderUserId()));
					model.setWorkflowCurrentStatus(taskInstance.getState());
					model.setWorkflowName(taskInstance.getWorkflow().getName());
					model.setDocumentTypeName(documentTypeNames.get(taskInstance.getDocumentTypeId()));
					model.setDocumentCreatedDate(taskInstance.getDocumentCreatedDate());					
					model.setDocumentAuthor(authorAndSenderUserNamesById.get(taskInstance.getDocumentAuthorId()));
					
					result.add(model);
				}
			}
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		return result;
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
}