package ro.cloudSoft.cloudDoc.utils.listItemLabelProviders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Obtine etichete pentru item-ii unei metadate de tip lista,
 * folosindu-se de definitia metadatei de tip lista.
 * 
 * 
 */
public class ListMetadataDefinitionListItemLabelProvider implements ListItemLabelProvider {
	
	private final String listMetadataName;
	private final Map<String, String> listItemLabelByListItemValue = Maps.newHashMap();
	
	public ListMetadataDefinitionListItemLabelProvider(ListMetadataDefinition listMetadataDefinition) {
		
		listMetadataName = listMetadataDefinition.getName();
		
		for (ListMetadataItem listItem : listMetadataDefinition.getListItems()) {
			listItemLabelByListItemValue.put(listItem.getValue(), listItem.getLabel());
		}
	}
	
	@Override
	public List<String> getListItemLabels(String metadataName, Collection<String> listItemValues) {
		
		if (!metadataName.equals(listMetadataName)) {
			String exceptionMessage = "Provider-ul nu este pentru metadata cu numele [], ci pentru metadata cu numele [].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		List<String> listItemLabels = Lists.newLinkedList();
		
		for (String listItemValue : listItemValues) {
			String listItemLabel = MapUtils.getString(listItemLabelByListItemValue, listItemValue, listItemValue);
			listItemLabels.add(listItemLabel);
		}
		
		return listItemLabels;
	}		
}