package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.ListWithSingleSelectionWindow;

public abstract class ChooseTemplateWindow extends ListWithSingleSelectionWindow {

	public ChooseTemplateWindow(List<DocumentTypeTemplateModel> templates) {
		setHeading(GwtLocaleProvider.getConstants().CHOOSE_TEMPLATE());
		setPossibleItems(templates);
	}
	
	@Override
	protected boolean isValid(String valueOfSelectedItem) {
		return (valueOfSelectedItem != null);
	}
}