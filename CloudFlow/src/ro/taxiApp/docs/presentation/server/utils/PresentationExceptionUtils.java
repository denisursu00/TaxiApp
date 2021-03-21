package ro.taxiApp.docs.presentation.server.utils;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.core.AppExceptionCodes;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;

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