package ro.cloudSoft.cloudDoc.utils.metadataValueFormatters;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.listItemLabelProviders.DocumentTypeServiceListItemLabelProvider;

/**
 * Formateaza valori de metadate pentru metadate tinute in wrapper-e.
 * 
 * 
 */
public class MetadataWrapperValueFormatter extends AbstractMetadataValueFormatter {

	private final DocumentTypeServiceListItemLabelProvider listItemLabelProvider;
	
	public MetadataWrapperValueFormatter(UserService userService, DocumentTypeService documentTypeService, 
			NomenclatorService nomenclatorService, GroupService groupService, DocumentService documentService, 
			CalendarService calendarService, ProjectService projectService, Long documentTypeId) {
		super(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
		listItemLabelProvider = new DocumentTypeServiceListItemLabelProvider(documentTypeService, documentTypeId);
	}
	
	public String format(String metadataName, MetadataWrapper metadataWrapper) {
		return format(metadataName, metadataWrapper.getType(), metadataWrapper.getValues(), listItemLabelProvider);
	}
}