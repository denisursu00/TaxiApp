package ro.cloudSoft.cloudDoc.services.export;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.AssignedEntityTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.InitiatorTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.ManuallyChosenEntitiesTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.UserMetadataTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;

public class WorkflowDataAsSqlExporter extends DataAsSqlBaseExporter {
	
	private static final String COL_NAME_TEMP_WORKFLOW_ID_TEMP = "temp_workflow_id_";
	
	private WorkflowDao workflowDao;
	private GroupPersistencePlugin groupPersistencePlugin;
	private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;	
	
	@Override
	public String export() {
		
		StringBuilder sql = new StringBuilder();
		
		addComment(sql, "Coloana ajutatoare la migrare.");
		addNewLine(sql);
		sql.append("ALTER TABLE workflowstate ADD COLUMN IF NOT EXISTS " + COL_NAME_TEMP_WORKFLOW_ID_TEMP + " BIGINT;");
		addNewLine(sql);
		
		List<Workflow> workflows = workflowDao.getAllWorkflows();
		if (CollectionUtils.isNotEmpty(workflows)) {
			for (Workflow workflow : workflows) {
				addNewLine(sql);
				sql.append(generateSQL(workflow));
			}
		}
		
		return sql.toString();
	}
	
	private String generateSQL(Workflow workflow) {
		
		StringBuilder allSql = new StringBuilder();
		
		addNewLine(allSql);
		addComment(allSql, "Workflow > " + workflow.getName());
		addNewLine(allSql);
		
		String wSql = ""
				+ "INSERT INTO workflow ("
				+ "id, description, name, processdefinitionid, version_number, base_version_id, source_version_id"
				+ ") VALUES ("
				+ "[id], [description], [name], null, [versionNumber], null, null"
				+ ");";
		wSql = replaceWithSqlGenerateID(wSql, "id");
		wSql = replaceWithFieldValue(wSql, "description", workflow.getDescription());
		wSql = replaceWithFieldValue(wSql, "name", workflow.getName());
		wSql = replaceWithFieldValue(wSql, "versionNumber", workflow.getVersionNumber());
		
		allSql.append(wSql);
				
		// >>> DocumentTypes Relation
		Set<DocumentType> documentTypes = workflow.getDocumentTypes();
		if (CollectionUtils.isNotEmpty(documentTypes)) {
			addNewLine(allSql);
			for (DocumentType documentType : documentTypes) {	
				addNewLine(allSql);
				allSql.append(generateSQLForWorkflowAndDocumentTypesRelation(workflow.getName(), documentType.getName()));
			}
		}
				
		// >>> Supervisors Relation
		Set<OrganizationEntity> supervisors = workflow.getSupervisors();
		if (CollectionUtils.isNotEmpty(supervisors))  {
			addNewLine(allSql);
			for (OrganizationEntity entity : supervisors) {
				addNewLine(allSql);
				allSql.append(generateSQLForWorkflowAndSupervisorRelation(workflow.getName(), entity));
			}
		}
		
		
		// >>> States
		List<WorkflowState> states = workflowDao.getWorkflowStatesByWorkflowId(workflow.getId());
		if (CollectionUtils.isNotEmpty(states))  {
			addNewLine(allSql);
			for (WorkflowState state : states) {
				addNewLine(allSql);
				allSql.append(generateSQLForWorkflowState(state, workflow.getName()));
			}
		}
		
		// >>> Transitions
		Set<WorkflowTransition> transitions = workflow.getTransitions();
		if (CollectionUtils.isNotEmpty(transitions)) {
			addNewLine(allSql);
			for (WorkflowTransition transition : transitions) {
				addNewLine(allSql);
				allSql.append(generateSQLForWorkflowTransition(transition, workflow.getName()));
			}
		}
		
		addNewLine(allSql);
		addComment(allSql, "Workflow < " + workflow.getName());
		
		return allSql.toString();
	}
	
	private String generateSQLForWorkflowState(WorkflowState state, String workflowName) {
		
		String sql = ""
				+ "INSERT INTO workflowstate ("
				+ "id, attachmentspermission, code, name, statetype, automaticrunning, classpath, " + COL_NAME_TEMP_WORKFLOW_ID_TEMP
				+ ") VALUES ("
				+ "[id], [attachmentspermission], [code], [name], [statetype], [automaticrunning], [classpath], [" +COL_NAME_TEMP_WORKFLOW_ID_TEMP + "]"
				+ ");";
		
		sql = replaceWithSqlGenerateID(sql, "id");
		sql = replaceWithFieldValue(sql, "attachmentspermission", state.getAttachmentsPermission());
		sql = replaceWithFieldValue(sql, "code", state.getCode());
		sql = replaceWithFieldValue(sql, "name", state.getName());
		sql = replaceWithFieldValue(sql, "statetype", state.getStateType());
		sql = replaceWithFieldValue(sql, "automaticrunning", state.isAutomaticRunning());
		sql = replaceWithFieldValue(sql, "classpath", state.getClassPath());
		sql = replaceWithFieldValueAsSelect(sql, COL_NAME_TEMP_WORKFLOW_ID_TEMP, getSqlForSelectWorkfowIdByName(workflowName));
		
		return sql;
	}
	
	private String generateSQLForWorkflowTransition(WorkflowTransition transition, String workflowName) {
		
		String sql = ""
				+ "INSERT INTO workflowtransition ("
				+ "id, available_4_auto_actions_only, deadlineaction, deadlineactiontype, deadlinenotifyresendinterval, deadlineperiod, name, "
				+ "routingcondition, routingdestinationid, routingdestinationparameter, routingtype, finalstate_id, startstate_id, workflow_id"
				+ ") VALUES ("
				+ "[id], [available_4_auto_actions_only], [deadlineaction], [deadlineactiontype], [deadlinenotifyresendinterval], [deadlineperiod], [name], "
				+ "[routingcondition], [routingdestinationid], [routingdestinationparameter], [routingtype], [finalstate_id], [startstate_id], [workflow_id]"
				+ ");";
		sql = replaceWithSqlGenerateID(sql, "id");
		sql = replaceWithFieldValue(sql, "available_4_auto_actions_only", transition.isAvailableForAutomaticActionsOnly());
		sql = replaceWithFieldValue(sql, "deadlineaction", transition.isDeadlineAction());
		sql = replaceWithFieldValue(sql, "deadlineactiontype", transition.getDeadlineActionType());
		sql = replaceWithFieldValue(sql, "deadlinenotifyresendinterval", transition.getDeadlineNotifyResendInterval());
		sql = replaceWithFieldValue(sql, "deadlineperiod", transition.getDeadlinePeriod());
		sql = replaceWithFieldValue(sql, "name", transition.getName());
		sql = replaceWithFieldValue(sql, "routingcondition", transition.getRoutingCondition());		
		String selectSqlRoutingDestination = null;
		if (StringUtils.isNotBlank(transition.getRoutingType())) {
			
			if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_GROUP)) {
				Long groupId = transition.getRoutingDestinationId();
				if (groupId == null) {
					throw new RuntimeException("no group Id when expected");
				}
				Group group = groupPersistencePlugin.getGroupById(groupId);
				selectSqlRoutingDestination = getSqlForSelectGroupIdByName(group.getName());
				
			} else if (transition.getRoutingType().equals(WorkflowTransition.ROUTING_OU)) {
				Long ouId = transition.getRoutingDestinationId();
				if (ouId == null) {
					throw new RuntimeException("no ou Id when expected");
				}
				OrganizationUnit ou = organizationUnitPersistencePlugin.getOrganizationUnitById(ouId);
				selectSqlRoutingDestination = getSqlForSelectOrganizationUnitIdByOuName(ou.getName());
			}
			
		}
		if (StringUtils.isNotBlank(selectSqlRoutingDestination)) {
			sql = replaceWithFieldValueAsSelect(sql, "routingdestinationid", selectSqlRoutingDestination);
		} else {
			sql = replaceWithNullValue(sql, "routingdestinationid");
		}
		
		sql = replaceWithFieldValue(sql, "routingdestinationparameter", transition.getRoutingDestinationParameter());
		sql = replaceWithFieldValue(sql, "routingtype", transition.getRoutingType());
		sql = replaceWithFieldValueAsSelect(sql, "finalstate_id", getSqlForSelectStateIdByStateCodeAndWorkflowName(transition.getFinalState().getCode(), workflowName));
		sql = replaceWithFieldValueAsSelect(sql, "startstate_id", getSqlForSelectStateIdByStateCodeAndWorkflowName(transition.getStartState().getCode(), workflowName));		
		sql = replaceWithFieldValueAsSelect(sql, "workflow_id", getSqlForSelectWorkfowIdByName(workflowName));
		
		
		// >>> ExtraViewers
		
		Set<OrganizationEntity> extraViewers = transition.getExtraViewers();
		if (CollectionUtils.isNotEmpty(extraViewers)) {
			StringBuilder extraViewersBuilder = new StringBuilder();
			for (OrganizationEntity entity : extraViewers) {
				extraViewersBuilder.append(generateSQLForTransitionAndExtraViewerRelation(workflowName, transition, entity));
				addNewLine(extraViewersBuilder);
			}
			sql = addNewLine(sql);
			sql += extraViewersBuilder.toString();
		}
		
		
		// >>> Notifications
		
		Set<TransitionNotification> notifications = transition.getNotifications();
		if (CollectionUtils.isNotEmpty(notifications)) {			
			StringBuilder notificationsBuilder = new StringBuilder();
			for (TransitionNotification notification : notifications) {
				notificationsBuilder.append(generateSQLForTransitionNotification(workflowName, transition, notification));
				addNewLine(notificationsBuilder);
			}
			sql = addNewLine(sql);
			sql += notificationsBuilder.toString();
		}
				
		return sql;
	}
	
	
	private String generateSQLForTransitionNotification(String workflowName, WorkflowTransition transition, TransitionNotification notification) {
		
		String sqlNotification = ""
				+ "WITH notificationRow AS (" + "\n"
				+ "\tINSERT INTO transition_notifications (id, transition_id, email_subject_template, email_content_template) VALUES ([id], [transition_id], [email_subject_template], [email_content_template])" + "\n"
				+ "\tRETURNING id" + "\n"
				+ ") ";
		
		sqlNotification = replaceWithSqlGenerateID(sqlNotification, "id");
		sqlNotification = replaceWithFieldValueAsSelect(sqlNotification, "transition_id", getSqlForSelectTransitionIdByWorkflowAndTransitionName(workflowName, transition.getName()));
		sqlNotification = replaceWithFieldValue(sqlNotification, "email_subject_template", notification.getEmailSubjectTemplate());
		sqlNotification = replaceWithFieldValue(sqlNotification, "email_content_template", notification.getEmailContentTemplate());
		
		String sqlSpecificNotification = "";
		if (notification instanceof AssignedEntityTransitionNotification) {
			sqlSpecificNotification += "INSERT INTO assigned_trans_notifs (";
			sqlSpecificNotification += "notification_id";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow)";
			sqlSpecificNotification += ");";
		} else if (notification instanceof HierarchicalSuperiorOfInitiatorTransitionNotification) {
			sqlSpecificNotification += "INSERT INTO sup_of_initiator_trans_notifs (";
			sqlSpecificNotification += "notification_id";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow)";
			sqlSpecificNotification += ");";
		} else if (notification instanceof HierarchicalSuperiorOfUserMetadataTransitionNotification) {
			
			HierarchicalSuperiorOfUserMetadataTransitionNotification specificnotification = (HierarchicalSuperiorOfUserMetadataTransitionNotification) notification;
			sqlSpecificNotification += "INSERT INTO sup_of_user_mtadta_trans_notif (";
			sqlSpecificNotification += "notification_id, metadata_name";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow), [metadata_name]";
			sqlSpecificNotification += ");";
			
			sqlSpecificNotification = replaceWithFieldValue(sqlSpecificNotification, "metadata_name", specificnotification.getMetadataName());
			
		} else if (notification instanceof InitiatorTransitionNotification) {
			sqlSpecificNotification += "INSERT INTO initiator_trans_notifs (";
			sqlSpecificNotification += "notification_id";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow)";
			sqlSpecificNotification += ");";
		} else if (notification instanceof ManuallyChosenEntitiesTransitionNotification) {
			// TODO - De imlementat tot
			/*sqlSpecificNotification += "INTO manual_trans_notifs (";
			sqlSpecificNotification += "notification_id";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow)";
			sqlSpecificNotification += ");";*/
			throw new RuntimeException("ManuallyChosenEntitiesTransitionNotification Not implemented");
		} else if (notification instanceof  UserMetadataTransitionNotification) {
			
			UserMetadataTransitionNotification specificnotification = (UserMetadataTransitionNotification) notification;
			sqlSpecificNotification += "INSERT INTO user_meta_trans_notifs (";
			sqlSpecificNotification += "notification_id, metadata_name";
			sqlSpecificNotification += ") VALUES (";
			sqlSpecificNotification += "(select id from notificationRow), [metadata_name]";
			sqlSpecificNotification += ");";
			
			sqlSpecificNotification = replaceWithFieldValue(sqlSpecificNotification, "metadata_name", specificnotification.getMetadataName());
			
		} else {
			throw new RuntimeException("notificare necunoscuta");
		}
		
		return sqlNotification + sqlSpecificNotification;
	}
	
	private String generateSQLForTransitionAndExtraViewerRelation(String workflowName, WorkflowTransition transition, OrganizationEntity entity) {
		String sql = ""
				+ "INSERT INTO workflowviewer_oe ("
				+ "workflowtransition_id, extraviewers_org_entity_id"
				+ ") VALUES ("
				+ "[workflowtransition_id], [extraviewers_org_entity_id]"
				+ ");";		
		sql = replaceWithFieldValueAsSelect(sql, "workflowtransition_id", getSqlForSelectTransitionIdByWorkflowAndTransitionName(workflowName, transition.getName()));
		sql = replaceWithFieldValueAsSelect(sql, "extraviewers_org_entity_id", getSqlForSelectOrganizationEntity(entity));
		return sql;
	}
	
	private String generateSQLForWorkflowAndDocumentTypesRelation(String worflowName, String documentTypeName) {
		String sql = ""
				+ "INSERT INTO workflow_doc_type ("
				+ "workflow_id, document_type_id"
				+ ") VALUES ("
				+ "[workflow_id], [document_type_id]"
				+ ");";
		sql = replaceWithFieldValueAsSelect(sql, "workflow_id", getSqlForSelectWorkfowIdByName(worflowName));
		sql = replaceWithFieldValueAsSelect(sql, "document_type_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
		return sql;		
	}
	
	private String generateSQLForWorkflowAndSupervisorRelation(String worflowName, OrganizationEntity entity) {
		String sql = ""
				+ "INSERT INTO workflow_oe ("
				+ "workflow_id, supervisors_org_entity_id"
				+ ") VALUES ("
				+ "[workflow_id], [supervisors_org_entity_id]"
				+ ");";	
		sql = replaceWithFieldValueAsSelect(sql, "workflow_id", getSqlForSelectWorkfowIdByName(worflowName));
		sql = replaceWithFieldValueAsSelect(sql, "supervisors_org_entity_id", getSqlForSelectOrganizationEntity(entity));
		return sql;		
	}
	
	protected String getSqlForSelectStateIdByStateCodeAndWorkflowName(String stateCode, String workflowName) {
		return "(select id from workflowstate where code='" + stateCode + "' and " + COL_NAME_TEMP_WORKFLOW_ID_TEMP + "="  + this.getSqlForSelectWorkfowIdByName(workflowName) + ")";
	}
	
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	
	public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
	
	public void setOrganizationUnitPersistencePlugin(
			OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
}
