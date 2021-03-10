package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;

public class NomenclatorDaoImpl extends HibernateDaoSupport implements NomenclatorDao {
	
	@Override
	public Long save(Nomenclator nomenclator) {
		if (nomenclator.getId() == null) {
			return (Long) getHibernateTemplate().save(nomenclator);
		} else {
			getHibernateTemplate().saveOrUpdate(nomenclator);
			return nomenclator.getId();
		}
	}

	@Override
	public Nomenclator find(Long id) {
		return (Nomenclator) getHibernateTemplate().get(Nomenclator.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Nomenclator> getAll() {
		String query = "FROM Nomenclator ORDER BY name";
		List<Nomenclator> nomenclators = getHibernateTemplate().find(query);
        return nomenclators;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Nomenclator> getVisibleNomenclators() {
		String query = "FROM Nomenclator WHERE hidden=? ORDER BY name";
		return getHibernateTemplate().find(query, Boolean.FALSE);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Nomenclator> getAllThatAllowProcessingValuesFromUI() {
		String query = "FROM Nomenclator WHERE hidden=? AND allow_processing_values_from_ui=? ORDER BY name";
		return getHibernateTemplate().find(query, Boolean.FALSE, Boolean.TRUE);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Nomenclator> getAllThatAllowProcessingStructureFromUI() {
		String query = "FROM Nomenclator WHERE hidden=? AND allow_processing_structure_from_ui=? ORDER BY name";
		return getHibernateTemplate().find(query, Boolean.FALSE, Boolean.TRUE);
	}
	
	@Override
	public void delete(Nomenclator nomenclator) {
		getHibernateTemplate().delete(nomenclator);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Nomenclator findByCode(String code) {
		String query = "FROM Nomenclator nomenclator WHERE nomenclator.code = ?";
		Object[] params = new Object[1];
		params[0] = code;
		List<Nomenclator> nomenclators = getHibernateTemplate().find(query, params);
		return DataAccessUtils.singleResult(nomenclators);
	}
}
