package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants;

public class DocumentLocationConstants {

	// noduri si proprietati
	public static final String NODE_NAME = "th:documentLocationDescription";
	public static final String NODE_TYPE = "th:documentLocation";
	public static final String PROPERTY_NAME = "th:name";
	public static final String PROPERTY_DESCRIPTION = "th:description";
	// noduri si proprietati pentru securitate
	public static final String SECURITY_NODE_TYPE = "th:documentLocationSecurity";
	public static final String SECURITY_PROPERTY_ENTITIES_WITH_ACCESS = "th:entitiesWithAccess";
	
	// workspace-uri implicite
	public static final String WORKSPACE_NAME_DEFAULT = "default";
	public static final String WORKSPACE_NAME_SECURITY = "security";
	
	// separatoare legate de document location
	public static final String SEPARATOR_NAMES = "@";
	public static final String SEPARATOR_SECURITY_ENTITY_TYPE_FROM_ID = "@";
}