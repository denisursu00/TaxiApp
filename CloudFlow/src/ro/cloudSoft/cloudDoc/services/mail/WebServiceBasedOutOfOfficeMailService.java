package ro.cloudSoft.cloudDoc.services.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.webServices.client.mail.OutOfOfficeMailWebServiceClient;
import ro.cloudSoft.cloudDoc.webServices.client.mail.OutOfOfficeMailWebServiceClientFactory;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class WebServiceBasedOutOfOfficeMailService implements OutOfOfficeMailService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(WebServiceBasedOutOfOfficeMailService.class);
	
	private String wsdlLocation;
	
	private boolean enabled;
	
	private String dateFormat;
	
	private String operationResponseOk;
	private String operationResponseError;

	private OutOfOfficeMailWebServiceClient outOfOfficeMailWebServiceClient;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			wsdlLocation,
			
			enabled,
			
			dateFormat,
			
			operationResponseOk,
			operationResponseError
		);
		
		if (enabled) {
			OutOfOfficeMailWebServiceClientFactory webServiceClientFactory = new OutOfOfficeMailWebServiceClientFactory(wsdlLocation);
			outOfOfficeMailWebServiceClient = webServiceClientFactory.getPort();
		}
	}

	@Override
	public void activateOutOfOffice(String emailAddress, Date startDate, Date endDate,
			String outOfOfficeEmailSubject, String outOfOfficeEmailBody) throws AppException {
		
		if (!enabled) {
			LOGGER.warn("Serviciul web este dezactivat, operatia de activare profil nu va avea loc.",
				"activarea out of office");
			return;
		}
		
		DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
		
		String startDateAsString = dateFormatter.format(startDate);
		String endDateAsString = dateFormatter.format(endDate);
		
		String response = "";
		try {
			response = outOfOfficeMailWebServiceClient.setOooStatus(emailAddress,
				startDateAsString, endDateAsString, outOfOfficeEmailSubject, outOfOfficeEmailBody);
		} catch (RuntimeException re) {
			
			String logMessage = "Exceptie la apelarea serviciului web pentru activarea out of office. " +
				"Parametrii trimisi sunt: adresa de mail [" + emailAddress + "], data inceput [" + startDateAsString + "], " +
				"data sfarsit [" + endDateAsString + "], subiect mail [" + outOfOfficeEmailSubject + "], " +
				"corp mail [" + outOfOfficeEmailBody + "].";
			LOGGER.error(logMessage, re, "activarea out of office");
			
			throw new AppException();
		}
		
		if (response.equals(operationResponseOk)) {
			return;
		} else if (response.equals(operationResponseError)) {
			
			String logMessage = "Serviciul web pentru activarea out of office a intors eroare ca raspuns. " +
				"Parametrii trimisi sunt: adresa de mail [" + emailAddress + "], data inceput [" + startDateAsString + "], " +
				"data sfarsit [" + endDateAsString + "], subiect mail [" + outOfOfficeEmailSubject + "], " +
				"corp mail [" + outOfOfficeEmailBody + "].";
			LOGGER.error(logMessage, "activarea out of office");
			
			throw new AppException();
		} else {
			
			String logMessage = "Serviciul web pentru activarea out of office a intors un raspuns necunoscut: [" + response + "]. " +
				"Parametrii trimisi sunt: adresa de mail [" + emailAddress + "], data inceput [" + startDateAsString + "], " +
				"data sfarsit [" + endDateAsString + "], subiect mail [" + outOfOfficeEmailSubject + "], " +
				"corp mail [" + outOfOfficeEmailBody + "].";
			LOGGER.error(logMessage, "activarea out of office");
			
			throw new AppException();
		}
	}

	@Override
	public void deactivateOutOfOffice(String emailAddress) throws AppException {
		
		if (!enabled) {
			LOGGER.warn("Serviciul web este dezactivat, operatia de dezactivare profil nu va avea loc.",
				"activarea out of office");
			return;
		}
		
		String response = "";
		try {
			response = outOfOfficeMailWebServiceClient.resetOooStatus(emailAddress);
		} catch (RuntimeException re) {
			
			String logMessage = "Exceptie la apelarea serviciului web pentru " +
				"dezactivarea out of office pentru adresa de mail [" + emailAddress + "]";
			LOGGER.error(logMessage, re, "dezactivarea out of office");
			
			throw new AppException();
		}
		
		if (response.equals(operationResponseOk)) {
			return;
		} else if (response.equals(operationResponseError)) {
			
			String logMessage = "Serviciul web pentru dezactivarea out of office " +
				"a intors eroare ca raspuns pentru adresa de mail [" + emailAddress + "].";
			LOGGER.error(logMessage, "dezactivarea out of office");
			
			throw new AppException();
		} else {
			
			String logMessage = "Serviciul web pentru dezactivarea out of office a intors " +
				"un raspuns necunoscut: [" + response + "], pentru adresa de mail [" + emailAddress + "].";
			LOGGER.error(logMessage, "dezactivarea out of office");
			
			throw new AppException();
		}
	}
	
	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public void setOperationResponseOk(String operationResponseOk) {
		this.operationResponseOk = operationResponseOk;
	}
	public void setOperationResponseError(String operationResponseError) {
		this.operationResponseError = operationResponseError;
	}
}