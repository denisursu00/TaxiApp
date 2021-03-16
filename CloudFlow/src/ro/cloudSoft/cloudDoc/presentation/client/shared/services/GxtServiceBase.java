package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interfata de baza pentru serviciile GWT RPC
 * 
 * 
 */
public interface GxtServiceBase extends RemoteService {

	/**
	 * Metoda trebuie sa aiba ca parametri obiecte de toate tipurile ce sunt trimise prin GWT RPC
	 * si extind / implementeaza java.io.Serializable. Acest lucru este necesar pentru ca tipurile sa fie incluse in
	 * serialization policy-ul serviciului. In caz contrar, la trimiterea unui obiect de un tip care nu se regaseste in
	 * parametrii metodei, GWT va arunca o exceptie de serializare.
	 * 
	 * De asemenea, ar trebui sa declare ca poate arunca toate tipurile de exceptii care ar putea fi trimise catre
	 * partea GWT client.
	 */
	void dummy(PermissionModel permissionModel, SecurityManagerModel securityManagerModel, UserModel userModel)
		throws PresentationException;
}