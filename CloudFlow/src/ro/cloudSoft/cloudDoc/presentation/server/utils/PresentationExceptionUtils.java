package ro.cloudSoft.cloudDoc.presentation.server.utils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;

/**
 * Reprezinta o clasa de legatura intre exceptiile de pe server si exceptiile din interfata grafica.
 * 
 * 
 */
public class PresentationExceptionUtils {

	public static PresentationException getPresentationException(AppException appException) {
		return getPresentationException(appException.getCode());
	}
	
	public static PresentationException getPresentationException(AppExceptionCodes appExceptionCode) {
		return new PresentationException(appExceptionCode.name());
	}
}