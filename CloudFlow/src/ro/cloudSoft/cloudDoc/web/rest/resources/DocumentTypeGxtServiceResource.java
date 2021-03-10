package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentTypeGxtService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;

@Component
@Path("/DocumentTypeGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DocumentTypeGxtServiceResource  {
	
	@Autowired
	private DocumentTypeGxtService documentTypeGxtService;
	
	@Autowired
	private DocumentTypeService documentTypeService;

	@POST
	@Path("/getAllDocumentTypesForDisplay")
	public List<DocumentTypeModel> getAllDocumentTypesForDisplay() throws PresentationException {
		return documentTypeGxtService.getAllDocumentTypesForDisplay();
	}

	@POST
	@Path("/getDocumentTypeById/{id}")
	public DocumentTypeModel getDocumentTypeById(@PathParam("id") Long id) throws PresentationException {
		documentTypeGxtService.clearTemporaryTemplates();
		return documentTypeGxtService.getDocumentTypeById(id);
	}

	@POST
	@Path("/prepareForAddOrEdit")
	public void prepareForAddOrEdit() throws PresentationException {
		documentTypeGxtService.prepareForAddOrEdit();
	}

	@POST
	@Path("/saveDocumentType")
	public void saveDocumentType(DocumentTypeModel documentTypeModel) throws PresentationException {
		documentTypeGxtService.saveDocumentType(documentTypeModel);
	}

	@POST
	@Path("/deleteDocumentType/{id}")
	public void deleteDocumentType(@PathParam("id") Long id) throws PresentationException {
		documentTypeGxtService.deleteDocumentType(id);
	}

	@POST
	@Path("/getUserMetadataDefinitions")
	public List<UserMetadataDefinitionModel> getUserMetadataDefinitions(List<Long> documentTypeIds) throws PresentationException {
		return documentTypeGxtService.getUserMetadataDefinitions(documentTypeIds);
	}

	@POST
	@Path("/getAvailableDocumentTypes")
	public List<DocumentTypeModel> getAvailableDocumentTypes() throws PresentationException {
		return documentTypeGxtService.getAvailableDocumentTypes();
	}

	@POST
	@Path("/getDocumentCreationInDefaultLocationViews")
	public List<DocumentCreationInDefaultLocationViewModel> getDocumentCreationInDefaultLocationViews() throws PresentationException {
		return documentTypeGxtService.getDocumentCreationInDefaultLocationViews();
	}

	@POST
	@Path("/getAvailableDocumentTypesForSearch")
	public List<DocumentTypeModel> getAvailableDocumentTypesForSearch() throws PresentationException {		
		return documentTypeGxtService.getAvailableDocumentTypesForSearch();
	}

	@POST
	@Path("/getDocumentTypesWithNoWorkflow")
	public List<DocumentTypeModel> getDocumentTypesWithNoWorkflow() throws PresentationException {
		return documentTypeGxtService.getDocumentTypesWithNoWorkflow();
	}

	@POST
	@Path("/existDocumentTypeWithName/{documentTypeName}")
	public boolean existDocumentTypeWithName(@PathParam("documentTypeName") String documentTypeName) throws PresentationException {
		return documentTypeService.existDocumentTypeWithName(documentTypeName);
	}
	
	@POST
	@Path("/getDocumentTypeIdByName/{documentTypeName}")
	public Long getDocumentTypeIdByName(@PathParam("documentTypeName") String documentTypeName) throws PresentationException {
		return documentTypeService.getDocumentTypeIdByName(documentTypeName);
	}

	@POST
	@Path("/getMetadataDefinitionIdByNameAndDocumentType/{metadataName}/{documentTypeId}")
	public Long getMetadataDefinitionIdByNameAndDocumentType(@PathParam(value = "metadataName") String name, @PathParam(value = "documentTypeId" ) Long documentTypeId) {
		return documentTypeService.getMetadataDefinitionIdByNameAndDocumentType(name, documentTypeId);
	}
}