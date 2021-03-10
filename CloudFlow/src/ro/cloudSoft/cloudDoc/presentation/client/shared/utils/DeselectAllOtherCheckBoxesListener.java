package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Listener pentru schimbarea starii unui checkbox a.i. sa fie un singur checkbox selectat din mai multe
 */
public class DeselectAllOtherCheckBoxesListener implements Listener<FieldEvent> {

	private final CheckBoxesProvider checkBoxesProvider;
	
	public DeselectAllOtherCheckBoxesListener(CheckBoxesProvider checkboxesProvider) {
		this.checkBoxesProvider = checkboxesProvider;
	}
	
	@Override
	public void handleEvent(FieldEvent event) {
		
		Field<?> currentField = event.getField();
		if (!(currentField instanceof CheckBox)) {
			String exceptionMessage = "Listener-ul este pentru checkbox-uri, insa " +
				"s-a declansat eveniment pentru [" + currentField.getClass().getName() + "].";
			throw new IllegalStateException(exceptionMessage);
		}
		CheckBox currentCheckBox = (CheckBox) currentField;
		
		Boolean oldValue = (Boolean) event.getOldValue();
		Boolean newValue = (Boolean) event.getValue();
		
		boolean isCheckBoxChecked = (!GwtBooleanUtils.isTrue(oldValue) && GwtBooleanUtils.isTrue(newValue));
		
		if (isCheckBoxChecked) {
			for (CheckBox checkBox : checkBoxesProvider.getCheckBoxes()) {
				if (!checkBox.equals(currentCheckBox)) {
					checkBox.setValue(false);
				}
			}
		}
	}
	
	public static interface CheckBoxesProvider {
		List<CheckBox> getCheckBoxes();
	}
	
	public static class ContainerCheckBoxesProvider implements CheckBoxesProvider {
		
		private final Container<? extends Component> container;
		
		public ContainerCheckBoxesProvider(Container<? extends Component> container) {
			this.container = container;
		}
		
		@Override
		public List<CheckBox> getCheckBoxes() {
			List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
			for (Component component : container.getItems()) {
				if (!(component instanceof CheckBox)) {
					String exceptionMessage = "Container-ul trebuie sa aiba doar checkbox-uri. " +
						"S-a gasit insa element de tipul [" + component.getClass().getName() + "].";
					throw new IllegalStateException(exceptionMessage);
				}
				CheckBox checkBox = (CheckBox) component;
				checkBoxes.add(checkBox);
			}
			return checkBoxes;
		}
	}
	
	public static class CheckBoxGroupCheckBoxesProvider implements CheckBoxesProvider {
		
		private final CheckBoxGroup checkBoxGroup;
		
		public CheckBoxGroupCheckBoxesProvider(CheckBoxGroup checkBoxGroup) {
			this.checkBoxGroup = checkBoxGroup;
		}
		
		@Override
		public List<CheckBox> getCheckBoxes() {
			List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
			for (Field<?> field : checkBoxGroup.getAll()) {
				if (!(field instanceof CheckBox)) {
					String exceptionMessage = "CheckBoxGroup-ul trebuie sa aiba doar checkbox-uri. " +
						"S-a gasit insa element de tipul [" + field.getClass().getName() + "].";
					throw new IllegalStateException(exceptionMessage);
				}
				CheckBox checkBox = (CheckBox) field;
				checkBoxes.add(checkBox);
			}
			return checkBoxes;
		}
	}
}