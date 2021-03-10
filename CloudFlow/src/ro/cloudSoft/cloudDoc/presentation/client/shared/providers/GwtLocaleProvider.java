package ro.cloudSoft.cloudDoc.presentation.client.shared.providers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.locale.LocaleConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.locale.LocaleExceptions;
import ro.cloudSoft.cloudDoc.presentation.client.shared.locale.LocaleMessages;

import com.google.gwt.core.client.GWT;

public class GwtLocaleProvider
{

    private static LocaleConstants localeConstants;
    private static LocaleMessages localeMessages;
    private static LocaleExceptions localeExceptions;

    private GwtLocaleProvider() {}

    public static void init()
    {
        localeConstants = GWT.create( LocaleConstants.class );
        localeMessages = GWT.create( LocaleMessages.class );
        localeExceptions = GWT.create(LocaleExceptions.class);
    }

    public static LocaleConstants getConstants()
    {
        if ( localeConstants == null )
        {
            localeConstants = GWT.create( LocaleConstants.class );
        }
        return localeConstants;
    }

    public static LocaleMessages getMessages()
    {
        if ( localeMessages == null )
        {
            localeMessages = GWT.create( LocaleMessages.class );
        }
        return localeMessages;
    }

    public static LocaleExceptions getExceptionMessages() {
    	if (localeExceptions == null) {
    		localeExceptions = GWT.create(LocaleExceptions.class);
    	}
    	return localeExceptions;
    } 
}