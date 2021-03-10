package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query;

import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.JackRabbitConstants;

/**
 * 
 */
public class DocumentsForPermissionReplacementQueryBuilder {

	public static String getQuery(Long documentTypeId) {
		String primaryTypeRestriction = ("@jcr:primaryType = " + ("'" + JackRabbitConstants.DOCUMENT_NODE_TYPE + "'"));
		String documentTypeIdRestriction = ("@" + JackRabbitConstants.DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID + " = " + ("'" + documentTypeId + "'"));
		String query = ("//element(*, " + JackRabbitConstants.DOCUMENT_NODE_TYPE + ")[" + (primaryTypeRestriction + " and " + documentTypeIdRestriction) + "]");
		return query;
	}
}