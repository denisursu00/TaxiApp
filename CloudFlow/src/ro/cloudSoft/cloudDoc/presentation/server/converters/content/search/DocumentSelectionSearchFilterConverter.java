package ro.cloudSoft.cloudDoc.presentation.server.converters.content.search;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilterModel;

public class DocumentSelectionSearchFilterConverter {

	public static DocumentSelectionSearchFilter getFromModel(DocumentSelectionSearchFilterModel model) {
		DocumentSelectionSearchFilter filter = new DocumentSelectionSearchFilter();
		filter.setDocumentLocationRealName(model.getDocumentLocationRealName());
		filter.setDocumentName(model.getDocumentName());
		filter.setDocumentTypeId(model.getDocumentTypeId());
		return filter;
	}
}
