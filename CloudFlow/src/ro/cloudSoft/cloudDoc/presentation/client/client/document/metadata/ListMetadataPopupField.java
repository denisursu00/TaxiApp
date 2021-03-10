package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.DeselectAllOtherCheckBoxesListener;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.DeselectAllOtherCheckBoxesListener.CheckBoxGroupCheckBoxesProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.ui.Widget;

public class ListMetadataPopupField extends AdapterField implements MetadataField {
	
	private static final Widget dummyWidget = new Widget();
	
	private String metadataDefinitionName;
	
	private boolean mandatory;
	
	private String customValue;
	
	private final boolean multipleSelection;
	private final boolean extendable;
	private final Map<String, String> itemMap;
	
	private List<String> selectedValues;

	private SelectItemsWindow selectItemsWindow;
	
	private HorizontalPanel horizontalPanel;
	
	private Button selectButton;
	private Text selectedItemsText;
	
	public ListMetadataPopupField(String metadataDefinitionName, boolean multipleSelection, boolean extendable, List<ListMetadataItemModel> listItems) {
		super(dummyWidget);
		
		this.metadataDefinitionName = metadataDefinitionName;
		
		this.customValue = null;
		
		this.mandatory = false;
		
		this.multipleSelection = multipleSelection;
		this.extendable = extendable;
		
		this.itemMap = new HashMap<String, String>();
		for (ListMetadataItemModel listItem : listItems) {
			this.itemMap.put(listItem.getValue(), listItem.getLabel());
		}
		
		this.selectedValues = new ArrayList<String>();		

		this.selectItemsWindow = new SelectItemsWindow();
		
		this.horizontalPanel = new HorizontalPanel();
		
		TableData tableData = new TableData();
		tableData.setHorizontalAlign(HorizontalAlignment.LEFT);
		tableData.setVerticalAlign(VerticalAlignment.MIDDLE);
		tableData.setPadding(5);
		
		this.selectedItemsText = new Text();
		this.selectedItemsText.setStyleName("textNormal");
		this.selectedItemsText.setText("");
		this.horizontalPanel.add(this.selectedItemsText, tableData);
		
		this.selectButton = new Button();
		this.selectButton.setText(GwtLocaleProvider.getConstants().SELECT());
		this.selectButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				Window parentWindow = ComponentUtils.getParentWindow(ListMetadataPopupField.this);
				if (parentWindow != null) {
					parentWindow.toBack();
				}
				ListMetadataPopupField.this.selectItemsWindow.prepareForSelect();
			}
		});
		this.horizontalPanel.add(this.selectButton, tableData);
		
		this.widget = this.horizontalPanel;
	}
	
	@Override
	public String getMetadataDefinitionName() {
		return metadataDefinitionName;
	}
	
	private void updateSelectedItemsText() {
		StringBuilder selectedItemsString = new StringBuilder();
		for (String value : this.selectedValues) {
			selectedItemsString.append(this.getItemLabel(value));
			selectedItemsString.append(", ");
		}
		if (selectedItemsString.length() > 0) {
			selectedItemsString.delete(selectedItemsString.lastIndexOf(", "), selectedItemsString.length());
		}
		selectedItemsText.setText(selectedItemsString.toString());
	}
	
	private String getItemLabel(String value) {
		String label = null;
		if ((label = this.itemMap.get(value)) == null) {
			label = value;
		}
		return label;
	}

	@Override
	public List<String> getMetadataValues() {
		return selectedValues;
	}

	@Override
	public void setMetadataValues(List<String> values) {
		/*
		 * Daca se face atribuire directa, lista care vine va fi de un tip care
		 * nu suporta golire (clear).
		 */
		this.selectedValues.clear();
		this.selectedValues.addAll(values);
		this.updateSelectedItemsText();
	}
	
	@Override
	public void setMetadataValues(String... values) {
		MetadataFieldHelper.setValues(this, values);
	}
	
	@Override
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	@Override
	public boolean isRestrictedOnEdit() {
		return !this.selectButton.isEnabled();
	}
	
	@Override
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		this.selectButton.setEnabled(!restrictedOnEdit);
	}
	
	@Override
	public boolean isValid(boolean param) {
		/*
		 * Metoda a trebuit suprascrisa intrucat AdapterField returneaza
		 * intotdeauna true.
		 */
		return validateValue(null);
	}
	
	@Override
	protected boolean validateValue(String param) {
		if (!mandatory) {
			clearInvalid();
			return true;
		}
		if (selectedValues.isEmpty()) {
			markInvalid(GwtLocaleProvider.getMessages().VALIDATOR_MANDATORY_FIELD());
			return false;
		}
		clearInvalid();
		return true;
	}
	
	private class SelectItemsWindow extends Window {
		
		private static final String CHECKBOX_PROPERTY_VALUE = "value";
		
		private VerticalPanel verticalPanelForItemSelection;
		private LayoutContainer layoutContainerForCustomItem;
		private Button okButton;
		private Button cancelButton;
		// elementele din "verticalPanelForItemSelection"
		private CheckBoxGroup itemSelectionCheckBoxGroup;
		// elementele din "layoutContainerForCustomItem"
		private TextField<String> customItemTextField;
		
		public SelectItemsWindow() {
			setHeading(GwtLocaleProvider.getConstants().LIST());
			setLayout(new FitLayout());
			setModal(true);
			setSize(400, 400);

			this.verticalPanelForItemSelection = new VerticalPanel();
			this.verticalPanelForItemSelection.setScrollMode(Scroll.AUTO);
			this.verticalPanelForItemSelection.setSpacing(10);
			add(this.verticalPanelForItemSelection);

			this.layoutContainerForCustomItem = new LayoutContainer();
			this.layoutContainerForCustomItem.setLayout(new FormLayout());

			this.customItemTextField = new TextField<String>();
			this.customItemTextField.setEnabled(ListMetadataPopupField.this.extendable);
			this.customItemTextField.setFieldLabel(GwtLocaleProvider.getConstants().CUSTOM_VALUE());
			this.customItemTextField.addListener(Events.Change, new Listener<FieldEvent>() {
				@Override
				public void handleEvent(FieldEvent fe) {
					if (ListMetadataPopupField.this.multipleSelection) {
						return;
					}
					if (GwtValidateUtils.isCompleted(fe.getValue())) {
						for (Field<?> field : SelectItemsWindow.this.itemSelectionCheckBoxGroup.getAll()) {
							field.setValue(null);
						}
					}
				}
			});
			this.layoutContainerForCustomItem.add(this.customItemTextField, new FormData("100%"));
			
			this.setBottomComponent(this.layoutContainerForCustomItem);
			
			this.okButton = new Button();
			this.okButton.setText("OK");
			this.getButtonBar().add(this.okButton);
			
			this.cancelButton = new Button();
			this.cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
			this.getButtonBar().add(this.cancelButton);
			
			this.okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent be) {
					ListMetadataPopupField.this.selectedValues.clear();
					for (Field<?> field: SelectItemsWindow.this.itemSelectionCheckBoxGroup.getAll()) {
						if (ComponentUtils.getCheckBoxValue((CheckBox) field).booleanValue()) {
							String value = field.getData(SelectItemsWindow.CHECKBOX_PROPERTY_VALUE);
							ListMetadataPopupField.this.selectedValues.add(value);
						}
					}
					
					if (!ListMetadataPopupField.this.selectedValues.contains(ListMetadataPopupField.this.customValue)){
						ListMetadataPopupField.this.customValue = null;
					}
					
					String customValueFromTextfield = SelectItemsWindow.this.customItemTextField.getValue();
					if (GwtValidateUtils.isCompleted(customValueFromTextfield)){
						
						ListMetadataPopupField.this.selectedValues.add(customValueFromTextfield);
						if (ListMetadataPopupField.this.customValue != null) {
							ListMetadataPopupField.this.selectedValues.remove(ListMetadataPopupField.this.customValue);
							ListMetadataPopupField.this.customValue = null;
							
						}
						
						if (ListMetadataPopupField.this.itemMap.get(customValueFromTextfield) == null) {
							ListMetadataPopupField.this.customValue = customValueFromTextfield;
						}
					}
					ListMetadataPopupField.this.updateSelectedItemsText();
					SelectItemsWindow.this.hide();
				}
			});
			this.cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent be) {
					SelectItemsWindow.this.hide();
				}
			});
		}
		
		public void prepareForSelect() {
			this.verticalPanelForItemSelection.removeAll();
			this.customItemTextField.setValue(null);
			
			this.itemSelectionCheckBoxGroup = new CheckBoxGroup();
			this.itemSelectionCheckBoxGroup.setOrientation(Orientation.VERTICAL);
			
			for (String value : ListMetadataPopupField.this.itemMap.keySet()) {
				this.addItemField(value);
			}
			
			for (String value : ListMetadataPopupField.this.selectedValues) {
				if (ListMetadataPopupField.this.itemMap.get(value) == null) {
					this.addItemField(value);
					if (ListMetadataPopupField.this.customValue == null){
						ListMetadataPopupField.this.customValue = value;
					}
				}
			}
			
			this.verticalPanelForItemSelection.add(this.itemSelectionCheckBoxGroup);
			this.verticalPanelForItemSelection.layout();
			this.show();
			this.toFront();
		}
		
		private void addItemField(String value) {
			CheckBox itemField = new CheckBox();
			if (!ListMetadataPopupField.this.multipleSelection) {
				itemField.addListener(Events.Change, new DeselectAllOtherCheckBoxesListener(new CheckBoxGroupCheckBoxesProvider(itemSelectionCheckBoxGroup)));
			}
			itemField.setData(SelectItemsWindow.CHECKBOX_PROPERTY_VALUE, value);
			itemField.setBoxLabel(ListMetadataPopupField.this.getItemLabel(value));
			itemField.setValue(Boolean.valueOf(ListMetadataPopupField.this.selectedValues.contains(value)));
			this.itemSelectionCheckBoxGroup.add(itemField);
		}
	}
	
	@Override
	public void onAddDocument(WorkflowStateModel startState) {
		// Tipul de metadata nu necesita logica suplimentara la adaugarea unui document.
	}
	
	@Override
	public void onEditDocument(WorkflowStateModel currentState) {
		// Tipul de metadata nu necesita logica suplimentara la editarea unui document.
	}
}