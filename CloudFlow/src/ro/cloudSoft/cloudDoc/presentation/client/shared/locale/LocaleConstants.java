package ro.cloudSoft.cloudDoc.presentation.client.shared.locale;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("ro")
public interface LocaleConstants extends ConstantsWithLookup {

	String YES();
	String NO();
  String DOCUMENT_NAME();
  String SUCCESS();
  String ERROR();
  String TOOLS();
  String ADD_WORKSPACE();
  String ADD_WORKSPACE_TOOLTIP();
  String DEL_WORKSPACE();
  String DEL_WORKSPACE_TOOLTIP();
  String ADD_FOLDER();
  String ADD_FOLDER_TOOLTIP();
  String DEL_FOLDER();
  String DEL_FOLDER_TOOLTIP();
  String MOVE_FOLDER();
  String MOVE_FOLDER_TOOLTIP();
  String SELECT_FOLDER_LOCATION();
  String CHECK_OUT();
  String CHECK_OUT_TOOLTIP();
  String CHECK_IN();
  String CHECK_IN_TOOLTIP();
  String ADD_FILE();
  String ADD_FILE_TOOLTIP();
  String MOVE_FILE();
  String MOVE_FILE_TOOLTIP();
  String DEL_FILE();
  String DEL_FILE_TOOLTIP();
  String VIEW_FILE();
  String VIEW_ICON_TOOLTIP();
  String VIEW_DETAILS_TOOLTIP();
  String SELECT_FILE_LOCATION();
  String SELECT_FILE();
  String DOC_NAME();
  String DOC_AUTHOR();
  String DOC_CREATED();
  String DOC_MODIFIED();
  String DOC_LOCK();
  String DOC_LOCK_TOOLTIP_ON();
  String DOC_LOCK_TOOLTIP_OFF();
  String DOC_WORKFLOW();
  String DOC_WORKFLOW_TOOLTIP_ON();

  @DefaultStringValue("The document is not on the workflow")
  String DOC_WORKFLOW_TOOLTIP_OFF();

  @DefaultStringValue("Document type")
  String DOC_TYPE();

  @DefaultStringValue("Description")
  String DOC_DESCRIPTION();

  /* WIZARD STEPS */
    /* buttons */
  @DefaultStringValue("Previous")
  String WIZ_STEP_PREVIOUS();

  @DefaultStringValue("Next")
  String WIZ_STEP_NEXT();

  @DefaultStringValue("Finish")
  String WIZ_STEP_FINISH();

    /* wizard workspace */
  @DefaultStringValue("Workspace")
  String WIZ_WORKSPACE_NAME();

  @DefaultStringValue("Description")
  String WIZ_WORKSPACE_DESCRIPTION();

  /* PERMISSIONS COMPONENT */
  @DefaultStringValue("Add")
  String PERMISSION_ADD();

  @DefaultStringValue("Remove")
  String PERMISSION_DEL();

  @DefaultStringValue("Search")
  String PERMISSION_SEARCH();

  @DefaultStringValue("Select permisssions for: ")
  String PERMISSION_SELECT();

    /* wizard folder */
  @DefaultStringValue("Path")
  String WIZ_FOLDER_PATH();

  @DefaultStringValue("Folder name")
  String WIZ_FOLDER_NAME();

  @DefaultStringValue("Description")
  String WIZ_FOLDER_DESCRIPTION();

    /* task panel */
  @DefaultStringValue("Task Panel")
  String TASK_TITLE();

  @DefaultStringValue("Process")
  String TASK_PROCESS();

  @DefaultStringValue("Document")
  String TASK_DOCUMENT();

  @DefaultStringValue("Actions")
  String TASK_ACTIONS();

  @DefaultStringValue("Author")
  String TASK_AUTHOR();

  @DefaultStringValue("Created")
  String TASK_CREATED();

  @DefaultStringValue("Modified")
  String TASK_MODIFIED();

  @DefaultStringValue("Type")
  String TASK_TYPE();

  @DefaultStringValue("Description")
  String TASK_DESCRIPTION();

  @DefaultStringValue("Approve")
  String TASK_APPROVE();

  @DefaultStringValue("Reject")
  String TASK_REJECT();

  @DefaultStringValue("Get document")
  String TASK_GET();

  @DefaultStringValue("Search results for ")
  String SEARCH_RESULTS_TEXT();

  @DefaultStringValue("View: ")
  String SEARCH_RESULTS_VIEW();

  @DefaultStringValue("Task Panel")
  String DOCK_TODO_TOOLTIP();

  @DefaultStringValue("File Manager")
  String DOCK_MANAGER_TOOLTIP();
  
  /*file editor*/
  @DefaultStringValue("Name")
  String FILE_EDITOR_NAME();

  @DefaultStringValue("Version")
  String FILE_EDITOR_VERSION();

  @DefaultStringValue("Type")
  String FILE_EDITOR_TYPE();

  @DefaultStringValue("Document")
  String FILE_EDITOR_DOCUMENT();

  @DefaultStringValue("Plain")
  String FILE_EDITOR_PLAIN();
  
  @DefaultStringValue("Attachment")
  String FILE_EDITOR_ATTACHMENT();

  @DefaultStringValue("Description")
  String FILE_EDITOR_DESCRIPTION();

  @DefaultStringValue("Permissions")
  String FILE_EDITOR_PERMISSIONS();

  @DefaultStringValue("History")
  String FILE_EDITOR_HISTORY();

  @DefaultStringValue("Activity")
  String FILE_EDITOR_ACTIVITY();

  @DefaultStringValue("Editor")
  String FILE_EDITOR_EDITOR();

  @DefaultStringValue("Created")
  String FILE_EDITOR_CREATED();

  @DefaultStringValue("Finished")
  String FILE_EDITOR_FINISHED();

  @DefaultStringValue("Workflow")
  String FILE_EDITOR_WORKFLOW();

  @DefaultStringValue("Initiate workflow")
  String FILE_EDITOR_INITIATE_CHECK();

  @DefaultStringValue("Initiate")
  String FILE_EDITOR_INITIATE();

  @DefaultStringValue("Editor")
  String FILE_EDITOR_EDITOR_TAB();

  @DefaultStringValue("Metadata")
  String FILE_EDITOR_METADATA();

  @DefaultStringValue("Save")
  String FILE_EDITOR_SAVE();

  @DefaultStringValue("Exit")
  String FILE_EDITOR_EXIT();

  @DefaultStringValue("The file is read-only")
  String FILE_EDITOR_TOOLTIP_FILE_READ();

  @DefaultStringValue("The file can be modified")
  String FILE_EDITOR_TOOLTIP_FILE_WRITE();

  @DefaultStringValue("The file is not on the workflow")
  String FILE_EDITOR_TOOLTIP_WORKFLOW_FALSE();

  @DefaultStringValue("The file is on the workflow")
  String FILE_EDITOR_TOOLTIP_WORKFLOW_TRUE();

  @DefaultStringValue("Upload file")
  String FILE_EDITOR_UPLOAD();

  @DefaultStringValue("Printer friendly")
  String FILE_EDITOR_PRINT();  
  
  @DefaultStringValue("Manage organization")
  String ADMIN_ORGANIZATION();
  
  @DefaultStringValue("Manage content")
  String ADMIN_CONTENT();
  
  String ADMIN_MIME_TYPES();
  
  @DefaultStringValue("Manage workflows")
  String ADMIN_WORKFLOW();
  
  @DefaultStringValue("Manage groups")
  String ADMIN_GROUPS();
  
  @DefaultStringValue("Structure")
  String STRUCTURE();
  
  @DefaultStringValue("Details")
  String DETAILS();
  
  @DefaultStringValue("Add")
  String ADD();
  
  String ADD_ALL();
  
  String EDIT();
  
  @DefaultStringValue("Delete")
  String DELETE();
  
  String REMOVE();
  
  String REMOVE_ALL();
  
  @DefaultStringValue("Refresh")
  String REFRESH();
  
  String CLOSE();
  
  String SAVE_AND_CLOSE();
  
  String SEND();
  
  String PRINT();
  
  String EXPORT();
  
  String IMPORT();
  
  @DefaultStringValue("Organization unit")
  String ORG_UNIT();
  
  String ORG_UNITS();
  
  @DefaultStringValue("User")
  String USER();
  
  String USER_FROM_DIRECTORY();
  
  String DEACTIVATE_USER();
  String REACTIVATE_USER();
  
  @DefaultStringValue("Users")
  String USERS();
  
  @DefaultStringValue("Name")
  String NAME();
  
  @DefaultStringValue("Save")
  String SAVE();
  
  @DefaultStringValue("Cancel")
  String CANCEL();
  
  @DefaultStringValue("Description")
  String DESCRIPTION();
  
  @DefaultStringValue("Manager")
  String MANAGER();

  @DefaultStringValue("First name")
  String FIRST_NAME();

  @DefaultStringValue("Last name")
  String LAST_NAME();

  @DefaultStringValue("Password")
  String PASSWORD();

  @DefaultStringValue("Title")
  String TITLE();
  
  @DefaultStringValue("Employee ID")
  String EMPLOYEE_ID();

  @DefaultStringValue("Username")
  String USERNAME();

  @DefaultStringValue("E-mail")
  String EMAIL();
  
  @DefaultStringValue("Group")
  String GROUP();
  
  String GROUPS();
  
  String GROUP_MEMBERS();
  
  @DefaultStringValue("Move")
  String MOVE();
  
  @DefaultStringValue("Log time")
  String LOG_TIME();
  
  @DefaultStringValue("Message")
  String MESSAGE();
  
  String LEVEL();
  String MODULE();
  
  @DefaultStringValue("User id")
  String USERID();
  
  @DefaultStringValue("Filter")
  String FILTER();
  
  @DefaultStringValue("Start date")
  String START_DATE();
  
  @DefaultStringValue("End date")
  String END_DATE();
  
  String GENERAL();
  
  String ATTACHMENTS();
  
  String MANDATORY_ATTACHMENT();
  
  String MANDATORY_ATTACHMENT_IN_STEPS();
  
  String MANDATORY_ATTACHMENT_WHEN_METADATA_HAS_VALUE();
  
  String ALLOWED_ATTACHMENT_TYPES();
  
  String METADATA_COLLECTIONS();
  
  String LABEL();
  
  String MANDATORY();
  
  String MANDATORY_IN_STEPS();
  
  String EDIT_RESTRICTION();
  
  String RESTRICTED_IN_STEPS();
  
  String INDEXED();
  
  String ORDER_NUMBER();
  
  String TYPE();
  String DEFAULT_VALUE();
  String AUTONUMBER();
  
  String PREFIX();
  
  String NUMBER_LENGTH();
  
  String ONLY_USERS_FROM_A_GROUP();
  
  String LIST();
  
  String INITIATOR();
  
  String INITIATORS();
  
  String ANY_INITIATOR_ALLOWED ();
		  
  String SIGNIFICANT();
		  
  String MULTIPLE_SELECTION();
  
  String EXTENDABLE();
  
  String ADD_METADATA();
  
  String ADD_EDIT_METADATA();
  
  String SAVE_METADATA();
  
  String ALLOWED_VALUES();
  
  String VALUE();
  
  String METADATA();
  
  String METADATA_NAME();
  
  String METADATA_VALUE();
  
  String METADATA_TYPE_USER();
  
  String ORGANIZATION();
  
  String ATTACHMENT_NAME();
  
  String ATTACHMENT_EXTENSION();
  
  String MIME_TYPE_NAME();
  
  String MIME_TYPE_EXTENSION();
  
  String ATTACHMENT_ALLOWED();
  
  String ORGANIZATIONAL_STRUCTURE();
  
  String INITIATORS_SELECTION();
  
  String DOCUMENT_TYPES();
  
  String SUPERVISORS();
  
  String SUPERVISORS_SELECTION();

  String ADMINISTRATION();

  String CLIENT();

  String ARCHIVE();

  String HELP();

  String USER_GROUPS();

  String CONTENT();

  String DOCUMENT_EXTENSIONS();
  
  String MIME_TYPES();
  
  String MIME_TYPE();

  String WORKFLOWS();

  String REPLACEMENT_PROFILES();

  String ARCHIVING_PROFILES();
  
  String ARCHIVING_PROFILE();
  
  String AUDIT();

  String HISTORY_AND_ERRORS();
  
  String SEARCH_USER();
  
  String USERS_AVAILABLE();
  
  String LOGOUT();  

  String CREATE_DOCUMENT();
  
  String MY_ACTIVITIES();
  
  String WORKSPACE_PANEL();
  
  String SEARCHES_AND_REPORTS();
  
  String PERFORMANCE_MONITORING();
  
  String REPLACEMENT_PROFILE();
  
  String WORKSPACE();
  
  String SECURITY();
  
  
  String EDIT_WORKSPACE();
  
  String EDIT_WORKSPACE_TOOLTIP();

  String WORKFLOW();
  
  String ADD_VERSION_REVISION();
  String VIEW_GRAPH();
  String WORKFLOW_VERSION();
  
  String WORKFLOW_ASSOCIATED_DOCUMENT_TYPES();
  
  // workflow states
  String STATES();  
  String ADD_STATE();  
  String DELETE_STATE();  
  String CODE();  
  String STATE_CODE();  
  String STATE_NAME();  
  String STATE_TYPE();  
  String STATE_START_TYPE();  
  String STATE_INTERMEDIATE_TYPE();  
  String STATE_STOP_TYPE();  
  String ATTACHMENTS_MANAGEMENT_PERMISSIONS();  
  String ATTACHMENTS_MANAGEMENT_PERMISSIONS_ADD();  
  String ATTACHMENTS_MANAGEMENT_PERMISSIONS_DELELTE();  
  String SAVE_STATE();
  
  // workflow transitions 
  String TRANSITIONS();
  String ADD_TRANSITION();  
  String DELETE_TRANSITION();  
  String TRANSITION_NAME();  
  String INITIAL_STATE_CODE();  
  String INITIAL_STATE();  
  String FINAL_STATE_CODE();  
  String FINAL_STATE();  
  String CONDITIONAL_ROUTING();  
  String CONDITIONAL_ROUTING_YES();  
  String CONDITIONAL_ROUTING_NO();  
  String ROUTING_CONDITION();  
  String ADDITIONAL_VIEWING_RIGHTS();  
  String ROUTING_TYPE();  
  String ROUTING_HIERARCHICAL_SUP();  
  String ROUTING_HIERARCHICAL_INF();  
  String ROUTING_HIERARCHICAL_SUB();  
  String ROUTING_INITIATOR();  
  String ROUTING_GROUP();  
  String ROUTING_PREVIOUS();  
  String ROUTING_MANUAL();  
  String ROUTING_PARAMETER();  
  String ROUTING_OU();  
  String DESTINATION_GROUP();  
  String DESTINATION_OU();  
  String DESTINATION_PARAMETER();  
  String NOTIFICATION();
  String NOTIFICATIONS();
  String ASSIGNED_ENTITY();
  String MANUALLY_CHOSEN_ENTITIES();
  String HIERARCHICAL_SUPERIOR_OF_INITIATOR();
  String NOTIFY_DESTINATION_BY_EMAIL(); 
  String NOTIFY_INITIATOR_BY_EMAIL();
  String DESTINATION_EMAIL();
  String RECIPIENTS_EMAIL();
  String INITIATOR_EMAIL();
  String EMAIL_SUBJECT();  
  String EMAIL_BODY();  
  String NOTIFY_RECIPIENTS_BY_EMAIL();  
  String NOTIFICATION_RECIPIENTS();
  String AVAILABLE_FOR_AUTOMATIC_ACTIONS_ONLY();
  String DEADLINE_AUTOMATIC_ACTION();  
  String ACTION();  
  String DEADLINE_ACTION_NOTIFY();  
  String DEADLINE_ACTION_ROUTE();  
  String DEADLINE_PERIOD();  
  String DAYS();  
  String NOTIFICATION_RESEND_INTERVAL();  
  String SAVE_TRANSITION();  
  String FOLDER();  
  String PATH();  
  String ALL();  
  String EDIT_FOLDER();  
  String EDIT_FOLDER_TOOLTIP();  
  String ALLOWED_DOCUMENT_TYPE();  
  String ENTITIES_WITH_RIGHTS_SELECTION(); 
  String ENTITY();
  String ENTITY_TYPE();
  String ENTITY_IDENTIFIER();
  String ENTITY_DISPLAY_NAME();
  String RIGHTS_LIST();  
  String COORDINATOR();  
  String COLLABORATOR();  
  String EDITOR();  
  String READER();
  String RIGHTS();  
  String TEXT();
  String NUMERIC();
  String AUTO_NUMBER();
  String DATE();
  String EXTENDED_TEXT();
  String METADATA_COLLECTION();
  String DOCUMENT();
  String VERSIONS();
  String VERSION_NR();
  String VERSION_AUTHOR();
  String VERSION_DATE();
  String WORKFLOW_GRAPH();
  
  String HISTORY();
  String HISTORY_AUTHOR();
  String HISTORY_DEPARTMENT();
  String HISTORY_ACTION();
  String HISTORY_DATE();
  
  
  String DOWNLOAD();
  String EXISTING_ATTACHMENTS();
  String NEW_ATTACHMENTS();
  String VALIDATION();
  
  // Searches And Reports
  String SEARCH();
  String REPORT();
  String SIMPLE_SEARCH();
  String DOCUMENT_CREATION_DATE();
  String DOCUMENT_CREATION_DATE_FROM();
  String DOCUMENT_CREATION_DATE_TO();
  String DOCUMENT_STATES();
  String ADVANCED_SEARCH();
  String SEARCH_IN_VERSIONS();
  String INDEXED_METADATAS();
  String SEARCH_RESULTS();
  String SIMPLE_SEARCH_RESULT();
  String ADVANCED_SEARCH_RESULT();
  String WORKFLOW_SENDER();
  String WORKFLOW_CURRENT_STATUS();
  String DOCUMENT_TYPE_NAME();
  String ANOTHER_VALUE();
  String KEEP_ALL_VERSIONS();
  String CHOOSE_TRANSITION();
  String CHOOSE_TEMPLATE();
  String TRANSITION();
  String ADMIN();
  String EMPTY();
  String SELECT();
  String CUSTOM_VALUE();
  String AUTO_GENERATED();
  String REQUIRED_METADATAS_TOOLTIP();
  String ADD_ATTACHMENT();
  String ALLOWED_TYPES();
  String OPERATION();
  String ACTOR_TYPE();
  String ACTOR_NAME();
  String EXCEPTION();
  String RESET();
  String DATE_TIME();
  String LOG_ENTRY();
  String TEMPLATES_FOR_EXPORT();
  String SELECT_TEMPLATE();
  
  String ANULARE_CERERE_DE_CONCEDIU();
  
  String REPLACEMENT_PROFILE_STATUS_INACTIVE();
  String REPLACEMENT_PROFILE_STATUS_ACTIVE();
  String REPLACEMENT_PROFILE_STATUS_RETURNED();
  String REPLACEMENT_PROFILE_STATUS_EXPIRED();

  String REQUESTER();
  String REPLACEMENT();
  String STATUS();
  String PROFILES();
  String COMMENTS();
  String OUT_OF_OFFICE();
  String OUT_OF_OFFICE_EMAIL_SETTINGS();
  String RETURNED();
  
  String EXPAND_ALL();
  
  String IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY();
  String DEACTIVATE_ALL_ACCOUNTS_OF_USER();
  String AUTO_COMPLETE_WITH_CURRENT_USER();
  String AUTO_COMPLETE_WITH_CURRENT_USER_STEP();
  
  String AUDIT_OPERATION_READ();
  String AUDIT_OPERATION_ADD();
  String AUDIT_OPERATION_MODIFY();
  String AUDIT_OPERATION_CREATE_VERSION();
  String AUDIT_OPERATION_MOVE();
  String AUDIT_OPERATION_DELETE();
  String AUDIT_OPERATION_CHECK_IN_NEW();
  String AUDIT_OPERATION_CHECK_IN_EXISTING();
  String AUDIT_OPERATION_CHECK_OUT();
  String AUDIT_OPERATION_SAVE_NEW();
  String AUDIT_OPERATION_SAVE_EXISTING();
  String AUDIT_OPERATION_UNDO_CHECK_OUT();
  String AUDIT_OPERATION_SEND();
  String AUDIT_OPERATION_RETURN();
  
  String MANAGEMENT();
  String PREVIEW();
}