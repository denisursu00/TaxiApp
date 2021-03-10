package ro.cloudSoft.cloudDoc.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntry;
import ro.cloudSoft.cloudDoc.domain.audit.AuditSearchCriteria;
import ro.cloudSoft.common.utils.PagingList;

public class HibernateAuditEntryDao extends HibernateDaoSupport implements AuditEntryDao {

	@Override
	public void save(AuditEntry auditEntry) {
		getHibernateTemplate().save(auditEntry);
	}
	
	@Override
	public PagingList<AuditEntry> searchAuditEntries(final int offset, final int pageSize, AuditSearchCriteria searchCriteria) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AuditEntry.class);
		
		if (searchCriteria.getStartDate() != null) {
			detachedCriteria.add(Restrictions.ge("dateTime", searchCriteria.getStartDate()));
		}
		if (searchCriteria.getEndDate() != null) {
			detachedCriteria.add(Restrictions.le("dateTime", searchCriteria.getEndDate()));
		}
		
		if (searchCriteria.getUserId() != null) {
			detachedCriteria.add(Restrictions.eq("userId", searchCriteria.getUserId()));
		}
		
		if (searchCriteria.getEntityType() != null) {
			detachedCriteria.add(Restrictions.eq("entityType", searchCriteria.getEntityType()));
		}
		
		if (searchCriteria.getEntityIdentifierTextFragment() != null) {
			detachedCriteria.add(Restrictions.like("entityIdentifier", searchCriteria.getEntityIdentifierTextFragment(), MatchMode.ANYWHERE));
		}
		if (searchCriteria.getEntityDisplayNameTextFragment() != null) {
			detachedCriteria.add(Restrictions.like("entityDisplayName", searchCriteria.getEntityDisplayNameTextFragment(), MatchMode.ANYWHERE).ignoreCase());
		}

		if (searchCriteria.getOperation() != null) {
			detachedCriteria.add(Restrictions.eq("operation", searchCriteria.getOperation()));
		}
		
		final DetachedCriteria detachedCriteriaForCount = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		int foundAuditEntriesCount = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForCount.getExecutableCriteria(session)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			}
		});

		final DetachedCriteria detachedCriteriaForListing = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		@SuppressWarnings("unchecked")
		List<AuditEntry> foundAuditEntriesInRange = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForListing.getExecutableCriteria(session)
					.setFirstResult(offset)
					.setMaxResults(pageSize)
					.addOrder(Order.desc("dateTime"))
					.list();
			}
		});
		
		return new PagingList<AuditEntry>(foundAuditEntriesCount, offset, foundAuditEntriesInRange);
	}
}