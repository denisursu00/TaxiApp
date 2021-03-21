package ro.taxiApp.docs.web.rest.config;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;

import ro.taxiApp.docs.core.AppExceptionCodes;
import ro.taxiApp.docs.presentation.client.shared.model.ErrorMessageModel;
import ro.taxiApp.docs.utils.log.LogHelper;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	
	private static final LogHelper logger = LogHelper.getInstance(GenericExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable t) {
		logger.error("Generic exception REST", t, "REST");
		int statusCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		if (t instanceof WebApplicationException) {
			statusCode = ((WebApplicationException) t).getResponse().getStatus();
		}
		String errorDetails = t.getMessage();
		if (StringUtils.isBlank(errorDetails)) {
			errorDetails = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
		}
		
		return Response.status(statusCode)
				.entity(new ErrorMessageModel(AppExceptionCodes.APPLICATION_ERROR.name(), errorDetails))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
