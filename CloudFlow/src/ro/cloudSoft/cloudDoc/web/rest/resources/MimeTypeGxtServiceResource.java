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

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.MimeTypeGxtService;

@Component
@Path("/MimeTypeGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MimeTypeGxtServiceResource {
	
	@Autowired
	private MimeTypeGxtService mimeTypeGxtService;

	@POST
	@Path("/getAllMimeTypes")
	public List<MimeTypeModel> getAllMimeTypes() throws PresentationException {
		return mimeTypeGxtService.getAllMimeTypes();
	}

	@POST
	@Path("/getMimeTypeById/{mimeTypeId}")
	public MimeTypeModel getMimeTypeById(@PathParam("mimeTypeId") Long mimeTypeId) throws PresentationException {
		return mimeTypeGxtService.getMimeTypeById(mimeTypeId);
	}

	@POST
	@Path("/saveMimeType")
	public void saveMimeType(MimeTypeModel mimeTypeModel) throws PresentationException{
		mimeTypeGxtService.saveMimeType(mimeTypeModel);
	}

	@POST
	@Path("/deleteMimeType/{mimeTypeId}")
	public void deleteMimeType(@PathParam("mimeTypeId") Long mimeTypeId) throws PresentationException {
		mimeTypeGxtService.deleteMimeType(mimeTypeId);
	}
}
