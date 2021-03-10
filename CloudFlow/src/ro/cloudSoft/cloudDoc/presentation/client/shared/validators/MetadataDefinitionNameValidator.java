package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Validator pentru numele unei definitii de metadata, ce valideaza atat caracterele permise, cat si unicitatea
 * 
 * 
 */
public class MetadataDefinitionNameValidator implements Validator {

	/** 
	 * Expresia regulata pentru numele unei metadate
	 * <br><br>
	 * NOTA: Expresia va rula in JavaScript (GWT) deci trebuie sa aiba formatul pt. JavaScript.
	 **/
	private static final String REGEX = "[a-zA-Z0-9\\s_]+";
	
	private final MetadataDefinitionNameValidatorHelper helper;
	
	public MetadataDefinitionNameValidator(MetadataDefinitionNameValidatorHelper helper) {
		this.helper = helper;
	}
	
	@Override
	public String validate(Field<?> field, String value) {
		
		if ((value == null) || !value.matches(REGEX)) {
			return GwtLocaleProvider.getMessages().METADATA_NAME_INCORRECT();
		}
		
		if (!isMetadataDefinitionNameUnique(value)) {
			return GwtLocaleProvider.getMessages().NAME_ALREADY_EXISTS();
		}
		
		return null;
	}
	
	private boolean isMetadataDefinitionNameUnique(String name) {
		MetadataDefinitionModel metadataDefinitionWithName = helper.findMetadataDefinitionByName(name);
		if (metadataDefinitionWithName == null) {
			return true;
		} else {
			MetadataDefinitionModel editedMetadataDefinition = helper.getEditedMetadataDefinition();
			if (metadataDefinitionWithName.equals(editedMetadataDefinition)) {
				// Numele este cel al definitiei de metadata care este in editare.
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static interface MetadataDefinitionNameValidatorHelper {
		
		MetadataDefinitionModel findMetadataDefinitionByName(String name);
		
		MetadataDefinitionModel getEditedMetadataDefinition();
	}
}