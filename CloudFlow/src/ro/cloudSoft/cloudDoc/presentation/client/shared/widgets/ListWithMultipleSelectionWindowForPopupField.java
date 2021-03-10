package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.List;

public class ListWithMultipleSelectionWindowForPopupField extends ListWithMultipleSelectionWindow {
	
	private final ListWithMultipleSelectionPopupField field;
	
	public ListWithMultipleSelectionWindowForPopupField(ListWithMultipleSelectionPopupField popupField) {
		field = popupField;
	}
	
	@Override
	protected void doWhenSelectedItemsChange() {
		updateSelectedItemsTextInField();
	}
	
	@Override
	protected void doWhenOkPressed(List<String> valuesOfSelectedItems) {}
	
	private void updateSelectedItemsTextInField() {
		List<String> labelsOfSelectedItems = getLabelsOfSelectedItems();
		field.updateSelectedItemsText(labelsOfSelectedItems);
	}
}