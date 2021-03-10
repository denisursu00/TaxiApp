package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.query;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.constants.AppConstants;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants.DocumentLocationConstants;
import ro.cloudSoft.cloudDoc.plugins.content.jackrabbit.constants.QueryConstants;
import ro.cloudSoft.cloudDoc.utils.SecurityUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class VisibleDocumentLocationsQueryBuilder {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(VisibleDocumentLocationsQueryBuilder.class);

	public static String getQuery(SecurityManager userSecurity) {
		
		List<String> entityRestrictions = new ArrayList<String>();
		if (!SecurityUtils.isUserAdmin(userSecurity)) {

			// Pune restrictie pe ID-ul utilizatorului curent.
			String userRestriction = AppConstants.ACL_ENTITY_TYPE_USER
				+ DocumentLocationConstants.SEPARATOR_SECURITY_ENTITY_TYPE_FROM_ID
				+ userSecurity.getUserIdAsString();
			entityRestrictions.add(userRestriction);
			// Pune restrictie pe ID-urile unitatilor organizatorice din care face parte utilizatorul.
			for (Long organizationUnitId : userSecurity.getOrganizationUnitIds()) {
				String organizationUnitString = AppConstants.ACL_ENTITY_TYPE_ORGANIZATION_UNIT
					+ DocumentLocationConstants.SEPARATOR_SECURITY_ENTITY_TYPE_FROM_ID
					+ organizationUnitId.toString();
				entityRestrictions.add(organizationUnitString);
			}
			// Pune restrictie pe ID-urile grupurilor din care face parte utilizatorul.
			for (Long groupId : userSecurity.getGroupIds()) {
				String groupString = AppConstants.ACL_ENTITY_TYPE_GROUP
					+ DocumentLocationConstants.SEPARATOR_SECURITY_ENTITY_TYPE_FROM_ID
					+ groupId.toString();
				entityRestrictions.add(groupString);
			}
		}

		StringBuilder queryRestriction = new StringBuilder();
		for (String entityRestriction : entityRestrictions) {
			queryRestriction.append("@" + DocumentLocationConstants.SECURITY_PROPERTY_ENTITIES_WITH_ACCESS + " = '" + entityRestriction + "'");
			queryRestriction.append(QueryConstants.OR);
		}
		if (queryRestriction.length() > 0) {
			queryRestriction.delete(queryRestriction.lastIndexOf(QueryConstants.OR), queryRestriction.length());
			queryRestriction.insert(0, '[');
			queryRestriction.insert(queryRestriction.length(), ']');
		}

		String query = "/jcr:root/element(*, " + DocumentLocationConstants.SECURITY_NODE_TYPE + ")" + queryRestriction.toString();

		if (LOGGER.isDebugEnabled()) {
			String message = "Se va executa urmatoarea interogare: " + query;
			LOGGER.debug(message, "construirea query-ului pentru lista de spatii de lucru vizibile", userSecurity);
		}

		return query;
	}
}