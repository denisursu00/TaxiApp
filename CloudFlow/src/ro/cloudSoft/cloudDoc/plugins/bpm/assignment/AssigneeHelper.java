package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

/**
 * Contine logica pentru crearea si parsarea asignarilor pentru grupuri, unitati organizatorice, utilizatori.
 * 
 * 
 */
public class AssigneeHelper {

	private static final String ASSIGNEE_TYPE_GROUP = "3";
	private static final String ASSIGNEE_TYPE_ORGANIZATION_UNIT = "2";
	
	private static final String ASSIGNEE_ID_SEPARATOR = "|";
	
	private static final String ASSIGNEE_PREFIX_GROUP = ASSIGNEE_TYPE_GROUP + ASSIGNEE_ID_SEPARATOR;
	private static final String ASSIGNEE_PREFIX_ORGANIZATION_UNIT = ASSIGNEE_TYPE_ORGANIZATION_UNIT + ASSIGNEE_ID_SEPARATOR;

	
	public static String getAssigneeValueForGroup(Long groupId) {
		return (ASSIGNEE_PREFIX_GROUP + groupId);
	}

	public static String getAssigneeValueForOrganizationUnit(Long organizationUnitId) {
		return (ASSIGNEE_PREFIX_ORGANIZATION_UNIT + organizationUnitId);
	}

	public static String getAssigneeValueForUser(Long userId) {
		return userId.toString();
	}
	
	
	public static boolean isGroup(String assignee) {
		return assignee.startsWith(ASSIGNEE_PREFIX_GROUP);
	}

	public static boolean isOrganizationUnit(String assignee) {
		return assignee.startsWith(ASSIGNEE_PREFIX_ORGANIZATION_UNIT);
	}

	public static boolean isUser(String assignee) {
		return (!isGroup(assignee) && !isOrganizationUnit(assignee));
	}
	
	
	private static Long getIdFromAssigne(String assignee, String prefixToRemove) {
		String idAsString = assignee.substring(prefixToRemove.length());
		return Long.valueOf(idAsString);
	}
	
	public static Long getGroupId(String assignee) {
		if (!isGroup(assignee)) {
			throw new IllegalArgumentException("Asignarea [" + assignee + "] nu este pentru un grup.");
		}
		return getIdFromAssigne(assignee, ASSIGNEE_PREFIX_GROUP);
	}
	
	public static Long getOrganizationUnitId(String assignee) {
		if (!isOrganizationUnit(assignee)) {
			throw new IllegalArgumentException("Asignarea [" + assignee + "] nu este pentru o unitate organizatorica.");
		}
		return getIdFromAssigne(assignee, ASSIGNEE_PREFIX_ORGANIZATION_UNIT);
	}
	
	public static Long getUserId(String assignee) {
		if (!isUser(assignee)) {
			throw new IllegalArgumentException("Asignarea [" + assignee + "] nu este pentru un utilizator.");
		}
		return Long.valueOf(assignee);
	}
}