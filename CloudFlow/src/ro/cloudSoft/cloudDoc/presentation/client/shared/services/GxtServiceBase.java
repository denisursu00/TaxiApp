package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.AssignedEntityTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.InitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.ManuallyChosenEntitiesTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.UserMetadataTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ContentEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.AbstractDocumentSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModelStatusOption;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.ModelWithAdditionalProperties;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.ModelWithAdditionalPropertiesDelegate;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interfata de baza pentru serviciile GWT RPC
 * 
 * 
 */
public interface GxtServiceBase extends RemoteService {

	/**
	 * Metoda trebuie sa aiba ca parametri obiecte de toate tipurile ce sunt trimise prin GWT RPC
	 * si extind / implementeaza java.io.Serializable. Acest lucru este necesar pentru ca tipurile sa fie incluse in
	 * serialization policy-ul serviciului. In caz contrar, la trimiterea unui obiect de un tip care nu se regaseste in
	 * parametrii metodei, GWT va arunca o exceptie de serializare.
	 * 
	 * De asemenea, ar trebui sa declare ca poate arunca toate tipurile de exceptii care ar putea fi trimise catre
	 * partea GWT client.
	 */
	void dummy(AbstractDocumentSearchResultsViewModel abstractDocumentSearchResultsViewModel, AssignedEntityTransitionNotificationModel assignedEntityTransitionNotificationModel,
		AttachmentModel attachmentModel, AuditEntryModel aem, AutoNumberMetadataDefinitionModel autoNumberMetadataDefinitionModel, CollectionInstanceModel collectionInstanceModel,
		ContentEntityModel contentEntityModel, DirectoryUserModel directoryUserModel, DocumentAdvancedSearchResultsViewModel documentAdvancedSearchResultsViewModel,
		DocumentAdvancedSearchResultsViewsWrapperModel documentAdvancedSearchResultsViewsWrapperModel, DocumentCreationInDefaultLocationViewModel dcidlvm,
		DocumentHistoryViewModel documentHistoryViewModel,
		DocumentLocationModel documentLocationModel, DocumentModel documentModel, DocumentSearchCriteriaModel documentSearchCriteriaModel,
		DocumentSimpleSearchResultsViewModel documentSimpleSearchResultsViewModel, DocumentTypeModel documentTypeModel, DocumentTypeTemplateModel documentTypeTemplateModel,
		DocumentVersionInfoViewModel documentVersionInfoViewModel, DocumentViewModel documentViewModel, ExtendedDocumentModel extendedDocumentModel, FolderModel folderModel,
		GroupModel groupModel, GwtReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants,
		HierarchicalSuperiorOfInitiatorTransitionNotificationModel hierarchicalSuperiorOfInitiatorTransitionNotificationModel, InitiatorTransitionNotificationModel initiatorTransitionNotificationModel,
		ListMetadataDefinitionModel listMetadataDefinitionModel, ListMetadataItemModel listMetadataItemModel, LogEntryModel logEntryModel,
		ManuallyChosenEntitiesTransitionNotificationModel manuallyChosenEntitiesTransitionNotificationModel, MetadataCollectionDefinitionModel metadataCollectionDefinitionModel,
		MetadataDefinitionModel metadataDefinitionModel, MetadataInstanceModel metadataInstanceModel, MimeTypeModel mimeTypeModel, ModelWithAdditionalProperties mwap,
		ModelWithAdditionalPropertiesDelegate mwapd, MyActivitiesViewModel myActivitiesViewModel,
		OrganizationEntityModel organizationEntityModel, OrganizationModel organizationModel, OrganizationUnitModel organizationUnitModel, PermissionModel permissionModel,
		ReplacementProfileModel replacementProfileModel, ReplacementProfileModelStatusOption replacementProfileModelStatusOption, SecurityManagerModel securityManagerModel,
		TransitionNotificationModel transitionNotificationModel, UserMetadataTransitionNotificationModel userMetadataTransitionNotificationModel, UserModel userModel,
		WorkflowInstanceResponseModel workflowInstanceResponseModel, WorkflowModel workflowModel, WorkflowStateModel workflowStateModel, WorkflowTransitionModel workflowTransitionModel)
		throws PresentationException;
}