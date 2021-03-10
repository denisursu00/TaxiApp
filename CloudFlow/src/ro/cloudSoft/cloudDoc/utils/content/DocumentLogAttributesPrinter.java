package ro.cloudSoft.cloudDoc.utils.content;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.DocumentLogAttributes;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

import com.google.common.collect.Lists;

public class DocumentLogAttributesPrinter {
	
	private static final String PATH_SEPARATOR = "/";

	public static String asString(DocumentLogAttributes documentLogAttributes) {
		
		StringBuilder stringRepresentation = new StringBuilder();
		
		stringRepresentation.append("numele 'real' al spatiului de lucru: [").append(documentLogAttributes.getDocumentLocationRealName()).append("]");
		
		stringRepresentation.append(", ");
		stringRepresentation.append("ID-ul: [").append(documentLogAttributes.getDocumentId()).append("]");

		stringRepresentation.append(", ");
		stringRepresentation.append("ID-ul tipului de document: [").append(documentLogAttributes.getDocumentTypeId()).append("]");

		List<String> parentFolderNamesFromRootToDocument = Lists.reverse(documentLogAttributes.getParentFolderNames());
		String folderPathToDocument = PATH_SEPARATOR;
		if (!parentFolderNamesFromRootToDocument.isEmpty()) {
			StringBuilder folderPathToDocumentTemp = new StringBuilder();
			folderPathToDocumentTemp.append(PATH_SEPARATOR);
			for (String parentFolderName : parentFolderNamesFromRootToDocument) {
				folderPathToDocumentTemp.append(parentFolderName);
				folderPathToDocumentTemp.append(PATH_SEPARATOR);
			}
			folderPathToDocument = folderPathToDocumentTemp.toString();
		}
		
		stringRepresentation.append(", ");
		stringRepresentation.append("calea (foldere): [").append(folderPathToDocument).append("]");

		Date createdDate = documentLogAttributes.getCreatedDate();
		String createdDateAsString = LogHelper.formatDateForLog(createdDate);
		
		stringRepresentation.append(", ");
		stringRepresentation.append("numele: [").append(documentLogAttributes.getDocumentName()).append("]");
		
		stringRepresentation.append(", ");
		stringRepresentation.append("data crearii: [").append(createdDateAsString).append("]");
		
		stringRepresentation.append(", ");
		stringRepresentation.append("ID-ul autorului: [").append(documentLogAttributes.getAuthorUserId()).append("]");
		
		return stringRepresentation.toString();
	}
}