package ro.cloudSoft.cloudDoc.services.arb;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;

public interface ComisieSauGLService {

	void saveReprezentanti(ReprezentantiComisieSauGLModel model) throws AppException;
	
	ReprezentantiComisieSauGLModel getReprezentantiByComisieSauGLId(Long comisieSauGLId);
	
	ReprezentantiComisieSauGLModel getReprezentantiById(Long id);
	
	List<NomenclatorValueModel> getAllInstitutiiOfMembriiComisieSauGL(Long comisieSauGLId) throws AppException;
	
	List<MembruReprezentantiComisieSauGLInfoModel> getMembriiReprezentantiComisieSauGLByInstitutie(Long comisieSauGLId, Long institutieId) throws AppException;
}
