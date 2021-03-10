package ro.cloudSoft.cloudDoc.presentation.server.converters.arb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.arb.DiplomaMembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Stare;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.DiplomaMembruReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLModel;

public class ReprezentantiComisieSauGLConverter {
	
	private NomenclatorValueDao nomenclatorValueDao;
	private ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao;
	private UserPersistencePlugin userPersistencePlugin;
	
	public ReprezentantiComisieSauGLModel toModel(ReprezentantiComisieSauGL entity) {
		
		ReprezentantiComisieSauGLModel model = new ReprezentantiComisieSauGLModel();
		model.setId(entity.getId());
		model.setComisieSauGLId(entity.getComisieSauGL().getId());
		if (entity.getPresedinte() != null) {
			model.setPresedinteId(entity.getPresedinte().getId());
		}		
		model.setDataInceputMandatPresedinte(entity.getDataInceputMandatPresedinte());
		model.setDataExpirareMandatPresedinte(entity.getDataExpirareMandatPresedinte());
		if (entity.getVicepresedinte1() != null) {
			model.setVicepresedinte1Id(entity.getVicepresedinte1().getId());
		}
		model.setDataInceputMandatVicepresedinte1(entity.getDataInceputMandatVicepresedinte1());
		model.setDataExpirareMandatVicepresedinte1(entity.getDataExpirareMandatVicepresedinte1());
		if (entity.getVicepresedinte2() != null) {
			model.setVicepresedinte2Id(entity.getVicepresedinte2().getId());
		}
		model.setDataInceputMandatVicepresedinte2(entity.getDataInceputMandatVicepresedinte2());
		model.setDataExpirareMandatVicepresedinte2(entity.getDataExpirareMandatVicepresedinte2());
		if (entity.getVicepresedinte3() != null) {
			model.setVicepresedinte3Id(entity.getVicepresedinte3().getId());
		}
		model.setDataInceputMandatVicepresedinte3(entity.getDataInceputMandatVicepresedinte3());
		model.setDataExpirareMandatVicepresedinte3(entity.getDataExpirareMandatVicepresedinte3());
		model.setDataExpirareMandatVicepresedinte3(entity.getDataExpirareMandatVicepresedinte3());
		if (entity.getResponsabilARB() != null) {
			model.setResponsabilARBId(entity.getResponsabilARB().getId());	
		}	
		if (entity.getMembruCDCoordonator() != null) {
			model.setMembruCDCoordonatorId(entity.getMembruCDCoordonator().getId());			
		}
		model.setDataInceputMandatMembruCDCoordonator(entity.getDataInceputMandatMembruCDCoordonator());
		model.setDataExpirareMandatMembruCDCoordonator(entity.getDataExpirareMandatMembruCDCoordonator());
		
		List<MembruReprezentantiComisieSauGLModel> membriModels = new ArrayList<MembruReprezentantiComisieSauGLModel>();		
		if (CollectionUtils.isNotEmpty(entity.getMembri())) {			
			for (MembruReprezentantiComisieSauGL membru : entity.getMembri()) {
				membriModels.add(getMembruModel(membru));
			}
		}
		model.setMembri(membriModels);
		
		return model;
	}
	
	public ReprezentantiComisieSauGL toEntity(ReprezentantiComisieSauGLModel model) {
		
		ReprezentantiComisieSauGL entity = new ReprezentantiComisieSauGL();
		if (model.getId() != null) {
			entity = this.reprezentantiComisieSauGLDao.getById(model.getId());
		}
		entity.setComisieSauGL(this.nomenclatorValueDao.find(model.getComisieSauGLId()));
		if (model.getPresedinteId() != null) {
			entity.setPresedinte(this.nomenclatorValueDao.find(model.getPresedinteId()));			
		} else {
			entity.setPresedinte(null);
		}
		entity.setDataInceputMandatPresedinte(model.getDataInceputMandatPresedinte());
		entity.setDataExpirareMandatPresedinte(model.getDataExpirareMandatPresedinte());
		if (model.getVicepresedinte1Id() != null) {
			entity.setVicepresedinte1(this.nomenclatorValueDao.find(model.getVicepresedinte1Id()));			
		} else {
			entity.setVicepresedinte1(null);
		}
		entity.setDataInceputMandatVicepresedinte1(model.getDataInceputMandatVicepresedinte1());
		entity.setDataExpirareMandatVicepresedinte1(model.getDataExpirareMandatVicepresedinte1());
		if (model.getVicepresedinte2Id() != null) {
			entity.setVicepresedinte2(this.nomenclatorValueDao.find(model.getVicepresedinte2Id()));
		} else {
			entity.setVicepresedinte2(null);
		}
		entity.setDataInceputMandatVicepresedinte2(model.getDataInceputMandatVicepresedinte2());
		entity.setDataExpirareMandatVicepresedinte2(model.getDataExpirareMandatVicepresedinte2());
		if (model.getVicepresedinte3Id() != null) {
			entity.setVicepresedinte3(this.nomenclatorValueDao.find(model.getVicepresedinte3Id()));
		} else {
			entity.setVicepresedinte3(null);
		}
		entity.setDataInceputMandatVicepresedinte3(model.getDataInceputMandatVicepresedinte3());
		entity.setDataExpirareMandatVicepresedinte3(model.getDataExpirareMandatVicepresedinte3());
		if (model.getResponsabilARBId() != null) {
			entity.setResponsabilARB(this.userPersistencePlugin.getUserById(model.getResponsabilARBId()));
		} else {
			entity.setResponsabilARB(null);
		}
		if (model.getMembruCDCoordonatorId() != null) {
			entity.setMembruCDCoordonator(this.nomenclatorValueDao.find(model.getMembruCDCoordonatorId()));			
		} else {
			entity.setMembruCDCoordonator(null);
		}
		entity.setDataInceputMandatMembruCDCoordonator(model.getDataInceputMandatMembruCDCoordonator());
		entity.setDataExpirareMandatMembruCDCoordonator(model.getDataExpirareMandatMembruCDCoordonator());
		
		if (entity.getMembri() == null) {
			entity.setMembri(new ArrayList<MembruReprezentantiComisieSauGL>());
		}
		
		List<MembruReprezentantiComisieSauGL> membriToRemove = new ArrayList<MembruReprezentantiComisieSauGL>();
		for (MembruReprezentantiComisieSauGL membruEntity : entity.getMembri()) {
			boolean foundModelForThis = false;
			for (MembruReprezentantiComisieSauGLModel membruModel : model.getMembri()) {
				if (membruModel.getId() != null && membruEntity.getId().equals(membruModel.getId())) {
					foundModelForThis = true;
					updateMembruEntity(membruEntity, membruModel);
				}
			}
			if (!foundModelForThis) {
				membriToRemove.add(membruEntity);
			}
		}
		for (MembruReprezentantiComisieSauGLModel membruModel : model.getMembri()) {
			if (membruModel.getId() == null) {
				entity.getMembri().add(getMembruFromModel(membruModel, entity));
			}
		}
		if (CollectionUtils.isNotEmpty(membriToRemove)) {
			entity.getMembri().removeAll(membriToRemove);
		}
		
		return entity;
	}
	
	public MembruReprezentantiComisieSauGLModel getMembruModel(MembruReprezentantiComisieSauGL entity) {
		MembruReprezentantiComisieSauGLModel model = new MembruReprezentantiComisieSauGLModel();
		model.setId(entity.getId());
		model.setInstitutieId(entity.getInstitutie().getId());
		if (entity.getMembruInstitutie() != null) {
			model.setMembruInstitutieId(entity.getMembruInstitutie().getId());
		} else {
			model.setNume(entity.getNume());
			model.setPrenume(entity.getPrenume());
			model.setFunctie(entity.getFunctie());
			model.setDepartament(entity.getDepartament());
			model.setEmail(entity.getEmail());
			model.setTelefon(entity.getTelefon());
		}
		model.setReprezentantiComisieSauGLId(entity.getReprezentantiComisieSauGL().getId());
		model.setCalitate(entity.getCalitate().name());
		model.setStare(entity.getStare().name());
		model.setDiplome(getDiplomaModels(entity.getDiplome()));
		return model;
	}
	
	private void updateMembruEntity(MembruReprezentantiComisieSauGL entity, MembruReprezentantiComisieSauGLModel model) {
		
		entity.setInstitutie(nomenclatorValueDao.find(model.getInstitutieId()));
		
		entity.setMembruInstitutie(null);
		entity.setNume(null);
		entity.setPrenume(null);
		entity.setFunctie(null);
		entity.setDepartament(null);
		entity.setEmail(null);
		entity.setTelefon(null);
		
		if (model.getMembruInstitutieId() != null) {
			entity.setMembruInstitutie(nomenclatorValueDao.find(model.getMembruInstitutieId()));
		} else {			
			entity.setNume(model.getNume());
			entity.setPrenume(model.getPrenume());
			entity.setFunctie(model.getFunctie());
			entity.setDepartament(model.getDepartament());
			entity.setEmail(model.getEmail());
			entity.setTelefon(model.getTelefon());
		}
		
		entity.setStare(Stare.valueOf(model.getStare()));
		entity.setCalitate(Calitate.valueOf(model.getCalitate()));
		updateDiplomeMembru(entity, model.getDiplome());
	}
	
	private MembruReprezentantiComisieSauGL getMembruFromModel(MembruReprezentantiComisieSauGLModel model, ReprezentantiComisieSauGL reprezentantiComisieSauGL) {
		MembruReprezentantiComisieSauGL entity = new MembruReprezentantiComisieSauGL();
		entity.setId(model.getId());
		entity.setInstitutie(nomenclatorValueDao.find(model.getInstitutieId()));
		if (model.getMembruInstitutieId() != null) {
			entity.setMembruInstitutie(nomenclatorValueDao.find(model.getMembruInstitutieId()));
		} else {
			entity.setNume(model.getNume());
			entity.setPrenume(model.getPrenume());
			entity.setFunctie(model.getFunctie());
			entity.setDepartament(model.getDepartament());
			entity.setEmail(model.getEmail());
			entity.setTelefon(model.getTelefon());
		}
		entity.setReprezentantiComisieSauGL(reprezentantiComisieSauGL);
		entity.setStare(Stare.valueOf(model.getStare()));
		entity.setCalitate(Calitate.valueOf(model.getCalitate()));
		updateDiplomeMembru(entity, model.getDiplome());
		return entity;
	}
	
	private List<DiplomaMembruReprezentantiComisieSauGLModel> getDiplomaModels(List<DiplomaMembruReprezentantiComisieSauGL> diplome) {
		List<DiplomaMembruReprezentantiComisieSauGLModel> models = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(diplome)) {
			for (DiplomaMembruReprezentantiComisieSauGL diploma : diplome) {
				models.add(getModelFromDiploma(diploma));
			}
		}
		return models;
	}
	
	private void updateDiplomeMembru(MembruReprezentantiComisieSauGL membru, List<DiplomaMembruReprezentantiComisieSauGLModel> models) {
		
		if (membru.getDiplome() == null) {
			membru.setDiplome(new ArrayList<>());
		}
		
		List<DiplomaMembruReprezentantiComisieSauGL> diplomeToRemove = new ArrayList<>();
		for (DiplomaMembruReprezentantiComisieSauGL diplomaEntity : membru.getDiplome()) {
			boolean foundDiplomaModel = false;
			if (CollectionUtils.isNotEmpty(models)) {
				for (DiplomaMembruReprezentantiComisieSauGLModel diplomaModel : models) {
					if (diplomaModel.getId() != null && diplomaEntity.getId().equals(diplomaModel.getId())) {
						foundDiplomaModel = true;
						diplomaEntity.setDenumire(diplomaModel.getDenumire());
						diplomaEntity.setAn(diplomaModel.getAn());
						diplomaEntity.setObservatii(diplomaModel.getObservatii());
					}
				}
			}
			if (!foundDiplomaModel) {
				diplomeToRemove.add(diplomaEntity);
			}
		}
		for (DiplomaMembruReprezentantiComisieSauGLModel diplomaModel : models) {
			if (diplomaModel.getId() == null) {
				membru.getDiplome().add(getDiplomaFromModel(diplomaModel, membru));
			}
		}
		if (CollectionUtils.isNotEmpty(diplomeToRemove)) {
			membru.getDiplome().removeAll(diplomeToRemove);
		}
	}
	
	private DiplomaMembruReprezentantiComisieSauGLModel getModelFromDiploma(DiplomaMembruReprezentantiComisieSauGL diploma) {
		DiplomaMembruReprezentantiComisieSauGLModel model = new DiplomaMembruReprezentantiComisieSauGLModel();
		model.setId(diploma.getId());
		model.setDenumire(diploma.getDenumire());
		model.setAn(diploma.getAn());
		model.setObservatii(diploma.getObservatii());
		return model;
	}
	
	private DiplomaMembruReprezentantiComisieSauGL getDiplomaFromModel(DiplomaMembruReprezentantiComisieSauGLModel model, MembruReprezentantiComisieSauGL membru) {
		
		DiplomaMembruReprezentantiComisieSauGL diploma = new DiplomaMembruReprezentantiComisieSauGL();
		if (model.getId() != null) {
			diploma = reprezentantiComisieSauGLDao.getDiplomaMembruById(model.getId());
		}
		diploma.setDenumire(model.getDenumire());
		diploma.setAn(model.getAn());
		diploma.setObservatii(model.getObservatii());
		diploma.setMembru(membru);
		
		return diploma;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setReprezentantiComisieSauGLDao(ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao) {
		this.reprezentantiComisieSauGLDao = reprezentantiComisieSauGLDao;
	}
	
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
}
