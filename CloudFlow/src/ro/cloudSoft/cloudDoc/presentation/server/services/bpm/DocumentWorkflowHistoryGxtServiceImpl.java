package ro.cloudSoft.cloudDoc.presentation.server.services.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.DocumentWorkflowHistoryGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.DocumentWorkflowHistoryConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.bpm.DocumentWorkflowHistoryService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DocumentWorkflowHistoryGxtServiceImpl extends GxtServiceImplBase implements DocumentWorkflowHistoryGxtService, InitializingBean {

	private DocumentWorkflowHistoryService documentWorkflowHistoryService;
	private UserService userService;
	
	public DocumentWorkflowHistoryGxtServiceImpl() {}
	
	public DocumentWorkflowHistoryGxtServiceImpl(DocumentWorkflowHistoryService documentWorkflowHistoryService, UserService userService)
	{
		this.documentWorkflowHistoryService=documentWorkflowHistoryService;
		this.userService=userService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentWorkflowHistoryService,
			userService
		);
	}

	@Override
	public List<DocumentHistoryViewModel> getDocumentHistory(String documentId) throws PresentationException
	{
		try {
				List <DocumentHistoryViewModel> documentHistoryModelList=new ArrayList<DocumentHistoryViewModel>();
				List <DocumentHistory> documentHistoryList=new ArrayList<DocumentHistory>();
				documentHistoryList=documentWorkflowHistoryService.getDocumentHistory(documentId);
				Set<String> userIds = new HashSet<String>();
				for(DocumentHistory docHist:documentHistoryList)
				{
					if(docHist.getActorId()!=null)
					{
						userIds.add(docHist.getActorId().toString());
					}
				}
				StringBuilder userIdsSb=new StringBuilder();
				Iterator<String> it=userIds.iterator();
				while(it.hasNext())
				{
					userIdsSb.append(it.next());
					userIdsSb.append(",");
				}
				if(userIdsSb.length()>0)
				{
					userIdsSb.deleteCharAt(userIdsSb.length()-1);
					List<HashMap<String, HashMap<String,String>>> userDepartmentList=userService.getUsersAndDepartment(userIdsSb.toString(), getSecurity());
					documentHistoryModelList= DocumentWorkflowHistoryConverter.getModelFromDocumentHistory(documentHistoryList,userDepartmentList);
				}
				return documentHistoryModelList;
			} 
			catch (AppException ae) {
				throw PresentationExceptionUtils.getPresentationException(ae);
			}
	}
}