package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorUiAttribute;

public interface NomenclatorUiAttributeDao {
	
	public NomenclatorUiAttribute find(Long id);

	public List<NomenclatorUiAttribute> getAllByNomenclatorId(Long id);

	void deleteAll(List<NomenclatorUiAttribute> nomenclatorUiAttributes);
}
