package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowMetadataVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.TextWithPlaceholdersHelper;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.CompositeWithPriorityPlaceholderValueContext;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.CompositeWithPriorityPlaceholderValueContext.Priority;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.JbpmExecutionDocumentAttributesPlaceholderValueContext;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.MetadataForPrintingPlaceholderValueContext;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.PlaceholderValueContext;
import ro.cloudSoft.common.utils.spring.SpringUtils;

/**
 * Evalueaza subiectul sau continutul unui email,
 * inlocuind marcajele metadatelor cu valorile corespunzatoare.
 * 
 */
public class EmailEvaluator {
	
	private final PlaceholderValueContext placeholderValueContext;
	
	public EmailEvaluator(OpenExecution execution) {
		
		UserService userService = SpringUtils.getBean("userService");
		DocumentTypeService documentTypeService = SpringUtils.getBean("documentTypeService");
		NomenclatorService nomenclatorService = SpringUtils.getBean("nomenclatorService");
		GroupService groupService = SpringUtils.getBean("groupService");
		DocumentService documentService = SpringUtils.getBean("documentService");
		CalendarService calendarService = SpringUtils.getBean("calendarService");
		ProjectService projectService = SpringUtils.getBean("projectService");
		
		Long documentTypeId = WorkflowVariableHelper.getDocumentTypeId(execution);
		Map<String, MetadataWrapper> metadataWrapperByMetadataName = WorkflowMetadataVariableHelper.getMetadataWrapperByMetadataName(execution.getVariables());
		
		JbpmExecutionDocumentAttributesPlaceholderValueContext documentAttributesPlaceholderValueContext = new JbpmExecutionDocumentAttributesPlaceholderValueContext(execution);
		MetadataForPrintingPlaceholderValueContext metadataPlaceholderValueContext = new MetadataForPrintingPlaceholderValueContext(userService, documentTypeService, nomenclatorService, groupService, documentService, calendarService, projectService, documentTypeId, metadataWrapperByMetadataName);

		CompositeWithPriorityPlaceholderValueContext compositeWithPriorityPlaceholderValueContext = new CompositeWithPriorityPlaceholderValueContext();
		compositeWithPriorityPlaceholderValueContext.addPlaceholderValueContext(Priority.HIGH, documentAttributesPlaceholderValueContext);
		compositeWithPriorityPlaceholderValueContext.addPlaceholderValueContext(Priority.MEDIUM, metadataPlaceholderValueContext);
		
		placeholderValueContext = compositeWithPriorityPlaceholderValueContext;
	}
	
	public String replacePlaceholders(String text) {
		
		if (StringUtils.isBlank(text)) {
			return text;
		}
		
		return TextWithPlaceholdersHelper.replacePlaceholders(text, placeholderValueContext);
	}
}