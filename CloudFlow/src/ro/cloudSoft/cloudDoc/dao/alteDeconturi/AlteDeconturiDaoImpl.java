package ro.cloudSoft.cloudDoc.dao.alteDeconturi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuialaAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportFilterModel;
import ro.cloudSoft.common.utils.DateUtils;

public class AlteDeconturiDaoImpl extends HibernateDaoSupport implements AlteDeconturiDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsOfExistingDeconturi() {
		String query = "SELECT DISTINCT EXTRACT(YEAR from decont.dataDecont) FROM AlteDeconturi decont";
		return (List<Integer>) getHibernateTemplate().find(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlteDeconturi> getAllAlteDeconturiByYear(Integer year) {
		String query = "FROM AlteDeconturi decont WHERE EXTRACT (YEAR from decont.dataDecont) = :year ORDER BY decont.dataDecont DESC";
		List<AlteDeconturi> deconturi = getHibernateTemplate().findByNamedParam(query, "year", year);
		return deconturi;
	}

	@Override
	public AlteDeconturi findDecontById(Long decontId) {
		return (AlteDeconturi) getHibernateTemplate().get(AlteDeconturi.class, decontId);	
	}

	@Override
	public Long saveAlteDeconturi(AlteDeconturi alteDeconturi) {
		if (alteDeconturi.getId() == null) {
			return (Long) getHibernateTemplate().save(alteDeconturi);
		} else {
			getHibernateTemplate().saveOrUpdate(alteDeconturi);
			return alteDeconturi.getId();
		}
	}

	@Override
	public void deleteCheltuieli(List<Long> cheltuieliIds) {
		for (Long id : cheltuieliIds) {
			AlteDeconturiCheltuiala alteDeconturiCheltuiala = findCheltuialaById(id);
			getHibernateTemplate().delete(alteDeconturiCheltuiala);
		}
	}

	@Override
	public AlteDeconturiCheltuiala findCheltuialaById(Long cheltuieliId) {
		return getHibernateTemplate().get(AlteDeconturiCheltuiala.class, cheltuieliId);
	}

	@Override
	public void deleteDecontById(Long decontId) {
		AlteDeconturi decont = findDecontById(decontId);
		getHibernateTemplate().delete(decont);
	}

	@Override
	public AlteDeconturiCheltuialaAtasament findAtasamentOfCheltuialaById(Long atasamentId) {
		return (AlteDeconturiCheltuialaAtasament) getHibernateTemplate().get(AlteDeconturiCheltuialaAtasament.class, atasamentId);
	}

	@Override
	public void deleteAtasamenteOfCheltuialaByIds(List<Long> atasamenteIds) {
		for (Long id : atasamenteIds) {
			AlteDeconturiCheltuialaAtasament atasament = findAtasamentOfCheltuialaById(id);
			getHibernateTemplate().delete(atasament);
		}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlteDeconturi> getByDecontCheltuieliAlteDeconturiReportFilterModel(
			DecontCheltuieliAlteDeconturiReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT alteDeconturi");
		query.append(" FROM AlteDeconturi alteDeconturi ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (filter.getTitular() != null) {
			query.append(" AND LOWER(alteDeconturi.titularDecont) = LOWER(?)");
			queryParameters.add(filter.getTitular());
		}
		if (filter.getDataDecont() != null) {
			query.append(" AND alteDeconturi.dataDecont >= ?");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataDecont()));
			query.append(" AND alteDeconturi.dataDecont <= ?");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataDecont()));
		}
		if (filter.getDataDecontDeLa() != null) {
			query.append(" AND alteDeconturi.dataDecont >= ?");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataDecontDeLa()));
		}
		if (filter.getDataDecontPanaLa() != null) {
			query.append(" AND alteDeconturi.dataDecont <= ?");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataDecontPanaLa()));
		}
		
		List<AlteDeconturi> deconturi = new ArrayList<AlteDeconturi>();

		if (queryParameters.size() == 0) {
			deconturi = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			deconturi = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			deconturi = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return deconturi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllDistinctTitulari() {
		String query = "SELECT DISTINCT lower(decont.titularDecont) FROM AlteDeconturi decont";
		return (List<String>) getHibernateTemplate().find(query);
	}

}
