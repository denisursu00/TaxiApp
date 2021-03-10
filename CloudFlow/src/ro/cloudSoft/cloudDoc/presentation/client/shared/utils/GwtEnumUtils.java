package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.ArrayList;
import java.util.List;

public class GwtEnumUtils {

	public static List<String> getNames(Enum<?>[] values) {
		List<String> names = new ArrayList<String>();
		for (Enum<?> value : values) {
			names.add(value.name());
		}
		return names;
	}
}