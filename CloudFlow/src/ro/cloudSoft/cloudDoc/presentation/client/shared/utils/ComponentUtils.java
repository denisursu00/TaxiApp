package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.ArrayList;
import java.util.Collection;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.ValidatorProvider;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.ui.Widget;

/**
 * Contine metode utilitare pentru elemente de interfata GXT.
 * 
 * 
 */
public class ComponentUtils {
	
	public static final Widget DUMMY_WIDGET = new Widget();
	
	// Daca un camp ocupa mai mult de 90%, atunci cateodata, cand campul e invalid, va aparea un scroll orizontal inutil.
	public static final FormData FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH = new FormData("90%");
	
	/**
	 * Reprezinta un callback pentru un message box de tip confirmare.
	 * 
	 * Suprascrierea metodelor este optionala intrucat nu intotdeauna se doreste
	 * specificarea unei actiuni pentru ambele raspunsuri ("da" si "nu").
	 * 
	 * Implicit, la alegerea raspunsului, nu se intampla nimic.
	 * Pentru a specifica o actiune la alegerea unui raspuns, se va suprascrie
	 * metoda corespunzatoare raspunsului dorit.
	 * 
	 * 
	 */
	public static abstract class ConfirmCallback {
		
		/**
		 * Specifica ce ar trebui sa se execute daca s-a ales "da".
		 */
		public void onYes() {}
		
		/**
		 * Specifica ce ar trebui sa se execute daca s-a ales "nu".
		 */
		public void onNo() {}
	}
	
	/**
	 * Cere confirmarea unei operatiuni printr-un message box.
	 * 
	 * @param title titlul message box-ului
	 * @param message mesajul din interiorul message box-ului
	 * @param confirmCallback callback-ul ce va specifica actiunea in functie de
	 * optiunea aleasa ("da" sau "nu")
	 */
	public static void confirm(String title, String message, final ConfirmCallback confirmCallback) {
		MessageBox.confirm(title, message, new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent mbe) {
				if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
					confirmCallback.onYes();
				} else {
					confirmCallback.onNo();
				}
			}
		});
	}

	/**
	 * Verifica daca , containerul contine componenta precizata.
	 */
	public static boolean containerHasComponent(Container<?> container, Component component) {
		return (container.findItem(component.getElement()) != null);
	}
	
	/**
	 * Returneaza valoarea REALA a unui checkbox.
	 * Problema unui checkbox e ca, daca nu se umbla la el (nu se bifeaza sau
	 * debifeaza), valoarea returnata va fi <code>null</code>, nu <code>false</code>.
	 * @param checkBox checkbox-ul
	 * @return valoarea reala a checkbox-ului
	 */
	public static Boolean getCheckBoxValue(CheckBox checkBox) {
		return (checkBox.getValue() != null) ? checkBox.getValue() : Boolean.FALSE;
	}
	
	/**
	 * Returneaza valoarea intreaga din campul dat.
	 * Daca nu este completat campul, atunci va returna null.
	 */
	public static Integer getIntegerFromField(NumberField field) {
		Number value = field.getValue();
		return (value != null) ? value.intValue() : null;
	}
	
	/**
	 * Obtine fereastra parinte a componentei alese.
	 * @param widget componenta
	 * @return fereastra parinte a componentei alese
	 */
	public static Window getParentWindow(Widget widget) {
		Widget candidate = widget;
		while ((candidate != null) && !(candidate instanceof Window)) {
			candidate = candidate.getParent();
		}
		return (candidate instanceof Window) ? (Window) candidate : null;
	}
	
	public static void putParentWindowToBackIfExists(Widget widget) {
		Window parentWindow = getParentWindow(widget);
		if (parentWindow == null) {
			return;
		}
		parentWindow.toBack();
	}
	
	/**
	 * Obtine un buton nou de inchidere a unei ferestre.
	 * @param parentWidget elementul in care se adauga fereastra
	 */
	public static Button getNewCloseButton(Widget parentWidget) {
		
		final Window parentWindow = getParentWindow(parentWidget);

		Button closeButton = new Button();
		
		closeButton.setText(GwtLocaleProvider.getConstants().CLOSE());
		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				if (parentWindow != null) {
					parentWindow.hide();
				}
			}
		});
		
		return closeButton;
	}
	
	/**
	 * Returneaza label-ul corect in functie de obligativitatea campului asociat.
	 * @param currentLabel label-ul curent
	 * @param isFieldMandatory daca , campul este obligatoriu
	 */
	public static String getProperFieldLabel(String currentLabel, boolean isFieldMandatory) {
		int index = currentLabel.lastIndexOf(" *");
		if (isFieldMandatory) {
			return (index == -1) ? currentLabel + " *" : currentLabel;
		} else {
			return (index == -1) ? currentLabel : currentLabel.substring(0, index);
		}
	}
	
	/**
	 * Verifica daca un checkbox este bifat, tinand cont de valoarea
	 * <code>null</code>.
	 * @param checkBox checkbox-ul
	 * @return <code>true</code> daca checkbox-ul este bifat, <code>false</code>
	 * altfel
	 */
	public static boolean isChecked(CheckBox checkBox) {
		return ((checkBox.getValue() != null) && checkBox.getValue().equals(Boolean.TRUE));
	}
	
	/**
	 * Seteaza campul ca fiind obligatoriu in formularul ce il contine.
	 * Atentie! Un camp poate avea un SINGUR validator.
	 * @param field campul (poate fi <code>TextField</code>, <code>NumberField</code>,
	 * <code>DateField</code>)
	 */
	public static void makeRequiredField(TextField<?> field) {
		field.setAllowBlank(false);
		field.setValidator(ValidatorProvider.getRequiredFieldValidator());
	}
	
	/** Verifica daca o fereastra este minimizata in desktop-ul GXT. */
	public static boolean isWindowMinimized(Window window) {
		return (window.getData("minimize") != null);
	}
	
	/**
	 * Elimina mesajele de validare pentru campurile din formularul dat.
	 * Metoda este utila atunci cand se reincarca sau se goleste formularul.
	 */
	public static void clearValidationMessages(FormPanel form) {
		for (Field<?> field : form.getFields()) {
			field.clearInvalid();
		}
	}
	
	public static void reValidateForm(FormPanel form) {
		for (Field<?> field : form.getFields()) {
			field.validate();
		}
	}
	
	/**
	 * Se asigura ca grid-ul dat este afisat corect.
	 * Aceasta operatie este necesara cand, cateodata, un grid nu este afisat bine
	 * (o coloana e prea mica sau scroll-ul apare iesit din container).
	 */
	public static <M extends ModelData> void ensureGridIsProperlyRendered(Grid<M> grid) {
		grid.reconfigure(grid.getStore(), grid.getColumnModel());
	}
	
	/**
	 * Returneaza "stramosii" (parintele, parintele parintelui s.a.m.d.) modelului din arborele cu store-ul dat.
	 * Daca modelul nu are stramori, atunci va returna o colectie goala.
	 * 
	 * @throws IllegalArgumentException daca modelul NU face parte din arborele cu store-ul dat
	 */
	public static Collection<ModelData> getAncestors(TreeStore<ModelData> treeStore, ModelData model) {
		
		if (treeStore.findModel(model) == null) {
			throw new IllegalArgumentException("Modelul [" + model + "] NU face parte din arborele cu store-ul dat.");
		}
		
		Collection<ModelData> ancestors = new ArrayList<ModelData>();
		while ((model = treeStore.getParent(model)) != null) {
			ancestors.add(model);
		}
		return ancestors;
	}
	
	// Nu merge.
	public static void openPopup(Widget opener, PopupWindowWrapper popupWindowWrapper) {
		Window openerWindow = getParentWindow(opener);
		if (openerWindow != null) {
			openerWindow.toBack();
		}
		popupWindowWrapper.open();
	}
	
	public static abstract class PopupWindowWrapper {
		
		public PopupWindowWrapper(final Window popupWindow) {
			popupWindow.addListener(Events.Show, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					popupWindow.toFront();
					popupWindow.removeListener(Events.Show, this);
				}
			});
		}
		
		public abstract void open();
	}
	
	/**
	 * Modifica ContentPanel-ul a.i. sa aiba bordura doar in partea de jos.
	 */
	public static void setBottomBorderOnly(ContentPanel contentPanel) {
		
		if (contentPanel.isRendered()) {
			throw new IllegalStateException("Nu se poate seta bordura daca panoul a fost randat deja.");
		}
		
		contentPanel.setBodyBorder(true);
		contentPanel.setBodyStyleName("x-panel-noTopBorder x-panel-noLeftBorder x-panel-noRightBorder");
	}
}