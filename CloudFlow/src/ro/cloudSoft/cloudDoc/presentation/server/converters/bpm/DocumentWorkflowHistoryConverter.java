package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.DocumentHistory;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;

public class DocumentWorkflowHistoryConverter {
	
	public static DocumentHistoryViewModel getModelFromDocumentHistory (DocumentHistory docHist, String deptName, String authorName)
	{
		DocumentHistoryViewModel docHistModel= new DocumentHistoryViewModel();
		docHistModel.setWorkflowActor(authorName);
		docHistModel.setOrganizationDepartment(deptName);
		
		if (docHist.getTransitionDate()!=null) {
			docHistModel.setWorkflowTransitionDate(docHist.getTransitionDate().getTime());
		}
		
		if(docHist.getTransitionName().length()>0)
		docHistModel.setWorkflowTransitionName(docHist.getTransitionName());
		else
			docHistModel.setWorkflowTransitionName("-");
		return docHistModel;
	}
	public static List <DocumentHistoryViewModel> getModelFromDocumentHistory (List<DocumentHistory> documentHistoryList, List<HashMap<String, HashMap<String,String>>> userDepartmentList)
	{
		//documentHistoryList poate fi mai mare decat userDepartmentList deoarece un 
		//task poate ajunge la acelasi user de mai multe ori, 
		//in istoric acesta fiind inregistrari distincte(ce au loc la momente diferite de timp)
		
		List <DocumentHistoryViewModel> documentHistoryModelList=new ArrayList<DocumentHistoryViewModel>();
		for(DocumentHistory docHist:documentHistoryList)
		{   
			DocumentHistoryViewModel docHistModel= new DocumentHistoryViewModel();
			for(HashMap<String, HashMap<String, String>> idUserDeptMap:userDepartmentList)
			{   //docHist.userID=idUserDeptMap.userID
				if (idUserDeptMap.containsKey(docHist.getActorId().toString()))
				{
					HashMap <String,String> userDepartmentMap=idUserDeptMap.get(docHist.getActorId().toString());
					Iterator<String> iterator = (userDepartmentMap.keySet()).iterator();
					while(iterator.hasNext()) {
					String author = iterator.next().toString();
					String deptName = userDepartmentMap.get(author).toString();
					docHistModel.setWorkflowActor(author);
					docHistModel.setOrganizationDepartment(deptName);
					}
					
				}
			}
			
			if (docHist.getTransitionDate()!=null) {
				docHistModel.setWorkflowTransitionDate(docHist.getTransitionDate().getTime());
			}
			
			if(docHist.getTransitionName().length()>0)
				docHistModel.setWorkflowTransitionName(docHist.getTransitionName());
			else
				docHistModel.setWorkflowTransitionName("-");
			documentHistoryModelList.add(docHistModel);
		}
		return documentHistoryModelList;
	}
}
