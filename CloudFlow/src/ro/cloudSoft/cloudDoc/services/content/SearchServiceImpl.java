package ro.cloudSoft.cloudDoc.services.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.builders.content.search.DocumentAdvancedSearchResultsViewsWrapperBuilder;
import ro.cloudSoft.cloudDoc.builders.content.search.DocumentSimpleSearchResultsViewsBuilder;
import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSelectionSearchResultView;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.SearchPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSelectionSearchResultViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.search.DocumentSelectionSearchFilterConverter;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class SearchServiceImpl implements SearchService, InitializingBean {
	
	private WorkflowService workflowService;
	private WorkflowExecutionService workflowExecutionService;
	private UserService userService;
	private DocumentTypeService documentTypeService;
	private DocumentService documentService;
	private NomenclatorService nomenclatorService;
	private GroupService groupService;
	private CalendarService calendarService;
	private ProjectService projectService;
	
	private SearchPlugin plugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			workflowService,
			workflowExecutionService,
			userService,
			documentTypeService,
			documentService,
			nomenclatorService,
			groupService,
			calendarService,
			projectService,
			
			plugin
		);
	}
	
	@Override
	public List<Document> findDocuments(DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity) throws AppException {
		return plugin.findDocuments(documentSearchCriteria, userSecurity);
	}
	
	@Override
	public List<DocumentSimpleSearchResultsView> findDocumentsUsingSimpleSearch(
			DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity)
			throws AppException {
		
		List<Document> documents = findDocuments(documentSearchCriteria, userSecurity);
		DocumentSimpleSearchResultsViewsBuilder viewsBuilder = new DocumentSimpleSearchResultsViewsBuilder(documents,
			userSecurity, workflowService, workflowExecutionService, userService, documentTypeService);
		return viewsBuilder.buildViews();
	}
	
	@Override
	public DocumentAdvancedSearchResultsViewsWrapper findDocumentsUsingAdvancedSearch(
			DocumentSearchCriteria documentSearchCriteria, SecurityManager userSecurity)
			throws AppException {
		
		List<Document> documents = findDocuments(documentSearchCriteria, userSecurity);

		Long documentTypeId = Iterables.getOnlyElement(documentSearchCriteria.getDocumentTypeIdList());
		DocumentType documentType = documentTypeService.getDocumentTypeById(documentTypeId);
		
		DocumentAdvancedSearchResultsViewsWrapperBuilder viewsWrapperBuilder = new DocumentAdvancedSearchResultsViewsWrapperBuilder(
			documentType, documents, userSecurity, workflowService, workflowExecutionService, userService, documentService, nomenclatorService, groupService, calendarService, projectService);
		return viewsWrapperBuilder.build();
	}
	
	@Override
	public List<DocumentSelectionSearchResultViewModel> findDocumentsForSelection(DocumentSelectionSearchFilterModel filterModel, SecurityManager userSecurity) throws AppException {
		
		List<DocumentSelectionSearchResultViewModel> resultViewModels = new ArrayList<>();
		
		DocumentSelectionSearchFilter filter = DocumentSelectionSearchFilterConverter.getFromModel(filterModel);
		List<DocumentSelectionSearchResultView> resultViews = plugin.findDocumentsForSelection(filter, userSecurity);
		if (CollectionUtils.isNotEmpty(resultViews)) {
			
			Map<String, String> userDisplayNameById = new HashMap<>();
			
			for (DocumentSelectionSearchResultView view : resultViews) {
				
				DocumentSelectionSearchResultViewModel viewModel = new DocumentSelectionSearchResultViewModel();
				viewModel.setDocumentLocationRealName(view.getDocumentLocationRealName());
				viewModel.setDocumentId(view.getDocumentId());
				viewModel.setDocumentName(view.getDocumentName());
				String authorDisplayName = userDisplayNameById.get(view.getAuthorId());
				if (authorDisplayName == null) {
					authorDisplayName = userService.getDisplayName(Long.valueOf(view.getAuthorId()));
					userDisplayNameById.put(view.getAuthorId(), authorDisplayName);
				}
				viewModel.setAuthor(authorDisplayName);
				viewModel.setCreatedDate(view.getCreatedDate());
				
				resultViewModels.add(viewModel);
			}
		}
		
		return resultViewModels;
	}
	
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}
    public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
    public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
    public void setPlugin(SearchPlugin plugin) {
        this.plugin = plugin;
    }
    public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
}