package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit;

import org.apache.jackrabbit.JcrConstants;

public class JackRabbitConstants {
	
	// elemente comune tuturor nodurilor din repository
	public final static String NODE_PROPERTY_FOLDER_OR_DOCUMENT_ID = JcrConstants.JCR_UUID;
	public final static String NODE_PROPERTY_PRIMARY_TYPE = JcrConstants.JCR_PRIMARYTYPE;
	
	// ACL
	public final static String ACL_PROPERTY_NAME = "th:acl";
	public final static String ACL_PROPERTY_PREFIX_ENTITY_ID = "e";
	public final static String ACL_PROPERTY_PREFIX_ENTITY_TYPE = "t";
	public final static String ACL_PROPERTY_PREFIX_PERMISSION = "p";
	
	// denumire noduri si proprietati folder
	public final static String FOLDER_NODE_TYPE = "th:folder";
	public final static String FOLDER_NODE_PREFIX = "fol_node_";
	public final static String FOLDER_PROPERTY_NAME = "th:name";
	public final static String FOLDER_PROPERTY_DOCUMENT_TYPE_ID = "th:documentTypeId";

	// denumire noduri si proprietati document
	public final static String DOCUMENT_NODE_TYPE = "th:document";
	public final static String DOCUMENT_NODE_PREFIX = "doc_node_";
	public final static String DOCUMENT_PROPERTY_DOCUMENT_NAME = "th:name";
	public final static String DOCUMENT_PROPERTY_DOCUMENT_TYPE_ID = "th:documentTypeId";
	public final static String DOCUMENT_PROP_WORKFLOW_STATE_ID = "th:workflowStateId";
	public final static String DOCUMENT_PROPERTY_LAST_VERSION_NUMBER = "th:lastVersionNumber";
	
	public static final String VERSION_NODE_PREFIX = "version";
	public static final String VERSION_NODE_TYPE = "th:documentVersion";
	public static final String VERSION_PROPERTY_LAST_STABLE_VERSION = "th:lastStableVersion";
	public static final String VERSION_PROPERTY_VERSION_AUTHOR = "th:versionAuthor";
	public static final String VERSION_PROPERTY_VERSION_DESCRIPTION = "th:versionDescription";
	public final static String VERSION_PROPERTY_WORKFLOW_STATE_ID = "th:versionWorkflowStateId";

	// separatoare folosite pentru metadatele collectiei
	public final static String SEPARATOR_IDS_FROM_VALUES = "#";
	
	public final static String ATTACHMENT_NODE_PREFIX = "th:attachment";
	public final static String ATTACHMENT_NODE_TYPE = "th:attachment";
	public final static String ATTACHMENT_PROPERTY_NAME = "th:name";
	public final static String ATTACHMENT_PROPERTY_DATA = "th:data";

	// denumire noduri si proprietati entity
	public final static String ENTITY_PROPERTY_NAME = "th:name";
	public final static String ENTITY_PROPERTY_AUTHOR = "th:author";
	public final static String ENTITY_PROPERTY_LOCKED_BY = "th:lockedBy";
	public final static String ENTITY_PROPERTY_DESCRIPTION = "th:description";
	public final static String ENTITY_PROPERTY_LAST_MODIFIED = "th:lastModified";
	public final static String ENTITY_PROPERTY_CREATED = "th:created";
	
	public final static String IN_CLAUSE_VALUES_SEPARATOR = ", ";
}