package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.user.client.Element;

/**
 * Contine un woraround pentru situatiile cand la modificarea vizibilitatii FieldSet-ului,
 * unele componente (de obicei AdapterField) NU sunt afisate corect.
 */
public class FieldSetWithFixes extends FieldSet {
	
	@Override
	protected void onRender(Element parent, int pos) {
		
		super.onRender(parent, pos);
		
		if (isExpanded()) {
			onExpand();
		}
	}

	@Override
	protected void onShow() {
		super.onShow();
		// Se reface layout a.i. componentele se vor re-desena.
		layout(true);
	}
}