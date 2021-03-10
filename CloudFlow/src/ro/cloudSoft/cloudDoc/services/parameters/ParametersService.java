package ro.cloudSoft.cloudDoc.services.parameters;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.parameters.ParameterModel;

public interface ParametersService {
	
	public Long save(ParameterModel parameterModel) throws AppException;

	public List<ParameterModel> getAllParameters();
	
	public ParameterModel getParameterByName(String name) throws AppException;
	
	public ParameterModel getParameterById(Long id);
	
	public void deleteById(Long id);
	
	public Integer getParamaterValueAsIntegerByParameterName(String name) throws AppException ;

	public Double getParamaterValueAsDoubleByParameterName(String name) throws AppException ;
}
