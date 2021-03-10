package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.EnumWithLocalizedLabel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.EnumWithLocalizedLabelComboBox.EnumWithLocalizedLabelComboBoxModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class EnumWithLocalizedLabelComboBox<E extends Enum<E> & EnumWithLocalizedLabel> extends ComboBox<EnumWithLocalizedLabelComboBoxModel> {
	
	private final Class<E> enumClass;
	
	public EnumWithLocalizedLabelComboBox(Class<E> enumClass) {
		
		this.enumClass = enumClass;
		
		setDisplayField(EnumWithLocalizedLabelComboBoxModel.PROPERTY_LABEL);
		setEditable(false);
		setStore(new ListStore<EnumWithLocalizedLabelComboBoxModel>());
		setTriggerAction(TriggerAction.ALL);
		
		for (EnumWithLocalizedLabel enumEntry : enumClass.getEnumConstants()) {
			
			String enumName = enumEntry.name();
			String label = enumEntry.getLocalizedLabel();
			
			EnumWithLocalizedLabelComboBoxModel model = new EnumWithLocalizedLabelComboBoxModel(enumName, label);
			getStore().add(model);
		}
	}
	
	/**
	 * Returneaza constanta din enum corespunzatoare elementului selectat.
	 * Daca nu s-a selectat nimic, atunci va returna null.
	 */
	public E getEnumConstantOfSelectedItem() {
		
		EnumWithLocalizedLabelComboBoxModel selectedModel = getValue();
		if (selectedModel == null) {
			return null;
		}
		
		String enumNameOfSelectedModel = selectedModel.getEnumName();
		E enumConstantOfSelectedModel = Enum.valueOf(enumClass, enumNameOfSelectedModel);
		return enumConstantOfSelectedModel;
	}

	public static class EnumWithLocalizedLabelComboBoxModel extends BaseModel {

		private static final long serialVersionUID = 1L;
		
		public static final String PROPERTY_ENUM_NAME = "enumName";
		public static final String PROPERTY_LABEL = "label";
		
		public EnumWithLocalizedLabelComboBoxModel(String enumName, String label) {
			setEnumName(enumName);
			setLabel(label);
		}
		
		public String getEnumName() {
			return get(PROPERTY_ENUM_NAME);
		}
		
		public void setEnumName(String enumName) {
			set(PROPERTY_ENUM_NAME, enumName);
		}
		
		public String getLabel() {
			return get(PROPERTY_LABEL);
		}
		
		public void setLabel(String label) {
			set(PROPERTY_LABEL, label);
		}
	}
}