package ro.cloudSoft.cloudDoc.dao.arb;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.arb.DiplomaMembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

public interface ReprezentantiComisieSauGLDao {

	ReprezentantiComisieSauGL getById(Long id);
	
	ReprezentantiComisieSauGL getByComisieSauGLId(Long comisieSauGLId);
	
	Long save(ReprezentantiComisieSauGL entity);
	
	MembruReprezentantiComisieSauGL getMembruById(Long id);
	
	void deleteMembri(List<Long> membriIds);
	
	DiplomaMembruReprezentantiComisieSauGL getDiplomaMembruById(Long id);
	
	void deleteDiplomeMembri(List<Long> diplomeIds);

	Boolean existsResponsabilArbInAllReprezentanti(Long oldResponsabilArbId);
	
	List<NomenclatorValue> getAllInstitutiiOfMembriiComisieSauGL(Long comisieSauGLId);
	
	List<MembruReprezentantiComisieSauGL> getMembriiReprezentantiComisieSauGLByInstitutie(Long comisieSauGLId, Long institutieId);
	
	List<ReprezentantiComisieSauGL> getAllWithCategorieComisie();
	

	List<ReprezentantiComisieSauGL> getAllWithExpiredMandatsSince60Days();
}
