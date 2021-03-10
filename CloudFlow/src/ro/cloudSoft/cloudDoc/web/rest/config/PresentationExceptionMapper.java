package ro.cloudSoft.cloudDoc.web.rest.config;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ErrorMessageModel;

@Provider
public class PresentationExceptionMapper implements ExceptionMapper<PresentationException> {

	@Override
	public Response toResponse(PresentationException pe) {
		
		int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		String errorCode = pe.getCode();
		if (StringUtils.isBlank(errorCode)) {
			errorCode = AppExceptionCodes.APPLICATION_ERROR.name();
		}
		String errorDetails = pe.getMessage();
		if (StringUtils.isBlank(errorDetails)) {
			errorDetails = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
		}
		
		return Response.status(status)
				.entity(new ErrorMessageModel(errorCode, errorDetails))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
