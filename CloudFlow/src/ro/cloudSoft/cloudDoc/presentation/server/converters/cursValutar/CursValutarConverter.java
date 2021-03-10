package ro.cloudSoft.cloudDoc.presentation.server.converters.cursValutar;

import ro.cloudSoft.cloudDoc.domain.cursValutar.CursValutar;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.cursValutar.CursValutarModel;

public class CursValutarConverter {
	
	public CursValutarModel toModel(CursValutar entity) {
		CursValutarModel model = new CursValutarModel();
		model.setId(entity.getId());
		model.setEur(entity.getEur());
		model.setUsd(entity.getUsd());
		model.setData(entity.getData());
		return model;
	}
}
