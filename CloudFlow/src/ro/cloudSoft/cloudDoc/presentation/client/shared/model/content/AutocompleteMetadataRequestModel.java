package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class AutocompleteMetadataRequestModel {
	
	private List<String> sourceMetadataValues;
	private Long targetMetadataCollectionDefinitionId;
	
	public String getSourceMetadataValue() {
		if (CollectionUtils.isNotEmpty(sourceMetadataValues)) { 
			if (sourceMetadataValues.size() > 1) {
				throw new RuntimeException("too many values when a single value is expected");
			}
			return sourceMetadataValues.get(0);
		}
		return null;		
	}
	
	public List<String> getSourceMetadataValues() {
		return sourceMetadataValues;
	}
	
	public void setSourceMetadataValues(List<String> sourceMetadataValues) {
		this.sourceMetadataValues = sourceMetadataValues;
	}
	
	public Long getTargetMetadataCollectionDefinitionId() {
		return targetMetadataCollectionDefinitionId;
	}
	
	public void setTargetMetadataCollectionDefinitionId(Long targetMetadataCollectionDefinitionId) {
		this.targetMetadataCollectionDefinitionId = targetMetadataCollectionDefinitionId;
	}
}
