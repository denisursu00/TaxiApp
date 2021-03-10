package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;

public class CheltuieliArbDaoImpl extends HibernateDaoSupport implements CheltuieliArbDao {

	@Override
	public CheltuialaArb findById(Long cheltuialaArbId) {
		return (CheltuialaArb) getHibernateTemplate().get(CheltuialaArb.class, cheltuialaArbId);
	}

	@Override
	public void deleteCheltuieliArb(List<Long> cheltuieliArbIds) {
		for (Long id : cheltuieliArbIds) {
			CheltuialaArb cheltuialaArb = findById(id);
			getHibernateTemplate().delete(cheltuialaArb);
		}
	}
}
