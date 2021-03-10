package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.MetadataTypeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class MetadataTypeComboBox extends ComboBox<MetadataTypeOption> {
	
	private static Map<String, MetadataTypeOption> optionMap;
	
	private MetadataTypeComboBox(boolean forCollection) {
		setEditable(false);
		setTriggerAction(TriggerAction.ALL);
		setDisplayField(MetadataTypeOption.PROPERTY_LABEL);
		setStore(new ListStore<MetadataTypeOption>());
		if (forCollection) {
			store.add(getOptionMap().get(MetadataDefinitionModel.TYPE_TEXT));
			store.add(getOptionMap().get(MetadataDefinitionModel.TYPE_NUMERIC));
			store.add(getOptionMap().get(MetadataDefinitionModel.TYPE_AUTO_NUMBER));
			store.add(getOptionMap().get(MetadataDefinitionModel.TYPE_DATE));
			store.add(getOptionMap().get(MetadataDefinitionModel.TYPE_LIST));
		} else {
			for (MetadataTypeOption option : getOptionMap().values()) {
				store.add(option);
			}
		}
	}
	
	public static MetadataTypeComboBox getInstance() {
		return new MetadataTypeComboBox(false);
	}
	
	public static MetadataTypeComboBox getInstanceForCollection() {
		return new MetadataTypeComboBox(true);
	}

	/** Returneaza tipul selectat SAU "" daca nu s-a selectat nimic. */
	public String getSelectedType() {
		if (getValue() != null) {
			return getValue().getValue();
		} else {
			return "";
		}
	}
	
	public void setSelectedType(String metadataType) {
		setValue(getOptionMap().get(metadataType));
	}
	
	private static Map<String, MetadataTypeOption> getOptionMap() {
		if (optionMap == null) {
			optionMap = new HashMap<String, MetadataTypeOption>();
			
			for(Entry<String, String> metadataTypeLabelByTypeNameEntry : MetadataTypeConstants.getMetadataTypeLabelByTypeName().entrySet()) {
				String typeName = metadataTypeLabelByTypeNameEntry.getKey();
				String typeLabel = metadataTypeLabelByTypeNameEntry.getValue();
				optionMap.put(typeName, new MetadataTypeOption(typeName, typeLabel));
			}
		}
		return optionMap;
	}
}