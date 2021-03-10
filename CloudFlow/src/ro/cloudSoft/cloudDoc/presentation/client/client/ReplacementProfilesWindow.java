package ro.cloudSoft.cloudDoc.presentation.client.client;

import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.ReplacementProfilesInnerPanel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 */
public class ReplacementProfilesWindow extends WindowToBeUsedOnDesktop implements AppEventHandler {
	
	private ReplacementProfilesInnerPanel replacementProfilesInnerPanel;

	public ReplacementProfilesWindow() {
		
		setHeading(GwtLocaleProvider.getConstants().REPLACEMENT_PROFILES());
		setLayout(new FitLayout());
		setMaximizable(false);
		setMinimizable(true);
		setResizable(false);
		setSize(800, 400);
		
		replacementProfilesInnerPanel = new ReplacementProfilesInnerPanel();
		add(replacementProfilesInnerPanel);
		
		addListener(Events.Show, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent event) {
				refresh();				
			}
		});
		
		AppEventController.subscribe(this, AppEventType.ReplacementProfile);
	}
	
	private void refresh() {
		replacementProfilesInnerPanel.refresh();
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType().equals(AppEventType.ReplacementProfile)) {
			if (isVisible() || ComponentUtils.isWindowMinimized(this)) {
				refresh();
			}
		}
	}
}