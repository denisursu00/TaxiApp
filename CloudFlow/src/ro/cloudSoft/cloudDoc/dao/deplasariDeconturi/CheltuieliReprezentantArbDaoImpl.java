package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;

public class CheltuieliReprezentantArbDaoImpl extends HibernateDaoSupport implements CheltuieliReprezentantArbDao {

	@Override
	public CheltuialaReprezentantArb findById(Long cheltuialaReprezentantArbId) {
		return (CheltuialaReprezentantArb) getHibernateTemplate().get(CheltuialaReprezentantArb.class, cheltuialaReprezentantArbId);
	}

	@Override
	public void deleteCheltuieliReprezentatiArb(List<Long> cheltuieliReprezentantiArbIds) {
		for (Long id : cheltuieliReprezentantiArbIds) {
			CheltuialaReprezentantArb cheltuialaReprezentantArb = findById(id);
			getHibernateTemplate().delete(cheltuialaReprezentantArb);
		}
	}
}
