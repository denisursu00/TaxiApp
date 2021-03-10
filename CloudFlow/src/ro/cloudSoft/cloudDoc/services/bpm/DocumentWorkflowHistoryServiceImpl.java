package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.cloudDoc.plugins.bpm.DocumentWorkflowHistoryPlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DocumentWorkflowHistoryServiceImpl implements DocumentWorkflowHistoryService, InitializingBean {
	
	private DocumentWorkflowHistoryPlugin documentWorkflowHistoryPlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			documentWorkflowHistoryPlugin
		);
	}
	
	public DocumentWorkflowHistoryServiceImpl(){}

	@Override
	public List <DocumentHistory> getDocumentHistory(String documentId) throws AppException
	{
		return documentWorkflowHistoryPlugin.getDocumentHistory( documentId); 
	}

    public void setPlugin(DocumentWorkflowHistoryPlugin val) {
        this.documentWorkflowHistoryPlugin = val;
    }

	public void setDocumentWorkflowHistoryPlugin(DocumentWorkflowHistoryPlugin documentWorkflowHistoryPlugin) {
		this.documentWorkflowHistoryPlugin = documentWorkflowHistoryPlugin;
	}
}