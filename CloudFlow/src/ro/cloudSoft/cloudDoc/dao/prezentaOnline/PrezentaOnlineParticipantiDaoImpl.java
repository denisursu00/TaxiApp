package ro.cloudSoft.cloudDoc.dao.prezentaOnline;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.prezentaOnline.PrezentaOnlineParticipanti;

public class PrezentaOnlineParticipantiDaoImpl extends HibernateDaoSupport implements PrezentaOnlineParticipantiDao{

	@Override
	public Long save(PrezentaOnlineParticipanti entity) {
		return (Long) getHibernateTemplate().save(entity);
	}

	@Override
	public void deleteById(Long id) {
		PrezentaOnlineParticipanti participant = findById(id);
		getHibernateTemplate().delete(participant);
	}
	
	@Override
	public PrezentaOnlineParticipanti findById(Long id) {
		return (PrezentaOnlineParticipanti) getHibernateTemplate().get(PrezentaOnlineParticipanti.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PrezentaOnlineParticipanti> getAllByDocument(String documentId, String documentLocationRealName) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM PrezentaOnlineParticipanti participanti ");
		query.append(" WHERE participanti.documentId = ? ");
		query.append(" AND participanti.documentLocationRealName = ? ");
		
		List<PrezentaOnlineParticipanti> results = getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName);
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean existByDocument(String documentId, String documentLocationRealName, Long membruInstitutieId) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM PrezentaOnlineParticipanti participanti ");
		query.append(" WHERE participanti.documentId = ? ");
		query.append(" AND participanti.documentLocationRealName = ? ");
		query.append(" AND participanti.membruInstitutie.id = ? ");
		
		List<PrezentaOnlineParticipanti> results = getHibernateTemplate().find(query.toString(), documentId, documentLocationRealName, membruInstitutieId);
		if (CollectionUtils.isNotEmpty(results)) {
			if (results.size() > 1) {
				throw new IllegalStateException("pe acelasi document de prezenta nu poate exista de mai multe ori acelasi membru acreditat");
			}
			return true;
		}
		return false;
	}
	
}
