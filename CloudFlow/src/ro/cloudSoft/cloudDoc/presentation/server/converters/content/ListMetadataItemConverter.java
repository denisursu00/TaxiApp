package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;

public class ListMetadataItemConverter {
	
	private static final int DEFAULT_ORDER_NUMBER = 0;
	
	public static ListMetadataItemModel getModelFromListMetadataItem(ListMetadataItem item){
		
		ListMetadataItemModel model = new ListMetadataItemModel();
		model.setId(item.getId());
		model.setLabel(item.getLabel());
		model.setValue(item.getValue());
		model.setOrderNumber(item.getOrderNumber());
		//model.setList(ListMetadataDefinitionConverter.getModelFromListMetadataDefinition(item.getList()));
		return model;
	}

	public static ListMetadataItem getListMetadataItemFromModel(ListMetadataItemModel model){
		
		ListMetadataItem item = new ListMetadataItem();
		item.setId(model.getId());
		item.setLabel(model.getLabel());
		item.setValue(model.getValue());
		item.setOrderNumber(model.getOrderNumber());
		if (item.getOrderNumber() == null) {
			item.setOrderNumber(DEFAULT_ORDER_NUMBER);
		}
		//item.setList(ListMetadataDefinitionConverter.getListMetadataDefinitionFromModel(model.getList()));
		return item;
	}
}
