package ro.cloudSoft.cloudDoc.builders.content.search;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DocumentSimpleSearchResultsViewsBuilder extends AbstractDocumentSearchResultsViewsBuilder<DocumentSimpleSearchResultsView> {
	
	
	private final DocumentTypeService documentTypeService;
	
	
	private Set<Long> documentTypeIds;
	
	private Map<Long, String> documentTypeNameById;
	
	
	public DocumentSimpleSearchResultsViewsBuilder(List<Document> documents, SecurityManager userSecurity,
			WorkflowService workflowService, WorkflowExecutionService workflowExecutionService,
			UserService userService, DocumentTypeService documentTypeService) {
		
		super(documents, userSecurity, workflowService, workflowExecutionService, userService);
		
		this.documentTypeService = documentTypeService;
	}
	
	@Override
	protected void additionalInit() {
		
		documentTypeIds = Sets.newHashSet();
		
		documentTypeNameById = Maps.newHashMap();
	}
	
	@Override
	protected void gatherAdditionalItemsForDocument(Document document) {
		documentTypeIds.add(document.getDocumentTypeId());
	}
	
	@Override
	protected void gatherAdditionalItemsBasedOnPreviouslyGatheredOnes() {
		documentTypeNameById = documentTypeService.getDocumentTypesNameMap(documentTypeIds);
	}
	
	@Override
	protected DocumentSimpleSearchResultsView createNewViewInstance() {
		return new DocumentSimpleSearchResultsView();
	}
	
	@Override
	protected void setAdditionalPropertiesForView(DocumentSimpleSearchResultsView view, Document document) {
		String documentTypeName = documentTypeNameById.get(document.getDocumentTypeId());
		view.setDocumentTypeName(documentTypeName);
	}
}