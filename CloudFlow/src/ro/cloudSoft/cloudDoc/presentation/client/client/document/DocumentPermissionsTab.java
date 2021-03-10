package ro.cloudSoft.cloudDoc.presentation.client.client.document;


import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtPermissionBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.ManagePermissionsComponent;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DocumentPermissionsTab extends DocumentBaseTab {
	
	ManagePermissionsComponent manageDocumentPermissionsComponent = new ManagePermissionsComponent();
	
	public DocumentPermissionsTab() {
		
		setText(GwtLocaleProvider.getConstants().SECURITY());
		setLayout(new FitLayout());
		
		add(manageDocumentPermissionsComponent);		
	}
	protected void changeSize(float wIndex, float hIndex){
		manageDocumentPermissionsComponent.changeSize(wIndex, hIndex);
	}
	
	@Override
	protected void reset() {
		manageDocumentPermissionsComponent.reset();
	}

	@Override
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		reset();
		manageDocumentPermissionsComponent.populate(null);
	}

	@Override
	public void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
		reset();
		manageDocumentPermissionsComponent.populate(document.getPermissions());
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		boolean canChangePermissions = GwtPermissionBusinessUtils.canChangePermissions(this.manageDocumentPermissionsComponent.getPermissions(), GwtRegistryUtils.getUserSecurity());
		manageDocumentPermissionsComponent.setReadOnly(readOnly || !canChangePermissions);
	}

	@Override
	public boolean isValid() {
		boolean hasEntities = true;
		boolean areAllPermissionsSet = true;
		if (manageDocumentPermissionsComponent.getSelectedEntities().isEmpty()) {
			hasEntities = false;
			ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_ENTITIES());
		}
		if (!manageDocumentPermissionsComponent.areAllPermissionsSet()) {
			areAllPermissionsSet = false;
			ErrorHelper.addError(GwtLocaleProvider.getMessages().PERMISSIONS_NOT_SET_FOR_ALL_SELECTED_ENTITIES());
		}
		return hasEntities && areAllPermissionsSet;
	}

	@Override
	public void populate(DocumentModel documentModel) {
		documentModel.setPermissions(manageDocumentPermissionsComponent.getPermissions());
	}
}