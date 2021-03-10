package ro.cloudSoft.common.utils.enums;

public class EnumUtils {

	/**
	 * Converteste dintr-un enum in altul folosind numele constantei.
	 * Daca enum-ul dat este null, atunci va returna tot null.
	 */
	public static <ES extends Enum<ES>, ED extends Enum<ED>> ED convert(ES sourceEnumConstant, Class<ED> destinationEnumClass) {
		
		if (sourceEnumConstant == null) {
			return null;
		}
		
		String constantName = sourceEnumConstant.name();
		ED destinationEnumConstant = Enum.valueOf(destinationEnumClass, constantName);
		return destinationEnumConstant;
	}
}