package ro.cloudSoft.cloudDoc.generators;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.Document;

/**
 * 
 */
public interface AutoNumberMetadataValueGenerator {

	void populateDocumentWithGeneratedValues(Document document, Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate) throws AppException;
}