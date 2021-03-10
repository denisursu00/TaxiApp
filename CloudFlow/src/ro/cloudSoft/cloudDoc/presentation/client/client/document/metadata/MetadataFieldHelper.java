package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.Arrays;
import java.util.List;

public class MetadataFieldHelper {

	public static void setValues(MetadataField field, String... values) {
		if (values != null) {
			field.setMetadataValues(Arrays.asList(values));
		}
	}
	
	public static String getValue(MetadataField field) {
		
		List<String> values = field.getMetadataValues();
		
		if (values.size() == 0) {
			return null;
		} else if (values.size() == 1) {
			return values.get(0);
		} else {
			// Sunt mai multe valori, dar s-a cerut o singura valoare.
			throw new IllegalArgumentException("Metadata cu numele [" + field.getMetadataDefinitionName() + "] e cu mai multe valori posibile.");
		}
	}
}