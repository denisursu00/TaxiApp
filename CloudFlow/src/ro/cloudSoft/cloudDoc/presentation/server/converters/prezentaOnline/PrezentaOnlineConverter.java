package ro.cloudSoft.cloudDoc.presentation.server.converters.prezentaOnline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineParticipanti;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.PrezentaMembriiReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class PrezentaOnlineConverter {
	
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorService nomenclatorService;
	
	public List<PrezentaMembriiReprezentantiComisieGl> fromModelToModel(List<MembruReprezentantiComisieSauGLModel> membriiReprezentanti) throws AppException {
		
		List<PrezentaMembriiReprezentantiComisieGl> membrii = new ArrayList<>();
		
		Map<Long, String> institutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		
		membriiReprezentanti.forEach(membruReprezentant -> {
			PrezentaMembriiReprezentantiComisieGl membru = new PrezentaMembriiReprezentantiComisieGl();	
			
			if (membruReprezentant.getMembruInstitutieId() != null) {
				membru.setNume(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME));
				membru.setPrenume(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME));
				membru.setFunctie(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_FUNCTIE));
				membru.setDepartament(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_DEPARTAMENT));
				membru.setTelefon(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_TELEFON));
				membru.setEmail(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(membruReprezentant.getMembruInstitutieId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_EMAIL));
			} else {
				membru.setNume(membruReprezentant.getNume());
				membru.setPrenume(membruReprezentant.getPrenume());
				membru.setFunctie(membruReprezentant.getFunctie());
				membru.setDepartament(membruReprezentant.getDepartament());
				membru.setTelefon(membruReprezentant.getTelefon());
				membru.setEmail(membruReprezentant.getEmail());
			}
			
			membru.setInstitutieId(membruReprezentant.getInstitutieId());
			membru.setNumeInstitutie(institutiiUiAttrValuesAsMap.get(membruReprezentant.getInstitutieId()));
			membru.setMembruInstitutieId(membruReprezentant.getMembruInstitutieId());
			membru.setCalitate(membruReprezentant.getCalitate());
			membrii.add(membru);
		});
		
		return membrii;
	}
	
	public PrezentaOnlineParticipanti toEntity(PrezentaMembriiReprezentantiComisieGl model) {
		PrezentaOnlineParticipanti entity = new PrezentaOnlineParticipanti();
		
		if (model.getMembruInstitutieId() != null) {
			entity.setMembruInstitutie(nomenclatorValueDao.find(model.getMembruInstitutieId()));
		} else {
			entity.setNume(model.getNume());
			entity.setPrenume(model.getPrenume());
		}
		
		entity.setInstitutie(nomenclatorValueDao.find(model.getInstitutieId()));
		entity.setFunctie(model.getFunctie());
		entity.setDepartament(model.getDepartament());
		entity.setTelefon(model.getTelefon());
		entity.setEmail(model.getEmail());
		entity.setCalitate(model.getCalitate());
		entity.setDocumentId(model.getDocumentId());
		entity.setDocumentLocationRealName(model.getDocumentLocationRealName());
		
		return entity;
	}
	
	public PrezentaMembriiReprezentantiComisieGl toModel( PrezentaOnlineParticipanti entity) throws AppException {
			
		PrezentaMembriiReprezentantiComisieGl model = new PrezentaMembriiReprezentantiComisieGl();
		Map<Long, String> institutiiUiAttrValuesAsMap = nomenclatorService.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		
		if (entity.getMembruInstitutie() != null) {
			model.setMembruInstitutieId(entity.getMembruInstitutie().getId());
			model.setNume(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(entity.getMembruInstitutie().getId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME));
			model.setPrenume(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(entity.getMembruInstitutie().getId()), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME));
		} else {
			model.setNume(entity.getNume());
			model.setPrenume(entity.getPrenume());
		}
		
		model.setId(entity.getId());
		model.setNumeInstitutie(institutiiUiAttrValuesAsMap.get(entity.getInstitutie().getId()));
		model.setInstitutieId(entity.getInstitutie().getId());
		model.setFunctie(entity.getFunctie());
		model.setDepartament(entity.getDepartament());
		model.setTelefon(entity.getTelefon());
		model.setEmail(entity.getEmail());
		model.setCalitate(entity.getCalitate());
		model.setDocumentId(entity.getDocumentId());
		model.setDocumentLocationRealName(entity.getDocumentLocationRealName());
		
		return model;
	}
	
	

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

}
