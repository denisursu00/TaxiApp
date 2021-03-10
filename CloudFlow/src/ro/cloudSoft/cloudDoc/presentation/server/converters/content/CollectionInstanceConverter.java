package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;

public class CollectionInstanceConverter {

	public static List<CollectionInstance> getCollectionList(List<CollectionInstanceModel> collectionInstanceModelList) {
		List<CollectionInstance> collectionInstanceList = new ArrayList<CollectionInstance>();

		for (CollectionInstanceModel cim : collectionInstanceModelList) {
			CollectionInstance ci = new CollectionInstance();
			ci.setId(cim.getId());
			ci.setMetadataInstanceList(MetadataInstanceConverter.getMetadataInstanceList(cim.getMetadataInstanceList()));
			collectionInstanceList.add(ci);
		}
		
		return collectionInstanceList;
	}

	public static List<CollectionInstanceModel> getCollectionInstanceModelList(List<CollectionInstance> collectionInstanceList) {
		List<CollectionInstanceModel> result = new ArrayList<CollectionInstanceModel>();
		
		for (CollectionInstance collectionInstance : collectionInstanceList) {
			CollectionInstanceModel model = new CollectionInstanceModel();
			model.setId(collectionInstance.getId());
			model.setMetadataInstanceList(MetadataInstanceConverter.getMetadataInstanceModelList(collectionInstance.getMetadataInstanceList()));
			result.add(model);
		}
		
		return result;
	}
}