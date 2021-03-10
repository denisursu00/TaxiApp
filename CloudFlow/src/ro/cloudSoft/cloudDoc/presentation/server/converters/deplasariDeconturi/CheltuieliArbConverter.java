package ro.cloudSoft.cloudDoc.presentation.server.converters.deplasariDeconturi;

import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.CheltuieliArbDao;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.TipDocumentJustificativForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.ValutaForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.CheltuialaArbModel;

public class CheltuieliArbConverter {
	
	private CheltuieliArbDao cheltuieliArbDao;
	
	public void setCheltuieliArbDao(CheltuieliArbDao cheltuieliArbDao) {
		this.cheltuieliArbDao = cheltuieliArbDao;
	}
	
	public CheltuialaArb getEntityFromModel(CheltuialaArbModel model) {
		
		CheltuialaArb entity = null;
		
		if (model.getId() != null) {
			entity = cheltuieliArbDao.findById(model.getId());
		} else {
			entity = new CheltuialaArb();
		}
		
		entity.setValuta(ValutaForCheltuieliArbEnum.valueOf(model.getValuta()));
		entity.setCursValutar(model.getCursValutar());
		entity.setModalitatePlata(ModalitatePlataForDecontEnum.valueOf(model.getModalitatePlata()));
		entity.setNumarDocumentJustificativ(model.getNumarDocumentJustificativ());
		entity.setDataDocumentJustificativ(model.getDataDocumentJustificativ());
		entity.setTipDocumentJustificativ(TipDocumentJustificativForCheltuieliArbEnum.valueOf(model.getTipDocumentJustificativ()));
		entity.setValoareCheltuiala(model.getValoareCheltuiala());
		entity.setExplicatie(model.getExplicatie());
		
		return entity;
	}

	public CheltuialaArbModel getModelFromEntity(CheltuialaArb entity) {
		
		CheltuialaArbModel model = new CheltuialaArbModel();
		
		model.setId(entity.getId());
		model.setValuta(entity.getValuta().toString());
		model.setCursValutar(entity.getCursValutar());
		model.setModalitatePlata(entity.getModalitatePlata().toString());
		model.setNumarDocumentJustificativ(entity.getNumarDocumentJustificativ());
		model.setDataDocumentJustificativ(entity.getDataDocumentJustificativ());
		model.setTipDocumentJustificativ(entity.getTipDocumentJustificativ().toString());
		model.setValoareCheltuiala(entity.getValoareCheltuiala());
		model.setExplicatie(entity.getExplicatie());
		
		return model;
	}

}
