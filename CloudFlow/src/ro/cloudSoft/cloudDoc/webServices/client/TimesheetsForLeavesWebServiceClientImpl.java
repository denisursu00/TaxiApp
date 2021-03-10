package ro.cloudSoft.cloudDoc.webServices.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.ApprovedTimesheetsForLeaveVerificationRequest;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.ApprovedTimesheetsForLeaveVerificationResponse;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetsForLeavesRequest;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetsForLeavesResponse;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDayDetails;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDetails;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class TimesheetsForLeavesWebServiceClientImpl implements TimesheetsForLeavesWebServiceClient, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(TimesheetsForLeavesWebServiceClientImpl.class);

	private static final String HTTP_REQUEST_METHOD_POST = "POST";
	private static final String HTTP_REQUEST_HEADER_CONTENT_TYPE = "Content-Type";
	
	private static final String MIME_TYPE_JSON = "application/json";
	
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	private String webServiceUrlAsString;

	private String secretToken;
	
	private String operationSuffix;
	
	private String operationNameCreate;
	private String operationNameCheckApproved;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			webServiceUrlAsString,
			
			secretToken,
			
			operationSuffix,
			
			operationNameCreate,
			operationNameCheckApproved
		);
	}
	
	private <T> T invokeWebService(String operationName, Object requestMessageObject,
			Class<T> responseMessageObjectClass) throws AppException {
		
		if (StringUtils.isBlank(operationName)) {
			throw new IllegalArgumentException("Trebuie specificata o operatie pentru serviciu.");
		}
		if (requestMessageObject == null) {
			throw new IllegalArgumentException("Trebuie trimis un mesaj de request serviciului web.");
		}
		
		String webServiceUrlWithOperation = new StringBuilder()
			.append(webServiceUrlAsString)
			.append("/")
			.append(operationName)
			.append(operationSuffix)
			.toString();
		
		URL webServiceUrl = null;
		try {
			webServiceUrl = new URL(webServiceUrlWithOperation);
		} catch (MalformedURLException e) {
			
			String logMessage = "URL-ul catre serviciul web pentru pontaje nu este intr-un format corect: [" + webServiceUrlWithOperation + "].";
			LOGGER.error(logMessage, "invokeWebService");
			
			throw new AppException();
		}
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) webServiceUrl.openConnection();
			connection.setRequestMethod(HTTP_REQUEST_METHOD_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			connection.addRequestProperty(HTTP_REQUEST_HEADER_CONTENT_TYPE, MIME_TYPE_JSON);
			JSON_MAPPER.writeValue(connection.getOutputStream(), requestMessageObject);
			
			int httpResponseCode = connection.getResponseCode();
			if (httpResponseCode != HttpURLConnection.HTTP_OK) {
				
				String logMessage = null;
				if (httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					logMessage = "Nu s-a gasit serviciul web pentru pontaje la URL-ul [" + webServiceUrlWithOperation + "].";
				} else if (httpResponseCode == HttpURLConnection.HTTP_FORBIDDEN) {
					logMessage = "Accesul nu este permis catre serviciul web pentru pontaje.";
				} else if (httpResponseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
					logMessage = "Nu s-a apelat bine serviciul web pentru pontaje. Trebuie verificata structura request-ului.";
				} else {
					logMessage = "S-a apelat serviciul web pentru pontaje, insa acesta NU a dat " +
						"raspunsul asteptat. Codul HTTP returnat este [" + httpResponseCode + "].";
				}
				LOGGER.error(logMessage, "invokeWebService");
				
				throw new AppException();
			}
			
			if (responseMessageObjectClass != null) {
				return JSON_MAPPER.readValue(connection.getInputStream(), responseMessageObjectClass);
			} else {
				return null;
			}
		} catch (IOException ioe) {
			
			String logMessage = "A aparut o eroare de I/O in timpul apelarii serviciului " +
				"web pentru pontaje. URL-ul serviciului este [" + webServiceUrlWithOperation + "].";
			LOGGER.error(logMessage, ioe, "invokeWebService");
			
			throw new AppException();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	@Override
	public Collection<CreateTimesheetForLeaveFailure> createTimesheets(
			List<LeaveDayDetails> leaveDayDetailsList) throws AppException {
		
		CreateTimesheetsForLeavesRequest request = new CreateTimesheetsForLeavesRequest();
		request.setSecretToken(secretToken);
		request.setLeaveDayDetailsList(leaveDayDetailsList);

		CreateTimesheetsForLeavesResponse response = invokeWebService(operationNameCreate, request, CreateTimesheetsForLeavesResponse.class);
		return response.getFailures();
	}

	@Override
	public boolean hasApprovedTimesheets(LeaveDetails leaveDetails) throws AppException {
		
		ApprovedTimesheetsForLeaveVerificationRequest request = new ApprovedTimesheetsForLeaveVerificationRequest();
		request.setSecretToken(secretToken);
		request.setLeaveDetails(leaveDetails);
		
		ApprovedTimesheetsForLeaveVerificationResponse response = invokeWebService(operationNameCheckApproved, request, ApprovedTimesheetsForLeaveVerificationResponse.class);
		return response.isHasApprovedTimesheets();
	}
	
	public void setWebServiceUrlAsString(String webServiceUrlAsString) {
		this.webServiceUrlAsString = webServiceUrlAsString;
	}
	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}
	public void setOperationSuffix(String operationSuffix) {
		this.operationSuffix = operationSuffix;
	}
	public void setOperationNameCreate(String operationNameCreate) {
		this.operationNameCreate = operationNameCreate;
	}
	public void setOperationNameCheckApproved(String operationNameCheckApproved) {
		this.operationNameCheckApproved = operationNameCheckApproved;
	}
}