package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AttachmentModel;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;

@Component
@Path("/AttachmentGxtService")
public class AttachmentGxtServiceResource extends BaseResource {
	
	@Autowired
	private FileSystemAttachmentManager fileSystemAttachmentManager;
	
	@POST
	@Path("/upload")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	public AttachmentModel uploadAttachment(
			@FormDataParam("attachment") InputStream uploadedInputStream,
			@FormDataParam("attachment") FormDataContentDisposition fileDetail) throws IOException {
		
		byte[] data = IOUtils.toByteArray(uploadedInputStream);
		String name = fileDetail.getFileName();
		Attachment attachment = new Attachment(name, data);

		fileSystemAttachmentManager.add(attachment, getSecurity().getUserUsername());
		
		return new AttachmentModel(fileDetail.getFileName());
	}	
	
	@GET
	@Path("/download/{atasamentName}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadAttachment(@PathParam("atasamentName") String attachmentName) throws PresentationException {
		DownloadableFile downloadableFile = fileSystemAttachmentManager.getAttachmentAsDownloadableFile(attachmentName, getSecurity().getUserUsername());
		if (downloadableFile != null) {
			return buildDownloadableFileResponse(downloadableFile);	
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
			
	}
	
	@POST
	@Path("/delete/{atasamentName}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public void deleteAttachment(@PathParam("atasamentName") String attachmentName) throws PresentationException {
		fileSystemAttachmentManager.remove(attachmentName, getSecurity().getUserUsername());
	}	
	
}
