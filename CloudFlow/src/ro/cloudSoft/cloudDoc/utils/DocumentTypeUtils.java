package ro.cloudSoft.cloudDoc.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;

import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;

public class DocumentTypeUtils {

	public static String getMetadataJrPropertyName(DocumentType documentType, String metadataName) {
		if (documentType == null || metadataName == null) {
			throw new IllegalArgumentException("invalid arguments 'null' for getMetadataJrPropertyName");
		}
		if (CollectionUtils.isEmpty(documentType.getMetadataDefinitions())) {
			throw new IllegalStateException("get metadata when document type has no metadatas");
		}
		
		for (MetadataDefinition md : documentType.getMetadataDefinitions()) {
			if (md.getName().equals(metadataName)) {
				return md.getJrPropertyName();
			}
		}
		throw new IllegalStateException("metadata [" + metadataName + "] not found into document type [" + documentType.getName() + "]");
	}
	
	public static String getMetadataCollectionJrPropertyName(DocumentType documentType, String metadataName) {
		if (documentType == null || metadataName == null) {
			throw new IllegalArgumentException("invalid arguments 'null' for getMetadataJrPropertyName");
		}
		if (CollectionUtils.isEmpty(documentType.getMetadataCollections())) {
			throw new IllegalStateException("get metadata collection when document type has no metadata collections");
		}
		
		for (MetadataCollection md : documentType.getMetadataCollections()) {
			if (md.getName().equals(metadataName)) {
				return md.getJrPropertyName();
			}
		}
		throw new IllegalStateException("metadata collection [" + metadataName + "] not found into document type [" + documentType.getName() + "]");
	}
	
	public static MetadataDefinition getMetadataDefinitionById(DocumentType documentType, Long metadataDefinitionId) {
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getId().equals(metadataDefinitionId)) {
				return metadataDefinition;
			}
		}
		throw new RuntimeException("Document of type [" + documentType.getName() + "] doesen't have a metadata definition with id [" + metadataDefinitionId + "].");
	}
	
	public static MetadataDefinition getMetadataDefinitionByName(DocumentType documentType, String metadataName) {
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getName().equals(metadataName)) {
				return metadataDefinition;
			}
		}
		throw new RuntimeException("Document of type [" + documentType.getName() + "] doesen't have a metadata definition with name [" + metadataName + "].");
	}
	
	public static MetadataCollection getMetadataCollectionDefinitionByName(DocumentType documentType, String metadataName) {
		for (MetadataCollection metadataDefinition : documentType.getMetadataCollections()) {
			if (metadataDefinition.getName().equals(metadataName)) {
				return metadataDefinition;
			}
		}
		throw new RuntimeException("Document of type [" + documentType.getName() + "] doesen't have a metadata collection definition with name [" + metadataName + "].");
	}
	
	public static MetadataDefinition getMetadataDefinitionFromCollectionByName(DocumentType documentType, String metadataCollectionName, String metadataName) {
		for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
			if (metadataCollection.getName().equals(metadataCollectionName)) {
				for (MetadataDefinition md : metadataCollection.getMetadataDefinitions()) {
					if (md.getName().equals(metadataName)) {
						return md;
					}
				}
				throw new RuntimeException("Document of type [" + documentType.getName() + "] doesen't have a metadata definition with name [" + metadataName + "].");
			}
		}
		throw new RuntimeException("Document of type [" + documentType.getName() + "] doesen't have a metadata collection definition with name [" + metadataCollectionName + "].");
	}
	
	public static Long getMetadataDefinitionIdByName(DocumentType documentType, String metadataDefinitionName) {
		MetadataDefinition metadataDefinition = getMetadataDefinitionByName(documentType, metadataDefinitionName);
		return metadataDefinition.getId();
	}
	
	public static Long getMetadataCollectionDefinitionIdByName(DocumentType documentType, String metadataCollectionName) {
		MetadataCollection metadataDefinition = getMetadataCollectionDefinitionByName(documentType, metadataCollectionName);
		return metadataDefinition.getId();
	}
	
	public static MetadataDefinition getMetadataDefinitionByName(List<? extends MetadataDefinition> metadataDefinitions, String metadataName) {
		for (MetadataDefinition metadataDefinition : metadataDefinitions) {
			if (metadataDefinition.getName().equals(metadataName)) {
				return metadataDefinition;
			}
		}
		throw new RuntimeException("no metadata founf for name [" + metadataName + "]");
	}
	
	public static Map<String, MetadataDefinition> getMetadataDefinitionsAsMap(DocumentType documentType) {
		Map<String, MetadataDefinition> metadataDefinitionsMap = new HashedMap();
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			metadataDefinitionsMap.put(metadataDefinition.getName(), metadataDefinition);
		}
		return metadataDefinitionsMap;
	}
	
	public static Map<String, MetadataCollection> getMetadataCollectionsAsMap(DocumentType documentType) {
		Map<String, MetadataCollection> metadataCollectionsMap = new HashedMap();
		for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
			metadataCollectionsMap.put(metadataCollection.getName(), metadataCollection);
		}
		return metadataCollectionsMap;
	}
}
