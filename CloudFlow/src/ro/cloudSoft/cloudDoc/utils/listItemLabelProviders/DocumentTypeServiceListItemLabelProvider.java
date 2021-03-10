package ro.cloudSoft.cloudDoc.utils.listItemLabelProviders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;

import com.google.common.collect.Lists;

/**
 * Obtine etichete pentru item-ii unei metadate de tip lista,
 * folosindu-se de serviciul tipurilor de documente.
 * 
 * 
 */
public class DocumentTypeServiceListItemLabelProvider implements ListItemLabelProvider {
	
	private final Map<String, Map<String, String>> listItemLabelByListItemValueByMetadataName;
	
	public DocumentTypeServiceListItemLabelProvider(DocumentTypeService documentTypeService, Long documentTypeId) {
		listItemLabelByListItemValueByMetadataName = documentTypeService.getListItemLabelByListItemValueByMetadataName(documentTypeId);
	}
	
	@Override
	public List<String> getListItemLabels(String metadataName, Collection<String> listItemValues) {
		
		Map<String, String> listItemLabelByListItemValue = listItemLabelByListItemValueByMetadataName.get(metadataName);
		List<String> listItemLabels = Lists.newLinkedList();
		
		for (String listItemValue : listItemValues) {
			String listItemLabel = MapUtils.getString(listItemLabelByListItemValue, listItemValue, listItemValue);
			listItemLabels.add(listItemLabel);
		}
		
		return listItemLabels;
	}
}