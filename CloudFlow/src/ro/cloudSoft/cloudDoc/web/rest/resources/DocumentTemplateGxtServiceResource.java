package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTemplateModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.FileSystemDocumentTemplateManager;

@Component
@Path("/DocumentTemplateGxtService")
public class DocumentTemplateGxtServiceResource extends BaseResource {

	@Autowired
	private FileSystemDocumentTemplateManager fileSystemDocumentTemplateManager;
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@POST
	@Path("/uploadDocumentTemplate")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	public DocumentTemplateModel uploadTemplate(
			@FormDataParam("template") InputStream uploadedInputStream,
			@FormDataParam("template") FormDataContentDisposition templateFileDetail,
			@FormDataParam("description") FormDataContentDisposition descriptionFileDetail) throws IOException {
		
		byte[] data = IOUtils.toByteArray(uploadedInputStream);
		String templateName = templateFileDetail.getFileName();
		String description = descriptionFileDetail.getFileName();
		DocumentTemplateModel templateModel = new DocumentTemplateModel(templateName, description, data);

		fileSystemDocumentTemplateManager.add(templateModel, getSecurity().getUserUsername());

		return new DocumentTemplateModel(templateFileDetail.getFileName(), descriptionFileDetail.getFileName());
	}
	
	@GET
	@Path("/downloadDocumentTemplate")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response exportDocument(@QueryParam(value = "templateName") String templateName, 
			@QueryParam(value = "documentTypeId") Long documentTypeId) throws PresentationException {
		
		DocumentTypeTemplate template = documentTypeService.getTemplate(documentTypeId, templateName);
		return buildDownloadableFileResponse(new DownloadableFile(template.getName(), template.getData()));
	}	
}
