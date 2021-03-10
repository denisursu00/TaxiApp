package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import com.extjs.gxt.ui.client.widget.form.CheckBox;

/**
 * Contine logica suplimentara a.i. pentru CheckBox, (null == false).
 */
public class CheckBoxWithNullFix extends CheckBox {
	
	public CheckBoxWithNullFix() {
		
		super();
		
		/*
		 * Valoarea initiala trebuie sa fie null a.i. la debifare
		 * (sau setare cu false) sa se declanseze evenimentul corespunzator.
		 */
		value = null;
	}

	@Override
	public Boolean getValue() {
		Boolean value = super.getValue();
		return (value != null) ? value : false;
	}
	
	@Override
	public void setValue(Boolean value) {
		if (value != null) {
			super.setValue(value);
		} else {
			super.setValue(false);
		}
	}
	
	public void setChecked(boolean checked) {
		setValue(checked);
	}
	
	public boolean isChecked() {
		return (getValue());
	}
}