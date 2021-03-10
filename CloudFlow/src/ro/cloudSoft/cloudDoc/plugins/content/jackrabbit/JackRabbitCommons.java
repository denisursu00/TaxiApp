package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.content.Permission;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class JackRabbitCommons {
	
	/**
	 * Inchide sesiunea (daca este activa).
	 * @param session sesiunea (poate fi activa sau nula)
	 */
	public static void logout(Session session) {
		if (session != null && session.isLive()) {
			session.logout();
		}
	}
	
	public static String[] prepareAclToJrValue(ACL acl) {
		if (acl == null) {
			throw new IllegalArgumentException("acl cannot be null");
		}
		if (CollectionUtils.isEmpty(acl.getPermissions())) {
			return null;
		}
		String[] jrPermisions = new String[acl.getPermissions().size()];
		List<Permission> permissions = acl.getPermissions();
		for (int i = 0; i < permissions.size(); i++) {
			Permission permission = permissions.get(i);
			String jrPermision = JackRabbitConstants.ACL_PROPERTY_PREFIX_ENTITY_TYPE + ":" + permission.getEntityType() + ","
					+ JackRabbitConstants.ACL_PROPERTY_PREFIX_ENTITY_ID + ":" + permission.getEntityId().trim() + ","
					+ JackRabbitConstants.ACL_PROPERTY_PREFIX_PERMISSION + ":" + permission.getPermission().trim();
			jrPermisions[i] = jrPermision;
		}
		return jrPermisions;
	}
	
	public static ACL getAclFromJrValue(String[] jrAcl) {
		ACL acl = new ACL();
		List<Permission> permissions = new ArrayList<Permission>();
		if (jrAcl != null && jrAcl.length > 0) {
			for (String aclString : jrAcl) {
				permissions.add(getPermissionFromJrFormat(aclString));					
			}
		}
		acl.setPermissions(permissions);
		return acl;
	}
	
	private static Permission getPermissionFromJrFormat(String aclPermisionAsJrFormat) {
		Permission  permission = new Permission();
		String[] aclProperties = StringUtils.splitByWholeSeparator(aclPermisionAsJrFormat, ",");
		for (String aclProperty : aclProperties) {
			String[] property = StringUtils.splitByWholeSeparator(aclProperty, ":");
			String prefix = property[0];
			String value = property[1];			
			if (prefix.equals(JackRabbitConstants.ACL_PROPERTY_PREFIX_ENTITY_TYPE)) {
				permission.setEntityType(Integer.valueOf(value));
			} else if (prefix.equals(JackRabbitConstants.ACL_PROPERTY_PREFIX_ENTITY_ID)) {
				permission.setEntityId(value);
			} else if (prefix.equals(JackRabbitConstants.ACL_PROPERTY_PREFIX_PERMISSION)) {
				permission.setPermission(value);
			} else {
				throw new RuntimeException("Permision storage format has unknown prefix [" + prefix + "]");
			}
		}
		return permission;
	}
	
	public static String getPropertyValueAsString(Node node, String propertyName) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("propertyName cannot be null/empty");
		}
		try {
			Property property = getNodeProperty(node, propertyName);
			if (property != null) {
				return property.getString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}		
		return null;
	}
	
	public static Date getPropertyValueAsDate(Node node, String propertyName) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("propertyName cannot be null/empty");
		}
		try {
			Property property = getNodeProperty(node, propertyName);
			if (property != null) {
				return DateUtils.parseDate(property.getString(), FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}		
		return null;
	}
	
	public static Property getNodeProperty(Node node, String propertyName)  {
		try {
			if (node.hasProperty(propertyName)) {
				return node.getProperty(propertyName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return null;
	}
	
	public static BigDecimal getPropertyValueAsBigDecimal(Node node, String propertyName) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("propertyName cannot be null/empty");
		}
		try {
			Property property = getNodeProperty(node, propertyName);
			if (property != null) {
				return property.getDecimal();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}		
		return null;
	}
	
	public static Long getPropertyValueAsLong(Node node, String propertyName) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("propertyName cannot be null/empty");
		}
		try {
			Property property = getNodeProperty(node, propertyName);
			if (property != null) {
				return property.getLong();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}		
		return null;
	}
	
	public static String[] getPropertyValueAsStringArray(Node node, String propertyName) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("propertyName cannot be null/empty");
		}
		try {
			Property property = getNodeProperty(node, propertyName);
			if (property != null) {
				Value[] values = property.getValues();
				if (values != null && values.length > 0) {
					String[] valueAsArray = new String[values.length];
					for (int i = 0; i < valueAsArray.length; i++) {
						valueAsArray[i] = values[i].getString();
					}
					return valueAsArray;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}	
		return null;
	}
	
	public static List<String> getPropertyValueAsStringList(Node node, String propertyName) {
		String[] asArray = getPropertyValueAsStringArray(node, propertyName);
		if (asArray != null && asArray.length > 0) {
			return Arrays.asList(asArray);
		}
		return null;
	}
	
	public static byte[] getContentFromAttachmentNode(Node attachmentNode) throws RepositoryException, IOException {
		try {
			Binary contentBinary = null;
			NodeIterator contentNodes = attachmentNode.getNodes(JcrConstants.JCR_CONTENT + "*");
			if (contentNodes.hasNext()) {
				Node contentNode = contentNodes.nextNode();
				contentBinary = contentNode.getProperty(JcrConstants.JCR_DATA).getValue().getBinary();
			}		
			return toByteArray(contentBinary);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static byte[] toByteArray(Binary binary) {
		byte[] attachmentContent = null;
		try {
			if (binary == null) {
				return null;
			}
			try (InputStream attachmentContentStream = binary.getStream()) {
				attachmentContent = IOUtils.toByteArray(attachmentContentStream);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return attachmentContent;
	}
	
	public static String[] preparePropertyValueAsMultipleFromMetadataInstance(MetadataInstance metadataInstance) {
		List<String> valuesAsList = metadataInstance.getValues();
		String[] valuesAsArray = null;
		if (CollectionUtils.isNotEmpty(valuesAsList)) {
			valuesAsArray = valuesAsList.toArray(new String[valuesAsList.size()]);
		}
		if (valuesAsArray == null) {
			valuesAsArray = new String[0];
		}
		return valuesAsArray;
	}
	
	private static String buildMetadataValueOfCollectionForEqualSearch(Long metadataDefinitionId, String filterValue) {
		String separator = JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES;		
		return metadataDefinitionId + separator + filterValue + separator;
	}
	
	private static String buildMetadataValueOfCollectionForContainSearch(Long metadataDefinitionId, String filterValue) {
		String separator = JackRabbitConstants.SEPARATOR_IDS_FROM_VALUES;		
		return metadataDefinitionId + separator + "%"+ filterValue + "%"+ separator;
	}
		
	public static String getContainsWithEqualMatchFilterInCollectionProperty(String nodeAlias, DocumentType documentType, String infoinvitatiMetadataCollectionJrPropertyName, String filterValue, String metadataCollectionName, String metadataName, boolean ignoreCase) {
		MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, metadataCollectionName, metadataName); 
		String builtMetadataValueOfCollectionForEqualSearch = JackRabbitCommons.buildMetadataValueOfCollectionForEqualSearch(metadataDefinition.getId(), filterValue) ;
		if (ignoreCase) {
			return "UPPER(" + nodeAlias +".[" + infoinvitatiMetadataCollectionJrPropertyName + "]) like '%" + builtMetadataValueOfCollectionForEqualSearch.toUpperCase() + "%'";
		} else {
			return nodeAlias +".[" + infoinvitatiMetadataCollectionJrPropertyName + "] like '%" + builtMetadataValueOfCollectionForEqualSearch + "%'";
		}
	}
	
	public static String getContainsWithContainMatchFilterInCollectionProperty(String nodeAlias, DocumentType documentType, String infoMetadataCollectionJrPropertyName, String filterValue, String metadataCollectionName, String metadataName, boolean ignoreCase) {
		MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, metadataCollectionName, metadataName); 
		// TODO : 0017387: De refacut cautarea in metadate cu contain filter
		if (ignoreCase) {
			return "UPPER(" + nodeAlias +".[" + infoMetadataCollectionJrPropertyName + "]) like '%" + JackRabbitCommons.buildMetadataValueOfCollectionForContainSearch(metadataDefinition.getId(), filterValue).toUpperCase() + "%'";
		} else {
			return nodeAlias +".[" + infoMetadataCollectionJrPropertyName + "] like '%" + JackRabbitCommons.buildMetadataValueOfCollectionForContainSearch(metadataDefinition.getId(), filterValue) + "%'";
		}	
	}
	
	public static String buildINComparisonValuesAsString(List<? extends Object> values) {
		if (CollectionUtils.isEmpty(values)) {
			throw new IllegalStateException("IN values is cannot be empty");
		}
		StringBuilder sb = new StringBuilder();
		for (Object value : values) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("'").append(value.toString()).append("'");
		}
		return "(" + sb.toString() + ")";
	}

}