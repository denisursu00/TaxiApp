package ro.cloudSoft.cloudDoc.core.constants;

/**
 * Contine constantele aplicatiei legate de securitate sau de
 * operatiile utilizatorului.
 * 
 * 
 */
public class AppConstants {
	
	// tipurile de entitati ce pot avea permisiuni
    public final static int ACL_ENTITY_TYPE_USER = 1;
    public final static int ACL_ENTITY_TYPE_ORGANIZATION_UNIT = 2;
    public final static int ACL_ENTITY_TYPE_GROUP = 3;
    
    // codurile rolurilor de securitate
    public final static String ACL_ROLE_COORDINATOR = "1111";
    public final static String ACL_ROLE_COLLABORATOR = "1110";
    public final static String ACL_ROLE_EDITOR = "1100";
    public final static String ACL_ROLE_READER = "1000";
    
    public final static String PREFIX_JR_PROPERTY_NAME_METADATA_STRING = "th:metadataString_";
    public final static String PREFIX_JR_PROPERTY_NAME_METADATA_STRING_MULTIPLE = "th:metadataStringMultiple_";
    public final static String PREFIX_JR_PROPERTY_NAME_METADATA_NUMBER = "th:metadataNumber_";
    public final static String PREFIX_JR_PROPERTY_NAME_METADATA_COLLECTION = "th:metadataCollection_";
}