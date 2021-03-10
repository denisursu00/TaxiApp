package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.TypeOfAutoCompleteWithMetadata;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;

import com.google.common.collect.Lists;

public class MetadataCollectionDefinitionConverter {
	
	public static MetadataCollectionDefinitionModel getModelFromMetadataCollectionDefinition(MetadataCollection def){
		
		MetadataCollectionDefinitionModel model = new MetadataCollectionDefinitionModel();
		model.setId(def.getId());
		model.setName(def.getName());
		model.setLabel(def.getLabel());
		//DanaSa: hibernate nu suporta onetomany catre o ierarhie de clase asa ca a trebuit sa schimb modelul
		//oricum nu cred ca trebuie neaparat aceste 3 campuri pe colectie, deoarece se regasesc pe fiecare metadata din colectie
		model.setMandatory(def.isMandatory());
		model.setMandatoryStates(def.getMandatoryStates());
//		model.setRepresentative(def.getRepresentative());
//		model.setIndexed(def.getIndexed());
		model.setOrderNumber(def.getOrderNumber());
		
		model.setRestrictedOnEdit(def.isRestrictedOnEdit());
		model.setRestrictedOnEditStates(def.getRestrictedOnEditStates());
		
		model.setInvisible(def.isInvisible());
		model.setInvisibleInStates(def.getInvisibleInStates());
		
		model.setType(MetadataDefinition.TYPE_METADATA_COLLECTION);		
		List<MetadataDefinitionModel> metaDefModels =  new ArrayList<MetadataDefinitionModel>();
		for(MetadataDefinition metaDef: def.getMetadataDefinitions()){
			metaDefModels.add(MetadataDefinitionConverter.getModelFromMetadataDefinition(metaDef));
		}
		model.setMetadataDefinitions(metaDefModels);
		
		model.setMetadataNameForAutoCompleteWithMetadata(def.getMetadataNameForAutoCompleteWithMetadata());
		if (def.getTypeOfAutoCompleteWithMetadata() != null) {
			model.setTypeOfAutoCompleteWithMetadata(def.getTypeOfAutoCompleteWithMetadata().name());
		}		
		model.setNomenclatorAttributeKeyForAutoCompleteWithMetadata(def.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		model.setClassNameForAutoCompleteWithMetadata(def.getClassNameForAutoCompleteWithMetadata());
		
		return model;
	}
	
	public static MetadataCollection getMetadataCollectionDefinitionFromModel(MetadataCollectionDefinitionModel model, GroupService groupService){
		
		MetadataCollection def = new MetadataCollection();
		def.setId(model.getId());
		def.setName(model.getName());
		def.setLabel(model.getLabel());
		
		def.setMandatory(model.isMandatory());
		def.setMandatoryStates(model.getMandatoryStates());
		def.setOrderNumber(model.getOrderNumber());
		
//		def.setRepresentative(model.isRepresentative());
//		def.setIndexed(model.isIndexed());
//		def.setMetadataType(model.getType());
		
		def.setRestrictedOnEdit(model.isRestrictedOnEdit());
		def.setRestrictedOnEditStates(model.getRestrictedOnEditStates());
		
		def.setInvisible(model.isInvisible());
		def.setInvisibleInStates(model.getInvisibleInStates());
		
		List<MetadataDefinition> metadataDefinitions = Lists.newLinkedList();	
		for (MetadataDefinitionModel metaDefModel: model.getMetadataDefinitions()){
			metadataDefinitions.add(MetadataDefinitionConverter.getMetadataDefinitionFromModel(metaDefModel, groupService));
		}
		def.setMetadataDefinitions(metadataDefinitions);
		
		def.setMetadataNameForAutoCompleteWithMetadata(model.getMetadataNameForAutoCompleteWithMetadata());
		if (StringUtils.isNotBlank(model.getTypeOfAutoCompleteWithMetadata())) {
			def.setTypeOfAutoCompleteWithMetadata(TypeOfAutoCompleteWithMetadata.valueOf(model.getTypeOfAutoCompleteWithMetadata()));
		}		
		def.setNomenclatorAttributeKeyForAutoCompleteWithMetadata(model.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		def.setClassNameForAutoCompleteWithMetadata(model.getClassNameForAutoCompleteWithMetadata());
		
		return def;
	}

}
