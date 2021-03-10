package ro.cloudSoft.cloudDoc.plugins.bpm.variables;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.model.OpenExecution;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;

/**
 * 
 */
public class WorkflowMetadataVariableHelper {
	
	private static final String PREFIX_VARIABLE_NAME = "metadata_";
	
	private static final String SEPARATOR_TYPE_FROM_VALUES = "||";
	private static final String SEPARATOR_VALUES = "##";
	
	private static String getVariableNameForMetadataName(String metadataName) {
		return (PREFIX_VARIABLE_NAME + metadataName);
	}
	
	private static String getMetadataNameFromVariableName(String variableName) {
		if (!isMetadataVariable(variableName)) {
			throw new IllegalArgumentException("Variabila cu numele [" + variableName + "] nu este pt. o metadata.");
		}
		String metadataName = StringUtils.removeStart(variableName, PREFIX_VARIABLE_NAME);
		return metadataName;
	}
	
	private static boolean isMetadataVariable(String variableName) {
		return variableName.startsWith(PREFIX_VARIABLE_NAME);
	}
	
	private static String getMetadataWrapperAsString(MetadataWrapper metadataWrapper) {
		String metadataValuesJoinedAsString = StringUtils.join(metadataWrapper.getValues(), SEPARATOR_VALUES);
		String metadataWrapperAsString = (metadataWrapper.getType() + SEPARATOR_TYPE_FROM_VALUES + metadataValuesJoinedAsString);
		return metadataWrapperAsString;
	}
	
	private static MetadataWrapper getMetadataWrapperFromString(String metadataWrapperAsString) {
		String[] typeAndValues = StringUtils.splitByWholeSeparator(metadataWrapperAsString, SEPARATOR_TYPE_FROM_VALUES);
		
		String type = typeAndValues[0];
		String valuesJoinedAsString = typeAndValues[1];
		
		String[] values = StringUtils.splitByWholeSeparator(valuesJoinedAsString, SEPARATOR_VALUES);
		
		MetadataWrapper metadataWrapper = new MetadataWrapper(type, Lists.newArrayList(values));		
		return metadataWrapper;
	}
	
	private static Collection<String> getMetadataValuesFromMetadataWrapperAsString(String metadataWrapperAsString) {
		String[] typeAndValues = StringUtils.splitByWholeSeparator(metadataWrapperAsString, SEPARATOR_TYPE_FROM_VALUES);
		String valuesJoinedAsString = typeAndValues[1];
		String[] values = StringUtils.splitByWholeSeparator(valuesJoinedAsString, SEPARATOR_VALUES);
		return Lists.newArrayList(values);
	}
	
	public static Map<String, Object> getMetadataWrapperAsStringByVariableName(Map<String, MetadataWrapper> metadataWrapperByMetadataName) {
		
		Map<String, Object> metadataWrapperAsStringByVariableName = Maps.newHashMap();
		
		for (Entry<String, MetadataWrapper> metadataWrapperByMetadataNameEntry : metadataWrapperByMetadataName.entrySet()) {
			
			String metadataName = metadataWrapperByMetadataNameEntry.getKey();
			String variableName = getVariableNameForMetadataName(metadataName);
			
			MetadataWrapper metadataWrapper = metadataWrapperByMetadataNameEntry.getValue();
			String metadataWrapperAsString = getMetadataWrapperAsString(metadataWrapper);
			
			if (metadataWrapperByMetadataNameEntry.getValue().getType().equals(MetadataDefinition.TYPE_TEXT_AREA)) {
				metadataWrapperAsStringByVariableName.put(variableName, metadataWrapperAsString.toCharArray());
			} else {
				metadataWrapperAsStringByVariableName.put(variableName, metadataWrapperAsString);
			}
		}
		
		return metadataWrapperAsStringByVariableName;
	}

	public static void setMetadataValues(ProcessEngine processEngine, String processInstanceId,
			Map<String, MetadataWrapper> metadataWrapperByMetadataName) {
		
		Map<String, Object> metadataWrapperAsStringByVariableName = getMetadataWrapperAsStringByVariableName(metadataWrapperByMetadataName);
		processEngine.getExecutionService().setVariables(processInstanceId, metadataWrapperAsStringByVariableName);
	}
	
	public static Collection<String> getMetadataValues(OpenExecution execution, String metadataName) {
		String variableName = getVariableNameForMetadataName(metadataName);
		String metadataWrapperAsString = (String) execution.getVariable(variableName);
		if (metadataWrapperAsString != null) {
			return getMetadataValuesFromMetadataWrapperAsString(metadataWrapperAsString);
		} else {
			return Collections.emptyList();
		}
	}
	
	public static Map<String, MetadataWrapper> getMetadataWrapperByMetadataName(Map<String, Object> executionVariableValueByName) {
		
		Map<String, MetadataWrapper> metadataWrapperByMetadataName = Maps.newHashMap();
		
		for (Entry<String, Object> executionVariableValueByNameEntry : executionVariableValueByName.entrySet()) {
			String variableName = executionVariableValueByNameEntry.getKey();
			if (isMetadataVariable(variableName)) {
				String metadataWrapperAsString = null;
				
				Object variableValueAsObject = executionVariableValueByNameEntry.getValue();
				if (variableValueAsObject instanceof char[]) {
					metadataWrapperAsString = String.valueOf((char[]) variableValueAsObject);
				} else if (variableValueAsObject instanceof String) {
					metadataWrapperAsString = (String) variableValueAsObject;
				} else {
					throw new RuntimeException("Unknown variableValueAsObject instance.");
				}
				
				
				MetadataWrapper metadataWrapper = getMetadataWrapperFromString(metadataWrapperAsString);
				String metadataName = getMetadataNameFromVariableName(variableName);
				
				metadataWrapperByMetadataName.put(metadataName, metadataWrapper);
			}
		}
		
		return metadataWrapperByMetadataName;
	}
}