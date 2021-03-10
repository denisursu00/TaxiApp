package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;

public abstract class ListSelectionWindow extends Window {
	
	private static final String DATA_ITEM_CHECKBOX_VALUE = "itemValue";
	private static final Listener<FieldEvent> EMPTY_CHECKBOX_CHANGE_LISTENER = new Listener<FieldEvent>() {
		
		@Override
		public void handleEvent(FieldEvent event) {}
	};
	
	private VerticalPanel verticalPanel;
	
	private Button okButton;
	private Button cancelButton;
	
	private List<String> valuesForPreviouslySelectedItems;
	
	public ListSelectionWindow() {

		setClosable(false);
		setHeading(GwtLocaleProvider.getConstants().LIST());
		setLayout(new FitLayout());
		setModal(true);
		setSize(400, 400);

		verticalPanel = new VerticalPanel();
		verticalPanel.setScrollMode(Scroll.AUTO);
		verticalPanel.setSpacing(10);
		add(verticalPanel);
		
		okButton = new Button();
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				List<String> valuesOfSelectedItems = getValuesOfSelectedItems();
				if (isValid(valuesOfSelectedItems)) {
					doWhenSelectedItemsChange();
					doWhenOkPressed(valuesOfSelectedItems);
					hide();
				}
			}
		});
		getButtonBar().add(okButton);
		
		if (isCancelable()) {
			cancelButton = new Button();
			cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
			cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent be) {
					restorePreviouslySelectedItems();
					hide();
				}
			});
			getButtonBar().add(cancelButton);
		}
		
		valuesForPreviouslySelectedItems = new ArrayList<String>();
	}
	
	protected boolean isCancelable() {
		return true;
	}
	
	protected boolean isValid(List<String> valuesOfSelectedItems) {
		return true;
	}
	
	protected abstract void doWhenOkPressed(List<String> valuesOfSelectedItems);
	
	protected void setPossibleItems(List<? extends ListItemModel> items) {

		verticalPanel.removeAll();
		
		TableData tableData = new TableData();
		tableData.setMargin(10);
		
		for (ListItemModel item : items) {
			
			CheckBox itemCheckBox = new CheckBox();
			
			itemCheckBox.setData(DATA_ITEM_CHECKBOX_VALUE, item.getItemValue());
			itemCheckBox.setBoxLabel(item.getItemLabel());
			
			itemCheckBox.setStyleName("checkBoxWithPossibleLongBoxLabel");
			
			itemCheckBox.addListener(Events.Change, getCheckboxChangeListener());
			
			verticalPanel.add(itemCheckBox, tableData);
		}

		verticalPanel.layout();
	}
	
	protected Listener<FieldEvent> getCheckboxChangeListener() {
		return EMPTY_CHECKBOX_CHANGE_LISTENER;
	}
	
	protected void setSelectedItemsByValues(Collection<String> valuesOfSelectedItems) {
		
		List<String> labelsOfSelectedItems = new ArrayList<String>();
		
		for (Component itemCheckBoxAsComponent : verticalPanel.getItems()) {
			
			CheckBox itemCheckBox = (CheckBox) itemCheckBoxAsComponent;
			String itemValue = itemCheckBox.getData(DATA_ITEM_CHECKBOX_VALUE);
			
			boolean isItemSelected = valuesOfSelectedItems.contains(itemValue);
			itemCheckBox.setValue(isItemSelected);
			
			if (isItemSelected) {
				String itemLabel = itemCheckBox.getBoxLabel();
				labelsOfSelectedItems.add(itemLabel);
			}
		}
		
		doWhenSelectedItemsChange();

		clearPreviouslySelectedItems();
	}
	
	protected void doWhenSelectedItemsChange() {}

	public void prepareForSelect() {
		
		updatePreviouslySelectedItems();
		
		show();
		toFront();
	}
	
	protected List<String> getValuesOfSelectedItems() {
		
		List<String> valuesOfSelectedItems = new ArrayList<String>();
		
		for (Component itemCheckBoxAsComponent : verticalPanel.getItems()) {
			
			CheckBox itemCheckBox = (CheckBox) itemCheckBoxAsComponent;
			if (ComponentUtils.isChecked(itemCheckBox)) {
				String itemValue = itemCheckBox.getData(DATA_ITEM_CHECKBOX_VALUE);
				valuesOfSelectedItems.add(itemValue);
			}
		}
		
		return valuesOfSelectedItems;
	}
	
	protected List<String> getLabelsOfSelectedItems() {
		
		List<String> labelsOfSelectedItems = new ArrayList<String>();
		
		for (Component itemCheckBoxAsComponent : verticalPanel.getItems()) {
			CheckBox itemCheckBox = (CheckBox) itemCheckBoxAsComponent;
			if (ComponentUtils.isChecked(itemCheckBox)) {
				String itemLabel = itemCheckBox.getBoxLabel();
				labelsOfSelectedItems.add(itemLabel);
			}
		}
		
		return labelsOfSelectedItems;
	}
	
	protected VerticalPanel getVerticalPanel() {
		return verticalPanel;
	}
	
	private void clearPreviouslySelectedItems() {
		valuesForPreviouslySelectedItems.clear();
	}
	
	private void updatePreviouslySelectedItems() {
		clearPreviouslySelectedItems();
		valuesForPreviouslySelectedItems.addAll(getValuesOfSelectedItems());
	}
	
	private void restorePreviouslySelectedItems() {
		setSelectedItemsByValues(valuesForPreviouslySelectedItems);
	}
}