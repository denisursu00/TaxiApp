package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;

/**
 * Converteste definitii de metadate de tip lista in obiecte de interfata si
 * invers. Conversia este doar partiala, convertind doar elementele specifice
 * tipului de metadata lista. De proprietatile generale ale definitiei se ocupa
 * MetadataDefinitionConverter.
 * 
 * 
 */
public class ListMetadataDefinitionConverter {

	public static ListMetadataDefinitionModel getModelFromListMetadataDefinition(ListMetadataDefinition listMetadataDefinition) {
		ListMetadataDefinitionModel listMetadataDefinitionModel = new ListMetadataDefinitionModel();
		
		listMetadataDefinitionModel.setMultipleSelection(listMetadataDefinition.getMultipleSelection());
		listMetadataDefinitionModel.setExtendable(listMetadataDefinition.getExtendable());
		
		List<ListMetadataItemModel> listMetadataItemModels = new ArrayList<ListMetadataItemModel>();
		for (ListMetadataItem item : listMetadataDefinition.getListItems()) {
			listMetadataItemModels.add(ListMetadataItemConverter.getModelFromListMetadataItem(item));
		}
		listMetadataDefinitionModel.setListItems(listMetadataItemModels);
		
		return listMetadataDefinitionModel;
	}

	public static ListMetadataDefinition getListMetadataDefinitionFromModel(ListMetadataDefinitionModel listMetadataDefinitionModel) {
		ListMetadataDefinition listMetadataDefinition = new ListMetadataDefinition();
		
		listMetadataDefinition.setMultipleSelection(listMetadataDefinitionModel.isMultipleSelection());
		listMetadataDefinition.setExtendable(listMetadataDefinitionModel.isExtendable());
		
		Set<ListMetadataItem> listItems = new HashSet<ListMetadataItem>();
		for (ListMetadataItemModel itemModel : listMetadataDefinitionModel.getListItems()) 
		{
			ListMetadataItem lItem = ListMetadataItemConverter.getListMetadataItemFromModel(itemModel);
			lItem.setList(listMetadataDefinition);
			listItems.add(lItem);
		}
		listMetadataDefinition.setListItems(listItems);
		
		return listMetadataDefinition;
	}
}