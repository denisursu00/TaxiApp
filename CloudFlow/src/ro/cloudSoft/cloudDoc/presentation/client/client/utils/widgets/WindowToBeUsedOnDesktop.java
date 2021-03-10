package ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;

import com.extjs.gxt.ui.client.widget.Window;

/**
 * Reprezinta o fereastra care va fi folosita intr-un desktop.
 * Contine workaround-uri pt. bug-uri specifice ferestrelor in desktop.
 */
public class WindowToBeUsedOnDesktop extends Window {
	
	private static final String DATA_NAME_MINIMIZE = "minimize";

	@Override
	public <X> X getData(String key) {
		X data = super.getData(key);
		if (GwtCompareUtils.areEqual(key, DATA_NAME_MINIMIZE)) {
			/*
			 * Workaround pt. bug conform caruia o fereastra minimizata
			 * NU se mai inchide apoi la apasarea butonului X, ci se minimizeaza.
			 */
			setData(DATA_NAME_MINIMIZE, null);
		}
		return data;
	}
}