package ro.cloudSoft.cloudDoc.dao.registruDocumenteJustificativePlati;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlati;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiAtasament;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;

public class RegistruDocumenteJustificativePlatiDaoImpl extends HibernateDaoSupport implements RegistruDocumenteJustificativePlatiDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruDocumenteJustificativePlati> getAll() {
		String query = "FROM RegistruDocumenteJustificativePlati ORDER BY dataInregistrare DESC";
		List<RegistruDocumenteJustificativePlati> documenteJustificativePlati = getHibernateTemplate().find(query);
        return documenteJustificativePlati;
	}

	@Override
	public RegistruDocumenteJustificativePlati find(Long id) {
		return (RegistruDocumenteJustificativePlati) getHibernateTemplate().get(RegistruDocumenteJustificativePlati.class, id);
	}

	@Override
	public Long save(RegistruDocumenteJustificativePlati documentJustificativPlati) {
		if (documentJustificativPlati.getId() == null) {
			return (Long) getHibernateTemplate().save(documentJustificativPlati);
		} else {
			getHibernateTemplate().saveOrUpdate(documentJustificativPlati);
			return documentJustificativPlati.getId();
		}
	}

	@Override
	public void delete(RegistruDocumenteJustificativePlati documentJustificativPlati) {
		getHibernateTemplate().delete(documentJustificativPlati);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsWithInregistrariDocumenteJustificativePlati() {
		String query = "SELECT DISTINCT EXTRACT(YEAR from registru.dataInregistrare) FROM RegistruDocumenteJustificativePlati registru";
		List<Integer> years = (List<Integer>) getHibernateTemplate().find(query);
		return years;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruDocumenteJustificativePlati> getAllByYear(Integer year) {
		String query = "FROM RegistruDocumenteJustificativePlati registru WHERE EXTRACT (YEAR from registru.dataInregistrare) = :year ORDER BY registru.dataInregistrare DESC";
		List<RegistruDocumenteJustificativePlati> registruDocumenteJustificativePlati = getHibernateTemplate().findByNamedParam(query, "year", year);
		return registruDocumenteJustificativePlati;
	}

	@Override
	public RegistruDocumenteJustificativePlatiAtasament findAtasamentOfRegistruIesiriById(Long atasamentId) {
		return (RegistruDocumenteJustificativePlatiAtasament) getHibernateTemplate().get(RegistruDocumenteJustificativePlatiAtasament.class, atasamentId);
	}

	@Override
	public void deleteAtasamenteOfRegistruDocumenteJustificativePlatiByIds(List<Long> atasamenteIdsToDelete) {
		for (Long id : atasamenteIdsToDelete) {
			RegistruDocumenteJustificativePlatiAtasament atasament = findAtasamentOfRegistruIesiriById(id);
			getHibernateTemplate().delete(atasament);
		}
	}

	@Override
	public Long saveAtasamentOfRegistruDocumentJustificativPlati(RegistruDocumenteJustificativePlatiAtasament atasament) {
		if (atasament.getId() == null) {
			return (Long) getHibernateTemplate().save(atasament);
		} else {
			getHibernateTemplate().saveOrUpdate(atasament);
			return atasament.getId();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getAttachmentIds(Long registruDocumenteJustificativeId) {
		String query = "SELECT a.id FROM RegistruDocumenteJustificativePlatiAtasament a JOIN a.registruDocumenteJustificativePlati r WHERE r.id = ?";
		return (List<Long>) getHibernateTemplate().find(query, registruDocumenteJustificativeId);
	}

	@Override
	public RegistruDocumenteJustificativePlati getByNrInregistrare(String nrInregistrare) {
		String query = "FROM RegistruDocumenteJustificativePlati WHERE numarInregistrare = '" + nrInregistrare + "'";
		List<RegistruDocumenteJustificativePlati> registreDocumenteJustificativePlati = getHibernateTemplate().find(query);
		
		if (CollectionUtils.isEmpty(registreDocumenteJustificativePlati)) {
			return null;
		} else {
			return registreDocumenteJustificativePlati.get(0);
		}
	}
}
