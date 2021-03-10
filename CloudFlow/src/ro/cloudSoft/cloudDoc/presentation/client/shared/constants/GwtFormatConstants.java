package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.i18n.client.DateTimeFormat;

public class GwtFormatConstants {

	public static final String DATE_FOR_DISPLAY = "dd.MM.yyyy";
	public static final String DATE_FOR_SAVING = "yyyy.MM.dd";
	public static final String DATE_FOR_LOG = "dd.MM.yyyy HH:mm:ss,SSS";
	
	public static final DateTimeFormat GWT_DATE_TIME_FORMATTER_FOR_DISPLAY = DateTimeFormat.getFormat(DATE_FOR_DISPLAY);
	public static final DateTimeFormat GWT_DATE_TIME_FORMATTER_FOR_LOG = DateTimeFormat.getFormat(DATE_FOR_LOG);
}