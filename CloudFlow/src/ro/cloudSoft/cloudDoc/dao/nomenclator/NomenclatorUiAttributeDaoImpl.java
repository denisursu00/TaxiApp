package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorUiAttribute;

public class NomenclatorUiAttributeDaoImpl extends HibernateDaoSupport implements NomenclatorUiAttributeDao {

	@Override
	public NomenclatorUiAttribute find(Long id) {
		return (NomenclatorUiAttribute) getHibernateTemplate().get(NomenclatorUiAttribute.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorUiAttribute> getAllByNomenclatorId(Long id) {
		String query = "FROM NomenclatorUiAttribute nomenclatorUiAttribute WHERE nomenclatorUiAttribute.nomenclator.id = ? order by nomenclatorUiAttribute.uiOrder";
		List<NomenclatorUiAttribute> nomenclatorUiAttributes = getHibernateTemplate().find(query, id);
		return nomenclatorUiAttributes;
	}
	
	@Override
	public void deleteAll(List<NomenclatorUiAttribute> nomenclatorUiAttributes) {
		getHibernateTemplate().deleteAll(nomenclatorUiAttributes);
	}

}
