package ro.cloudSoft.cloudDoc.services.cursValutar;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.cursValutar.CursValutarModel;

public interface CursValutarService {
	
	CursValutarModel getCursValutarCurent();

	void loadCursValutar() throws Exception;
}
