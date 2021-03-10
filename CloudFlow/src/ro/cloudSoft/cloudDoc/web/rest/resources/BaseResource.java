package ro.cloudSoft.cloudDoc.web.rest.resources;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;

public class BaseResource {

	public SecurityManager getSecurity() {
		return SecurityManagerHolder.getSecurityManager();
    }
    
    protected Response buildDownloadableFileResponse(DownloadableFile downloadableFile) {
    	return Response.ok(downloadableFile.getContent(), MediaType.APPLICATION_OCTET_STREAM_TYPE)
    			.header("Content-Disposition","attachment;filename=" + downloadableFile.getFileName())
    			.header("Access-Control-Expose-Headers", "Content-Disposition")
    			.build();
    }
}
