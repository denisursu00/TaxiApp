package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.web.GwtUrlBuilder;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.menu.Item;
import com.google.gwt.user.client.Window;

/**
 * Contine metode pentru operatii legate de navigarea utilizatorului in browser.
 * 
 * 
 */
public class NavigationUtils {

	/**
	 * Redirectioneaza utilizatorul spre pagina precizata prin URL.
	 * @param url adresa paginii web
	 */
	public static void redirect(String url) {
		Window.Location.replace(url);
	}
	
	/**
	 * Reincarca pagina curenta.
	 */
	public static void refresh() {
		Window.Location.reload();
	}
	
	/**
	 * Iese din aplicatie.
	 * 
	 * @param module modulul din care se face iesirea
	 */
	public static void logout(String module) {
		String requestParameterNameRequestedModule = GwtRegistryUtils
			.getWebConstants().getRequestParameterOrAttributeNameRequestedModule();
		String logoutUrl = new GwtUrlBuilder(NavigationConstants.getLogoutLink())
			.setParameter(requestParameterNameRequestedModule, module)
			.build();
		redirect(logoutUrl);
	}
	
	/**
	 * Creeaza o corespondenta intre elementul din meniu si pagina precizata.
	 * @param menuItem elementul din meniu
	 * @param url URL-ul paginii
	 */
	public static void linkTo(Item menuItem, final String url) {
		menuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent event) {
				redirect(url);
			}
		});
	}

	/**
	 * Creeaza o corespondenta intre buton si pagina precizata.
	 * @param button butonul
	 * @param url URL-ul paginii
	 */
	public static void linkTo(Button button, final String url) {
		button.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				redirect(url);
			}
		});
	}

	/**
	 * Creeaza o corespondenta intre buton si pagina precizata.
	 * @param button butonul
	 * @param url URL-ul paginii
	 */
	public static void linkTo(IconButton button, final String url) {
		button.addSelectionListener(new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent event) {
				redirect(url);
			}
		});
	}
}