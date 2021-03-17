package ro.taxiApp.docs.presentation.client.shared.utils;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Dialog;

/**
 * Clasa foloseste la afisarea mesajelor de eroare utilizatorului.
 * 
 * 
 */
public class ErrorHelper {
	
	private static List<String> errorMessages = new ArrayList<String>();
	
	/**
	 * Adauga o eroare.
	 * @param errorMessage mesajul de eroare
	 */
	public static void addError(String errorMessage) {
		errorMessages.add(errorMessage);
	}
	
	/**
	 * Afiseaza mesajele de eroare.
	 */
	public static void displayErrors() {
		if (errorMessages.size() > 0) {
			ErrorDialog.showErrors(errorMessages);
			errorMessages.clear();
		}
	}
	
	/**
	 * Reprezinta o fereastra ce afiseaza mesaje de eroare.
	 * 
	 * 
	 */
	private static class ErrorDialog extends Dialog {
		
		private static ErrorDialog errorDialog;
		
		private ErrorDialog() {
			setButtons(Dialog.OK);
			setHeading("Errors");
			setHideOnButtonClick(true);
			setModal(true);
			setScrollMode(Scroll.AUTO);
			setSize(400, 300);
		}
		
		public static void showErrors(List<String> errors) {
			if (errorDialog == null) {
				errorDialog = new ErrorDialog();
			}
			errorDialog.setErrors(errors);
			errorDialog.show();
			errorDialog.focus();
		}
		
		private void setErrors(List<String> errors) {
			removeAll();
			for (String error : errors) {
				addText(error);
			}
		}
	}
}