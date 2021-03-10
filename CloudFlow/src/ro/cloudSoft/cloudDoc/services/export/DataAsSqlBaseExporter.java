package ro.cloudSoft.cloudDoc.services.export;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;

public abstract class DataAsSqlBaseExporter {
	
	protected static String SQL_NULL = "null";
	protected static String SQL_TRUE = "TRUE";
	protected static String SQL_FALSE = "FALSE";
	protected static String SQL_GENERATE_ID = "nextval('HIBERNATE_SEQUENCE')";
	
	public abstract String export();
	
	private String buildFieldPlaceholder(String fieldName) {
		return "[" + fieldName + "]";
	}
	
	protected String replaceWithNullValue(String sqlText, String fieldName) {
		return StringUtils.replace(sqlText, this.buildFieldPlaceholder(fieldName), SQL_NULL);
	}
	
	protected String replaceWithFieldValueAsSelect(String sqlText, String fieldName, String valueAsSelect) {
		if (StringUtils.isBlank(valueAsSelect)) {
			throw new RuntimeException("no select when required");
		}
		return StringUtils.replace(sqlText, this.buildFieldPlaceholder(fieldName), valueAsSelect);
	}
	
	protected String replaceWithFieldValue(String sqlText, String fieldName, String value) {
		String sqlValue = SQL_NULL;
		if (StringUtils.isNotBlank(value)) {
			sqlValue = "'" + StringEscapeUtils.escapeSql(value.trim()) + "'";
		}
		return StringUtils.replace(sqlText, this.buildFieldPlaceholder(fieldName), sqlValue);
	}
	
	protected String replaceWithFieldValue(String sqlText, String fieldName, Integer value) {
		String sqlValue = SQL_NULL;
		if (value != null) {
			sqlValue = value.toString();
		}
		return StringUtils.replace(sqlText, buildFieldPlaceholder(fieldName), sqlValue);
	}
	
	protected String replaceWithFieldValue(String sqlText, String fieldName, Long value) {
		String sqlValue = SQL_NULL;
		if (value != null) {
			sqlValue = value.toString();
		}
		return StringUtils.replace(sqlText, buildFieldPlaceholder(fieldName), sqlValue);
	}
	
	protected String replaceWithFieldValue(String sqlText, String fieldName, Boolean value) {
		String sqlValue = SQL_NULL;
		if (value != null) {
			sqlValue = value ? SQL_TRUE : SQL_FALSE;
		}
		return StringUtils.replace(sqlText, buildFieldPlaceholder(fieldName), sqlValue);
	}
	
	protected String replaceWithSqlGenerateID(String sqlText, String idFieldName) {
		return StringUtils.replace(sqlText, buildFieldPlaceholder(idFieldName), SQL_GENERATE_ID);
	}
	
	protected String getSqlForSelectGroupIdByName(String groupName) {
		return "(select ORG_ENTITY_ID from edocgroup where name='" + groupName + "')";
	}
	
	protected String getSqlForSelectWorkfowIdByName(String workflowName) {
		return "(select id from workflow where name='" + workflowName + "')";
	}
	
	protected String getSqlForSelectDocumentTypeIdByName(String documentTypeName) {
		return "(select id from documenttype where name='" + documentTypeName + "')";
	}
	
	protected String getSqlForSelectMetadataCollectionIdByDocumentTypeAndCollectionName(String documentTypeName, String collectionName) {
		return "(select id from metadatacollection where name='" + collectionName + "' and documenttype_id=" + getSqlForSelectDocumentTypeIdByName(documentTypeName) + ")";
	}
	
	protected String getSqlForSelectMimeTypeIdByExtension(String extension) {
		return "(select id from mimetype where EXTENSION='" + extension + "')";
	}
	
	protected String getSqlForSelectOrganizationUnitIdByOuName(String ouName) {
		return "(select ORG_ENTITY_ID from ORGANIZATIONUNIT where name='" + ouName  + "')";
	}
	
	protected String getSqlForSelectUserIdByUsername(String username) {
		return "(select ORG_ENTITY_ID from edocuser where username='" + username + "')";
	}
	
	protected String getSqlForSelectTransitionIdByWorkflowAndTransitionName(String workflowName, String transitionName) {
		return "(select id from workflowtransition where name='" + transitionName + "' and workflow_id="  + this.getSqlForSelectWorkfowIdByName(workflowName)+")";
	}
	
	protected String getSqlForSelectNomenclatorIdByName(String nomenclatorName) {
		return "(select id from nomenclator where name='" + nomenclatorName + "')";
	}
	
	protected String getSqlForSelectCalendarIdByName(String calendarName) {
		return "(select id from calendar where name='" + calendarName + "')";
	}
	
	protected String getSqlForSelectOrganizationEntity(OrganizationEntity entity) {
		if (entity instanceof User) {
			User user = (User) entity;
			return getSqlForSelectUserIdByUsername(user.getUsername());
		} else if (entity instanceof Group) {
			Group group = (Group) entity;
			return getSqlForSelectGroupIdByName(group.getName());
		}  else if (entity instanceof OrganizationUnit) {
			OrganizationUnit ou = (OrganizationUnit) entity;
			return getSqlForSelectOrganizationUnitIdByOuName(ou.getName());
		}
		throw new RuntimeException("unknow entity");
	}
	
	protected void addComment(StringBuilder sql, String comment) {
		sql.append("-- " + comment);
	}
	
	protected String addComment(String sql, String text) {
		String comment = "-- " + text;
		return sql != null ? (sql + comment) : comment;
	}
	
	protected void addNewLine(StringBuilder sql) {
		sql.append("\n");
	}
	
	protected void addTwoNewLines(StringBuilder sql) {
		sql.append("\n\n");
	}
	
	protected String addNewLine(String sql) {
		return sql != null ? (sql + "\n") : "\n";
	}
}
