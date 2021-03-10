package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;

public class DocumentCreationInDefaultLocationViewConverter {

	public static DocumentCreationInDefaultLocationViewModel getModel(DocumentCreationInDefaultLocationView view) {
		return new DocumentCreationInDefaultLocationViewModel(
				
			view.getDocumentTypeId(),
			view.getDocumentTypeName(),
			
			view.getParentDocumentLocationRealNameForDefaultLocation(),
			view.getFolderIdForDefaultLocation()
		);
	}
}