package ro.cloudSoft.cloudDoc.presentation.client.shared;


import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener implements Listener<FieldEvent> {
	
	private static final int PIXELS_PER_CHARACTER = 7;
	 
	private static final DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener INSTANCE = new DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener();
	
	@Override
	public void handleEvent(FieldEvent fe) {
		
		if (!(fe.getField() instanceof ComboBox)) {
			throw new IllegalArgumentException("Only ComboBox type components can use this listener.");
		}
		
		int max = 0;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ComboBox<? extends BaseModelData> comboBox = (ComboBox) fe.getField();
		String displayField = comboBox.getDisplayField();

		if (GwtStringUtils.isNotBlank(displayField)) {
			for (ModelData modelData : comboBox.getStore().getModels()) {
				Object displayFieldValue = modelData.get(displayField);
				if (displayFieldValue != null) {
					int length = displayFieldValue.toString().length();
					if (length > max ) {
						max = length;
					}
				}
			}
		}

		comboBox.setMinListWidth(max * PIXELS_PER_CHARACTER);
		comboBox.repaint();
	}
	
	
	public static void attachTo(ComboBox<? extends BaseModelData> comboBox) {
		comboBox.addListener(Events.TriggerClick, INSTANCE);
	}
	
}
