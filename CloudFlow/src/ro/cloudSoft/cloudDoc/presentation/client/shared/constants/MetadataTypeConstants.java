package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import java.util.HashMap;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

public class MetadataTypeConstants {
	
	private static Map<String, String> metadataTypeLabelByTypeName;
	
	public static Map<String, String> getMetadataTypeLabelByTypeName() {
		if (metadataTypeLabelByTypeName == null) {
			metadataTypeLabelByTypeName = new HashMap<String, String>();
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_TEXT, GwtLocaleProvider.getConstants().TEXT());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_NUMERIC, GwtLocaleProvider.getConstants().NUMERIC());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_AUTO_NUMBER, GwtLocaleProvider.getConstants().AUTO_NUMBER());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_DATE, GwtLocaleProvider.getConstants().DATE());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_LIST, GwtLocaleProvider.getConstants().LIST());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_USER, GwtLocaleProvider.getConstants().METADATA_TYPE_USER());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_TEXT_AREA, GwtLocaleProvider.getConstants().EXTENDED_TEXT());
			metadataTypeLabelByTypeName.put(MetadataDefinitionModel.TYPE_METADATA_COLLECTION, GwtLocaleProvider.getConstants().METADATA_COLLECTION());
		}
		return metadataTypeLabelByTypeName;
	}

}
