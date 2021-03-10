package ro.cloudSoft.cloudDoc.dao.content;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.utils.hibernate.QueryUtils;
import ro.cloudSoft.common.utils.MapUtils;

public class DocumentTypeDaoImpl extends HibernateDaoSupport implements DocumentTypeDao, InitializingBean {
	
	@Override
	public List<DocumentType> getAllDocumentTypesLazy() {
		String query = "FROM DocumentType ORDER BY name";
		@SuppressWarnings("unchecked")
        List<DocumentType> documentTypes = getHibernateTemplate().find(query);
        return documentTypes;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long saveDocumentType(DocumentType documentType) {
		
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			getHibernateTemplate().saveOrUpdate(metadataDefinition);
			updateMetadataDefinitionDiscriminatorColumnValue(metadataDefinition);
		}
	
		for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
			for (MetadataDefinition metadataDefinition : metadataCollection.getMetadataDefinitions()) {
				getHibernateTemplate().saveOrUpdate(metadataDefinition);
				updateMetadataDefinitionDiscriminatorColumnValue(metadataDefinition);
			}
			getHibernateTemplate().saveOrUpdate(metadataCollection);
		}
		
		if (documentType.getId() == null) {
			return  (Long) this.getHibernateTemplate().save(documentType);
		} else {
			this.getHibernateTemplate().saveOrUpdate(documentType);
			removeOrphanMetadata();
			removeOrphanMetadataCollections();
			return  documentType.getId();
		}
	}
	
	private void updateMetadataDefinitionDiscriminatorColumnValue(MetadataDefinition metadataDefinition) {
		
		boolean isMetadataDefinitionNew = (metadataDefinition.getId() == null);
		if (isMetadataDefinitionNew) {
			return;
		}
		
		String discriminatorColumnName = MetadataDefinition.DISCRIMINATOR_COLUMN_NAME;
		String discriminatorColumnValue = metadataDefinition.getDiscriminatorColumnValue();
		
		String query = "UPDATE MetadataDefinition SET " + discriminatorColumnName + " = ? WHERE id = ?";
		Object[] queryParameters = new Object[] {
			discriminatorColumnValue,
			metadataDefinition.getId()
		};
		
		getHibernateTemplate().bulkUpdate(query, queryParameters);
	}
	
	@SuppressWarnings("unchecked")
	private void removeOrphanMetadata() {
		String query =
			"SELECT metadata " +
			"FROM MetadataDefinition metadata " +
			"WHERE collection IS NULL " +
			"AND id NOT IN " +
			"	(" +
			"	SELECT md.id " +
			"	FROM DocumentType tipDoc " +
			"	JOIN tipDoc.metadataDefinitions md " +
			"	)";
		
		List<MetadataDefinition> orphanMetadata = getHibernateTemplate().find(query); 
		getHibernateTemplate().deleteAll(orphanMetadata);
		
	}
	
	@SuppressWarnings("unchecked")
	private void removeOrphanMetadataCollections() {
		
		String query =
			"SELECT colectii " +
			"FROM MetadataCollection colectii " +
			"WHERE id NOT IN " +
			"	(" +
			"	SELECT colectie.id " +
			"	FROM DocumentType tipDoc " +
			"	JOIN tipDoc.metadataCollections colectie " +
			"	)";
		
		List<MetadataCollection> orphanMetadataCollections = getHibernateTemplate().find(query);
		getHibernateTemplate().deleteAll(orphanMetadataCollections);
	}

	@Override
	public DocumentType find(Long id) {
		DocumentType docType = (DocumentType) getHibernateTemplate().get(DocumentType.class, id);
		return docType;
        
    }

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void removeDocumentType(Long id) {
		DocumentType docType = find( id );
		getHibernateTemplate().delete( docType );
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserMetadataDefinition> getUserMetadataDefinitions(final List<Long> documentTypeIds) {
		
		if (CollectionUtils.isEmpty(documentTypeIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT metadataDefinition " +
					"FROM DocumentType documentType " +
					"JOIN documentType.metadataDefinitions metadataDefinition " +
					"WHERE documentType.id IN (:documentTypeIds) " +
					"AND metadataDefinition.metadataType = :metadataType";
				return session.createQuery(query)
					.setParameterList("documentTypeIds", documentTypeIds)
					.setParameter("metadataType", MetadataDefinition.TYPE_USER)
					.list();
			}
		});
	}
	
	@Override
	public Map<String, Map<String, String>> getListItemLabelByListItemValueByMetadataName(Long documentTypeId) {
		
		String query =
			"SELECT " +
			"	metadata.name, " +
			"	listItem.value, " +
			"	listItem.label " +
			"FROM DocumentType documentType " +
			"JOIN documentType.metadataDefinitions metadata " +
			"JOIN metadata.listItems listItem " +
			"WHERE documentType.id = ? " +
			"AND metadata.metadataType = ?";
		Object[] queryParameters = {
			documentTypeId,
			MetadataDefinition.TYPE_LIST
		};

		@SuppressWarnings("unchecked")
		List<Object[]> results = getHibernateTemplate().find(query, queryParameters);
		Map<String, Map<String, String>> listItemLabelByListItemValueByMetadataName = Maps.newHashMap();
		
		for (Object[] result : results) {
			
			String metadataName = (String) result[0];
			String listItemValue = (String) result[1];
			String listItemLabel = (String) result[2];
			
			Map<String, String> listItemLabelByListItemValue = MapUtils.getAndInitIfNull(
				listItemLabelByListItemValueByMetadataName, metadataName, 
				Maps.<String, String> newHashMap()
			);
			listItemLabelByListItemValue.put(listItemValue, listItemLabel);
		}
		
		return listItemLabelByListItemValueByMetadataName;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentType> getAvailableDocumentTypes(SecurityManager userSecurity) {
		
		final Set<Long> idsForDocumentTypesThatUserCanInitiate = getIdsForDocumentTypesThatUserCanInitiate(userSecurity);
		if (idsForDocumentTypesThatUserCanInitiate.isEmpty()) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"FROM DocumentType " +
					"WHERE id IN (:ids) " +
					"ORDER BY LOWER(name)";
				return session.createQuery(query)
					.setParameterList("ids", idsForDocumentTypesThatUserCanInitiate)
					.list();
			}
		});
	}
	
	@Override
	public List<DocumentCreationInDefaultLocationView> getDocumentCreationInDefaultLocationViews(SecurityManager userSecurity) {
		
		final Set<Long> idsForDocumentTypesThatUserCanInitiate = getIdsForDocumentTypesThatUserCanInitiate(userSecurity);
		if (idsForDocumentTypesThatUserCanInitiate.isEmpty()) {
			return Collections.emptyList();
		}

		@SuppressWarnings("unchecked")
		List<Object[]> documentTypePropertiesList = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT " +
					"	id," +
					"	name, " +
					"	parentDocumentLocationRealNameForDefaultLocation, " +
					"	folderIdForDefaultLocation " +
					"FROM DocumentType " +
					"WHERE (" +
					"	(parentDocumentLocationRealNameForDefaultLocation IS NOT NULL) " +
					"	AND " +
					"	(folderIdForDefaultLocation IS NOT NULL) " +
					") AND id IN (:ids) " +
					"ORDER BY LOWER(name)";
				return session.createQuery(query)
					.setParameterList("ids", idsForDocumentTypesThatUserCanInitiate)
					.list();
			}
		});
		List<DocumentCreationInDefaultLocationView> views = Lists.newArrayList();
		
		for (Object[] documentTypeProperties : documentTypePropertiesList) {
			
			Long documentTypeId = (Long) documentTypeProperties[0];
			String documentTypeName = (String) documentTypeProperties[1];
			
			String parentDocumentLocationRealNameForDefaultLocation = (String) documentTypeProperties[2];
			String folderIdForDefaultLocation = (String) documentTypeProperties[3];
			
			DocumentCreationInDefaultLocationView view = new DocumentCreationInDefaultLocationView(
				
				documentTypeId,
				documentTypeName,
				
				parentDocumentLocationRealNameForDefaultLocation,
				folderIdForDefaultLocation
			);
			views.add(view);
		}
		
		return views;
	}
	
	private Set<Long> getIdsForDocumentTypesThatUserCanInitiate(final SecurityManager userSecurity) {
		
		Set<Long> ids = Sets.newHashSet();
		
		String queryByAllUsersInitiatiorsSetting =
			"SELECT dt.id " +
			"FROM DocumentType dt " +
			"WHERE dt.allUsersInitiators = ?";
		@SuppressWarnings("unchecked")
		List<Long> idsByAllUsersInitiatiorsSetting = getHibernateTemplate().find(queryByAllUsersInitiatiorsSetting, true);
		ids.addAll(idsByAllUsersInitiatiorsSetting);
		
		String queryByUser =
			"SELECT dt.id " +
			"FROM " +
			"	DocumentType dt, " +
			"	User theUser " +
			"JOIN dt.initiators init " +
			"WHERE init.id = ? " +
			"AND init.id = theUser.id";
		@SuppressWarnings("unchecked")
		List<Long> idsByUser = getHibernateTemplate().find(queryByUser, userSecurity.getUserId());
		ids.addAll(idsByUser);

		if (!userSecurity.getOrganizationUnitIds().isEmpty()) {
			@SuppressWarnings("unchecked")
			List<Long> idsByOrganizationUnits = getHibernateTemplate().executeFind(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryByOrganizationUnits =
						"SELECT dt.id " +
						"FROM " +
						"	DocumentType dt, " +
						"	OrganizationUnit theOrgUnit " +
						"JOIN dt.initiators init " +
						"WHERE init.id IN (:orgUnitIds) " +
						"AND init.id = theOrgUnit.id";
					return session.createQuery(queryByOrganizationUnits)
						.setParameterList("orgUnitIds", userSecurity.getOrganizationUnitIds())
						.list();
				}
			});
			ids.addAll(idsByOrganizationUnits);
		}

		if (!userSecurity.getGroupIds().isEmpty()) {
			@SuppressWarnings("unchecked")
			List<Long> idsByGroups = getHibernateTemplate().executeFind(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryByOrganizationUnits =
						"SELECT dt.id " +
						"FROM " +
						"	DocumentType dt, " +
						"	Group theGroup " +
						"JOIN dt.initiators init " +
						"WHERE init.id IN (:groupIds) " +
						"AND init.id = theGroup.id";
					return session.createQuery(queryByOrganizationUnits)
						.setParameterList("groupIds", userSecurity.getGroupIds())
						.list();
				}
			});
			ids.addAll(idsByGroups);
		}
		
		return ids;
	}

	@Override
	public MetadataDefinition getMetadataDefinition(Long long1) {
		MetadataDefinition mDef = (MetadataDefinition) getHibernateTemplate().get(MetadataDefinition.class, long1);
		return mDef;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, String> getDocumentTypesNameMap(Set<Long> ids) {
		Map<Long, String> documentTypesNameMap = new HashMap<Long, String>();
		
		if (ids.size() > 0) {
			StringBuilder queryRestriction = new StringBuilder();
			for (Long id : ids) {
				queryRestriction.append("dt.id = " + id.toString());
				queryRestriction.append(" OR ");
			}
			queryRestriction.delete(queryRestriction.lastIndexOf(" OR "), queryRestriction.length());
			
			List<Object[]> results = getHibernateTemplate().find("SELECT dt.id, dt.name FROM DocumentType dt WHERE " + queryRestriction.toString());
			
			for (Object[] result : results) {
				documentTypesNameMap.put((Long) result[0], (String) result[1]);
			}
		}
		
		return documentTypesNameMap;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentType> getDocumentTypesWithNoWorkflow() {
		String query = "SELECT documentType " +
			"FROM DocumentType documentType " +
			"WHERE documentType.id NOT IN (" +
			"	SELECT DISTINCT documentTypeWithWorkflow.id " +
			"	FROM Workflow workflow " +
			"	JOIN workflow.documentTypes documentTypeWithWorkflow" +
			") " +
			"ORDER BY documentType.name";
        return getHibernateTemplate().find(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DocumentTypeTemplate getTemplate(Long documentTypeId, String templateName) {
		List<DocumentTypeTemplate> list = this.getHibernateTemplate().find("SELECT template FROM DocumentTypeTemplate template WHERE template.documentTypeId = ? AND template.name = ?", new Object[] {documentTypeId, templateName});
		
		if (list.isEmpty()) {
			return null;
		}
		
		DocumentTypeTemplate template = list.get(0);
		
		template.getData();
		
		return template;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentTypeTemplate> getTemplates(Long documentTypeId) {
		return this.getHibernateTemplate().find("SELECT template FROM DocumentTypeTemplate template WHERE template.documentTypeId = ?", documentTypeId);
	}
	
	@Override
	public SetMultimap<Long, String> getMetadataNamesByDocumentTypeId(final Collection<Long> documentTypeIds) {
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT documentType.id, metadata.name " +
					"FROM DocumentType documentType " +
					"JOIN documentType.metadataDefinitions metadata " +
					"WHERE documentType.id IN (:documentTypeIds)";
				return session.createQuery(query)
					.setParameterList("documentTypeIds", documentTypeIds)
					.list();
			}
		});
			
		
		SetMultimap<Long, String> metadataNamesByDocumentTypeId = HashMultimap.create();
		
		for (Object[] result : results) {
			
			Long documentTypeId = (Long) result[0];
			String metadataName = (String) result[1];
			
			metadataNamesByDocumentTypeId.put(documentTypeId, metadataName);
		}
		
		return metadataNamesByDocumentTypeId;
	}
	
	@Override
	public boolean isMimeTypeUsedInDocumentTypes(final Long mimeTypeId){
		Long count = (Long) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String queryString =
					"SELECT COUNT(*) " +
					"FROM DocumentType documentType " +
					"JOIN documentType.allowedMimeTypes mimeType " +
					"WHERE mimeType.id = :mimeTypeId";
				
				return session.createQuery(queryString)
					.setParameter("mimeTypeId", mimeTypeId)
					.uniqueResult();
			}
		});
		return (count > 0);
	}
	
	@Override
	public String getDocumentTypeName(Long id) {
		String query = "SELECT name FROM DocumentType WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<String> nameAsList = getHibernateTemplate().find(query, id);
		String name = Iterables.getOnlyElement(nameAsList);
		return name;
	}

	@Override
	public boolean existDocumentTypeWithName(String name) {
		String query = "SELECT COUNT(*) FROM DocumentType WHERE name = ?";
		int numberOfDocumentTypesWithName = DataAccessUtils.intResult(getHibernateTemplate().find(query, name));
		return numberOfDocumentTypesWithName != 0;
	}

	@Override
	public MetadataCollection getMetadataCollectionDefinition(Long collectionMetadataId) {
		MetadataCollection metadataCollection = (MetadataCollection) getHibernateTemplate().get(MetadataCollection.class, collectionMetadataId);
		return metadataCollection;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, String> getMetadataDefinitionIdAndJrPropertyNameMapOfDocumentType(Long documentTypeId) {
		
		String query =
				"SELECT metadataDefinition.id, metadataDefinition.jrPropertyName " +
				"FROM DocumentType documentType " +
				"JOIN documentType.metadataDefinitions metadataDefinition " +
				"WHERE documentType.id = ? and metadataDefinition.jrPropertyName is not null ";		
		List<Object[]> results = getHibernateTemplate().find(query, documentTypeId);
		
		Map<Long, String> metadataJrPropertyNameByDefinitionId = new HashMap<>();
		if (CollectionUtils.isNotEmpty(results)) {
			for (Object[] result : results) {
				Long definitionId = (Long) result[0];
				String jrPropertyName = (String) result[1];
				metadataJrPropertyNameByDefinitionId.put(definitionId, jrPropertyName);
			}
		}
		return metadataJrPropertyNameByDefinitionId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMetadataDefinitionIdByNameAndDocumentType(String name, Long documentTypeId) {
		
		String query =
				"SELECT metadataDefinition.id " +
				"FROM DocumentType documentType " +
				"JOIN documentType.metadataDefinitions metadataDefinition " +
				"WHERE documentType.id = ? and metadataDefinition.jrPropertyName is not null and metadataDefinition.jrPropertyName = ?";		
		List<Long> result = getHibernateTemplate().find(query, documentTypeId, name);
		
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, String> getMetadataCollectionIdAndJrPropertyNameMapOfDocumentType(Long documentTypeId) {
		
		String query =
				"SELECT metadataCollection.id, metadataCollection.jrPropertyName " +
				"FROM DocumentType documentType " +
				"JOIN documentType.metadataCollections metadataCollection " +
				"WHERE documentType.id = ? and metadataCollection.jrPropertyName is not null";
		List<Object[]> results = getHibernateTemplate().find(query, documentTypeId);
		
		Map<Long, String> metadataJrPropertyNameByDefinitionId = new HashMap<>();
		if (CollectionUtils.isNotEmpty(results)) {
			for (Object[] result : results) {
				Long definitionId = (Long) result[0];
				String jrPropertyName = (String) result[1];
				metadataJrPropertyNameByDefinitionId.put(definitionId, jrPropertyName);
			}
		}
		return metadataJrPropertyNameByDefinitionId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DocumentType getDocumentTypeByName(String documentTypeName) {
		String query = "SELECT documentType FROM DocumentType documentType WHERE documentType.name = ? ";
		return (DocumentType) DataAccessUtils.singleResult(getHibernateTemplate().find(query, documentTypeName));
	}

	@Override
	public Map<Long, MetadataDefinition> getMetadataDefinionsMapByIds(List<Long> metadataDefinitionIds) {
		Map<Long, MetadataDefinition> metadataDefById = new HashMap<>();
		String query =
				"SELECT metadataDef.id, metadataDef " +
				"FROM MetadataDefinition metadataDef " +
				"WHERE metadataDef.id IN " + QueryUtils.joinAsINValues(metadataDefinitionIds) ;
		
		List<Object[]> results = getHibernateTemplate().find(query);
		results.forEach(result -> {
			metadataDefById.put((Long)result[0], (MetadataDefinition)result[1]);
		});
		
		return metadataDefById;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentType> getDocumentTypesByNames(String... documentTypeNames) {
		String query = "SELECT documentType FROM DocumentType documentType WHERE documentType.name IN (:documentTypeNames) ";
		return getHibernateTemplate().findByNamedParam(query, "documentTypeNames", documentTypeNames);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentType> getArchivableDocumentTypes() {
		String query = "SELECT documentType FROM DocumentType documentType WHERE documentType.archivable = ? ";
		return (List<DocumentType>) getHibernateTemplate().find(query, Boolean.TRUE);
	}
	
	@Override
	public void deleteAllValueSelectionFiltersOfNomenclatorMetadataDefinition(Long metadataDefinitionId) {
		String query = "FROM NomenclatorMetadataDefinitionValueSelectionFilter f WHERE f.metadataDefinition.id = ?";
		@SuppressWarnings("unchecked")
		List<WorkflowTransition> filters = getHibernateTemplate().find(query, metadataDefinitionId);
		getHibernateTemplate().deleteAll(filters);
	}
}



