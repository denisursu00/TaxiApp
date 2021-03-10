package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;

/**
 * Camp ce tine un widget custom
 * 
 * Inaltimea (height) campului trebuie specificata la construire.
 * Latimea (width) campului va fi stabilita la adaugarea campului in formular (vezi FormData).
 */
public abstract class CustomAdapterFieldWithAutoWidth extends CustomAdapterField {

	private LayoutContainer outerContainer;
	
	protected CustomAdapterFieldWithAutoWidth(int height) {
		
		super(ComponentUtils.DUMMY_WIDGET);
		
		setResizeWidget(true);
		
		outerContainer = new LayoutContainer();
		outerContainer.setLayout(new FitLayout());
		outerContainer.setHeight(height);
		
		widget = outerContainer;
	}
	
	/**
	 * Metoda trebuie apelata pentru a seta componenta care va fi inclusa in AdapterField.
	 */
	protected void setWidgetOfField(Widget widget) {
		outerContainer.removeAll();
		outerContainer.add(widget, new FitData(5));
		if (outerContainer.isRendered()) {
			outerContainer.layout();
		}
	}
	
	@Override
	public void recalculate() {
		super.recalculate();
		outerContainer.layout(true);
	}
}