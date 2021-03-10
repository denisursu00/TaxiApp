package ro.cloudSoft.cloudDoc.utils.placeholderValueContexts;

import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.MetadataWrapperValueFormatter;

/**
 * Context de valori pentru metadate, reprezentandu-le intr-o forma afisabila utilizatorului
 * 
 * 
 */
public class MetadataForPrintingPlaceholderValueContext implements PlaceholderValueContext {
	
	private final MetadataWrapperValueFormatter metadataValueFormatter;
	private final Map<String, MetadataWrapper> metadataWrapperByMetadataName;
	
	public MetadataForPrintingPlaceholderValueContext(UserService userService, DocumentTypeService documentTypeService, NomenclatorService nomenclatorService, 
				GroupService groupService, DocumentService documentService, CalendarService calendarService, ProjectService projectService, Long documentTypeId, Map<String, MetadataWrapper> metadataWrapperByMetadataName) {
		this.metadataValueFormatter = new MetadataWrapperValueFormatter(userService, documentTypeService, nomenclatorService, groupService, documentService, calendarService, projectService, documentTypeId);
		this.metadataWrapperByMetadataName = metadataWrapperByMetadataName;
	}
	
	@Override
	public String getValue(String placeholderName) {
		
		MetadataWrapper metadataWrapper = metadataWrapperByMetadataName.get(placeholderName);
		
		if (metadataWrapper == null) {
			return PlaceholderValueContext.VALUE_WHEN_NOT_FOUND;
		}
		
		return metadataValueFormatter.format(placeholderName, metadataWrapper);
	}
}