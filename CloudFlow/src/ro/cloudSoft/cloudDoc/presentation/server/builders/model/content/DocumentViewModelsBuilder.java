package ro.cloudSoft.cloudDoc.presentation.server.builders.model.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.content.ImportedDocumentDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentMetadataViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.ConcreteMetadataValueFormatter;

public class DocumentViewModelsBuilder {
	
	private final List<Document> documents;
	
	private final boolean sameType;
	
	private final DocumentTypeService documentTypeService;
	private final UserService userService;
	private final NomenclatorService nomenclatorService;
	private final GroupService groupService;
	private final DocumentService documentService;
	private final CalendarService calendarService;
	private final ProjectService projectService;
	private final WorkflowInstanceDao workflowInstanceDao;
	private final ImportedDocumentDao importedDocumentDao;
	
	private final SecurityManager userSecurity;
	
	private DocumentType documentType;
	private ConcreteMetadataValueFormatter metadataValueFormatter;
	
	private Map<Long, MetadataDefinition> metadataDefinitionMap;
	private Map<Long, String> userNameMap;
	private Map<Long, String> documentTypeNameMap;
	
	/**
	 * Se creeaza un builder pentru documente de acelasi tip sau de tipuri
	 * diferite.
	 * <br><br>
	 * a) Acelasi tip: In interfata se vor afisa atat datele generale ale
	 * documentelor, cat si valorile metadatelor reprezentative.
	 * <br><br>
	 * b) Tipuri diferite: In interfata se vor afisa doar datele generale ale
	 * documentelor.
	 */
	public DocumentViewModelsBuilder(List<Document> documents, boolean sameType, DocumentTypeService documentTypeService, UserService userService,
				NomenclatorService nomenclatorService, GroupService groupService, DocumentService documentService, CalendarService calendarService,
				ProjectService projectService, WorkflowInstanceDao workflowInstanceDao, ImportedDocumentDao importedDocumentDao, SecurityManager userSecurity) {
		this.documents = documents;
		
		this.sameType = sameType;
		
		this.documentTypeService = documentTypeService;
		this.userService = userService;
		this.nomenclatorService = nomenclatorService;
		this.groupService = groupService;
		this.documentService = documentService;
		this.calendarService = calendarService;
		this.projectService = projectService;
		this.workflowInstanceDao = workflowInstanceDao;
		this.importedDocumentDao = importedDocumentDao;
		
		this.userSecurity = userSecurity;
		
		this.metadataDefinitionMap = new HashMap<Long, MetadataDefinition>();
		this.userNameMap = new HashMap<Long, String>();
		this.documentTypeNameMap = new HashMap<Long, String>();
	}

	/**
	 * Obtine lista cu view-uri de documente.
	 */
	public List<DocumentViewModel> getViewModels() {
		// Pregateste datele ajutatoare.
		this.prepareDocumentTypeAndMetadataFormatter();
		this.prepareMetadataDefinitionMap();
		this.prepareUserNameMap();
		this.prepareDocumentTypeNameMap();
		
		List<DocumentViewModel> documentViewModels = new ArrayList<DocumentViewModel>();
		
		for (Document document : this.documents) {
			DocumentViewModel documentViewModel = new DocumentViewModel();
			
			documentViewModel.setDocumentId(document.getId());
			documentViewModel.setDocumentName(document.getName());
			documentViewModel.setDocumentTypeName(this.documentTypeNameMap.get(document.getDocumentTypeId()));
			documentViewModel.setDocumentAuthorName(this.userNameMap.get(Long.valueOf(document.getAuthor())));
			documentViewModel.setDocumentCreatedDate(document.getCreated().getTime());
			documentViewModel.setDocumentLastModifiedDate(document.getLastUpdate().getTime());
			WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(document.getId());
			if (workflowInstance != null) {
				if (workflowInstance.getStatus().equalsIgnoreCase(WorkflowInstance.STATUS_FINNISHED)) {
					documentViewModel.setDocumentStatus(WorkflowInstance.STATUS_FINNISHED);
				} else if (workflowInstance.getStatus().equalsIgnoreCase(WorkflowInstance.STATUS_RUNNING)){
					documentViewModel.setDocumentStatus(WorkflowInstance.STATUS_RUNNING);
				}
			} else if (importedDocumentDao.isDocumentImported(new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId()))){
				documentViewModel.setDocumentStatus(WorkflowInstance.STATUS_IMPORTED);
			} else {
				documentViewModel.setDocumentStatus(WorkflowInstance.STATUS_NONE);
			}
			
			if (document.isLocked()) {
				documentViewModel.setDocumentLockedByName(this.userNameMap.get(Long.valueOf(document.getLockedBy())));
			}

			List<String> representativeMetadataLabels = new ArrayList<String>();
			if (this.documentType != null) {
				for (MetadataInstance metadataInstance : document.getMetadataInstanceList()) {
					
					MetadataDefinition metadataDefinition = this.metadataDefinitionMap.get(metadataInstance.getMetadataDefinitionId());
					if (metadataDefinition == null) {
						// Metadata a fost stearsa din tipul de document.
						continue;
					}
					
					if (metadataDefinition.getRepresentative()) {
						
						String label = metadataDefinition.getLabel();
						String name = metadataDefinition.getName();
						String value = metadataValueFormatter.format(metadataDefinition, metadataInstance);

						representativeMetadataLabels.add(label);
						documentViewModel.setMetadataValue(name, value);
						
						DocumentMetadataViewModel documentMetadata = new DocumentMetadataViewModel();
						documentMetadata.setLabel(label);
						documentMetadata.setName(name);
						documentMetadata.setValue(value);
						
						if (CollectionUtils.isEmpty(documentViewModel.getDocumentMetadata())) {
							documentViewModel.setDocumentMetadata(new ArrayList<DocumentMetadataViewModel>());
						}
						documentViewModel.getDocumentMetadata().add(documentMetadata);
					}
				}
			}
			documentViewModel.setDocumentRepresentativeMetadataLabels(representativeMetadataLabels);
			
			documentViewModels.add(documentViewModel);
		}
		
		return documentViewModels;
	}
	
	/**
	 * Pregateste tipul de document, daca documentele sunt de acelasi tip.
	 */
	private void prepareDocumentTypeAndMetadataFormatter() {
		// Daca documentele sunt de acelasi tip si exista documente in lista...
		if (this.sameType && !this.documents.isEmpty()) {
			/*
			 * Iau ID-ul tipului de document de la primul document (toate sunt
			 * de acelasi tip).
			 */
			Long documentTypeId = this.documents.get(0).getDocumentTypeId();
			// Iau tipul de document dupa ID.
			this.documentType = this.documentTypeService.getDocumentTypeById(documentTypeId);
			metadataValueFormatter = new ConcreteMetadataValueFormatter(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
		}
	}
	
	/**
	 * Pregateste map-ul cu definitiile metadatelor. Aceasta actiune se
	 * realizeaza numai cand documentele sunt de acelasi tip.
	 */
	private void prepareMetadataDefinitionMap() {
		// Daca documentele sunt de acelasi tip si am tipul documentelor...
		if (this.sameType && (this.documentType != null)) {
			for (MetadataDefinition metadataDefinition : this.documentType.getMetadataDefinitions()) {
				this.metadataDefinitionMap.put(metadataDefinition.getId(), metadataDefinition);
			}
		}
	}
	
	/**
	 * Pregateste map-ul cu numele tipurilor de documente ale documentelor.
	 * Daca toate documentele sunt de acelasi tip, vom avea un singur nume.
	 */
	private void prepareDocumentTypeNameMap() {
		// Daca documentele sunt de acelasi tip si am tipul documentelor...
		if (this.sameType && (this.documentType != null)) {
			this.documentTypeNameMap.put(this.documentType.getId(), this.documentType.getName());
		} else {
			Set<Long> documentTypeIds = new HashSet<Long>();
			
			for (Document document : this.documents) {
				documentTypeIds.add(document.getDocumentTypeId());
			}
			
			this.documentTypeNameMap = this.documentTypeService.getDocumentTypesNameMap(documentTypeIds);
		}
	}
	
	/**
	 * Pregateste map-ul cu numele utilizatorilor ce au legatura cu documentele.
	 */
	private void prepareUserNameMap() {
		Set<Long> userIds = new HashSet<Long>();
		
		for (Document document : this.documents) {
			Long authorId = Long.valueOf(document.getAuthor());
			userIds.add(authorId);
			
			if (document.isLocked()) {
				Long lockedById = Long.valueOf(document.getLockedBy());
				userIds.add(lockedById);
			}
		}
		
		this.userNameMap = this.userService.getUsersNameMap(userIds, this.userSecurity);
	}
}