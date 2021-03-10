package ro.cloudSoft.cloudDoc.dao.content;

import java.util.Collection;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataSequenceValue;

/**
 * 
 */
public interface AutoNumberMetadataSequenceValueDao {
	
	Set<Long> getDefinitionIdsForExistingSequences(Collection<Long> autoNumberMetadataDefinitionIds);

	void create(AutoNumberMetadataSequenceValue autoNumberMetadataSequenceValue);
	
	int getExistingSequenceValue(Long autoNumberMetadataDefinitionId);
	
	boolean updateSequenceValue(Long autoNumberMetadataDefinitionId, int requiredOldSequenceValue, int newSequenceValue);
}