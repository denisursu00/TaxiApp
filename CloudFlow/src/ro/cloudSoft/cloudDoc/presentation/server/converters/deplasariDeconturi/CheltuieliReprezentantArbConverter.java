package ro.cloudSoft.cloudDoc.presentation.server.converters.deplasariDeconturi;

import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.CheltuieliReprezentantArbDao;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.TipDocumentJustificativForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.ValutaForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.CheltuialaReprezentantArbModel;

public class CheltuieliReprezentantArbConverter {
	
	private CheltuieliReprezentantArbDao cheltuieliReprezentantArbDao;
	
	public void setCheltuieliReprezentantArbDao(CheltuieliReprezentantArbDao cheltuieliReprezentantArbDao) {
		this.cheltuieliReprezentantArbDao = cheltuieliReprezentantArbDao;
	}
	
	public CheltuialaReprezentantArb getEntityFromModel(CheltuialaReprezentantArbModel model) {
		
		CheltuialaReprezentantArb entity = null;
		
		if (model.getId() != null) {
			entity = cheltuieliReprezentantArbDao.findById(model.getId());
		} else {
			entity = new CheltuialaReprezentantArb();
		}
		
		entity.setValuta(ValutaForCheltuieliReprezentantArbEnum.valueOf(model.getValuta()));
		entity.setCursValutar(model.getCursValutar());
		entity.setModalitatePlata(ModalitatePlataForDecontEnum.valueOf(model.getModalitatePlata()));
		entity.setNumarDocumentJustificativ(model.getNumarDocumentJustificativ());
		entity.setDataDocumentJustificativ(model.getDataDocumentJustificativ());
		entity.setTipDocumentJustificativ(TipDocumentJustificativForCheltuieliReprezentantArbEnum.valueOf(model.getTipDocumentJustificativ()));
		entity.setValoareCheltuiala(model.getValoareCheltuiala());
		entity.setExplicatie(model.getExplicatie());
		
		return entity;
	}

	public CheltuialaReprezentantArbModel getModelFromEntity(CheltuialaReprezentantArb entity) {
		
		CheltuialaReprezentantArbModel model = new CheltuialaReprezentantArbModel();
		
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
