package ro.cloudSoft.cloudDoc.dao.prezentaOnline;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineFinalizata;

public class PrezentaOnlineFinalizataDaoImpl  extends HibernateDaoSupport implements PrezentaOnlineFinalizataDao{

	@Override
	public Long save(PrezentaOnlineFinalizata entity) {
		if (entity.getId() == null) {
			return (Long) getHibernateTemplate().save(entity);
		} else {
			getHibernateTemplate().saveOrUpdate(entity);			
			return entity.getId();
		}
	}

	@Override
	public void deleteByDocument(String documentId, String documentLocationRealName) {
		StringBuilder query = new StringBuilder();
		query.append(" DELETE FROM PrezentaOnlineFinalizata prezenta ");
		query.append(" WHERE prezenta.documentId = ? ");
		query.append(" AND prezenta.documentLocationRealName = ? ");
		
		getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existByDocument(String documentId, String documentLocationRealName) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM PrezentaOnlineFinalizata prezenta ");
		query.append(" WHERE prezenta.documentId = ? ");
		query.append(" AND prezenta.documentLocationRealName = ? ");
		
		List<PrezentaOnlineFinalizata> results = getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName);
		if (CollectionUtils.isNotEmpty(results)) {
			if (results.size() > 1) {
				throw new IllegalStateException("nu pot exista mai multe prezente pentru acelasi document de Prezenta Comisie/GL");
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isImportedByDocument(String documentId, String documentLocationRealName) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM PrezentaOnlineFinalizata prezenta ");
		query.append(" WHERE prezenta.documentId = ? ");
		query.append(" AND prezenta.documentLocationRealName = ? ");
		query.append(" AND prezenta.hasImported = true ");
		
		List<PrezentaOnlineFinalizata> results = getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName);
		if (CollectionUtils.isNotEmpty(results)) {
			if (results.size() > 1) {
				throw new IllegalStateException("nu pot exista mai multe prezente pentru acelasi document de Prezenta Comisie/GL");
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PrezentaOnlineFinalizata getByDocument(String documentId, String documentLocationRealName) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM PrezentaOnlineFinalizata prezenta ");
		query.append(" WHERE prezenta.documentId = ? ");
		query.append(" AND prezenta.documentLocationRealName = ? ");
		
		List<PrezentaOnlineFinalizata> results = getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName);
		if (CollectionUtils.isNotEmpty(results)) {
			if (results.size() > 1) {
				throw new IllegalStateException("nu pot exista mai multe prezente pentru acelasi document de Prezenta Comisie/GL");
			}
			return results.get(0);
		}
		return new PrezentaOnlineFinalizata();
	}

}
