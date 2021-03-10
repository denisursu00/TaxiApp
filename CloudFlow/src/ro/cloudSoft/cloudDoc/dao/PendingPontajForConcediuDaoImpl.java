package ro.cloudSoft.cloudDoc.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.PendingPontajForConcediu;

import com.google.common.collect.Sets;

/**
 * 
 */
public class PendingPontajForConcediuDaoImpl extends HibernateDaoSupport implements PendingPontajForConcediuDao, InitializingBean {

	@Override
	public Set<PendingPontajForConcediu> getAll() {
		@SuppressWarnings("unchecked")
		List<PendingPontajForConcediu> allAsList = getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(PendingPontajForConcediu.class));
		return Sets.newHashSet(allAsList);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteAllForDay(Date day) {
		Date dayNormalized = DateUtils.truncate(day, Calendar.DAY_OF_MONTH);
		String query = "DELETE FROM PendingPontajForConcediu WHERE id.day = ?";
		getHibernateTemplate().bulkUpdate(query, dayNormalized);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteAllInIntervalForSolicitant(String solicitantEmail, Date dataInceput, Date dataSfarsit) {
		
		Date dataInceputNormalized = DateUtils.truncate(dataInceput, Calendar.DAY_OF_MONTH);
		Date dataSfarsitNormalized = DateUtils.truncate(dataSfarsit, Calendar.DAY_OF_MONTH);
		
		String query =
			"DELETE FROM PendingPontajForConcediu " +
			"WHERE LOWER(id.solicitantEmail) = LOWER(?) " +
			"AND (? <= id.day AND id.day <= ?)";
		Object[] queryParameters = { solicitantEmail, dataInceputNormalized, dataSfarsitNormalized };
		
		getHibernateTemplate().bulkUpdate(query, queryParameters);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(Collection<PendingPontajForConcediu> pendingPontajeForConcediiToDelete) {
		getHibernateTemplate().deleteAll(pendingPontajeForConcediiToDelete);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void save(Collection<PendingPontajForConcediu> pendingPontajeForConcediiToSave) {
		for (PendingPontajForConcediu pendingPontajForConcediuToSave : pendingPontajeForConcediiToSave) {
			getHibernateTemplate().merge(pendingPontajForConcediuToSave);
		}
	}
}