package ro.cloudSoft.cloudDoc.utils.listItemLabelProviders;

import java.util.Collection;
import java.util.List;

/**
 * Obtine etichete pentru item-ii unei metadate de tip lista.
 * 
 * 
 */
public interface ListItemLabelProvider {
	
	/**
	 * Returneaza etichetele item-ilor metadatei de tip lista.
	 * 
	 * @param metadataName numele metadatei de tip lista
	 * @param listItemValues valorile item-ilor pentru care se doreste aducerea etichetelor
	 */
	List<String> getListItemLabels(String metadataName, Collection<String> listItemValues);
}