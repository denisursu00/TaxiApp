package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFileUtils;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Reprezinta un validator pentru un camp de upload.
 * Se pot specifica extensiile permise pentru fisier.
 * 
 * 
 */
public class FileFieldValidator implements Validator {
	
	private final Set<String> validExtensions;
	
	public FileFieldValidator() {
		this.validExtensions = new HashSet<String>();
	}
	
	public FileFieldValidator(String validExtension) {
		this();
		this.validExtensions.add(validExtension);
	}
	
	public FileFieldValidator(Collection<String> validExtensions) {
		this();
		this.validExtensions.addAll(validExtensions);
	}

	@Override
	public String validate(Field<?> field, String value) {
		if (value == null) {
			return GwtLocaleProvider.getMessages().REQUIRED_FIELD();
		}
		if (!this.validExtensions.isEmpty() && !this.validExtensions.contains(GwtFileUtils.getExtension(value))) {
			return GwtLocaleProvider.getMessages().RESTRICTED_FILE_TYPE();
		}
		return null;
	}
}