package ro.cloudSoft.cloudDoc.dao.log;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.log.LogEntry;
import ro.cloudSoft.cloudDoc.domain.log.LogEntrySearchCriteria;
import ro.cloudSoft.common.utils.PagingList;

public class LogDaoImpl extends HibernateDaoSupport implements LogDao, InitializingBean {

	@Override
	public PagingList<LogEntry> searchLog(final int offset, final int pageSize, final LogEntrySearchCriteria searchCriteria) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(LogEntry.class);

		if (searchCriteria.getLevel() != null) {
			detachedCriteria.add(Restrictions.eq("level", searchCriteria.getLevel()));
		}
		
		if (searchCriteria.getTimeStartDate() != null) {
			detachedCriteria.add(Restrictions.ge("time", searchCriteria.getTimeStartDate()));
		}
		if (searchCriteria.getTimeEndDate() != null) {
			detachedCriteria.add(Restrictions.le("time", searchCriteria.getTimeEndDate()));
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getModuleText())) {
			detachedCriteria.add(Restrictions.like("module", searchCriteria.getModuleText(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (StringUtils.isNotBlank(searchCriteria.getOperationText())) {
			detachedCriteria.add(Restrictions.like("operation", searchCriteria.getOperationText(), MatchMode.ANYWHERE).ignoreCase());
		}
		
		if (searchCriteria.getActorType() != null) {
			detachedCriteria.add(Restrictions.eq("actorType", searchCriteria.getActorType()));
		}
		if (StringUtils.isNotBlank(searchCriteria.getActorDisplayNameText())) {
			detachedCriteria.add(Restrictions.like("actorDisplayName", searchCriteria.getActorDisplayNameText(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (searchCriteria.getUserId() != null) {
			detachedCriteria.add(Restrictions.eq("userId", searchCriteria.getUserId()));
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getMessageText())) {
			detachedCriteria.add(Restrictions.like("message", searchCriteria.getMessageText(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (StringUtils.isNotBlank(searchCriteria.getExceptionText())) {
			detachedCriteria.add(Restrictions.like("exception", searchCriteria.getExceptionText(), MatchMode.ANYWHERE).ignoreCase());
		}
		
		final DetachedCriteria detachedCriteriaForCount = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		int foundLogEntriesCount = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForCount.getExecutableCriteria(session)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			}
		});

		final DetachedCriteria detachedCriteriaForListing = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		@SuppressWarnings("unchecked")
		List<LogEntry> foundLogEntriesInRange = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForListing.getExecutableCriteria(session)
					.setFirstResult(offset)
					.setMaxResults(pageSize)
					.addOrder(Order.desc("time"))
					.list();
			}
		});
		
		return new PagingList<LogEntry>(foundLogEntriesCount, offset, foundLogEntriesInRange);
	}
}