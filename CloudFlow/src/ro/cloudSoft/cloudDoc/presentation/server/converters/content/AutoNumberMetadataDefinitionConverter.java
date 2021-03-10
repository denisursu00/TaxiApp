package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;

/**
 * Converteste definitii de metadate de tip auto number in obiecte de interfata
 * si invers. Conversia este doar partiala, convertind doar elementele specifice
 * tipului de metadata auto number. De proprietatile generale ale definitiei se
 * ocupa MetadataDefinitionConverter.
 * 
 * 
 */
public class AutoNumberMetadataDefinitionConverter {

	public static AutoNumberMetadataDefinitionModel getModelFromAutoNumberMetadataDefinition(AutoNumberMetadataDefinition autoNumberMetadataDefinition) {
		AutoNumberMetadataDefinitionModel autoNumberMetadataDefinitionModel = new AutoNumberMetadataDefinitionModel();
		
		autoNumberMetadataDefinitionModel.setPrefix(autoNumberMetadataDefinition.getPrefix());
		autoNumberMetadataDefinitionModel.setNumberLength(autoNumberMetadataDefinition.getNumberLength());

		return autoNumberMetadataDefinitionModel;
	}

	public static AutoNumberMetadataDefinition getAutoNumberMetadataDefinitionFromModel(AutoNumberMetadataDefinitionModel autoNumberMetadataDefinitionModel) {
		AutoNumberMetadataDefinition autoNumberMetadataDefinition = new AutoNumberMetadataDefinition();
		
		autoNumberMetadataDefinition.setPrefix(autoNumberMetadataDefinitionModel.getPrefix());
		autoNumberMetadataDefinition.setNumberLength(autoNumberMetadataDefinitionModel.getNumberLength());
		
		return autoNumberMetadataDefinition;
	}
}