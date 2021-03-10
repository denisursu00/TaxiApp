package ro.cloudSoft.cloudDoc.presentation.client.client;

import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

public class HelpWindow extends WindowToBeUsedOnDesktop {

	public HelpWindow() {
		setHeading(GwtLocaleProvider.getConstants().HELP());
		setMaximizable(true);
		setMinimizable(true);
	}
}