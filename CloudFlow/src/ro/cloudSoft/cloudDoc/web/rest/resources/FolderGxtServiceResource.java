package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.extjs.gxt.ui.client.data.ModelData;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetFolderPathRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MoveFolderRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.FolderGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.FolderService;

@Component
@Path("/FolderGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class FolderGxtServiceResource extends BaseResource {
	
	@Autowired
	private FolderGxtService folderGxtService;
	
	@Autowired
	private FolderService folderService;
	
	@POST
	@Path("/getFoldersFromFolder/{documentLocationRealName}/{parentId}")
	public List<ModelData> getFoldersFromFolder(
			@PathParam("documentLocationRealName") String documentLocationRealName,
			@PathParam("parentId") String parentId) throws PresentationException {
		
		return folderGxtService.getFoldersFromFolder(documentLocationRealName, parentId);
	}

	@POST
	@Path("/getFolderById/{folderId}/{documentLocationRealName}")
	public FolderModel getFolderById(
			@PathParam("folderId") String folderId,
			@PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		
		return folderGxtService.getFolderById(folderId, documentLocationRealName);
	}
	
	@POST
	@Path("/saveFolder")
	public void saveFolder(FolderModel folderModel) throws PresentationException {
		folderGxtService.saveFolder(folderModel);
	}
	
	@POST
	@Path("/deleteFolder/{folderId}/{documentLocationRealName}")
	public void deleteFolder(
			@PathParam("folderId") String folderId,
			@PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		
		folderGxtService.deleteFolder(folderId, documentLocationRealName);
	}
	
	@POST
	@Path("/moveFolder")
	public void moveFolder(MoveFolderRequestModel moveFolderRequestModel) throws PresentationException {
		folderGxtService.moveFolder(
				moveFolderRequestModel.getFolderToMoveId(),
				moveFolderRequestModel.getDestinationFolderId(),
				moveFolderRequestModel.getDocumentLocationRealName());
	}
	
	@POST
	@Path("/getIdsForFolderHierarchy/{documentLocationRealName}/{folderId}")
	public List<String> getIdsForFolderHierarchy(
			@PathParam("documentLocationRealName") String documentLocationRealName, 
			@PathParam("folderId") String folderId) throws PresentationException {
		
		return folderGxtService.getIdsForFolderHierarchy(documentLocationRealName, folderId);
	}
	
	@POST
	@Path("/getFolderPath")
	public String getFolderPath(GetFolderPathRequestModel folderPathRequestModel) throws PresentationException {
		try {
			return this.folderService.getFolderPath(folderPathRequestModel.getDocumentLocationRealName(), folderPathRequestModel.getFolderId(), getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
}