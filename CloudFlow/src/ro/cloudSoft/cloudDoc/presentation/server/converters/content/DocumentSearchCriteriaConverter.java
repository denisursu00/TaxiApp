package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.CollectionSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.MetadataSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel.CollectionSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel.MetadataSearchCriteriaModel;

public class DocumentSearchCriteriaConverter {

	public static DocumentSearchCriteriaModel getModelFromDocumentSearchCriteria(DocumentSearchCriteria documentSearchCriteria){
		DocumentSearchCriteriaModel model = new DocumentSearchCriteriaModel();
		model.setDocumentLocationRealName(documentSearchCriteria.getDocumentLocationRealName());
		model.setCreatedStart(documentSearchCriteria.getCreatedStart());
		model.setCreatedEnd(documentSearchCriteria.getCreatedEnd());
		model.setSearchInVersions(documentSearchCriteria.isSearchInVersions());
		model.setDocumentTypeIdList(documentSearchCriteria.getDocumentTypeIdList());
		model.setWorkflowStateIdList(documentSearchCriteria.getWorkflowStateIdList());
		model.setMetadataSearchCriteriaList(MetadataSearchCriteriaConverter.
				getModelsFromMetadataCriteria(documentSearchCriteria.getMetadataSearchCriteriaList()));
		model.setCollectionSearchCriteriaList(CollectionSearchCriteriaConverter.
				getModelsFromCollectionCriteria(documentSearchCriteria.getCollectionSearchCriteriaList()));
		return model;
	}
	
	public static DocumentSearchCriteria getDocumentSearchCriteriaFromModel(DocumentSearchCriteriaModel model){
		DocumentSearchCriteria documentSearchCriteria = new DocumentSearchCriteria();
		documentSearchCriteria.setDocumentLocationRealName(model.getDocumentLocationRealName());
		documentSearchCriteria.setCreatedStart(model.getCreatedStart());
		documentSearchCriteria.setCreatedEnd(model.getCreatedEnd());
		documentSearchCriteria.setSearchInVersions(model.isSearchInVersions());
		documentSearchCriteria.setDocumentTypeIdList(model.getDocumentTypeIdList());
		documentSearchCriteria.setWorkflowStateIdList(model.getWorkflowStateIdList());
		documentSearchCriteria.setMetadataSearchCriteriaList(
				MetadataSearchCriteriaConverter.getMetadataCriteriaFromModels(model.getMetadataSearchCriteriaList()));
		documentSearchCriteria.setCollectionSearchCriteriaList(
				CollectionSearchCriteriaConverter.getCollectionCriteriaFromModels(model.getCollectionSearchCriteriaList()));
		return documentSearchCriteria;
	}
	
	public static class MetadataSearchCriteriaConverter {
		
		public static MetadataSearchCriteriaModel getModelFromMetadataSearchCriteria(MetadataSearchCriteria metadataSearchCriteria){
			MetadataSearchCriteriaModel model = new MetadataSearchCriteriaModel(metadataSearchCriteria.getMetadataDefinitionId(), 
					metadataSearchCriteria.getValue());
			return model;
		}
		
		public static MetadataSearchCriteria getMetadataSearchCriteriaFromModel(MetadataSearchCriteriaModel model){
			MetadataSearchCriteria metadataSearchCriteria = new MetadataSearchCriteria(
					model.getMetadataDefinitionId(), model.getValue());
			return metadataSearchCriteria;
		}
		
		public static List<MetadataSearchCriteriaModel> getModelsFromMetadataCriteria(List<MetadataSearchCriteria> criteria){
			List<MetadataSearchCriteriaModel> models = new ArrayList<MetadataSearchCriteriaModel>();
			if (criteria != null){
				for(MetadataSearchCriteria criterion : criteria){
					MetadataSearchCriteriaModel model = getModelFromMetadataSearchCriteria(criterion);
					models.add(model);
				}
			}
			return models;
		}
		
		public static List<MetadataSearchCriteria> getMetadataCriteriaFromModels(List<MetadataSearchCriteriaModel> models){
			List<MetadataSearchCriteria> criteria = new ArrayList<MetadataSearchCriteria>();
			if (models != null){
				for(MetadataSearchCriteriaModel model : models){
					MetadataSearchCriteria criterion = getMetadataSearchCriteriaFromModel(model);
					criteria.add(criterion);
				}
			}
			return criteria;
		}
		
	}
	
	public static class CollectionSearchCriteriaConverter {
		
		public static CollectionSearchCriteriaModel getModelFromCollectionSearchCriteria(CollectionSearchCriteria collectionSearchCriteria){
			CollectionSearchCriteriaModel model = new CollectionSearchCriteriaModel(collectionSearchCriteria.getCollectionDefinitionId(), 
					collectionSearchCriteria.getMetadataDefinitionId(), collectionSearchCriteria.getValue());
			return model;
		}
		
		public static CollectionSearchCriteria getCollectionSearchCriteriaFromModel(CollectionSearchCriteriaModel model){
			CollectionSearchCriteria collectionSearchCriteria = new CollectionSearchCriteria(
					model.getCollectionDefinitionId(), model.getMetadataDefinitionId(), model.getValue());
			return collectionSearchCriteria;
		}
		
		public static List<CollectionSearchCriteriaModel> getModelsFromCollectionCriteria(List<CollectionSearchCriteria> criteria){
			List<CollectionSearchCriteriaModel> models = new ArrayList<CollectionSearchCriteriaModel>();
			if (criteria != null){
				for(CollectionSearchCriteria criterion : criteria){
					CollectionSearchCriteriaModel model = getModelFromCollectionSearchCriteria(criterion);
					models.add(model);
				}
			}
			return models;
		}
		
		public static List<CollectionSearchCriteria> getCollectionCriteriaFromModels(List<CollectionSearchCriteriaModel> models){
			List<CollectionSearchCriteria> criteria = new ArrayList<CollectionSearchCriteria>();
			if (models != null){
				for(CollectionSearchCriteriaModel model : models){
					CollectionSearchCriteria criterion = getCollectionSearchCriteriaFromModel(model);
					criteria.add(criterion);
				}
			}
			return criteria;
		}
		
	}
	
}
