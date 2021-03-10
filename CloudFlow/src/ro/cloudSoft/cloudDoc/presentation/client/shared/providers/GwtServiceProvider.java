package ro.cloudSoft.cloudDoc.presentation.client.shared.providers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AclGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AclGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AuditGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AuditGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.CereriDeConcediuGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.CereriDeConcediuGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.LogGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.LogGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.OrganizationGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.OrganizationGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.DocumentWorkflowHistoryGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.DocumentWorkflowHistoryGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.WorkflowGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.WorkflowGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentLocationGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentLocationGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentTypeGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentTypeGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.FolderGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.FolderGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.MimeTypeGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.MimeTypeGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles.ReplacementProfilesGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles.ReplacementProfilesGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.search.DocumentSearchGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.search.DocumentSearchGxtServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GwtServiceProvider {

    public static DocumentLocationGxtServiceAsync getDocumentLocationService(){
        DocumentLocationGxtServiceAsync service = (DocumentLocationGxtServiceAsync)GWT.create(DocumentLocationGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "DocumentLocationGxtService.form");
        return service;
    }

    public static FolderGxtServiceAsync getFolderService(){
        FolderGxtServiceAsync service = (FolderGxtServiceAsync)GWT.create(FolderGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "FolderGxtService.form");
        return service;
    }

    public static DocumentGxtServiceAsync getDocumentService(){
        DocumentGxtServiceAsync service = (DocumentGxtServiceAsync)GWT.create(DocumentGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "DocumentGxtService.form");
        return service;
    }

    public static AclGxtServiceAsync getAclService(){
        AclGxtServiceAsync service = (AclGxtServiceAsync)GWT.create(AclGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "AclGxtService.form");
        return service;
    }

   

    public static OrganizationGxtServiceAsync getOrgService(){
        OrganizationGxtServiceAsync service = (OrganizationGxtServiceAsync)GWT.create(OrganizationGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "OrganizationGxtService.form");
        return service;
    }
    
    public static AuditGxtServiceAsync getAuditService() {
    	AuditGxtServiceAsync service = GWT.create(AuditGxtService.class);
    	((ServiceDefTarget) service).setServiceEntryPoint(GWT.getModuleBaseURL() + "AuditGxtService.form");
        return service;
    }
    
    public static LogGxtServiceAsync getLogService(){
        LogGxtServiceAsync service = (LogGxtServiceAsync)GWT.create(LogGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
                                                               "LogGxtService.form");
        return service;
    }

    public static AppGxtServiceAsync getAppService(){
        AppGxtServiceAsync service = GWT.create(AppGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "AppGxtService.form");
        return service;
    }

    public static DocumentTypeGxtServiceAsync getDocumentTypeService() {
        DocumentTypeGxtServiceAsync service = GWT.create(
    		DocumentTypeGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "DocumentTypeGxtService.form");
        return service;
    }
    
    public static MimeTypeGxtServiceAsync getMimeTypeService() {
        MimeTypeGxtServiceAsync service = GWT.create(
    		MimeTypeGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "MimeTypeGxtService.form");
        return service;
    }
    
    public static WorkflowGxtServiceAsync getWorkflowService() {
    	WorkflowGxtServiceAsync service = GWT.create(WorkflowGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "WorkflowGxtService.form");
        return service;
    }
    
    public static DocumentSearchGxtServiceAsync getDocumentSearchService() {
    	DocumentSearchGxtServiceAsync service = GWT.create(DocumentSearchGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "DocumentSearchGxtService.form");
        return service;
    }
    public static DocumentWorkflowHistoryGxtServiceAsync getDocumentHistoryService() {
    	DocumentWorkflowHistoryGxtServiceAsync service = GWT.create(DocumentWorkflowHistoryGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(
    		GWT.getModuleBaseURL() + "DocumentWorkflowHistoryGxtService.form");
        return service;
    }
    
    public static ReplacementProfilesGxtServiceAsync getReplacementProfilesService() {
    	ReplacementProfilesGxtServiceAsync service = GWT.create(ReplacementProfilesGxtService.class);
    	((ServiceDefTarget) service).setServiceEntryPoint(GWT.getModuleBaseURL() + "ReplacementProfilesGxtService.form");
        return service;
    }

    public static CereriDeConcediuGxtServiceAsync getCereriDeConcediuService() {
    	CereriDeConcediuGxtServiceAsync service = GWT.create(CereriDeConcediuGxtService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint(GWT.getModuleBaseURL() + "CereriDeConcediuGxtService.form");
        return service;
    }
}