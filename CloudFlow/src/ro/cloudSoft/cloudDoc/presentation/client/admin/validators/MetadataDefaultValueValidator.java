package ro.cloudSoft.cloudDoc.presentation.client.admin.validators;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.MetadataDefinitionAddOrEditForm;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * 
 */
public class MetadataDefaultValueValidator implements Validator {
	
	private final MetadataDefinitionAddOrEditForm form;
	
	public MetadataDefaultValueValidator(MetadataDefinitionAddOrEditForm form) {
		this.form = form;
	}

	@Override
	public String validate(Field<?> field, String value) {
		
		if (value == null) {
			return null;
		}
		
		String metadataType = form.getSelectedType();
		
		if (GwtStringUtils.isBlank(metadataType)) {
			return GwtLocaleProvider.getMessages().VALUE_CANNOT_BE_VALID_UNLESS_METADATA_TYPE_SELECTED();
		}
		
		if (metadataType.equals(MetadataDefinitionModel.TYPE_TEXT)) {
			return null;
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_NUMERIC)) {
			return (isValidNumericValue(value)) ? null : GwtLocaleProvider.getMessages().VALUE_MUST_BE_NUMERIC();
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
			return GwtLocaleProvider.getMessages().AUTO_NUMBER_METADATA_CANNOT_HAVE_DEFAULT_VALUE();
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_DATE)) {
			return (isValidDateValue(value)) ? null : GwtLocaleProvider.getMessages().DATE_MUST_BE_IN_FORMAT(GwtFormatConstants.DATE_FOR_SAVING);
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_LIST)) {
			return (isValidListValue(value)) ? null : GwtLocaleProvider.getMessages().NO_LIST_ITEM_FOUND_FOR_SPECIFIED_VALUE();
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_USER)) {
			return (isValidUserValue(value)) ? null : GwtLocaleProvider.getMessages().NO_USER_FOUND_FOR_SPECIFIED_ID();
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_TEXT_AREA)) {
			return null;
		} else if (metadataType.equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
			return GwtLocaleProvider.getMessages().METADATA_COLLECTION_CANNOT_HAVE_DEFAULT_VALUE();
		} else {
			return GwtLocaleProvider.getMessages().VALUE_CANNOT_BE_VALID_METADATA_TYPE_NOT_KNOWN();
		}
	}
	
	private boolean isValidNumericValue(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private boolean isValidDateValue(String value) {
		try {
			DateTimeFormat.getFormat(GwtFormatConstants.DATE_FOR_SAVING).parseStrict(value);
			return true;
		} catch (IllegalArgumentException iae) {
			return false;
		}
	}
	
	private boolean isValidListValue(String valueToCheck) {
		
		List<ListMetadataItemModel> listItems = form.getListItems();
		for (ListMetadataItemModel listItem : listItems) {
			if (GwtCompareUtils.areEqual(listItem.getValue(), valueToCheck)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidUserValue(String value) {
		ListStore<UserModel> usersStore = AppStoreCache.getUserListStore();
		UserModel foundUser = usersStore.findModel(UserModel.USER_PROPERTY_USERID, value);
		return (foundUser != null);
	}
}