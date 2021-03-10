package ro.cloudSoft.cloudDoc.presentation.server.converters.alteDeconturi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.alteDeconturi.AlteDeconturiDao;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi.TipAvansPrimitEnum;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuialaAtasament;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala.TipDocumentJustificativ;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiCheltuialaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiCheltuialaViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiCheltuialaAtasamentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiCheltuialaAtasamentViewModel;

public class AlteDeconturiConverter {

	private AlteDeconturiDao alteDeconturiDao;
	
	public void setAlteDeconturiDao(AlteDeconturiDao alteDeconturiDao) {
		this.alteDeconturiDao = alteDeconturiDao;
	}
	
	public List<AlteDeconturiViewModel> getAlteDeconturiViewModelsFromEntities(List<AlteDeconturi> entities) {
		List<AlteDeconturiViewModel> deconturiViewModels = new ArrayList<AlteDeconturiViewModel>();
		for (AlteDeconturi decont : entities) {
			AlteDeconturiViewModel decontViewModel = getAlteDeconturiViewModelFromEntity(decont);
			deconturiViewModels.add(decontViewModel);
		}
		return deconturiViewModels;
	}

	public AlteDeconturiViewModel getAlteDeconturiViewModelFromEntity(AlteDeconturi entity) {
		
		AlteDeconturiViewModel model = new AlteDeconturiViewModel();
		
		model.setId(entity.getId());
		
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMMM");
		model.setMonth(simpleDateformat.format(entity.getDataDecont()).toUpperCase());
		model.setTitularDecont(entity.getTitularDecont());
		model.setNumarDecont(entity.getNumarDecont());
		model.setDataDecont(entity.getDataDecont());
		model.setAvansPrimit(entity.getAvansPrimit());
		model.setTipAvansPrimit(entity.getTipAvansPrimit().toString());
		model.setCheltuieli(getAlteDeconturiCheltuieliViewModelsFromEntities(entity.getCheltuieli()));
		model.setTotalCheltuieli(entity.getTotalCheltuieli());
		model.setTotalDeIncasatRestituit(entity.getTotalDeIncasatRestituit());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setFinalizat(entity.isFinalizat());
		
		return model;
	}

	public AlteDeconturiModel getAlteDeconturiModelFromEntity(AlteDeconturi entity) {
		
		AlteDeconturiModel model = new AlteDeconturiModel();
		
		model.setId(entity.getId());
		
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMMM");
		model.setMonth(simpleDateformat.format(entity.getDataDecont()).toUpperCase());
		model.setTitularDecont(entity.getTitularDecont());
		model.setNumarDecont(entity.getNumarDecont());
		model.setDataDecont(entity.getDataDecont());
		model.setAvansPrimit(entity.getAvansPrimit());
		model.setTipAvansPrimit(entity.getTipAvansPrimit().toString());
		model.setCheltuieli(getAlteDeconturiCheltuieliModelsFromEntities(entity.getCheltuieli()));
		model.setTotalCheltuieli(entity.getTotalCheltuieli());
		model.setTotalDeIncasatRestituit(entity.getTotalDeIncasatRestituit());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setFinalizat(entity.isFinalizat());
		
		return model;
	}

	private List<AlteDeconturiCheltuialaModel> getAlteDeconturiCheltuieliModelsFromEntities(List<AlteDeconturiCheltuiala> entities) {
		List<AlteDeconturiCheltuialaModel> cheltuieliModels = new ArrayList<AlteDeconturiCheltuialaModel>();
		for (AlteDeconturiCheltuiala cheltuiala : entities) {
			AlteDeconturiCheltuialaModel cheltuialaModel = getCheltuieliModelFromEntity(cheltuiala);
			cheltuieliModels.add(cheltuialaModel);
		}
		return cheltuieliModels;
	}

	private AlteDeconturiCheltuialaModel getCheltuieliModelFromEntity(AlteDeconturiCheltuiala entity) {
		
		AlteDeconturiCheltuialaModel model = new AlteDeconturiCheltuialaModel();
		
		model.setId(entity.getId());
		model.setTipDocumentJustificativ(entity.getTipDocumentJustificativ().toString());
		model.setExplicatie(entity.getExplicatie());
		model.setNumarDocumentJustificativ(entity.getNumarDocumentJustificativ());
		model.setDataDocumentJustificativ(entity.getDataDocumentJustificativ());
		model.setValoareCheltuiala(entity.getValoareCheltuiala());
		
		List<AlteDeconturiCheltuialaAtasamentModel> atasamenteModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entity.getAtasamente())) {
			for (AlteDeconturiCheltuialaAtasament atasament : entity.getAtasamente()) {
				AlteDeconturiCheltuialaAtasamentModel atasamentModel = getCheltuialaAtasamentModelFromEntity(atasament);
				atasamenteModel.add(atasamentModel);
			}
		}
		
		model.setAtasamente(atasamenteModel);
		
		return model;
	}

	public List<AlteDeconturiCheltuialaViewModel> getAlteDeconturiCheltuieliViewModelsFromEntities(List<AlteDeconturiCheltuiala> entities) {
		List<AlteDeconturiCheltuialaViewModel> cheltuieliViewModels = new ArrayList<AlteDeconturiCheltuialaViewModel>();
		for (AlteDeconturiCheltuiala cheltuiala : entities) {
			AlteDeconturiCheltuialaViewModel cheltuialaViewModel = getCheltuieliViewModelFromEntity(cheltuiala);
			cheltuieliViewModels.add(cheltuialaViewModel);
		}
		return cheltuieliViewModels;
	}

	public AlteDeconturiCheltuialaViewModel getCheltuieliViewModelFromEntity(AlteDeconturiCheltuiala entity) {
		
		AlteDeconturiCheltuialaViewModel model = new AlteDeconturiCheltuialaViewModel();
		
		model.setId(entity.getId());
		model.setTipDocumentJustificativ(entity.getTipDocumentJustificativ().toString());
		model.setExplicatie(entity.getExplicatie());
		model.setNumarDocumentJustificativ(entity.getNumarDocumentJustificativ());
		model.setDataDocumentJustificativ(entity.getDataDocumentJustificativ());
		model.setValoareCheltuiala(entity.getValoareCheltuiala());
		List<AlteDeconturiCheltuialaAtasamentViewModel> atasamenteModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entity.getAtasamente())) {
			for (AlteDeconturiCheltuialaAtasament atasament : entity.getAtasamente()) {
				AlteDeconturiCheltuialaAtasamentViewModel atasamentModel = getCheltuialaAtasamentViewModelFromEntity(atasament);
				atasamenteModel.add(atasamentModel);
			}
		}
		
		model.setAtasamenteModel(atasamenteModel);
		
		return model;
	}

	public AlteDeconturi getAlteDeconturiFromModel(AlteDeconturiModel model) {
		
		AlteDeconturi entity = null;
		
		if (model.getId() != null) {
			entity = alteDeconturiDao.findDecontById(model.getId());
		} else {
			entity = new AlteDeconturi();
		}
		
		entity.setTitularDecont(model.getTitularDecont());
		entity.setDataDecont(model.getDataDecont());
		entity.setAvansPrimit(model.getAvansPrimit());
		if (model.getTipAvansPrimit() != null) {
			entity.setTipAvansPrimit(TipAvansPrimitEnum.valueOf(model.getTipAvansPrimit()));
		}
		
		List<AlteDeconturiCheltuiala> cheltuieli = new ArrayList<>();
		for (AlteDeconturiCheltuialaModel cheltuieliModel : model.getCheltuieli()) {
			AlteDeconturiCheltuiala cheltuiala = null;
			if (cheltuieliModel.getId() != null) {
				cheltuiala = alteDeconturiDao.findCheltuialaById(cheltuieliModel.getId());
			} else {
				cheltuiala = new AlteDeconturiCheltuiala();
			}
			cheltuiala.setId(cheltuieliModel.getId());
			cheltuiala.setExplicatie(cheltuieliModel.getExplicatie());
			cheltuiala.setNumarDocumentJustificativ(cheltuieliModel.getNumarDocumentJustificativ());
			cheltuiala.setDataDocumentJustificativ(cheltuieliModel.getDataDocumentJustificativ());
			if (cheltuieliModel.getTipDocumentJustificativ() != null) {
				cheltuiala.setTipDocumentJustificativ(TipDocumentJustificativ.valueOf(cheltuieliModel.getTipDocumentJustificativ()));
			}
			cheltuiala.setValoareCheltuiala(cheltuieliModel.getValoareCheltuiala());
			
			List<AlteDeconturiCheltuialaAtasament> atasamente = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(cheltuieliModel.getAtasamente())) {
				for (AlteDeconturiCheltuialaAtasamentModel atasamentModel : cheltuieliModel.getAtasamente()) {
					AlteDeconturiCheltuialaAtasament atasament = getCheltuialaAtasamentEntityFromModel(atasamentModel);
					atasament.setAlteDeconturiCheltuiala(cheltuiala);
					atasamente.add(atasament);
				}
			}
			cheltuiala.setAtasamente(atasamente);
			cheltuiala.setAlteDeconturi(entity);
			cheltuieli.add(cheltuiala);
		}
		entity.setCheltuieli(cheltuieli);
		
		entity.setTotalCheltuieli(model.getTotalCheltuieli());
		entity.setTotalDeIncasatRestituit(model.getTotalDeIncasatRestituit());
		
		return entity;
	}
	
	private AlteDeconturiCheltuialaAtasament getCheltuialaAtasamentEntityFromModel(AlteDeconturiCheltuialaAtasamentModel model) {
		
		AlteDeconturiCheltuialaAtasament entity = null;
		
		if (model.getId() != null) {
			entity = alteDeconturiDao.findAtasamentOfCheltuialaById(model.getId());
		} else {
			entity = new AlteDeconturiCheltuialaAtasament();
		}
		
		entity.setFileName(model.getFileName());
			
		
		return entity;
	}

	private AlteDeconturiCheltuialaAtasamentModel getCheltuialaAtasamentModelFromEntity(AlteDeconturiCheltuialaAtasament entity) {
		
		AlteDeconturiCheltuialaAtasamentModel model = new AlteDeconturiCheltuialaAtasamentModel();
		
		model.setId(entity.getId());
		model.setFileName(entity.getFileName());
				
		return model;
	}
	
	private AlteDeconturiCheltuialaAtasamentViewModel getCheltuialaAtasamentViewModelFromEntity(AlteDeconturiCheltuialaAtasament entity) {
		
		AlteDeconturiCheltuialaAtasamentViewModel model = new AlteDeconturiCheltuialaAtasamentViewModel();
		
		model.setId(entity.getId());
		model.setFileName(entity.getFileName());
				
		return model;
	}

}
