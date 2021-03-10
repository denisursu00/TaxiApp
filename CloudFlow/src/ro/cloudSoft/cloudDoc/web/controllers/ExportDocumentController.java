package ro.cloudSoft.cloudDoc.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jasper.DocumentDataSource;
import ro.cloudSoft.cloudDoc.plugins.content.jasper.ReportWriter;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.bpm.DocumentWorkflowHistoryService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.ConcreteMetadataValueFormatter;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.beans.BeanUtils;

/**
 * 
 */
public class ExportDocumentController implements Controller, InitializingBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(ExportDocumentController.class);
	
	private static final String SEPARATOR_METADATA_COLLECTION_NAME_FROM_METADATA_DEFINITION_NAME = "_";
	
	private static final String CONTENT_TYPE = "application/pdf";
	private static final String EXTENSION = "pdf";
	
	private DocumentService documentService;
	private DocumentTypeService documentTypeService;
	private DocumentWorkflowHistoryService documentWorkflowHistoryService;
	private UserService userService;
	private NomenclatorService nomenclatorService;
	private GroupService groupService;
	private CalendarService calendarService;
	private ProjectService projectService;
	
	private String valueWhenNull;
	
	public ExportDocumentController() {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			documentService,
			documentTypeService,
			documentWorkflowHistoryService,
			userService,
			nomenclatorService,
			groupService,
			calendarService,
			projectService,
			
			valueWhenNull
		);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String documentLocationRealName = request.getParameter("documentLocationRealName");
		String documentId = request.getParameter("documentId");
		String templateName = request.getParameter("templateName");
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();
		
		Document document = null;
		try {
			document = documentService.getDocumentById(documentId, documentLocationRealName, userSecurity);
		} catch (AppException ae) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "Exceptie la luarea documentului cu atributele: " + documentLogAttributes;
			LOGGER.error(logMessage, ae, "luarea documentului pentru export", userSecurity);
			
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
		DocumentTypeTemplate documentTypeTemplate = documentTypeService.getTemplate(documentType.getId(), templateName);
		
		Map<String, Object> reportParameters = Maps.newHashMap();
		ConcreteMetadataValueFormatter metadataValueFormatter = new ConcreteMetadataValueFormatter(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
		
		putParameterValuesForMetadataDefinitions(reportParameters, documentType, document, metadataValueFormatter);
		putParameterValuesForMetadataCollections(reportParameters, documentType, document, metadataValueFormatter);
		
		//TODO de pus alti parametri utili de pe instanta de flux
		List<DocumentHistory> history = null;
		try {
			history = documentWorkflowHistoryService.getDocumentHistory(document.getId());
		} catch (AppException ae) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "Exceptie la luarea istoricului pentru documentul cu atributele: " + documentLogAttributes;
			LOGGER.error(logMessage, ae, "luarea istoricului unui document pentru export", userSecurity);
			
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		List<String> actors = new ArrayList<String>();
		for (int i = 0; history != null && i < history.size(); i++ )
		{
			DocumentHistory aHistoryItem = history.get( i );
			User theUser = userService.getUserById(aHistoryItem.getActorId());
			if ( theUser != null )
			{
				actors.add( theUser.getDisplayName());
			}
		}
		reportParameters.put("CLOUD_DOC_ACTORS", actors);
		
		Long authorId = Long.valueOf(document.getAuthor());
		User superior = userService.getSuperior(authorId);
		reportParameters.put("CLOUD_DOC_USER_SUP", superior != null ? superior.getName() : "" );
		
		InputStream templateStream = new ByteArrayInputStream(documentTypeTemplate.getData());
		DocumentDataSource dataSource = new DocumentDataSource();
		
		ReportWriter reportWriter = new ReportWriter(templateStream, reportParameters, dataSource);
		
		OutputStream outputStream = response.getOutputStream();
		
		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getName() + "." + EXTENSION + "\"");
            response.setContentType(CONTENT_TYPE);
            
			reportWriter.writePdfToStream(outputStream);
		} catch (Exception e) {
			
			DocumentLogAttributes documentLogAttributes = documentService.getDocumentLogAttributes(documentLocationRealName, documentId);
			
			String logMessage = "Exceptie la trimiterea documentului exportat clientului." +
				"Documentul are atributele: " + documentLogAttributes + ". " +
				"Tipul de document are ID-ul [" + documentType.getId() + "], " +
				"iar template-ul ales are ID-ul [" + documentTypeTemplate.getId() + "].";
			LOGGER.error(logMessage, e, "trimiterea documentului exportat clientului", userSecurity);
			
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			templateStream.close();
		}
		
		return null;
	}
	
	private void putParameterValuesForMetadataDefinitions(Map<String, Object> reportParameters,
			DocumentType documentType, Document document, ConcreteMetadataValueFormatter metadataValueFormatter) {
		
		Map<Long, MetadataInstance> metadataInstanceByDefinitionId = BeanUtils.getAsMap(document.getMetadataInstanceList(), "metadataDefinitionId");
		
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			
			MetadataInstance metadataInstance = metadataInstanceByDefinitionId.get(metadataDefinition.getId());

			String metadataName = metadataDefinition.getName();
			String metadataValue = valueWhenNull;
			if (metadataInstance != null) {
				metadataValue = metadataValueFormatter.format(metadataDefinition, metadataInstance);
			}
			
			reportParameters.put(metadataName, metadataValue);
		}
	}
	
	private void putParameterValuesForMetadataCollections(Map<String, Object> reportParameters,
			DocumentType documentType, Document document, ConcreteMetadataValueFormatter metadataValueFormatter) {
		
		Multimap<Long, MetadataInstance> metadataInstancesByMetadataDefinitionId = LinkedHashMultimap.create();
			
		for (List<CollectionInstance> collectionInstanceList : document.getCollectionInstanceListMap().values()) {			
			for (CollectionInstance collectionInstance : collectionInstanceList) {
				for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
					metadataInstancesByMetadataDefinitionId.put(metadataInstance.getMetadataDefinitionId(), metadataInstance);
				}
			}
		}
		
		for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
			for (MetadataDefinition metadataDefinition : metadataCollection.getMetadataDefinitions()) {
				
				String parameterNameForMetadataDefinition = metadataCollection.getName() +
					SEPARATOR_METADATA_COLLECTION_NAME_FROM_METADATA_DEFINITION_NAME + metadataDefinition.getName();
				
				Collection<MetadataInstance> metadataInstancesForCollection = metadataInstancesByMetadataDefinitionId.get(metadataDefinition.getId());
				
				String formattedMetadataValuesAsString = valueWhenNull;
				
				if (!metadataInstancesForCollection.isEmpty()) {
					Set<String> formattedMetadataValues = Sets.newLinkedHashSet();
					for (MetadataInstance metadataInstanceForCollection : metadataInstancesForCollection) {
						String formattedMetadataValue = metadataValueFormatter.format(metadataDefinition, metadataInstanceForCollection);
						formattedMetadataValues.add(formattedMetadataValue);
					}
					formattedMetadataValuesAsString = StringUtils.join(formattedMetadataValues, ", ");
				}
				
				reportParameters.put(parameterNameForMetadataDefinition, formattedMetadataValuesAsString);
			}
		}
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setDocumentWorkflowHistoryService(DocumentWorkflowHistoryService documentWorkflowHistoryService) {
		this.documentWorkflowHistoryService = documentWorkflowHistoryService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
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

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setValueWhenNull(String valueWhenNull) {
		this.valueWhenNull = valueWhenNull;
	}
}