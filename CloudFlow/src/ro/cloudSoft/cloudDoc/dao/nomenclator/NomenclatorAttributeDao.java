package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeSelectionFilter;

public interface NomenclatorAttributeDao {
	
	public Long save(NomenclatorAttribute nomenclatorAttribute);
	
	public NomenclatorAttribute find(Long id);
	
	public List<NomenclatorAttribute> getAllByNomenclatorId(Long nomenclatorId);
	
	public void saveAttributes(List<NomenclatorAttribute> nomenclatorAttributes);
	
	public NomenclatorAttribute findByNomenclatorIdAndKey(Long nomenclatorId, String key);

	void deleteAll(List<NomenclatorAttribute> nomenclatorAttributes);

	public void deleteOrphanAttributes(List<NomenclatorAttribute> nomenclatorAttributes, Nomenclator entity);
	
	void deleteNomenclatorAttributeSelectionFiltersByIds(List<Long> attributeIds);
	
	NomenclatorAttributeSelectionFilter findNomenclatorAttributeSelectionFilter(Long id);
	
	void deleteByIds(List<Long> attributeIds);
}
