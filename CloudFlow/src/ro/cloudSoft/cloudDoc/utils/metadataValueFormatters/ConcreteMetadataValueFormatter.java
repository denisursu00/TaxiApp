package ro.cloudSoft.cloudDoc.utils.metadataValueFormatters;

import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.listItemLabelProviders.ListItemLabelProvider;
import ro.cloudSoft.cloudDoc.utils.listItemLabelProviders.ListMetadataDefinitionListItemLabelProvider;
import ro.cloudSoft.cloudDoc.utils.listItemLabelProviders.NonListMetadataDefinitionListItemLabelProvider;

/**
 * Formateaza valori de metadate pentru un tip de document.
 * 
 * 
 */
public class ConcreteMetadataValueFormatter extends AbstractMetadataValueFormatter {

	private static final ListItemLabelProvider NON_LIST_METADATA_DEFINITION_LIST_ITEM_LABEL_PROVIDER = new NonListMetadataDefinitionListItemLabelProvider();
	
	public ConcreteMetadataValueFormatter(UserService userService, NomenclatorService nomenclatorService, 
			GroupService groupService, DocumentService documentService, CalendarService calendarService,
			ProjectService projectService) {
		super(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
	}
	
	public String format(MetadataDefinition metadataDefinition, MetadataInstance metadataInstance) {
		
		ListItemLabelProvider listItemLabelProvider = NON_LIST_METADATA_DEFINITION_LIST_ITEM_LABEL_PROVIDER;
		if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_LIST)) {
			ListMetadataDefinition listMetadataDefinition = (ListMetadataDefinition) metadataDefinition;
			listItemLabelProvider = new ListMetadataDefinitionListItemLabelProvider(listMetadataDefinition);
		}
		
		return format(metadataDefinition.getName(),
			metadataDefinition.getMetadataType(),
			metadataInstance.getValues(),
			listItemLabelProvider);
	}
}