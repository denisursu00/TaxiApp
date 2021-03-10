package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;

public interface NomenclatorDao {
	
	public Long save(Nomenclator nomenclator);
	
	public Nomenclator find(Long id);
	
	public List<Nomenclator> getAll();
	
	public List<Nomenclator> getVisibleNomenclators();
	
	public List<Nomenclator> getAllThatAllowProcessingValuesFromUI();
	
	public List<Nomenclator> getAllThatAllowProcessingStructureFromUI();
	
	public void delete(Nomenclator nomenclator);
	
	public Nomenclator findByCode(String code);
}
