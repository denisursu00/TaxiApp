package ro.cloudSoft.cloudDoc.presentation.server.converters.registruDocumenteJustificativePlati;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiDao;
import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlati;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator.NomenclatorValueConverter;

public class RegistruDocumenteJustificativePlatiConverter {
	
	private RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao;
	private NomenclatorValueConverter nomenclatorValueConverter;
	private NomenclatorValueDao nomenclatorValueDao;

	public RegistruDocumenteJustificativePlatiModel getModelFromRegistruDocumenteJustificativePlati(
			RegistruDocumenteJustificativePlati entity) {

		RegistruDocumenteJustificativePlatiModel model = new RegistruDocumenteJustificativePlatiModel();
		
		model.setId(entity.getId());
		model.setLunaInregistrare(getRegistrationMonth(entity.getDataInregistrare()));
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setDataInregistrare(entity.getDataInregistrare());
		model.setEmitent(entity.getEmitent());
		model.setTipDocument(nomenclatorValueConverter.getModel(entity.getTipDocument()));
		model.setNumarDocument(entity.getNumarDocument());
		model.setDataDocument(entity.getDataDocument());
		model.setModLivrare(entity.getModLivrare());
		model.setDetalii(entity.getDetalii());
		model.setValoare(entity.getValoare());
		model.setMoneda(nomenclatorValueConverter.getModel(entity.getMoneda()));
		model.setDataScadenta(entity.getDataScadenta());
		model.setModalitatePlata(entity.getModalitatePlata());
		model.setReconciliereCuExtrasBanca(entity.isReconciliereCuExtrasBanca());
		model.setPlatit(entity.isPlatit());
		model.setDataPlatii(entity.getDataPlatii());
		model.setIncadrareConformBVC(entity.getIncadrareConformBVC());
		model.setIntrareEmitere(entity.getIntrareEmitere());
		model.setPlataScadenta(entity.getPlataScadenta());
		model.setScadentaEmitere(entity.getScadentaEmitere());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		
		if (!model.isPlatit()) {
			recalculatePlataScadenta(model);
		}
		
		model.setAtasamente(getAtasamenteModelFromRegistruDocumenteJustificativePlatiEntity(entity));
		
		return model;
	}
	
	private void recalculatePlataScadenta(RegistruDocumenteJustificativePlatiModel model) {
		Date dataScadenta = model.getDataScadenta();
		Long oneDayInMiliseconds = 24*60*60*1000L;
		
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
        Date currentDate = calendar.getTime();
		
		Double days = Math.floor( (currentDate.getTime() - dataScadenta.getTime()) / oneDayInMiliseconds );
		model.setPlataScadenta(days.longValue());
	}

	public RegistruDocumenteJustificativePlati getEntityFromModel(
			RegistruDocumenteJustificativePlatiModel model) {
		
		RegistruDocumenteJustificativePlati entity = null;
		if (model.getId() != null) {
			entity = registruDocumenteJustificativePlatiDao.find(model.getId());
		} else {
			entity = new RegistruDocumenteJustificativePlati();
		}
		
		entity.setNumarInregistrare(model.getNumarInregistrare());
		entity.setDataInregistrare(model.getDataInregistrare());
		entity.setEmitent(model.getEmitent());
		entity.setTipDocument(nomenclatorValueDao.find(model.getTipDocument().getId()));
		entity.setNumarDocument(model.getNumarDocument());
		entity.setDataDocument(model.getDataDocument());
		entity.setModLivrare(model.getModLivrare());
		entity.setDetalii(model.getDetalii());
		entity.setValoare(model.getValoare());
		entity.setMoneda(nomenclatorValueDao.find(model.getMoneda().getId()));
		entity.setDataScadenta(model.getDataScadenta());
		entity.setModalitatePlata(model.getModalitatePlata());
		entity.setReconciliereCuExtrasBanca(model.isReconciliereCuExtrasBanca());
		entity.setPlatit(model.isPlatit());
		entity.setDataPlatii(model.getDataPlatii());
		entity.setIncadrareConformBVC(model.getIncadrareConformBVC());
		entity.setIntrareEmitere(model.getIntrareEmitere());
		entity.setPlataScadenta(model.getPlataScadenta());
		entity.setScadentaEmitere(model.getScadentaEmitere());
		entity.setAnulat(model.isAnulat());
		entity.setMotivAnulare(model.getMotivAnulare());
		
		return entity;
	}
	
	private List<AtasamentModel> getAtasamenteModelFromRegistruDocumenteJustificativePlatiEntity(RegistruDocumenteJustificativePlati entity) {
		List<AtasamentModel> atasamente = new ArrayList<>();
		for (RegistruDocumenteJustificativePlatiAtasament atasament : entity.getAtasamente()) {
			AtasamentModel atasamentModel = new AtasamentModel();
			atasamentModel.setId(atasament.getId());
			atasamentModel.setFileName(atasament.getFileName());
			atasamente.add(atasamentModel);
		}
		return atasamente;
	}
	
	private String getRegistrationMonth(Date date) {		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM");
		return simpleDateFormat.format(date).toUpperCase();
	}

	public void setRegistruDocumenteJustificativePlatiDao(
			RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao) {
		this.registruDocumenteJustificativePlatiDao = registruDocumenteJustificativePlatiDao;
	}
	
	public void setNomenclatorValueConverter(NomenclatorValueConverter nomenclatorValueConverter) {
		this.nomenclatorValueConverter = nomenclatorValueConverter;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

}
