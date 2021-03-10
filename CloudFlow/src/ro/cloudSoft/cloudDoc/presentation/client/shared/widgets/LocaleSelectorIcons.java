package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtServiceAsync;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocaleSelectorIcons extends HorizontalPanel {
	
	private static final String LOCALE_NAME = "localeName";
	private static final AppGxtServiceAsync appService = GwtServiceProvider.getAppService();

	public LocaleSelectorIcons(){
		String[] availableLocaleNames = LocaleInfo.getAvailableLocaleNames();
		for (String localName : availableLocaleNames){
			this.add(makeNewButton(localName));			
		}		
	}
	private String styleNameBuilder(String locString, boolean on){
		StringBuilder styleName = new StringBuilder(locString);
		if (on) 
			styleName.append("_on");
		else 
			styleName.append("_off");
		return styleName.toString();
	}
	
	private IconButton makeNewButton(String localName){
		IconButton button = new IconButton();
		button.setData(LOCALE_NAME, localName);
		int buttonHeight = 11;
		int buttonWidth = 16;
		button.setSize(buttonWidth, buttonHeight);
		
		final String onStyleName = styleNameBuilder(localName,true);
		final String offStyleName = styleNameBuilder(localName, false);	
		button.setStyleName(offStyleName);
		
		button.addListener(Events.OnMouseOver, new Listener<IconButtonEvent>(){
			public void handleEvent(IconButtonEvent ibe) {
				ibe.getIconButton().setStyleName(onStyleName);				
			}			
		});
		button.addListener(Events.OnMouseOut, new Listener<IconButtonEvent>(){
			public void handleEvent(IconButtonEvent ibe) {
				ibe.getIconButton().setStyleName(offStyleName);				
			}	
		});
		
		button.addSelectionListener(new SelectionListener<IconButtonEvent>(){
			public void componentSelected(IconButtonEvent ics) {
				if (ics.getIconButton().getData(LOCALE_NAME).equals(LocaleInfo.getCurrentLocale()))
					return;
				appService.setLocale((String)ics.getIconButton().getData(LOCALE_NAME),new AsyncCallback<Void>(){
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
					}
					public void onSuccess(Void result) {
						Window.Location.reload();
					}					
				});				
			}	
		});
		
		return button;
	}
}
