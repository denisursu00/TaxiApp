package ro.cloudSoft.cloudDoc.dao.cursValutar;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.cursValutar.CursValutar;

public class CursValutarDaoImpl extends HibernateDaoSupport implements CursValutarDao {

	@Override
	public Long save(CursValutar cursValutar) {
		if (cursValutar.getId() == null) {
			return (Long) getHibernateTemplate().save(cursValutar);
		} else {
			getHibernateTemplate().saveOrUpdate(cursValutar);
			return cursValutar.getId();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public CursValutar getCursValutarCurent() {
		String query = " "
				+ " SELECT cursValutar FROM CursValutar cursValutar "
				+ " WHERE cursValutar.data = "
				+ " ( "
				+ " 	SELECT MAX(cursValutar.data) FROM CursValutar cursValutar "
				+ " )";
		List<CursValutar> cursuriValutare = getHibernateTemplate().find(query);
		return DataAccessUtils.singleResult(cursuriValutare);
	}

}
