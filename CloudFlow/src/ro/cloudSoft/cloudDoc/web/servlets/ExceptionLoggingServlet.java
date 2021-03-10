package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

/**
 * Raporteaza exceptiile "scapate" din zona principala a aplicatiei.
 * 
 * 
 */
public class ExceptionLoggingServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static final LogHelper LOGGER = LogHelper.getInstance(ExceptionLoggingServlet.class);
	
	private static final String REQUEST_ATTRIBUTE_NAME_EXCEPTION = "javax.servlet.error.exception";
	private static final String REQUEST_ATTRIBUTE_NAME_REQUESTED_URI = "javax.servlet.error.request_uri";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logException(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logException(request, response);
	}
	
	private void logException(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Throwable exception = (Throwable) request.getAttribute(REQUEST_ATTRIBUTE_NAME_EXCEPTION);		
		if (exception != null) {
			String requestedUri = (String) request.getAttribute(REQUEST_ATTRIBUTE_NAME_REQUESTED_URI);
			LOGGER.error("Exceptie la accesarea [" + requestedUri + "]", exception, null);
		}
		
		String exceptionClassName = exception.getClass().getName();
		String exceptionMessage = exception.getMessage();
		
		String errorMessage = exceptionClassName;
		if (StringUtils.isNotBlank(exceptionMessage)) {
			errorMessage = exceptionClassName + ": " + exceptionMessage;
		}
		
		response.getWriter().print("[EROARE] " + errorMessage);
	}
}