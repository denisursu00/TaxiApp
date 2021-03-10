package ro.cloudSoft.cloudDoc.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.Concediu;

import com.google.common.collect.Iterables;

/**
 */
public class ConcediuDaoImpl extends HibernateDaoSupport implements ConcediuDao, InitializingBean {

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSolicitantsIdsByPeriod(final Date dataInceput,	final Date dataSfarsit) {
	
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			String query = 
				"SELECT DISTINCT solicitantId " + 
				"FROM Concediu concediu " + 
				"WHERE (" +
				"	(:dataInceput <= concediu.dataInceput AND concediu.dataInceput <= :dataSfarsit)" +
				"	 OR " + 
				"	(:dataInceput <= concediu.dataSfarsit AND concediu.dataSfarsit <= :dataSfarsit)" +
				"	 OR " +
				"	(concediu.dataInceput <= :dataInceput AND :dataSfarsit <= concediu.dataSfarsit)" +
				")";
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery(query)
					.setParameter("dataInceput", dataInceput)
					.setParameter("dataSfarsit", dataSfarsit)
					.list();
			}
		});

	}

	@Override
	public void save(Concediu concediu) {
		getHibernateTemplate().saveOrUpdate(concediu);
	}
	
	@Override 
	public void deleteByDocumentId(String documentId) {
		
		String query =
			"DELETE " +
			"FROM Concediu " +
			"WHERE documentId = ?";
		
		getHibernateTemplate().bulkUpdate(query, documentId);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveAll(Collection<Concediu> concedii) {
		getHibernateTemplate().saveOrUpdateAll(concedii);
	}
	
	@Override
	public Concediu getForDocument(String documentId) {
		String query = "FROM Concediu WHERE documentId = ?";
		@SuppressWarnings("unchecked")
		List<Concediu> concediuAsList = getHibernateTemplate().find(query, documentId);
		return Iterables.getOnlyElement(concediuAsList, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getSolicitantIdsForConcediiWithCerereAprobataThatContainDay(Date day) {
		
		Date dayNormalized = DateUtils.truncate(day, Calendar.DAY_OF_MONTH);
		
		String query =
			"SELECT solicitantId " +
			"FROM Concediu " +
			"WHERE cerereAprobata = ? " +
			"AND (dataInceput <= ? AND ? <= dataSfarsit)";
		Object[] queryParameters = { true, dayNormalized, dayNormalized };
		return getHibernateTemplate().find(query, queryParameters);
	}
}