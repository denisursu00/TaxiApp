package ro.cloudSoft.cloudDoc.dao.parameters;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.parameters.Parameter;

public interface ParametersDao {
	
	public Long save(Parameter parameter);
	
	public Parameter getById(Long id);
	
	public Parameter getByName(String name);
	
	public List<Parameter> getAll();
	
	public void delete(Long parameterId);
}
