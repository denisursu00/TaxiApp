package ro.taxiApp.docs.utils.listItemLabelProviders;

import java.util.Collection;
import java.util.List;

/**
 * Provider pentru etichetele item-ilor unei metadate de tip lista, folosit doar pentru compatibilitate.
 * Este destinat a fi utilizat in cazul in care trebuie specificat un parametru de tip provider,
 * dar metadata corespunzatoare executiei NU este de tip lista.
 * 
 * 
 */
public class NonListMetadataDefinitionListItemLabelProvider implements ListItemLabelProvider {
	
	/**
	 * Metoda va arunca exceptie intrucat acest provider NU trebuie folosit pentru o metadata de tip lista.
	 */
	@Override
	public List<String> getListItemLabels(String metadataName, Collection<String> listItemValues) {
		throw new RuntimeException("Provider-ul nu este pentru o metadata de tip lista.");
	}
}