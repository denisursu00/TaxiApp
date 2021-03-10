export class ApiPathConstants {

	// DocumentSearchGxtService
	public static readonly GET_MY_ACTIVITIES = "/DocumentSearchGxtService/getMyActivities";
	public static readonly FIND_DOCUMENTS_USING_SIMPLE_SEARCH = "/DocumentSearchGxtService/findDocumentsUsingSimpleSearch";
	public static readonly FIND_DOCUMENTS_USING_ADVANCED_SEARCH = "/DocumentSearchGxtService/findDocumentsUsingAdvancedSearch";
	public static readonly FIND_DOCUMENTS_FOR_SELECTION = "/DocumentSearchGxtService/findDocumentsForSelection";

	// DocumentLocationGxtService
	public static readonly GET_ALL_DOCUMENT_LOCATIONS = "/DocumentLocationGxtService/getAllDocumentLocations";
	public static readonly GET_ALL_DOCUMENT_LOCATIONS_AS_MODELDATA = "/DocumentLocationGxtService/getAllDocumentLocationsAsModelData";
	public static readonly GET_DOCUMENT_LOCATION = "/DocumentLocationGxtService/getDocumentLocation";
	public static readonly GET_FOLDERS_FROM_DOCUMENT_LOCATION = "/DocumentLocationGxtService/getFoldersFromDocumentLocation";
	public static readonly SAVE_DOCUMENT_LOCATION = "/DocumentLocationGxtService/saveDocumentLocation";
	public static readonly DELETE_DOCUMENT_LOCATION = "/DocumentLocationGxtService/deleteDocumentLocation";
	public static readonly GET_DOCUMENT_LOCATION_BY_REALNAME = "/DocumentLocationGxtService/getDocumentLocationByRealName";
	
	// FolderGxtService
	public static readonly GET_FOLDERS_FROM_FOLDER = "/FolderGxtService/getFoldersFromFolder";
	public static readonly GET_FOLDER_BY_ID = "/FolderGxtService/getFolderById";
	public static readonly SAVE_FOLDER = "/FolderGxtService/saveFolder";
	public static readonly DELETE_FOLDER = "/FolderGxtService/deleteFolder";
	public static readonly MOVE_FOLDER = "/FolderGxtService/moveFolder";
	public static readonly GET_IDS_FOR_FOLDER_HIERARCHY = "/FolderGxtService/getIdsForFolderHierarchy";
	public static readonly GET_FOLDER_PATH = "/FolderGxtService/getFolderPath";
	
	// OrganizationGxtService
	public static readonly GET_ORGANIZATION_TREE = "/OrganizationGxtService/getOrganizationTree";
	public static readonly GET_GROUPS = "/OrganizationGxtService/getGroups";
	public static readonly GET_SORTED_USERS = "/OrganizationGxtService/getSortedUsers";
	public static readonly GET_ALL_USERNAMES = "/OrganizationGxtService/getAllUsernames";
	public static readonly GET_USERS_WITH_USERNAME = "/OrganizationGxtService/getUsersWithUsername";
	public static readonly GET_USERS = "/OrganizationGxtService/getUsers";
	public static readonly GET_ACTIVE_USERS = "/OrganizationGxtService/getAllActiveUsers";
	public static readonly GET_USERS_FROM_GROUP = "/OrganizationGxtService/getUsersFromGroup";
	public static readonly GET_USERS_FROM_GROUP_BY_GROUP_NAME = "/OrganizationGxtService/getUsersFromGroupByGroupName";
	public static readonly GET_USER_BY_ID = "/OrganizationGxtService/getUserById";
	public static readonly SET_USER = "/OrganizationGxtService/setUser";
	public static readonly GET_ORG_UNIT_BY_ID = "/OrganizationGxtService/getOrgUnitById";
	public static readonly SET_ORG_UNIT = "/OrganizationGxtService/setOrgUnit";
	public static readonly GET_USERS_FROM_ORG_UNIT = "/OrganizationGxtService/getUsersFromOrgUnit";
	public static readonly FIND_USERS_IN_DIRECTORY = "/OrganizationGxtService/findUsersInDirectory";
	public static readonly IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY = "/OrganizationGxtService/importOrganizationalStructureFromDirectory";
	public static readonly DELETE_ORGANIZATION_UNIT = "/OrganizationGxtService/deleteOrgUnit";
	public static readonly DELETE_USER = "/OrganizationGxtService/deleteUser";
	public static readonly DEACTIVATE_USER = "/OrganizationGxtService/deactivateUser";
	public static readonly REACTIVATE_USER_WITH_ID = "/OrganizationGxtService/reactivateUserWithId";
	public static readonly MOVE_USER_TO_ORGANIZATION = "/OrganizationGxtService/moveUserToOrganization";
	public static readonly MOVE_USER_TO_ORGANIZATION_UNIT = "/OrganizationGxtService/moveUserToOrganizationUnit";
	public static readonly MOVE_ORGANIZATION_UNIT_TO_ORGANIZATION = "/OrganizationGxtService/moveOrganizationUnitToOrganization";
	public static readonly MOVE_ORGANIZATION_UNIT_TO_ORGANIZATION_UNIT = "/OrganizationGxtService/moveOrganizationUnitToOrganizationUnit";
	public static readonly GET_NAMES_FOR_ORGANIZATION_ENTITIES = "/OrganizationGxtService/getNamesForOrganizationEntities";
	public static readonly DETELE_GROUP = "/OrganizationGxtService/deleteGroup";
	public static readonly GET_GROUP_BY_ID = "/OrganizationGxtService/getGroupById";
	public static readonly SET_GROUP = "/OrganizationGxtService/setGroup";
	public static readonly GET_AVAILABLE_ROLES_FOR_USER = "/OrganizationGxtService/getAvailableRolesForUser";
	public static readonly GET_ALL_ROLES = "/OrganizationGxtService/getAllRoles";
	public static readonly GET_ALL_ROLE_PERMISSION_MAPPING_VIEWS = "/OrganizationGxtService/getAllRolePermissionMappingViews";
	public static readonly GET_ALL_USERS_WITH_ASSIGNED_TASKS = "/OrganizationGxtService/getAllUsersWithAssignedTasks";

	// DocumentGxtService
	public static readonly GET_PAGED_DOCUMENTS_FROM_FOLDER = "/DocumentGxtService/getPagedDocumentsFromFolder";
	public static readonly GET_PAGED_DOCUMENTS = "/DocumentGxtService/getPagedDocuments";
	public static readonly GET_DOCUMENT_VERSIONS = "/DocumentGxtService/getDocumentVersions";
	public static readonly DELETE_DOCUMENT = "/DocumentGxtService/deleteDocument";
	public static readonly GET_DOCUMENT_ADD_BUNDLE = "/DocumentGxtService/getDocumentAddBundle";
	public static readonly SAVE_DOCUMENT = "/DocumentGxtService/save";
	public static readonly GET_DOCUMENT_VIEW_OR_EDIT_BUNDLE = "/DocumentGxtService/getDocumentViewOrEditBundle";
	public static readonly GET_DOCUMENT_VIEW_VERSION_BUNDLE = "/DocumentGxtService/getDocumentViewVersionBundle";
	public static readonly CHECKIN_DOCUMENT = "/DocumentGxtService/checkin";
	public static readonly UNDO_CHECKOUT_DOCUMENT = "/DocumentGxtService/undoCheckout";
	public static readonly CHECKOUT_DOCUMENT = "/DocumentGxtService/checkout";
	public static readonly SEND_DOCUMENT_TO_WORKFLOW = "/DocumentGxtService/sendDocumentToWorkflow";
	public static readonly EXIST_DOCUMENTS_OF_TYPE = "/DocumentGxtService/existDocumentsOfType";
	public static readonly VALIDATE_DOCUMENT = "/DocumentGxtService/validateDocument";
	public static readonly VALIDATE_DOCUMENT_COLLECTION = "/DocumentGxtService/validateDocumentCollection";
	public static readonly GET_DOCUMENT_NAME = "/DocumentGxtService/getDocumentName";
	public static readonly AUTOCOMPLETE_METADATA = "/DocumentGxtService/autocompleteMetadata";
	public static readonly EXPORT_DOCUMENT = "/DocumentGxtService/exportDocument";
	public static readonly DOWNLOAD_DOCUMENT_ATTACHMENT = "/DocumentGxtService/downloadDocumentAttachment";
	public static readonly GET_ADMIN_UPDATE_DOCUMENT_BUNDLE = "/DocumentGxtService/getAdminUpdateDocumentBundle";
	public static readonly ADMIN_UPDATE_DOCUMENT = "/DocumentGxtService/adminUpdateDocument";

	// DocumentTypeGxtService
	public static readonly GET_ALL_DOCUMENT_TYPES_FOR_DISPLAY = "/DocumentTypeGxtService/getAllDocumentTypesForDisplay";
	public static readonly GET_DOCUMENT_TYPE_BY_ID = "/DocumentTypeGxtService/getDocumentTypeById";
	public static readonly PREPARE_FOR_ADD_OR_EDIT = "/DocumentTypeGxtService/prepareForAddOrEdit";
	public static readonly SAVE_DOCUMENT_TYPE = "/DocumentTypeGxtService/saveDocumentType";
	public static readonly DELETE_DOCUMENT_TYPE = "/DocumentTypeGxtService/deleteDocumentType";
	public static readonly GET_USER_METADATA_DEFINITIONS = "/DocumentTypeGxtService/getUserMetadataDefinitions";
	public static readonly GET_AVAILABLE_DOCUMENT_TYPES = "/DocumentTypeGxtService/getAvailableDocumentTypes";
	public static readonly GET_DOCUMENT_CREATIONIN_DEFAULTLOCATION_VIEWS = "/DocumentTypeGxtService/getDocumentCreationInDefaultLocationViews";
	public static readonly GET_AVAILABLE_DOCUMENT_TYPES_FOR_SEARCH = "/DocumentTypeGxtService/getAvailableDocumentTypesForSearch";
	public static readonly GET_DOCUMENT_TYPES_WITH_NO_WORKFLOW = "/DocumentTypeGxtService/getDocumentTypesWithNoWorkflow";
	public static readonly EXIST_DOCUMENT_TYPE_WITH_NAME = "/DocumentTypeGxtService/existDocumentTypeWithName";
	public static readonly GET_DOCUMENT_TYPE_BY_NAME = "/DocumentTypeGxtService/getDocumentTypeIdByName";

	// AclGxtService
	public static readonly GET_SECURITY_MANAGER = "/AclGxtService/getSecurityManager";

	// AttachmentGxtService
	public static readonly UPLOAD_ATTACHMENT = "/AttachmentGxtService/upload";
	public static readonly DOWNLOAD_ATTACHMENT = "/AttachmentGxtService/download";
	public static readonly DELETE_ATTACHMENT = "/AttachmentGxtService/delete";

	// TemplateGxtService
	public static readonly UPLOAD_DOCUMENT_TEMPLATE = "/DocumentTemplateGxtService/uploadDocumentTemplate";
	public static readonly DOWNLOAD_DOCUMENT_TEMPLATE = "/DocumentTemplateGxtService/downloadDocumentTemplate";
	
	// DocumentWorkflowHistoryGxtService
	public static readonly GET_DOCUMENT_HISTORY = "/DocumentWorkflowHistoryGxtService/getDocumentHistory";

	// WorkflowGxtServiceResource
	public static readonly GET_STATES_BY_DOCUMENT_TYPE_IDS = "/WorkflowGxtServiceResource/getStatesByDocumentTypeIds";
	public static readonly GET_ALL_WORKFLOWS = "/WorkflowGxtServiceResource/getAllWorkflows";
	public static readonly GET_WORKFLOW_STATES_BY_DOCUMENT_TYPE = "/WorkflowGxtServiceResource/getWorkflowStatesByDocumentType";
	public static readonly GET_WORKFLOW_BY_ID = "/WorkflowGxtServiceResource/getWorkflowById";
	public static readonly SAVE_WORKFLOW = "/WorkflowGxtServiceResource/saveWorkflow";
	public static readonly HAS_INSTANCES = "/WorkflowGxtServiceResource/hasInstances";
	public static readonly CREATE_NEW_VERSION = "/WorkflowGxtServiceResource/createNewVersion";
	public static readonly DELETE_WORKFLOW = "/WorkflowGxtServiceResource/deleteWorkflow";
	
	// ReplacementProfilesGxtService
	public static readonly GET_VISIBLE_REPLACEMENT_PROFILES = "/ReplacementProfilesGxtService/getVisibleReplacementProfiles";
	public static readonly DELETE_REPLACEMENT_PROFILE_BY_ID = "/ReplacementProfilesGxtService/deleteReplacementProfileById";
	public static readonly GET_REPLACEMENT_PROFILE_BY_ID = "/ReplacementProfilesGxtService/getReplacementProfileById";
	public static readonly SAVE_REPLACEMENT_PROFILE = "/ReplacementProfilesGxtService/saveReplacementProfile";
	public static readonly RETURNED = "/ReplacementProfilesGxtService/returned";
	public static readonly GET_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT = "/ReplacementProfilesGxtService/getAvailableReplacementProfilesInWhichRequesterIsReplacement";
	
	// AppConstantsService
	public static readonly GET_GUI_CONSTANTS = "/AppConstantsService/getGuiConstants";
	public static readonly GET_BUSINESS_CONSTANTS = "/AppConstantsService/getBusinessConstants";
	public static readonly GET_CERERI_DE_CONCEDIU_CONSTANTS = "/AppConstantsService/getCereriDeConcediuConstants";
	public static readonly GET_WEB_CONSTANTS = "/AppConstantsService/getWebConstants";
	public static readonly GET_APP_COMPONENTS_AVAILABILITY_CONSTANTS = "/AppConstantsService/getAppComponentsAvailabilityConstants";
	public static readonly GET_REPLACEMENT_PROFILES_OUT_OF_OFFICE_CONSTANTS = "/AppConstantsService/getReplacementProfilesOutOfOfficeConstants";
	public static readonly GET_SUPPORTED_ATTACHMENT_TYPES_FOR_PREVIEW_CONSTANTS = "/AppConstantsService/getSupportedAttachmentTypesForPreviewConstants";
	public static readonly GET_APPLICATION_INFO = "/AppConstantsService/getApplicationInfo";

	// MimeTypeGxtService
	public static readonly GET_ALL_MIME_TYPES = "/MimeTypeGxtService/getAllMimeTypes";
	public static readonly GET_MIME_TYPE_BY_ID = "/MimeTypeGxtService/getMimeTypeById";
	public static readonly SAVE_MIME_TYPE = "/MimeTypeGxtService/saveMimeType";
	public static readonly DELETE_MIME_TYPE = "/MimeTypeGxtService/deleteMimeType";

	// Nomenclator
	public static readonly GET_ALL_NOMENCLATORS = "/Nomenclator/getAllNomenclators";
	public static readonly GET_VISIBLE_NOMENCLATORS = "/Nomenclator/getVisibleNomenclators";
	public static readonly GET_AVAILABLE_NOMENCLATORS_FOR_PROCESSING_VALUES_FROM_UI= "/Nomenclator/getAvailableNomenclatorsForProcessingValuesFromUI";
	public static readonly GET_AVAILABLE_NOMENCLATORS_FOR_PROCESSING_STRUCTURE_FROM_UI= "/Nomenclator/getAvailableNomenclatorsForProcessingStructureFromUI";
	public static readonly GET_NOMENCLATOR = "/Nomenclator/getNomenclator";
	public static readonly GET_NOMENCLATOR_BY_CODE = "/Nomenclator/getNomenclatorByCode";
	public static readonly GET_NOMENCLATOR_ATTRIBUTES_BY_NOMENCLATOR_ID = "/Nomenclator/getNomenclatorAttributesByNomenclatorId";
	public static readonly GET_NOMENCLATOR_ATTRIBUTES_BY_NOMENCLATOR_CODE = "/Nomenclator/getNomenclatorAttributesByNomenclatorCode";
	public static readonly GET_UI_ATTRIBUTE_VALUES = "/Nomenclator/getUiAttributeValues";
	public static readonly SEARCH_NOMENCLATOR_VALUES_AS_VIEW = "/Nomenclator/searchNomenclatorValuesAsView";
	public static readonly GET_LIST_OF_CONCATENATED_UI_ATTRIBUTES_BY_NOMENCLATOR_ID = "/Nomenclator/getListOfConcatenatedUiAttributesByNomenclatorId";
	public static readonly GET_LIST_OF_CONCATENATED_UI_ATTRIBUTES_BY_NOMENCLATOR_CODE = "/Nomenclator/getListOfConcatenatedUiAttributesByNomenclatorCode";
	public static readonly SAVE_NOMENCLATOR = "/Nomenclator/saveNomenclator";
	public static readonly NOMENCLATOR_HAS_VALUE = "/Nomenclator/nomenclatorHasValue";
	public static readonly NOMENCLATOR_HAS_VALUE_BY_NOMENCLATOR_CODE = "/Nomenclator/getNomenclatorAttributesByNomenclatorCode";
	public static readonly DELETE_NOMENCLATOR = "/Nomenclator/deleteNomenclator";
	public static readonly GET_NOMENCLATOR_VALUE = "/Nomenclator/getNomenclatorValue";
	public static readonly SAVE_NOMENCLATOR_VALUE = "/Nomenclator/saveNomenclatorValue";
	public static readonly DELETE_NOMENCLATOR_VALUE = "/Nomenclator/deleteNomenclatorValue";
	public static readonly GET_NOMENCLATOR_ID_BY_CODE_AS_MAP = "/Nomenclator/getNomenclatorIdByCodeAsMap";
	public static readonly GET_NOMENCLATOR_VALUES_BY_NOMENCLATOR_ID = "/Nomenclator/getNomenclatorValuesByNomenclatorId";
	public static readonly GET_NOMENCLATOR_VALUES_BY_NOMENCLATOR_CODE = "/Nomenclator/getNomenclatorValuesByNomenclatorCode";
	public static readonly GET_NOMENCLATOR_VALUES_BY_REQUEST_MODEL = "/Nomenclator/getNomenclatorValues";
	public static readonly GET_YEARS_FROM_NOMENCLATOR_VALUES_BY_NOMENCLATOR_CODE_AND_ATTRIBUTE_KEY = "/Nomenclator/getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey";
	public static readonly GET_CUSTOM_NOMENCLATOR_SELECTION_FILTERS = "/Nomenclator/getCustomNomenclatorSelectionFilters";
	public static readonly NOMENCLATOR_RUN_EXPRESSIONS = "/Nomenclator/runExpressions";
	public static readonly GET_NOMENCLATOR_UI_VALUES_AS_FILTER_FOR_NOMENCLATOR_ATTRIBUTE_THAT_USE_IT = "/Nomenclator/getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt";
	public static readonly EXISTS_PERSON_AND_INSTITUTION_IN_NOM_PERSOANE = "/Nomenclator/existsPersonAndInstitutionInNomPersoane";

	// Calendar
	public static readonly GET_ALL_CALENDARS = "/Calendar/getAllCalendars";
	public static readonly GET_CALENDAR = "/Calendar/getCalendar";
	public static readonly SAVE_CALENDAR = "/Calendar/saveCalendar";
	public static readonly DELETE_CALENDAR = "/Calendar/deleteCalendar";
	public static readonly GET_ALL_CALENDARS_FOR_USER = "/Calendar/getAllCalendarsForUser";
	public static readonly GET_CALENDARS_EVENTS = "/Calendar/getCalendarsEvents";
	public static readonly SAVE_EVENT = "/Calendar/saveEvent";
	public static readonly GET_CALENDAR_EVENT = "/Calendar/getCalendarEvent";
	public static readonly DELETE_EVENT = "/Calendar/deleteEvent";
	
	// PROJECT
	public static readonly GET_ALL_PROJECTS = "/Project/getAllProjects";
	public static readonly GET_USER_TASKS = "/Project/getUserTasks";
	public static readonly GET_TASK = "/Project/getTask";
	public static readonly SAVE_TASK = "/Project/saveTask";
	public static readonly COMPLETE_TASK = "/Project/completeTask";
	public static readonly CANCEL_TASK = "/Project/cancelTask";
	public static readonly GET_USER_IN_PROGRESS_TASKS_MODELS = "/Project/getUserInProgressTasksModels";
	public static readonly GET_TASK_VIEW_MODEL = "/Project/getTaskViewModel";
	public static readonly GET_USER_PROJECTS = "/Project/getUserProjects";
	public static readonly SAVE_PROJECT = "/Project/saveProject";
	public static readonly GET_PROJECT_PARTICIPANTS = "/Project/getProjectParticipants";
	public static readonly GET_PROJECT_BY_ID = "/Project/getProjectById";
	public static readonly CLOSE_PROJECT = "/Project/closeProject";
	public static readonly GET_PAGED_PROJECT_TASK_VIEW_MODELS = "/Project/getPagedProjectTaskViewModels";
	public static readonly GET_PROJECT_WITH_DSP_VIEW_MODELS = "/Project/getProjectWithDspViewModels";
	public static readonly GET_GRAD_DE_REALIZARE_PENTRU_PROIECTELE_CU_DSPMODEL = "/Project/getGradDeRealizarePentruProiecteleCuDspModel";
	public static readonly GET_ALL_OPENED_PROJECTS_WITH_DSP = "/Project/getAllOpenedProjectsWithDsp";
	public static readonly GET_DSP_VIEW_MODEL = "/Project/getDspViewModelByProjectId";
	public static readonly GET_TASKS_VIEW_MODEL = "/Project/getTasksViewModelByProjectId";
	public static readonly DOWNLOAD_TASK_ATTACHMENT = "/Project/downloadTaskAttachment";
	public static readonly GET_PROJECT_VIEW_MODEL_BY_ID = "/Project/getProjectViewModelById";
	public static readonly EXPORT_DSP = "/Project/exportDsp";
	public static readonly GET_ALL_IN_PROGRESS_TASK_NAMES_BY_PROJECT_ABREVIATION = "/Project/getAllInProgressTaskNamesByProjectAbbreviation";
	public static readonly EXISTS_PROJECT_BY_ABBREVIATION = "/Project/existsAbbreviation";
	public static readonly EXISTS_PROJECT_BY_NAME = "/Project/existsName";
	public static readonly GET_ALL_TASK_EVENTS = "/Project/getAllTaskEvents";
	public static readonly UPDATE_TASK_EVENT_DESCRIPTION = "/Project/updateTaskEventDescription";
	public static readonly GET_ALL_PROJECT_SUBACTIVITIES = "/Project/getAllProjectSubactivities";
	public static readonly IS_SUBACTIVITY_USED_IN_ANY_TASK = "/Project/isSubactivityUsedInAnyTask";

	// RegistruDocumenteJustificativePlatiService
	public static readonly GET_ALL_DOCUMENTE_JUSTIFICATIVE_PLATI = "/RegistruDocumenteJustificativePlati/getAllDocumenteJustificativePlati";
	public static readonly GET_DOCUMENT_JUSTIFICATIV_PLATI = "/RegistruDocumenteJustificativePlati/getDocumentJustificativPlati";
	public static readonly SAVE_DOCUMENT_JUSTIFICATIV_PLATI = "/RegistruDocumenteJustificativePlati/saveDocumentJustificativPlati";
	public static readonly CANCEL_DOCUMENT_JUSTIFICATIV_PLATI = "/RegistruDocumenteJustificativePlati/cancelDocumentJustificativPlati";
	public static readonly GET_ALL_BY_YEAR = "/RegistruDocumenteJustificativePlati/getAllByYear";
	public static readonly GET_YEARS_WITH_INREGISTRARI_DOCUMENTE_JUSTIFICATIVE_PLATI = "/RegistruDocumenteJustificativePlati/getYearsWithInregistrariDocumenteJustificativePlati";
	public static readonly DOWNLOAD_ATTACHMENT_OF_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_BY_ID = "/RegistruDocumenteJustificativePlati/downloadAtasamentById";
	
	// ComisieSauGL
	public static readonly GET_REPREZENTANTI_EDIT_BUNDLE_BY_COMISIE_SAU_GL_ID = "/ComisieSauGL/getReprezentantiEditBundleByComisieSauGLId";
	public static readonly SAVE_REPREZENTANTI_COMISIE_SAU_GL = "/ComisieSauGL/saveReprezentanti";
	public static readonly GET_ALL_INSTITUTII_OF_MEMBRII_COMISIE_SAU_GL = "/ComisieSauGL/getAllInstitutiiOfMembriiComisieSauGL";
	public static readonly GET_MEMBRII_REPREZENTANTI_COMISIE_GL_BY_INSTITUTIE = "/ComisieSauGL/getMembriiReprezentantiComisieSauGLByInstitutie";

	// RegistruIntrariIesiriService
	public static readonly GET_ALL_INTRARI = "/RegistruIntrariIesiri/getAllIntrari";
	public static readonly GET_ALL_REGISTRU_INTRARI_VIEW_MODELS = "/RegistruIntrariIesiri/getAllRegistruIntrariViewModels";
	public static readonly GET_YEARS_OF_EXISTING_INTRARI = "/RegistruIntrariIesiri/getYearsOfExistingIntrari";
	public static readonly GET_YEARS_OF_EXISTING_IESIRI = "/RegistruIntrariIesiri/getYearsOfExistingIesiri";
	public static readonly GET_ALL_REGISTRU_INTRARI_VIEW_MODELS_BY_YEAR = "/RegistruIntrariIesiri/getAllRegistruIntrariViewModelsByYear";
	public static readonly GET_REGISTRU_IESIRI_VIEW_MODELS_BY_FILTER = "/RegistruIntrariIesiri/getRegistruIesiriViewModelsByFilter";
	public static readonly GET_REGISTRU_INTRARI_BY_FILTER = "/RegistruIntrariIesiri/getRegistruIntrariByFilter";
	public static readonly GET_NR_INREGISTRARE_MAPPED_REGISTRI_BY_REGISTRU_ID = "/RegistruIntrariIesiri/getNrInregistrareOfMappedRegistriByRegistruId";
	public static readonly SAVE_REGISTRU_INTRARI = "/RegistruIntrariIesiri/saveRegistruIntrari";
	public static readonly SAVE_REGISTRU_IESIRI = "/RegistruIntrariIesiri/saveRegistruIesiri";
	public static readonly GET_REGISTRU_IESIRI = "/RegistruIntrariIesiri/getRegistruIesiri";
	public static readonly GET_ALL_REGISTRU_IESIRI_VIEW_MODELS = "/RegistruIntrariIesiri/getAllRegistruIesiriViewModels";
	public static readonly GET_REGISTRU_INTRARI_BY_ID = "/RegistruIntrariIesiri/getRegistruIntrariById";
	public static readonly CANCEL_REGISTRU_INTRARI = "/RegistruIntrariIesiri/cancelRegistruIntrari";
	public static readonly CANCEL_REGISTRU_IESIRI = "/RegistruIntrariIesiri/cancelRegistruIesiri";
	public static readonly IS_REGISTRU_IESIRI_CANCELED = "/RegistruIntrariIesiri/isRegistruIesiriCanceled";
	public static readonly IS_REGISTRU_INTRARI_CANCELED = "/RegistruIntrariIesiri/isRegistruIntrariCanceled";
	public static readonly IS_REGISTRU_INTRARI_FINALIZED = "/RegistruIntrariIesiri/isRegistruIntrariFinalized";
	public static readonly FINALIZE_REGISTRU_INTRARI = "/RegistruIntrariIesiri/finalizareRegistruIntrari";
	public static readonly FINALIZE_REGISTRU_IESIRI = "/RegistruIntrariIesiri/finalizareRegistruIesiri";
	public static readonly DOWNLOAD_ATTACHMENT_OF_REGISTRU_INTRARI_BY_ID = "/RegistruIntrariIesiri/downloadAtasamentOfRegistruIntrariById";
	public static readonly DOWNLOAD_ATTACHMENT_OF_REGISTRU_IESIRI_BY_ID = "/RegistruIntrariIesiri/downloadAtasamentOfRegistruIesiriById";
	public static readonly GET_LAST_NR_INREGISTRARE_BY_TIP_AND_YEAR = "/RegistruIntrariIesiri/getLastNumarInregistrareByTipRegistruAndYear";
	public static readonly IS_SUBACTIVITY_USED_IN_ANY_REGISTER_ENTRY = "/RegistruIntrariIesiri/isSubactivityUsedInAnyRegisterEntry";

	// Parameters
	public static readonly GET_ALL_PARAMETERS = "/Parameters/getAllParameters";
	public static readonly SAVE_PARAMETER = "/Parameters/saveParameter";
	public static readonly GET_PARAMETER_BY_ID = "/Parameters/getParameterById";
	public static readonly DELETE_PARAMETER_BY_ID = "/Parameters/deleteParameterById";

	// AlteDeconturiService
	public static readonly GET_YEARS_OF_EXISTING_DECONTURI = "/AlteDeconturi/getYearsOfExistingDeconturi";
	public static readonly GET_ALL_ALTE_DECONTURI_VIEW_MODELS_BY_YEAR = "/AlteDeconturi/getAllAlteDeconturiViewModelsByYear";
	public static readonly GET_DECONT_BY_ID = "/AlteDeconturi/getDecontById";
	public static readonly IS_DECONT_CANCELED = "/AlteDeconturi/isDecontCanceled";
	public static readonly SAVE_ALTE_DECONTURI = "/AlteDeconturi/saveAlteDeconturi";
	public static readonly DELETE_DECONT_BY_ID = "/AlteDeconturi/deleteDecontById";
	public static readonly CANCEL_DECONT = "/AlteDeconturi/cancelDecont";
	public static readonly FINALIZE_DECONT_BY_ID = "/AlteDeconturi/finalizeDecontById";
	public static readonly IS_DECONT_FINALIZED = "/AlteDeconturi/isDecontFinalized";
	public static readonly DOWNLOAD_ATASAMENT_OF_CHELTUIALA = "/AlteDeconturi/downloadAtasamentOfCheltuiala";
	public static readonly GET_TITULARI_OF_EXISTING_DECONTURI = "/AlteDeconturi/getAllDistinctTitulari";

	// DeplasariDeconturiService
	public static readonly SAVE_DEPLASARE_DECONT = "/DeplasariDeconturi/saveDeplasareDecont";
	public static readonly GET_NUMAR_DECIZII_DEPLASARI_APROBATE_BY_REPREZENTANT = "/DeplasariDeconturi/getDeciziiAprobateNealocateForReprezentantForDeplasareDecont";
	public static readonly GET_DOCUMENT_DECIZIE_DEPLASARE = "/DeplasariDeconturi/getDocumentDecizieDeplasare";
	public static readonly GET_YEARS_OF_EXISTING_DEPLASARI_DECONTURI = "/DeplasariDeconturi/getYearsOfExistingDeplasariDeconturi";
	public static readonly GET_ALL_DEPLASARI_DECONTURI_VIEW_MODELS_BY_YEAR = "/DeplasariDeconturi/getAllDeplasariDeconturiViewModelsByYear";
	public static readonly IS_DEPLASARE_DECONT_CANCELED = "/DeplasariDeconturi/isDeplasareDecontCanceled";
	public static readonly GET_DEPLASATE_DECONT_BY_ID = "/DeplasariDeconturi/getDeplasareDecontById";
	public static readonly CANCEL_DEPLASARE_DECONT = "/DeplasariDeconturi/cancelDeplasareDecont";
	public static readonly REMOVE_DEPLASARE_DECONT = "/DeplasariDeconturi/removeDeplasareDecont";
	public static readonly FINALIZE_DEPLASARE_DECONT = "/DeplasariDeconturi/finalizeDeplasareDecont";
	public static readonly GET_TITULARI_OF_EXISTING_DEPLASARE_DECONT = "/DeplasariDeconturi/getAllDistinctTitulari";
	public static readonly GET_NUMAR_DECIZII_OF_EXISTING_DEPLASARE_DECONT = "/DeplasariDeconturi/getAllNumarDeciziiByFilter";

	// CursValutarService
	public static readonly GET_CURS_VALUTAR_CURENT = "/CursValutar/getCursValutarCurent";

	// ReportService
	public static readonly GET_ADERARE_OIORO_REPORT = "/Report/getAderareOioroReport";
	public static readonly GET_NUMAR_SEDINTE_SI_PARTICIPANTI_REPORT = "/Report/getNumarSedinteCdPvgSiParticipantiReport";
	public static readonly GET_DEPLASARI_DECONTURI_DECONT_CHELT_REPR_ARB = "/Report/getDeplasariDeconturiCheltuieliReprezentantArb";
	public static readonly GET_PREZENTA_SEDINTE_MEMBRII = "/Report/getPrezentaSedinteCdPvgMembriiReport";
	public static readonly GET_DOCUMENTE_TRIMISE_DE_ARB = "/Report/getDocumenteTrimiseDeArbReport";
	public static readonly GET_PREZENTA_SEDINTE_INVITATI_ARB_REPORT = "/Report/getPrezentaSedinteCdPvgInvitatiArbReport";
	public static readonly GET_PREZENTA_SEDINTE_INVITATI_EXTERNI_REPORT = "/Report/getPrezentaSedinteCdPvgInvitatiExterniReport";
	public static readonly GET_PARTICIPARI_LA_EVENIMENTE_REPORT = "/Report/getParticipariEvenimenteReport";
	public static readonly GET_LISTA_PROIECTELOR_CARE_AU_VIZAT_ACTIUNILE_LUNII_REPORT = "/Report/getListaProiectelorCareAuVizatActiunileLuniiReport";
	public static readonly GET_ACTIUNI_PE_PROIECT_REPORT = "/Report/getActiuniPeProiectReport";
	public static readonly GET_DEPLASARI_DECONT_ALTE_DECONTURI_REPORT = "/Report/getAlteDeconturiCheltuieliReprezentantArbReport";
	public static readonly GET_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI_REPORT = "/Report/getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport";
	public static readonly GET_CHELTUIELI_ARB_SI_REPREZENTANT_ARB_REPORT = "/Report/getCheltuieliArbSiReprezentantArbReport";
	public static readonly GET_PREZENTA_COMISII_GL_IN_INTERVAL_REPORT_BUNDLE = "/Report/getPrezentaComisiiGlInIntervalReportBundle";
	public static readonly GET_PREZENTA_COMISII_GL_IN_INTERVAL_REPORT = "/Report/getPrezentaComisiiGlInIntervalReport";
	public static readonly GET_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_REPORT = "/Report/getNumarParticipantiSedinteComisieGlReport";
	public static readonly GET_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_REPORT_BUNDLE = "/Report/getNumarParticipantiSedinteComisieGlReportBundle";
	public static readonly GET_ACTIUNI_ORGANIZATE_DE_ARB_REPORT = "/Report/getActiuniOrganizateDeArbReport";
	public static readonly GET_DASHBOARD_PROIECTE_REPORT = "/Report/getDashboardProiecteReport";
	public static readonly GET_DASHBOARD_PROIECTE_REPORT_BUNDLE= "/Report/getDashboardProiecteFilterBundle";
	public static readonly GET_PREZENTA_AGA_REPORT = "/Report/getPrezentaAgaReport";
	public static readonly GET_NUMAR_SEDINTE_COMISIE_GL_REPORT = "/Report/getNumarSedinteComisieGlReport";
	public static readonly GET_MEMBRII_AFILIATI_REPORT = "/Report/getMembriiAfiliatiReport";
	public static readonly GET_TASKURI_CUMULATE_REPORT = "/Report/getTaskuriCumulateReport";
	public static readonly GET_DEPLASARI_DECONTURI_REPORT = "/Report/getDeplasariDeconturiReport";
	public static readonly GET_DEPLASARI_DECONTURI_REPORT_BUNDLE = "/Report/getDeplasariDeconturiReportBundle";
	public static readonly GET_CERERI_CONCEDIU_REPORT_BUNDLE = "/Report/getCereriConcediuReportBundle";
	public static readonly GET_CERERI_CONCEDIU_REPORT = "/Report/getCereriConcediuReport";
	public static readonly GET_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_REPORT = "/Report/getReprezentantiBancaPerFunctieSiComisieReport";
	public static readonly GET_PREZENTA_COMISII_GL_REPREZENTATIVITATE = "/Report/getPrezentaComisiiArbReprezentativitate";
	public static readonly GET_RASPUNSURI_BANCI_REPORT = "/Report/getRaspunsuriBanciReport";
	public static readonly GET_RASPUNSURI_BANCI_REPORT_BUNDLE = "/Report/getRaspunsuriBanciReportBundle";
	public static readonly GET_CENTRALIZATOR_PREZENTA_PERIOADA_REPORT = "/Report/getCentralizatorPrezentaPerioadaReport";
	public static readonly GET_NOTA_GENERALA_PE_MEMBRII_ARB_REPORT = "/Report/getNotaGeneralaPeMembriiArbReport";
	public static readonly GET_NOTA_GENERALA_REPORT = "/Report/getNotaGeneralaReport";
	public static readonly GET_NOTE_BANCI_REPORT = "/Report/getNoteBanciReport";
	public static readonly GET_NIVEL_REPREZENTARE_COMISII_REPORT = "/Report/getNivelReprezentareComisiiReport";

	// Auth
	public static readonly AUTH_LOGIN = "/Auth/login";
	public static readonly AUTH_GET_LOGGED_IN_USER = "/Auth/getLoggedInUser";
	public static readonly AUTH_CHANGE_PASSWORD = "/Auth/changePassword";

	// PrezentaOnline
	public static readonly GET_ALL_DOCUMENTS_PREZENTA = "/PrezentaOnline/getAllDocumentsPrezenta";
	public static readonly GET_ALL_MEMBRII_REPREZENTANTI_BY_COMISIE_GL_ID = "/PrezentaOnline/getAllMembriiReprezentantiByComisieGlId";
	public static readonly SAVE_PARTICIPANT = "/PrezentaOnline/saveParticipant";
	public static readonly GET_ALL_PARTICIPANTI_BY_DOCUMENT = "/PrezentaOnline/getAllParticipantiByDocument";
	public static readonly DELETE_PARTICIPANT = "/PrezentaOnline/deleteParticipant";
	public static readonly FINALIZARE_PREZENTA_BY_DOCUMENT = "/PrezentaOnline/finalizarePrezentaByDocument";
	public static readonly IS_PREZENTA_FINALIZATA_BY_DOCUMENT = "/PrezentaOnline/isPrezentaFinalizataByDocument";
	public static readonly IMPORTA_PREZENTA_ONLINE_BY_DOCUMENT = "/PrezentaOnline/importaPrezentaOnlineByDocument";
}