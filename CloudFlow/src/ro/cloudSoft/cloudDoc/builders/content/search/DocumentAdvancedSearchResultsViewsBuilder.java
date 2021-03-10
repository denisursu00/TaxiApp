package ro.cloudSoft.cloudDoc.builders.content.search;

import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.ConcreteMetadataValueFormatter;
import ro.cloudSoft.common.utils.MapUtils;

import com.google.common.collect.Maps;

public class DocumentAdvancedSearchResultsViewsBuilder extends AbstractDocumentSearchResultsViewsBuilder<DocumentAdvancedSearchResultsView> {
	
	
	private final DocumentType documentType;
	
	
	private final UserService userService;
	private final DocumentService documentService;
	private final NomenclatorService nomenclatorService;
	private final GroupService groupService;
	private final CalendarService calendarService;
	private final ProjectService projectService;
	
	private Map<Long, MetadataDefinition> representativeMetadataDefinitionById;
	private Map<DocumentIdentifier, Map<Long, String>> metadataDisplayValueByMetadataDefinitionIdByDocumentIdentifier;
	
	
	public DocumentAdvancedSearchResultsViewsBuilder(DocumentType documentType, List<Document> documents, SecurityManager userSecurity,
			WorkflowService workflowService, WorkflowExecutionService workflowExecutionService, UserService userService, DocumentService documentService, 
			NomenclatorService nomenclatorService, GroupService groupService, CalendarService calendarService, ProjectService projectService) {
		
		super(documents, userSecurity, workflowService, workflowExecutionService, userService);
		
		this.documentType = documentType;
		
		this.userService = userService;
		this.documentService = documentService;
		this.nomenclatorService = nomenclatorService;
		this.groupService = groupService;
		this.calendarService = calendarService;
		this.projectService = projectService;
	}
	
	@Override
	protected void additionalInit() {
		representativeMetadataDefinitionById = Maps.newHashMap();
		metadataDisplayValueByMetadataDefinitionIdByDocumentIdentifier = Maps.newHashMap();
	}
	
	@Override
	protected void gatherAdditionalItems() {
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			
			if (!metadataDefinition.getRepresentative()) {
				continue;
			}
			
			representativeMetadataDefinitionById.put(metadataDefinition.getId(), metadataDefinition);
		}
	}
	
	@Override
	protected void gatherAdditionalItemsForDocument(Document document) {
		
		if (!document.getDocumentTypeId().equals(documentType.getId())) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(document.getDocumentLocationRealName(), document.getId());
			
			String exceptionMessage = "Documentul cu atributele: " + documentLogAttributes + ", NU este de tipul asteptat. Tipul asteptat: cu ID-ul " +
				"[" + documentType.getId() + "]. Tipul actual: cu ID-ul [" + document.getDocumentTypeId() + "].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		DocumentIdentifier documentIdentifier = new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId());
		Map<Long, String> metadataDisplayValueByMetadataDefinitionId = MapUtils.getAndInitIfNull(
			metadataDisplayValueByMetadataDefinitionIdByDocumentIdentifier, documentIdentifier,
			Maps.<Long, String> newHashMap());
		
		for (MetadataInstance metadataInstance : document.getMetadataInstanceList()) {
			
			MetadataDefinition metadataDefinition = representativeMetadataDefinitionById.get(metadataInstance.getMetadataDefinitionId());
			
			boolean isRepresentative = (metadataDefinition != null);
			if (!isRepresentative) {
				continue;
			}

			ConcreteMetadataValueFormatter metadataValueFormatter = new ConcreteMetadataValueFormatter(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
			
			String metadataDisplayValue = metadataValueFormatter.format(metadataDefinition, metadataInstance);
			metadataDisplayValueByMetadataDefinitionId.put(metadataDefinition.getId(), metadataDisplayValue);
		}
	}
	
	@Override
	protected DocumentAdvancedSearchResultsView createNewViewInstance() {
		return new DocumentAdvancedSearchResultsView();
	}
	
	@Override
	protected void setAdditionalPropertiesForView(DocumentAdvancedSearchResultsView view, Document document) {
		DocumentIdentifier documentIdentifier = new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId());
		Map<Long, String> metadataDisplayValueByMetadataDefinitionId = metadataDisplayValueByMetadataDefinitionIdByDocumentIdentifier.get(documentIdentifier);
		view.setDocumentMetadataInstanceDisplayValueByDefinitionId(metadataDisplayValueByMetadataDefinitionId);
	}
}