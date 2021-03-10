package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.ListWithSingleSelectionWindow;

public abstract class ChooseTransitionWindow extends ListWithSingleSelectionWindow {

	public ChooseTransitionWindow(List<String> transitionNames) {
		
		setHeading(GwtLocaleProvider.getConstants().CHOOSE_TRANSITION());
		
		List<SimpleListItemModel> listItemModels = new ArrayList<SimpleListItemModel>();
		for (String transitionName : transitionNames) {
			SimpleListItemModel listItemModel = new SimpleListItemModel(transitionName);
			listItemModels.add(listItemModel);
		}
		setPossibleItems(listItemModels);
	}
	
	@Override
	protected boolean isCancelable() {
		return false;
	}
	
	@Override
	protected boolean isValid(String valueOfSelectedItem) {
		return (valueOfSelectedItem != null);
	}
}