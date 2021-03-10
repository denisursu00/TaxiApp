package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.ReplacementProfilesInnerPanel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 */
public class ReplacementProfilesPanel extends ContentPanel implements AppEventHandler {
	
	private ReplacementProfilesInnerPanel replacementProfilesInnerPanel;

	public ReplacementProfilesPanel() {
		
		setHeading(GwtLocaleProvider.getConstants().REPLACEMENT_PROFILES());
		setLayout(new FitLayout());
		
		initInnerPanel();
		
		addListener(Events.Attach, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				refresh();
			}
		});
		
		AppEventController.subscribe(this, AppEventType.ReplacementProfile);
	}
	
	private void initInnerPanel() {
		
		replacementProfilesInnerPanel = new ReplacementProfilesInnerPanel();
		
		replacementProfilesInnerPanel.getButtonsToolBar().setSpacing(5);
		replacementProfilesInnerPanel.getButtonsToolBar().setStyleName("admin_toolbar");
		replacementProfilesInnerPanel.getButtonsToolBar().setSpacing(10);
		replacementProfilesInnerPanel.getButtonsToolBar().setBorders(true);
		
		replacementProfilesInnerPanel.getAddButton().setStyleName("admin_toolbar_buttons");
		replacementProfilesInnerPanel.getAddButton().setIconStyle("icon-add");
		replacementProfilesInnerPanel.getAddButton().setBorders(true);
		
		replacementProfilesInnerPanel.getDeleteButton().setStyleName("admin_toolbar_buttons");
		replacementProfilesInnerPanel.getDeleteButton().setIconStyle("icon-delete");
		replacementProfilesInnerPanel.getDeleteButton().setBorders(true);

		replacementProfilesInnerPanel.getRefreshButton().setStyleName("admin_toolbar_buttons");
		replacementProfilesInnerPanel.getRefreshButton().setIconStyle("icon-refresh");
		replacementProfilesInnerPanel.getRefreshButton().setBorders(true);
		
		add(replacementProfilesInnerPanel);
	}
	
	private void refresh() {
		replacementProfilesInnerPanel.refresh();
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType().equals(AppEventType.ReplacementProfile)) {
			if (AdminPanelDispatcher.isActivePanel(this)) {
				refresh();
			}
		}
	}
}