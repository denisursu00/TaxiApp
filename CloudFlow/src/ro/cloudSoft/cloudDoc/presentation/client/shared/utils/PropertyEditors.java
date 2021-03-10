package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.BooleanPropertyEditor;
import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.DatePropertyEditor;
import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.IntegerPropertyEditor;
import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.LongPropertyEditor;

/**
 * Contine editoare de proprietati pentru campuri de formular GXT.
 * 
 * 
 */
public class PropertyEditors {

	public static final BooleanPropertyEditor BOOLEAN = new BooleanPropertyEditor();	
	public static final DatePropertyEditor DATE = new DatePropertyEditor();
	public static final IntegerPropertyEditor INTEGER = new IntegerPropertyEditor();
	public static final LongPropertyEditor LONG = new LongPropertyEditor();
}