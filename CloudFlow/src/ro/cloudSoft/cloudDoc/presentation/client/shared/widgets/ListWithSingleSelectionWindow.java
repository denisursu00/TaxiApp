package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.DeselectAllOtherCheckBoxesListener;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.DeselectAllOtherCheckBoxesListener.ContainerCheckBoxesProvider;

import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;

public abstract class ListWithSingleSelectionWindow extends ListSelectionWindow {
	
	private DeselectAllOtherCheckBoxesListener deselectAllOtherCheckBoxesListener;

	public ListWithSingleSelectionWindow() {
		deselectAllOtherCheckBoxesListener = new DeselectAllOtherCheckBoxesListener(new ContainerCheckBoxesProvider(getVerticalPanel()));
	}
	
	@Override
	protected Listener<FieldEvent> getCheckboxChangeListener() {
		return deselectAllOtherCheckBoxesListener;
	}
	
	@Override
	protected boolean isValid(List<String> valuesOfSelectedItems) {
		String valueOfSelectedItem = getValueOfSelectedItem(valuesOfSelectedItems);
		return isValid(valueOfSelectedItem);
	}
	
	protected boolean isValid(String valueOfSelectedItem) {
		return true;
	}
	
	@Override
	protected void doWhenOkPressed(List<String> valuesOfSelectedItems) {
		String valueOfSelectedItem = getValueOfSelectedItem(valuesOfSelectedItems);
		doWhenOkPressed(valueOfSelectedItem);
	}
	
	protected abstract void doWhenOkPressed(String valueOfSelectedItem);
	
	private String getValueOfSelectedItem(List<String> valuesOfSelectedItems) {
		if (valuesOfSelectedItems.size() == 0) {
			return null;
		} else if (valuesOfSelectedItems.size() == 1) {
			return valuesOfSelectedItems.get(0);
		} else {
			throw new IllegalStateException("Desi este lista cu selectie simpla, s-au putut selecta mai multe elemente.");
		}
	}
}