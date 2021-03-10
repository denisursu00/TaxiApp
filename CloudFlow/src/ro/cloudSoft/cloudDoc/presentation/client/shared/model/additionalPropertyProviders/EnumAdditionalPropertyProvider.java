package ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;

import com.extjs.gxt.ui.client.data.Model;

/**
 * Property provider pentru numele unei proprietati de tip enum
 */
public class EnumAdditionalPropertyProvider implements AdditionalPropertyProviderForModel {
	
	private static final long serialVersionUID = 1L;
	
	private String propertyNameWhichUsesThisProvider;
	private String propertyNameForEnum;
	private Model model;
	
	/**
	 * Constructor necesar pentru serializare
	 */
	protected EnumAdditionalPropertyProvider() {}
	
	/**
	 * @param propertyNameWhichUsesThisProvider numele proprietatii pt. care va fi folosit acest provider
	 * @param propertyNameForEnum numele proprietatii ce tine enum-ul
	 * @param model modelul ce contine valoarea enum-ului
	 */
	public EnumAdditionalPropertyProvider(String propertyNameWhichUsesThisProvider, String propertyNameForEnum, Model model) {
		this.propertyNameWhichUsesThisProvider = propertyNameWhichUsesThisProvider;
		this.propertyNameForEnum = propertyNameForEnum;
		this.model = model;
	}

	@Override
	public String getPropertyName() {
		return propertyNameWhichUsesThisProvider;
	}

	@Override
	public Object getPropertyValue() {
		Enum<?> enumPropertyValue = model.get(propertyNameForEnum);
		if (enumPropertyValue != null) {
			return enumPropertyValue.name();
		} else {
			return null;
		}
	}
}