package ro.cloudSoft.cloudDoc.services.export;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DateMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DateTimeMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentValidationDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.domain.content.MonthMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ProjectMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;

public class DocumentTypeDataAsSqlExporter extends DataAsSqlBaseExporter {
	
	private DocumentTypeDao documentTypeDao;
	private GroupPersistencePlugin groupPersistencePlugin;
	private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	private NomenclatorDao nomenclatorDao;
	private UserPersistencePlugin userPersistencePlugin;
	private CalendarDao calendarDao;
	
	@Override
	public String export() {
		
		StringBuilder sql = new StringBuilder();
				
		List<DocumentType> documentTypes = documentTypeDao.getAllDocumentTypesLazy();
		if (CollectionUtils.isNotEmpty(documentTypes)) {			
			for (DocumentType documentType : documentTypes) {
				addNewLine(sql);
				sql.append(generateSQL(documentType));
			}
		}
		
		return sql.toString();
	}
	
	private String generateSQL(DocumentType documentType) {
		
		StringBuilder allSql = new StringBuilder();
		addComment(allSql, "Document Type > " + documentType.getName());
		addNewLine(allSql);
		
		// >>> DocumentType
		
		String dtSql = ""
				+ "INSERT INTO documenttype ("
				+ "id, allusersinitiators, description, keepallversions, mandatoryattachment, name, mand_attach_when_metad_has_val, meta_name_in_mand_attach_cond, "
				+ "meta_value_in_mand_attach_cond, mandatory_attachment_in_states, mandatory_attachment_states, par_doc_loc_real_nam_4_def_loc, folder_id_4_default_location"
				+ ") VALUES ("
				+ "[id], [allusersinitiators], [description], [keepallversions], [mandatoryattachment], [name], [mand_attach_when_metad_has_val], [meta_name_in_mand_attach_cond], "
				+ "[meta_value_in_mand_attach_cond], [mandatory_attachment_in_states], [mandatory_attachment_states], [par_doc_loc_real_nam_4_def_loc], [folder_id_4_default_location]"
				+ ");";
		
		dtSql = replaceWithSqlGenerateID(dtSql, "id");
		dtSql = replaceWithFieldValue(dtSql, "allusersinitiators", documentType.isAllUsersInitiators());
		dtSql = replaceWithFieldValue(dtSql, "description", documentType.getDescription());
		dtSql = replaceWithFieldValue(dtSql, "keepallversions", documentType.isKeepAllVersions());
		dtSql = replaceWithFieldValue(dtSql, "mandatoryattachment", documentType.getMandatoryAttachment());
		dtSql = replaceWithFieldValue(dtSql, "name", documentType.getName());
		dtSql = replaceWithFieldValue(dtSql, "mand_attach_when_metad_has_val", documentType.isMandatoryAttachmentWhenMetadataHasValue());
		dtSql = replaceWithFieldValue(dtSql, "meta_name_in_mand_attach_cond", documentType.getMetadataNameInMandatoryAttachmentCondition());
		dtSql = replaceWithFieldValue(dtSql, "meta_value_in_mand_attach_cond", documentType.getMetadataValueInMandatoryAttachmentCondition());
		dtSql = replaceWithFieldValue(dtSql, "mandatory_attachment_in_states", documentType.isMandatoryAttachmentInStates());
		dtSql = replaceWithFieldValue(dtSql, "mandatory_attachment_states", documentType.getMandatoryAttachmentStates());
		dtSql = replaceWithFieldValue(dtSql, "par_doc_loc_real_nam_4_def_loc", "");
		dtSql = replaceWithFieldValue(dtSql, "folder_id_4_default_location", "");
		
		allSql.append(dtSql);
		
		
		// >>> MimeTypes
		
		Set<MimeType> mimeTypes = documentType.getAllowedMimeTypes();
		if (CollectionUtils.isNotEmpty(mimeTypes)) {
			addNewLine(allSql);
			for (MimeType mimeType : mimeTypes) {
				addNewLine(allSql);
				allSql.append(generateSQLForDocumentTypeAndMimeTypeRelation(mimeType, documentType.getName()));
			}
		}
		
		
		// >>> Initiators
		
		Set<OrganizationEntity> initiators = documentType.getInitiators();
		if (CollectionUtils.isNotEmpty(initiators)) {
			addNewLine(allSql);	
			for (OrganizationEntity initiator : initiators) {
				addNewLine(allSql);
				allSql.append(generateSQLForDocumentTypeAndInitiatorRelation(documentType.getName(), initiator));
			}
		}
		
		
		// >>> MetadataDefinitions
		
		List<? extends MetadataDefinition> metadataDefinitions = documentType.getMetadataDefinitions();
		if (CollectionUtils.isNotEmpty(metadataDefinitions)) {
			addNewLine(allSql);
			for (MetadataDefinition md : metadataDefinitions) {
				addNewLine(allSql);
				allSql.append(generateSQLForMetadataDefinition(md, documentType.getName(), null));
			}
		}
				
		// >>> MetadataCollectionDefinitions
		
		List<MetadataCollection> metadataCollections = documentType.getMetadataCollections();
		if (CollectionUtils.isNotEmpty(metadataCollections)) {
			for (MetadataCollection mc: metadataCollections) {
				addNewLine(allSql);
				allSql.append(generateSQLForMetadataCollection(documentType.getName(), mc));
			}
		}
		
		// >>> Validations
		
		List<DocumentValidationDefinition> validations = documentType.getValidationDefinitions();
		if (CollectionUtils.isNotEmpty(validations)) {
			addNewLine(allSql);
			for (DocumentValidationDefinition validation: validations) {
				addNewLine(allSql);
				allSql.append(generateSQLForDocumentValidation(validation, documentType.getName(), null));
			}
		}
		addNewLine(allSql);
		addComment(allSql, "Document Type < " + documentType.getName());
		
		return allSql.toString();
	}
	
	private String generateSQLForDocumentTypeAndMimeTypeRelation(MimeType mimeType, String documentTypeName) {
		String sql = ""
				+ "INSERT INTO documenttype_mimetype ("
				+ "documenttype_id, allowedmimetypes_id"
				+ ") VALUES ("
				+ "[documenttype_id], [allowedmimetypes_id]"
				+ ");";	
		sql = replaceWithFieldValueAsSelect(sql, "documenttype_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
		sql = replaceWithFieldValueAsSelect(sql, "allowedmimetypes_id", getSqlForSelectMimeTypeIdByExtension(mimeType.getExtension()));
		return sql;
	}
	
	private String generateSQLForDocumentTypeAndInitiatorRelation(String documentTypeName, OrganizationEntity organizationEntity) {
		String sql = ""
				+ "INSERT INTO doctype_oe ("
				+ "documenttype_id, initiators_org_entity_id"
				+ ") VALUES ("
				+ "[documenttype_id], [initiators_org_entity_id]"
				+ ");";
		sql = replaceWithFieldValueAsSelect(sql, "documenttype_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
		sql = replaceWithFieldValueAsSelect(sql, "initiators_org_entity_id", getSqlForSelectOrganizationEntity(organizationEntity));
		return sql;
	}
	
	private String generateSQLForMetadataDefinition(MetadataDefinition metadataDefinition, String documentTypeName, String metadataCollectionName) {
		
		String sql = ""
				+ "INSERT INTO metadatadefinition ("
				+ "basemetadatatype, id, default_value, indexed, label, mandatory, mandatorystates, metadatatype, name, ordernumber, representative, "
				+ "restrictedonedit, restrictedoneditstates, numberlength, prefix, extendable, multipleselection, collection_id, documenttype_id, "
				+ "only_users_from_group, id_of_group_of_permitted_users, auto_compl_with_current_user, auto_cpl_w_crt_usr_state_code, nomenclator_id, "
				+ "auto_compl_with_current_date, invisible, invisible_in_states, auto_compl_with_current_month, md_name_for_auto_cmpl_wth_md, "
				+ "type_of_auto_cmpl_wth_md, nom_atr_ky_for_aut_cmpl_wth_md, auto_compl_with_crnt_date_time, jr_property_name, metadata_document_type_id, "
				+ "multiple_documents_selection, multiple_projects_selection, class_name_for_aut_cmpl_wth_md"
				+ ") VALUES ("
				+ "[basemetadatatype], [id], [default_value], [indexed], [label], [mandatory], [mandatorystates], [metadatatype], [name], [ordernumber], [representative], "
				+ "[restrictedonedit], [restrictedoneditstates], [numberlength], [prefix], [extendable], [multipleselection], [collection_id], [documenttype_id], "
				+ "[only_users_from_group], [id_of_group_of_permitted_users], [auto_compl_with_current_user], [auto_cpl_w_crt_usr_state_code], [nomenclator_id], "
				+ "[auto_compl_with_current_date], [invisible], [invisible_in_states], [auto_compl_with_current_month], [md_name_for_auto_cmpl_wth_md], "
				+ "[type_of_auto_cmpl_wth_md], [nom_atr_ky_for_aut_cmpl_wth_md], [auto_compl_with_crnt_date_time], [jr_property_name], [metadata_document_type_id], "
				+ "[multiple_documents_selection], [multiple_projects_selection], [class_name_for_aut_cmpl_wth_md]"
				+ ");";
		
		sql = replaceWithFieldValue(sql, "basemetadatatype", metadataDefinition.getDiscriminatorColumnValue());
		sql = replaceWithSqlGenerateID(sql, "id");
		
		String defaultValue = metadataDefinition.getDefaultValue();		
		if (StringUtils.isNotBlank(defaultValue)) {
			String defaultValueAsSelect = null;
			if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_USER)) {
				User user =  userPersistencePlugin.getUserById(Long.valueOf(defaultValue));
				defaultValueAsSelect = getSqlForSelectUserIdByUsername(user.getUsername());				
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_GROUP)) {
				Group group =  groupPersistencePlugin.getGroupById(Long.valueOf(defaultValue));
				defaultValueAsSelect = getSqlForSelectUserIdByUsername(group.getName());
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NOMENCLATOR)) {
				Nomenclator nomenclator =  nomenclatorDao.find(Long.valueOf(defaultValue));
				defaultValueAsSelect = getSqlForSelectNomenclatorIdByName(nomenclator.getName());
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_CALENDAR)) {
				Calendar calendar =  calendarDao.find(Long.valueOf(defaultValue));
				defaultValueAsSelect = getSqlForSelectCalendarIdByName(calendar.getName());
			} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DOCUMENT)) {
				throw new RuntimeException("default value for metadata type document is not implemented");
			} else {
				sql = replaceWithFieldValue(sql, "default_value", defaultValue);
			}			
			if (StringUtils.isNotBlank(defaultValueAsSelect)) {
				sql = replaceWithFieldValueAsSelect(sql, "default_value", defaultValueAsSelect);
			}	
		} else {
			sql = replaceWithNullValue(sql, "default_value");
		}
		
		
		sql = replaceWithFieldValue(sql, "indexed", metadataDefinition.getIndexed());
		sql = replaceWithFieldValue(sql, "label", metadataDefinition.getLabel());
		sql = replaceWithFieldValue(sql, "mandatory", metadataDefinition.getMandatory());
		sql = replaceWithFieldValue(sql, "mandatorystates", metadataDefinition.getMandatoryStates());
		sql = replaceWithFieldValue(sql, "metadatatype", metadataDefinition.getMetadataType());
		sql = replaceWithFieldValue(sql, "name", metadataDefinition.getName());
		sql = replaceWithFieldValue(sql, "ordernumber", metadataDefinition.getOrderNumber());
		sql = replaceWithFieldValue(sql, "representative", metadataDefinition.getRepresentative());
		
		sql = replaceWithFieldValue(sql, "restrictedonedit", metadataDefinition.isRestrictedOnEdit());
		sql = replaceWithFieldValue(sql, "restrictedoneditstates", metadataDefinition.getRestrictedOnEditStates());			
		if (metadataDefinition instanceof AutoNumberMetadataDefinition) {
			AutoNumberMetadataDefinition anMetadataDefinition = (AutoNumberMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "numberlength", anMetadataDefinition.getNumberLength());
			sql = replaceWithFieldValue(sql, "prefix", anMetadataDefinition.getPrefix());
		} else {
			sql = replaceWithNullValue(sql, "numberlength");
			sql = replaceWithNullValue(sql, "prefix");
		}			
		if (metadataDefinition instanceof ListMetadataDefinition) {
			ListMetadataDefinition lMetadataDefinition = (ListMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "extendable", lMetadataDefinition.getExtendable());
			sql = replaceWithFieldValue(sql, "multipleselection", lMetadataDefinition.getMultipleSelection());
		} else {
			sql = replaceWithNullValue(sql, "extendable");
			sql = replaceWithNullValue(sql, "multipleselection");
		}		
		
		if (StringUtils.isBlank(metadataCollectionName)) {
			sql = replaceWithNullValue(sql, "collection_id");
			sql = replaceWithFieldValueAsSelect(sql, "documenttype_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
		} else {
			sql = replaceWithNullValue(sql, "documenttype_id");
			sql = replaceWithFieldValueAsSelect(sql, "collection_id", getSqlForSelectMetadataCollectionIdByDocumentTypeAndCollectionName(documentTypeName, metadataCollectionName));	
		}
		
		if (metadataDefinition instanceof UserMetadataDefinition) {
			UserMetadataDefinition userMetadataDefinition = (UserMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "only_users_from_group", userMetadataDefinition.isOnlyUsersFromGroup());
			Group group = userMetadataDefinition.getGroupOfPermittedUsers();
			if (group != null) {
				sql = replaceWithFieldValueAsSelect(sql, "id_of_group_of_permitted_users", getSqlForSelectGroupIdByName(group.getName()));
			} else {
				sql = replaceWithNullValue(sql, "id_of_group_of_permitted_users");
			}
			sql = replaceWithFieldValue(sql, "auto_compl_with_current_user", userMetadataDefinition.isAutoCompleteWithCurrentUser());
			sql = replaceWithFieldValue(sql, "auto_cpl_w_crt_usr_state_code", userMetadataDefinition.getAutoCompleteWithCurrentUserStateCode());
		} else {
			sql = replaceWithNullValue(sql, "only_users_from_group");
			sql = replaceWithNullValue(sql, "id_of_group_of_permitted_users");
			sql = replaceWithNullValue(sql, "auto_compl_with_current_user");
			sql = replaceWithNullValue(sql, "auto_cpl_w_crt_usr_state_code");
		}
		if (metadataDefinition instanceof NomenclatorMetadataDefinition) {
			NomenclatorMetadataDefinition nomenclatorMetadataDefinition = (NomenclatorMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValueAsSelect(sql, "nomenclator_id", getSqlForSelectNomenclatorIdByName(nomenclatorMetadataDefinition.getNomenclator().getName()));
		} else {
			sql = replaceWithNullValue(sql, "nomenclator_id");				
		}
		if (metadataDefinition instanceof DateMetadataDefinition) {
			DateMetadataDefinition dateMetadataDefinition = (DateMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "auto_compl_with_current_date", dateMetadataDefinition.isAutoCompleteWithCurrentDate());
		} else {
			sql = replaceWithNullValue(sql, "auto_compl_with_current_date");			
		}
		sql = replaceWithFieldValue(sql, "invisible", metadataDefinition.isInvisible());
		sql = replaceWithFieldValue(sql, "invisible_in_states", metadataDefinition.getInvisibleInStates());
		if (metadataDefinition instanceof MonthMetadataDefinition) {
			MonthMetadataDefinition monthMetadataDefinition = (MonthMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "auto_compl_with_current_month", monthMetadataDefinition.isAutoCompleteWithCurrentMonth());
		} else {
			sql = replaceWithNullValue(sql, "auto_compl_with_current_month");			
		}
		sql = replaceWithFieldValue(sql, "md_name_for_auto_cmpl_wth_md", metadataDefinition.getMetadataNameForAutoCompleteWithMetadata());
		if (metadataDefinition.getTypeOfAutoCompleteWithMetadata() != null) {
			sql = replaceWithFieldValue(sql, "type_of_auto_cmpl_wth_md", metadataDefinition.getTypeOfAutoCompleteWithMetadata().name());
			sql = replaceWithFieldValue(sql, "nom_atr_ky_for_aut_cmpl_wth_md", metadataDefinition.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		} else {
			sql = replaceWithNullValue(sql, "type_of_auto_cmpl_wth_md");
			sql = replaceWithNullValue(sql, "nom_atr_ky_for_aut_cmpl_wth_md");
		}
		
		if (metadataDefinition instanceof DateTimeMetadataDefinition) {
			DateTimeMetadataDefinition dtMetadataDefiniton = (DateTimeMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "auto_compl_with_crnt_date_time", dtMetadataDefiniton.isAutoCompleteWithCurrentDateTime());
		} else {
			sql = replaceWithNullValue(sql, "auto_compl_with_crnt_date_time");			
		}
		sql = replaceWithFieldValue(sql, "jr_property_name", metadataDefinition.getJrPropertyName());
		if (metadataDefinition instanceof DocumentMetadataDefinition) {
			DocumentMetadataDefinition documentMetadataDefiniton = (DocumentMetadataDefinition) metadataDefinition;
			String metadataDocumentTypeName = documentTypeDao.getDocumentTypeName(documentMetadataDefiniton.getMetadataDocumentTypeId());
			sql = replaceWithFieldValueAsSelect(sql, "metadata_document_type_id", getSqlForSelectDocumentTypeIdByName(metadataDocumentTypeName));
			sql = replaceWithFieldValue(sql, "multiple_documents_selection", documentMetadataDefiniton.isMultipleDocumentsSelection());
		} else {
			sql = replaceWithNullValue(sql, "metadata_document_type_id");	
			sql = replaceWithNullValue(sql, "multiple_documents_selection");
		}
		if (metadataDefinition instanceof ProjectMetadataDefinition) {
			ProjectMetadataDefinition projectMetadataDefiniton = (ProjectMetadataDefinition) metadataDefinition;
			sql = replaceWithFieldValue(sql, "multiple_projects_selection", projectMetadataDefiniton.isMultipleProjectsSelection());
		} else {
			sql = replaceWithNullValue(sql, "multiple_projects_selection");
		}
		
		if (metadataDefinition instanceof AutoNumberMetadataDefinition) {
			AutoNumberMetadataDefinition anMetadataDefinition = (AutoNumberMetadataDefinition) metadataDefinition;
			sql = addNewLine(sql);
			sql += generateSQLForAutonumberMetadataSequenceValue(documentTypeName, anMetadataDefinition);
		} else if (metadataDefinition instanceof ListMetadataDefinition) {
			ListMetadataDefinition listMetadataDefinition = (ListMetadataDefinition) metadataDefinition;
			for (ListMetadataItem item : listMetadataDefinition.getListItems()) {
				sql = addNewLine(sql);
				sql += generateSQLForListMetadataItem(item, documentTypeName, metadataCollectionName);
			}
		}
		
		sql = replaceWithFieldValue(sql, "class_name_for_aut_cmpl_wth_md", metadataDefinition.getClassNameForAutoCompleteWithMetadata());
		
		return sql;
	}
	
	private String generateSQLForAutonumberMetadataSequenceValue(String documentTypeName, AutoNumberMetadataDefinition autoNumberMetadataDefinition) {
		
		String sql = ""
				+ "INSERT INTO autonumbermetadataseqvalue ("
				+ "autonumbermetadatadefinitionid, sequencevalue"
				+ ") VALUES ("
				+ "[autonumbermetadatadefinitionid], [sequencevalue]"
				+ ");";	
		
		sql = replaceWithFieldValueAsSelect(sql, "autonumbermetadatadefinitionid", getSqlForSelectMetadataDefinitionIdByDocumentTypeNameAndMetadataName(documentTypeName, autoNumberMetadataDefinition.getName()));
		sql = replaceWithFieldValue(sql, "sequencevalue", 0);
		return sql;
	}
	
	private String generateSQLForListMetadataItem(ListMetadataItem item, String documentTypeName, String collectionName) {
				
		String sql = ""
				+ "INSERT INTO listmetadataitem ("
				+ "id, label, value, list_id, order_number"
				+ ") VALUES ("
				+ "[id], [label], [value], [list_id], [order_number]"
				+ ");";	
		
		sql = replaceWithSqlGenerateID(sql, "id");	
		sql = replaceWithFieldValue(sql, "label", item.getLabel());
		sql = replaceWithFieldValue(sql, "value", item.getValue());
		if (StringUtils.isBlank(collectionName)) {
			sql = replaceWithFieldValueAsSelect(sql, "list_id", getSqlForSelectMetadataDefinitionIdByDocumentTypeNameAndMetadataName(documentTypeName, item.getList().getName()));
		} else {
			sql = replaceWithFieldValueAsSelect(sql, "list_id", getSqlForSelectMetadataDefinitionIdByDocumentTypeNameAndCollectionNameAndMetadataName(documentTypeName, collectionName, item.getList().getName()));
		}		
		sql = replaceWithFieldValue(sql, "order_number", item.getOrderNumber());
		
		return sql;
	}
	
	private String getSqlForSelectMetadataDefinitionIdByDocumentTypeNameAndMetadataName(String documentTypeName, String metadataName) {
		return "(select id from metadatadefinition where documenttype_id=" + getSqlForSelectDocumentTypeIdByName(documentTypeName) + " and name='" + metadataName + "')";
	}
	
	private String getSqlForSelectMetadataDefinitionIdByDocumentTypeNameAndCollectionNameAndMetadataName(String documentTypeName, String collectionName, String metadataName) {
		return "(select id from metadatadefinition where collection_id=" + getSqlForSelectMetadataCollectionIdByDocumentTypeAndCollectionName(documentTypeName, collectionName) + " and name='" + metadataName + "')";
	}
	
	private String generateSQLForMetadataCollection(String documentTypeName, MetadataCollection metadataCollection) {
		
		String sql = ""
				+ "INSERT INTO metadatacollection ("
				+ "id, label, name, documenttype_id, restricted_on_edit, restricted_on_edit_steps, mandatory, mandatory_steps, ordernumber, invisible, invisible_in_states, jr_property_name, "
				+ "md_name_for_auto_cmpl_wth_md, type_of_auto_cmpl_wth_md, nom_atr_ky_for_aut_cmpl_wth_md, class_name_for_aut_cmpl_wth_md"
				+ ") VALUES ("
				+ "[id], [label], [name], [documenttype_id], [restricted_on_edit], [restricted_on_edit_steps], [mandatory], [mandatory_steps], [ordernumber], [invisible], [invisible_in_states], [jr_property_name], "
				+ "[md_name_for_auto_cmpl_wth_md], [type_of_auto_cmpl_wth_md], [nom_atr_ky_for_aut_cmpl_wth_md], [class_name_for_aut_cmpl_wth_md]"
				+ ");";	
		
		sql = replaceWithSqlGenerateID(sql, "id");
		sql = replaceWithFieldValue(sql, "label", metadataCollection.getLabel());
		sql = replaceWithFieldValue(sql, "name", metadataCollection.getName());
		sql = replaceWithFieldValueAsSelect(sql, "documenttype_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
		sql = replaceWithFieldValue(sql, "restricted_on_edit", metadataCollection.isRestrictedOnEdit());
		sql = replaceWithFieldValue(sql, "restricted_on_edit_steps", metadataCollection.getRestrictedOnEditStates());
		sql = replaceWithFieldValue(sql, "mandatory", metadataCollection.isMandatory());
		sql = replaceWithFieldValue(sql, "mandatory_steps", metadataCollection.getMandatoryStates());
		sql = replaceWithFieldValue(sql, "ordernumber", metadataCollection.getOrderNumber());
		sql = replaceWithFieldValue(sql, "invisible", metadataCollection.isInvisible());
		sql = replaceWithFieldValue(sql, "invisible_in_states", metadataCollection.getInvisibleInStates());
		sql = replaceWithFieldValue(sql, "jr_property_name", metadataCollection.getJrPropertyName());
		sql = replaceWithFieldValue(sql, "md_name_for_auto_cmpl_wth_md", metadataCollection.getMetadataNameForAutoCompleteWithMetadata());
		if (metadataCollection.getTypeOfAutoCompleteWithMetadata() != null) {
			sql = replaceWithFieldValue(sql, "type_of_auto_cmpl_wth_md", metadataCollection.getTypeOfAutoCompleteWithMetadata().name());
		} else {
			sql = replaceWithNullValue(sql, "type_of_auto_cmpl_wth_md");
		}		
		sql = replaceWithFieldValue(sql, "nom_atr_ky_for_aut_cmpl_wth_md", metadataCollection.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		sql = replaceWithFieldValue(sql, "class_name_for_aut_cmpl_wth_md", metadataCollection.getClassNameForAutoCompleteWithMetadata());
		
		
		// >>> MetadataDefinitions
		
		List<? extends MetadataDefinition> metadataDefinitions = metadataCollection.getMetadataDefinitions();
		if (CollectionUtils.isNotEmpty(metadataDefinitions)) {
			for (MetadataDefinition md : metadataDefinitions) {
				sql = addNewLine(sql);
				sql += generateSQLForMetadataDefinition(md, documentTypeName, metadataCollection.getName());
			}
		}
		
		
		// >>> Validations
		
		List<DocumentValidationDefinition> validations = metadataCollection.getValidationDefinitions();
		if (CollectionUtils.isNotEmpty(validations)) {
			sql = addNewLine(sql);
			for (DocumentValidationDefinition validation: validations) {
				sql = addNewLine(sql);
				sql += generateSQLForDocumentValidation(validation, documentTypeName, metadataCollection.getName());
			}
		}
		
		return sql;
	}	
	
	private String generateSQLForDocumentValidation(DocumentValidationDefinition validation, String documentTypeName, String collectionName) {
		
		if (StringUtils.isBlank(documentTypeName)) {
			throw new RuntimeException("generateSQLForDocumentValidation: documentTypeName is null");
		}
		
		String sql = ""
				+ "INSERT INTO document_validation_definition ("
				+ "id, document_type_id, metadata_collection_id, condition_expression, validation_in_states, message, validation_order"
				+ ") VALUES ("
				+ "[id], [document_type_id], [metadata_collection_id], [condition_expression], [validation_in_states], [message], [validation_order]"
				+ ");";	
		
		sql = replaceWithSqlGenerateID(sql, "id");		
		if (StringUtils.isBlank(collectionName)) {
			sql = replaceWithFieldValueAsSelect(sql, "document_type_id", getSqlForSelectDocumentTypeIdByName(documentTypeName));
			sql = replaceWithNullValue(sql, "metadata_collection_id");
		} else {
			sql = replaceWithNullValue(sql, "document_type_id");
			sql = replaceWithFieldValueAsSelect(sql, "metadata_collection_id", getSqlForSelectMetadataCollectionIdByDocumentTypeAndCollectionName(documentTypeName, collectionName));
		}		
		sql = replaceWithFieldValue(sql, "condition_expression", validation.getConditionExpression());
		sql = replaceWithFieldValue(sql, "validation_in_states", validation.getValidationInStates());
		sql = replaceWithFieldValue(sql, "message", validation.getMessage());
		sql = replaceWithFieldValue(sql, "validation_order", validation.getValidationOrder());
		
		return sql;
	}
	
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	
	public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
	
	public void setOrganizationUnitPersistencePlugin(
			OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}
}
