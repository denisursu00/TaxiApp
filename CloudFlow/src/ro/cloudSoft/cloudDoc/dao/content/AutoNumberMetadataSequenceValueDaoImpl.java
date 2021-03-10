package ro.cloudSoft.cloudDoc.dao.content;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataSequenceValue;

import com.google.common.collect.Sets;

/**
 * 
 */
public class AutoNumberMetadataSequenceValueDaoImpl extends HibernateDaoSupport implements AutoNumberMetadataSequenceValueDao, InitializingBean {
	
	@Override
	public Set<Long> getDefinitionIdsForExistingSequences(final Collection<Long> autoNumberMetadataDefinitionIds) {
		
		if (autoNumberMetadataDefinitionIds.isEmpty()) {
			return Collections.emptySet();
		}
		
		@SuppressWarnings("unchecked")
		List<Long> definitionIdsForExistingSequences = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT autoNumberMetadataDefinitionId " +
					"FROM AutoNumberMetadataSequenceValue " +
					"WHERE autoNumberMetadataDefinitionId IN (:autoNumberMetadataDefinitionIds)";
				return session.createQuery(query)
					.setParameterList("autoNumberMetadataDefinitionIds", autoNumberMetadataDefinitionIds)
					.list();
			}
		});
		return Sets.newHashSet(definitionIdsForExistingSequences);
	}

	@Override
	public void create(AutoNumberMetadataSequenceValue autoNumberMetadataSequenceValue) {
		getHibernateTemplate().save(autoNumberMetadataSequenceValue);
	}

	@Override
	public int getExistingSequenceValue(Long autoNumberMetadataDefinitionId) {
		String queryForExistingValue =
			"SELECT sequenceValue " +
			"FROM AutoNumberMetadataSequenceValue " +
			"WHERE autoNumberMetadataDefinitionId = ?";
		int existingSequenceValue = DataAccessUtils.intResult(getHibernateTemplate().find(queryForExistingValue, autoNumberMetadataDefinitionId));
		return existingSequenceValue;
	}

	@Override
	public boolean updateSequenceValue(Long autoNumberMetadataDefinitionId, int requiredOldSequenceValue, int newSequenceValue) {
		
		String queryForUpdateValue =
			"UPDATE AutoNumberMetadataSequenceValue " +
			"SET sequenceValue = ? " +
			"WHERE autoNumberMetadataDefinitionId = ? " +
			"AND sequenceValue = ?";
		Object[] parametersForUpdateValue = {
			newSequenceValue,
			autoNumberMetadataDefinitionId,
			requiredOldSequenceValue
		};
		
		int updatedRows = getHibernateTemplate().bulkUpdate(queryForUpdateValue, parametersForUpdateValue);
		return (updatedRows == 1);
	}
}