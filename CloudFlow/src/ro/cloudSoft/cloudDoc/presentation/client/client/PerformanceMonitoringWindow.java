package ro.cloudSoft.cloudDoc.presentation.client.client;

import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

public class PerformanceMonitoringWindow extends WindowToBeUsedOnDesktop {

	public PerformanceMonitoringWindow() {
		setHeading(GwtLocaleProvider.getConstants().PERFORMANCE_MONITORING());
		setMaximizable(true);
		setMinimizable(true);
	}
}