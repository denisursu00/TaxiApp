package ro.cloudSoft.common.utils;

import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

/**
 * Contine metode utilitare legate de Apache JackRabbit.
 * 
 * 
 */
public class JackRabbitUtils {
	

	/**
	 * Filtreaza numele unui nod, inlocuind caracterele interzise.
	 * @param name numele nodului
	 * @return numele nodului, filtrat
	 */
	/*public static String filterNodeName(String name) {
		StringBuilder filteredName = new StringBuilder();
		
		for (int i = 0; i < name.length(); i++) {
			
			char character = name.charAt(i);
			
			switch (character) {
				case ' ':
					filteredName.append('_');
					break;
				case '[':
					filteredName.append('(');
					break;
				case ']':
					filteredName.append(')');
					break;
				default:
					filteredName.append(character);
					break;
			}
		}
		
		return filteredName.toString();
	}*/
	
	public static String formatListOfStringForINClause( Set<String> inList) {
		StringBuffer inListBuffer = new StringBuffer();
//		inListBuffer.append("(");
		boolean isFirstItem = true;
		for (String item : inList) {
			if (!isFirstItem) {
				inListBuffer.append(JackRabbitConstants.IN_CLAUSE_VALUES_SEPARATOR);
			}
			inListBuffer.append("'");
			inListBuffer.append(item);
			inListBuffer.append("'");
			isFirstItem = false;
		}
//		inListBuffer.append(")");
		return inListBuffer.toString();
	}

	public static Object getMetadataValue(Document document, DocumentType documentType, String metadataName) {
		List<MetadataInstance> metadataInstanceList = document.getMetadataInstanceList();
	
		MetadataDefinition metadataDefinition = null;
		for (MetadataDefinition md : documentType.getMetadataDefinitions()) {
			if (md.getName().equalsIgnoreCase(metadataName)) {
				metadataDefinition = md;
			}
		}
		if (metadataDefinition != null) {

			for (MetadataInstance mtdInstance : metadataInstanceList) {
				if (mtdInstance.getMetadataDefinitionId().equals(metadataDefinition.getId())) {
					return mtdInstance.getValue();
				}
			}
		}
		return null;
	}
}