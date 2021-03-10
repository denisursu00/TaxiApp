package ro.cloudSoft.cloudDoc.builders.content.search;

import java.util.LinkedHashMap;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;

import com.google.common.collect.Maps;

public class DocumentAdvancedSearchResultsViewsWrapperBuilder {

	private final DocumentType documentType;
	private final DocumentAdvancedSearchResultsViewsBuilder searchResultsViewsBuilder;
	
	public DocumentAdvancedSearchResultsViewsWrapperBuilder(DocumentType documentType, List<Document> documents, SecurityManager userSecurity,
			WorkflowService workflowService, WorkflowExecutionService workflowExecutionService, UserService userService, DocumentService documentService, 
			NomenclatorService nomenclatorService, GroupService groupService, CalendarService calendarService, ProjectService projectService) {
		
		this.documentType = documentType;
		this.searchResultsViewsBuilder = new DocumentAdvancedSearchResultsViewsBuilder(documentType, documents, userSecurity, workflowService, workflowExecutionService, userService, documentService, nomenclatorService, groupService, calendarService, projectService);
	}
	
	public DocumentAdvancedSearchResultsViewsWrapper build() throws AppException {
		
		DocumentAdvancedSearchResultsViewsWrapper wrapper = new DocumentAdvancedSearchResultsViewsWrapper();
		
		LinkedHashMap<Long, String> representativeMetadataDefinitionLabelById = buildRepresentativeMetadataDefinitionLabelById();
		wrapper.setRepresentativeMetadataDefinitionLabelById(representativeMetadataDefinitionLabelById);
		
		List<DocumentAdvancedSearchResultsView> searchResultsViews = searchResultsViewsBuilder.buildViews();
		wrapper.setSearchResultsViews(searchResultsViews);
		
		return wrapper;
	}
	
	private LinkedHashMap<Long, String> buildRepresentativeMetadataDefinitionLabelById() {
		LinkedHashMap<Long, String> representativeMetadataDefinitionLabelById = Maps.newLinkedHashMap();
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getRepresentative()) {
				representativeMetadataDefinitionLabelById.put(metadataDefinition.getId(), metadataDefinition.getLabel());
			}
		}
		return representativeMetadataDefinitionLabelById;
	}
}