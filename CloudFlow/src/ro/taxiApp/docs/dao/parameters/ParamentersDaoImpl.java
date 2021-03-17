package ro.taxiApp.docs.dao.parameters;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.parameters.Parameter;

public class ParamentersDaoImpl extends HibernateDaoSupport implements ParametersDao {

	@Override
	public Long save(Parameter parameter) {
		if (parameter.getId() == null) {
			return (Long) getHibernateTemplate().save(parameter);
		} else {
			getHibernateTemplate().saveOrUpdate(parameter);
			return parameter.getId();
		}
	}

	@Override
	public Parameter getById(Long id) {
		return (Parameter) getHibernateTemplate().get(Parameter.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Parameter getByName(String name) {
		String query = "SELECT parameter FROM Parameter parameter WHERE parameter.name = ?";
		List<Parameter> parameters = getHibernateTemplate().find(query, name);
		return DataAccessUtils.singleResult(parameters);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Parameter> getAll() {
		String query = "SELECT parameter FROM Parameter parameter";
		return getHibernateTemplate().find(query);
	}

	@Override
	public void delete(Long parameterId) {
		Parameter parameter = getById(parameterId);
		getHibernateTemplate().delete(parameter);
	}

}