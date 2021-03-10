package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.Collections;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.ui.Widget;

public class ListWithMultipleSelectionPopupField extends CustomAdapterField {
	
	private static final Widget DUMMY_WIDGET = new Widget();
	
	private boolean selectionRequired;
	
	private ListWithMultipleSelectionWindowForPopupField selectItemsWindow;
	
	private HorizontalPanel horizontalPanel;
	
	private Button selectButton;
	private Text selectedItemsText;

	public ListWithMultipleSelectionPopupField() {
		
		super(DUMMY_WIDGET);
		
		selectionRequired = false;
		
		selectItemsWindow = new ListWithMultipleSelectionWindowForPopupField(this);
		
		horizontalPanel = new HorizontalPanel();

		TableData tableData = new TableData();
		tableData.setHorizontalAlign(HorizontalAlignment.LEFT);
		tableData.setVerticalAlign(VerticalAlignment.MIDDLE);
		tableData.setPadding(5);
		
		selectedItemsText = new Text();
		selectedItemsText.setStyleName("textNormal");
		selectedItemsText.setText("");
		horizontalPanel.add(selectedItemsText, tableData);
		
		selectButton = new Button();
		selectButton.setText(GwtLocaleProvider.getConstants().SELECT());
		selectButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				
				Window parentWindow = ComponentUtils.getParentWindow(ListWithMultipleSelectionPopupField.this);
				if (parentWindow != null) {
					parentWindow.toBack();
				}
				
				selectItemsWindow.prepareForSelect();
			}
		});
		horizontalPanel.add(selectButton, tableData);
		
		widget = horizontalPanel;
	}
	
	public void setSelectionRequired(boolean selectionRequired) {
		this.selectionRequired = selectionRequired;
	}
	
	public void resetSelectedItems() {
		setSelectedItemsByValues(Collections.<String> emptyList());
	}
	
	public void resetPossibleItems() {
		setPossibleItems(Collections.<ListItemModel> emptyList());
	}
	
	public void setPossibleItems(List<? extends ListItemModel> items) {
		selectItemsWindow.setPossibleItems(items);
		selectButton.setEnabled(!items.isEmpty());
	}
	
	public void setSelectedItemsByValues(List<String> values) {
		selectItemsWindow.setSelectedItemsByValues(values);
		validate();
	}
	
	public void updateSelectedItemsText(List<String> labelsOfSelectedItems) {
		String labelsOfSelectedItemsJoined = GwtStringUtils.join(labelsOfSelectedItems, ", ");
		selectedItemsText.setText(labelsOfSelectedItemsJoined);
	}
	
	@Override
	protected boolean validateCustomAdapterField() {
		
		if (!selectionRequired) {
			clearInvalid();
			return true;
		}
		
		List<String> valuesOfSelectedItems = getValuesOfSelectedItems();
		if (valuesOfSelectedItems.isEmpty()) {
			markInvalid(GwtLocaleProvider.getMessages().LIST_MUST_HAVE_SELECTED_ITEMS());
			return false;
		} else {
			clearInvalid();
			return true;
		}
	}
	
	public List<String> getValuesOfSelectedItems() {
		return selectItemsWindow.getValuesOfSelectedItems();
	}
	
	protected ListWithMultipleSelectionWindowForPopupField getSelectItemsWindow() {
		return selectItemsWindow;
	}
}