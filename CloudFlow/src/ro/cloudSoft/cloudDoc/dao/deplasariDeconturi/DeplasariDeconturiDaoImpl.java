package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.ValutaForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.ValutaForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbAvansPrimit;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbDiurna;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiRePrezentantArbRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportFilterModel;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.cloudDoc.utils.hibernate.QueryUtils;

public class DeplasariDeconturiDaoImpl extends HibernateDaoSupport implements DeplasariDeconturiDao {

	@Override
	public DeplasareDecont findById(Long deplasareDecontId) {
		return (DeplasareDecont) getHibernateTemplate().get(DeplasareDecont.class, deplasareDecontId);
	}

	@Override
	public Long saveDeplasareDecont(DeplasareDecont deplasareDecont) {
		if (deplasareDecont.getId() == null) {
			return (Long) getHibernateTemplate().save(deplasareDecont);
		} else {
			getHibernateTemplate().saveOrUpdate(deplasareDecont);
			return deplasareDecont.getId();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsOfExistingDeconturi() {
		String query = "SELECT DISTINCT EXTRACT(YEAR from deplasareDecont.dataDecizie) FROM DeplasareDecont deplasareDecont";
		return (List<Integer>) getHibernateTemplate().find(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DeplasareDecont> getAllDeplasariDeconturiByYear(Integer year) {
		String query = "FROM DeplasareDecont deplasareDecont WHERE EXTRACT (YEAR from deplasareDecont.dataDecizie) = :year ORDER BY deplasareDecont.dataDecizie DESC";
		List<DeplasareDecont> deplasariDeconturi = getHibernateTemplate().findByNamedParam(query, "year", year);
		return deplasariDeconturi;
	}

	@Override
	public void deleteById(Long id) {
		DeplasareDecont deplasareDecont = findById(id);
		getHibernateTemplate().delete(deplasareDecont);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CheltuialaArb> getCheltuieliArbByDeplsareDecontId(Long deplasareDecontId) {
		String query = "SELECT cheltuialaArb FROM CheltuialaArb cheltuialaArb WHERE cheltuialaArb.deplasareDecont.id = ?";
		List<CheltuialaArb> cheltuieliArb = getHibernateTemplate().find(query, deplasareDecontId);
		return cheltuieliArb;
	}
	
	@Override
	public CheltuialaArb findCheltuialaArbById(Long id) {
		return getHibernateTemplate().get(CheltuialaArb.class, id);
	}
	
	@Override
	public void removeCheltuielaArb(List<Long> cheltuialiArbIds) {
		for (Long id : cheltuialiArbIds) {
			CheltuialaArb cheltuialaArb = findCheltuialaArbById(id);
			getHibernateTemplate().delete(cheltuialaArb);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CheltuialaReprezentantArb> getCheltuieliReprezentantArbByDeplsareDecontId(Long deplasareDecontId) {
		String query = "SELECT cheltuialaReprezentantArb FROM CheltuialaReprezentantArb cheltuialaReprezentantArb WHERE cheltuialaReprezentantArb.deplasareDecont.id = ?";
		List<CheltuialaReprezentantArb> cheltuieliReprezentantArb = getHibernateTemplate().find(query, deplasareDecontId);
		return cheltuieliReprezentantArb;
	}

	@Override
	public CheltuialaReprezentantArb findCheltuialaReprezentantArbById(Long id) {
		return getHibernateTemplate().get(CheltuialaReprezentantArb.class, id);
	}

	@Override
	public void removeCheltuielaReprezentantArb(List<Long> cheltuialiReprezentantArbIds) {
		for (Long id : cheltuialiReprezentantArbIds) {
			CheltuialaReprezentantArb cheltuialaReprezentantArb = findCheltuialaReprezentantArbById(id);
			getHibernateTemplate().delete(cheltuialaReprezentantArb);
		}
	}

	@Override
	public List<String> getListaNrDeciziiAlocateByReprezentantId(Long reprezentantArbId) {
		String query = "SELECT numarDecizie FROM DeplasareDecont deplasareDecont where deplasareDecont.reprezentantArb.id = ?";
		List<String> result = (List<String>) getHibernateTemplate().find(query,reprezentantArbId);
		return result;
	}

	@Override
	public List<DeplasareDecont> getDeplasariDeconturiByTitularAndDataDecont(String titular,
			Date dataDecont, Date dataDecontDeLa, Date dataDecontPanaLa) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" FROM DeplasareDecont deplasareDecont ");
		String conditionTrue = " 1 = 1 ";
		queryBuilder.append(" WHERE " + conditionTrue);
		
		List<Object> queryParams = new ArrayList<Object>();
		
		if (titular != null) {
			queryBuilder.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParams.add(titular);
		}
		if (dataDecont != null) {
			queryBuilder.append(" AND deplasareDecont.cheltuieliArbDataDecont = ?");
			queryParams.add(dataDecont);
		}
		if (dataDecontDeLa != null) {
			queryBuilder.append(" AND deplasareDecont.cheltuieliArbDataDecont >= ?");
			queryParams.add(dataDecontDeLa);
		}
		if (dataDecontPanaLa != null) {
			queryBuilder.append(" AND deplasareDecont.cheltuieliArbDataDecont <= ?");
			queryParams.add(dataDecontPanaLa);
		}

		List<DeplasareDecont> deplasariDeconturi = QueryUtils.executeQueryWithAppropiateParameters(getHibernateTemplate(), queryBuilder.toString(), queryParams.toArray());
		return deplasariDeconturi;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllDistinctTitulariWithDecont() {
		String query = ""
				+ " SELECT DISTINCT decont.cheltuieliArbTitularDecont "
				+ " FROM DeplasareDecont decont "
				+ " WHERE EXISTS ( "
				+ "		SELECT cheltuieliArb "
				+ " 	FROM CheltuialaArb cheltuieliArb "
				+ " 	WHERE cheltuieliArb.deplasareDecont = decont "
				+ ") OR EXISTS ( "
				+ " 	SELECT cheltuialaReprezentantArb "
				+ " 	FROM CheltuialaReprezentantArb cheltuialaReprezentantArb "
				+ " 	WHERE cheltuialaReprezentantArb.deplasareDecont = decont "
				+ ")";
		return (List<String>) getHibernateTemplate().find(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CheltuieliArbSiRePrezentantArbRowModel> getCheltuieliArbByCheltuieliArbSiReprezentantArbReportFilterModel(
			CheltuieliArbSiReprezentantArbReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT new  " + CheltuieliArbSiRePrezentantArbRowModel.class.getName() + "(cheltuieliArb.tipDocumentJustificativ, "
				+ "SUM(cheltuieliArb.valoareCheltuiala), cheltuieliArb.modalitatePlata )");
		query.append(" FROM DeplasareDecont deplasareDecont ");
		query.append(" JOIN deplasareDecont.cheltuieliArb cheltuieliArb ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (filter.getTitular() != null) {
			query.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParameters.add(filter.getTitular());
		}
		if (filter.getNumarDecizie() != null) {
			query.append(" AND deplasareDecont.numarDecizie = ?");
			queryParameters.add(filter.getNumarDecizie());
		}
		if (filter.getOrganismId() != null) {
			query.append(" AND deplasareDecont.organismId = ?");
			queryParameters.add(filter.getOrganismId());
		}
		if (filter.getComitet() != null) {
			query.append(" AND deplasareDecont.denumireComitet = ?");
			queryParameters.add(filter.getComitet());
		}
		if (filter.getValuta() != null) {
			query.append(" AND cheltuieliArb.valuta = ?");
			queryParameters.add(ValutaForCheltuieliArbEnum.valueOf(filter.getValuta()));
		}
		
		query.append(" GROUP BY  cheltuieliArb.tipDocumentJustificativ, cheltuieliArb.modalitatePlata ");
		
		List<CheltuieliArbSiRePrezentantArbRowModel> rows = new ArrayList<CheltuieliArbSiRePrezentantArbRowModel>();

		if (queryParameters.size() == 0) {
			rows = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			rows = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			rows = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CheltuieliArbSiRePrezentantArbRowModel> getCheltuieliReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(
			CheltuieliArbSiReprezentantArbReportFilterModel filter) {

		StringBuilder query = new StringBuilder();
		
		query.append("SELECT new  " + CheltuieliArbSiRePrezentantArbRowModel.class.getName() + "(cheltuieliReprezentantArb.tipDocumentJustificativ, "
				+ "SUM(cheltuieliReprezentantArb.valoareCheltuiala), cheltuieliReprezentantArb.modalitatePlata )");
		query.append(" FROM DeplasareDecont deplasareDecont ");
		query.append(" JOIN deplasareDecont.cheltuieliReprezentantArb cheltuieliReprezentantArb ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (filter.getTitular() != null) {
			query.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParameters.add(filter.getTitular());
		}
		if (filter.getNumarDecizie() != null) {
			query.append(" AND deplasareDecont.numarDecizie = ?");
			queryParameters.add(filter.getNumarDecizie());
		}
		if (filter.getOrganismId() != null) {
			query.append(" AND deplasareDecont.organismId = ?");
			queryParameters.add(filter.getOrganismId());
		}
		if (filter.getComitet() != null) {
			query.append(" AND deplasareDecont.denumireComitet = ?");
			queryParameters.add(filter.getComitet());
		}
		if (filter.getValuta() != null) {
			query.append(" AND cheltuieliReprezentantArb.valuta = ?");
			queryParameters.add(ValutaForCheltuieliReprezentantArbEnum.valueOf(filter.getValuta()));
		}
		
		query.append(" GROUP BY  cheltuieliReprezentantArb.tipDocumentJustificativ, cheltuieliReprezentantArb.modalitatePlata ");
		
		List<CheltuieliArbSiRePrezentantArbRowModel> rows = new ArrayList<CheltuieliArbSiRePrezentantArbRowModel>();

		if (queryParameters.size() == 0) {
			rows = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			rows = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			rows = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return rows;
	}


	@SuppressWarnings({ "unchecked"})
	@Override
	public CheltuialaReprezentantArbDiurna getDiurnaReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(
			CheltuieliArbSiReprezentantArbReportFilterModel filter) {

		StringBuilder query = new StringBuilder();
		
		query.append("SELECT new  " + CheltuialaReprezentantArbDiurna.class.getName() + "( deplasareDecont.cheltuieliReprezentantArbDiurnaZilnica, "
				+ "deplasareDecont.cheltuieliReprezentantArbNumarZile, deplasareDecont.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata )");
		query.append(" FROM DeplasareDecont deplasareDecont ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (filter.getTitular() != null) {
			query.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParameters.add(filter.getTitular());
		}
		if (filter.getNumarDecizie() != null) {
			query.append(" AND deplasareDecont.numarDecizie = ?");
			queryParameters.add(filter.getNumarDecizie());
		}
		if (filter.getOrganismId() != null) {
			query.append(" AND deplasareDecont.organismId = ?");
			queryParameters.add(filter.getOrganismId());
		}
		if (filter.getComitet() != null) {
			query.append(" AND deplasareDecont.denumireComitet = ?");
			queryParameters.add(filter.getComitet());
		}
		if (filter.getValuta() != null) {
			query.append(" AND deplasareDecont.cheltuieliReprezentantArbDiurnaZilnicaValuta = ?");
			queryParameters.add(filter.getValuta());
		}
		
		List<CheltuialaReprezentantArbDiurna> diurna = new ArrayList<CheltuialaReprezentantArbDiurna>();

		if (queryParameters.size() == 0) {
			diurna = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			diurna = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			diurna = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		if (diurna.size() == 1) {
			return diurna.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CheltuialaReprezentantArbAvansPrimit getAvansReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(
			CheltuieliArbSiReprezentantArbReportFilterModel filter) {

		StringBuilder query = new StringBuilder();
		
		query.append("SELECT  new " + CheltuialaReprezentantArbAvansPrimit.class.getName() + "( deplasareDecont.cheltuieliReprezentantArbAvansPrimitSuma, "
				+ "deplasareDecont.cheltuieliReprezentantArbAvansPrimitCardSauNumerar )");
		query.append(" FROM DeplasareDecont deplasareDecont ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (filter.getTitular() != null) {
			query.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParameters.add(filter.getTitular());
		}
		if (filter.getNumarDecizie() != null) {
			query.append(" AND deplasareDecont.numarDecizie = ?");
			queryParameters.add(filter.getNumarDecizie());
		}
		if (filter.getOrganismId() != null) {
			query.append(" AND deplasareDecont.organismId = ?");
			queryParameters.add(filter.getOrganismId());
		}
		if (filter.getComitet() != null) {
			query.append(" AND deplasareDecont.denumireComitet = ?");
			queryParameters.add(filter.getComitet());
		}
		if (filter.getValuta() != null) {
			query.append(" AND deplasareDecont.cheltuieliReprezentantArbAvansPrimitSumaValuta = ?");
			queryParameters.add(filter.getValuta());
		}
		
		List<CheltuialaReprezentantArbAvansPrimit> avans = new ArrayList<CheltuialaReprezentantArbAvansPrimit>();

		if (queryParameters.size() == 0) {
			avans = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			avans = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			avans = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		if (avans.size() == 1) {
			return avans.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllNumarDecizieByDate(Date startDate, Date endDate) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT deplasareDecont.numarDecizie");
		query.append(" FROM DeplasareDecont deplasareDecont ");
		
		String conditionTrue = " 1 = 1 ";
		query.append(" WHERE " + conditionTrue);
 
		List<Object> queryParameters = new ArrayList<>();
		
		if (startDate != null) {
			query.append(" AND deplasareDecont.cheltuieliArbDataDecont >= ?");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(startDate));
		}
		if (endDate != null) {
			query.append(" AND deplasareDecont.cheltuieliArbDataDecont <= ?");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(endDate));
		}
		
		List<String> decizii = new ArrayList<String>();

		if (queryParameters.size() == 0) {
			decizii = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			decizii = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			decizii = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return decizii;
	}

	@Override
	public List<DeplasareDecont> getAllByDeplasariDeconturiReportFilterModel(DeplasariDeconturiReportFilterModel filter) {
		
		filter.setDataPlecareDeLa(DateUtils.nullHourMinutesSeconds(filter.getDataPlecareDeLa()));
		filter.setDataSosireDeLa(DateUtils.nullHourMinutesSeconds(filter.getDataSosireDeLa()));
		filter.setDataPlecarePanaLa(DateUtils.maximizeHourMinutesSeconds(filter.getDataPlecarePanaLa()));
		filter.setDataSosirePanaLa(DateUtils.maximizeHourMinutesSeconds(filter.getDataSosirePanaLa()));
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" FROM DeplasareDecont deplasareDecont ");
		String conditionTrue = " 1 = 1 ";
		queryBuilder.append(" WHERE " + conditionTrue);
		
		List<Object> queryParams = new ArrayList<Object>();
		
		if (filter.getDenumireInstitutie() != null) {
			queryBuilder.append(" AND deplasareDecont.denumireInstitutie = ?");
			queryParams.add(filter.getDenumireInstitutie());
		}
		if (filter.getReprezentantId() != null) {
			queryBuilder.append(" AND deplasareDecont.reprezentantArb.id = ?");
			queryParams.add(filter.getReprezentantId());
		}
		if (filter.getOrganismId() != null) {
			queryBuilder.append(" AND deplasareDecont.organismId = ?");
			queryParams.add(filter.getOrganismId());
		}
		if (filter.getDenumireComitet() != null) {
			queryBuilder.append(" AND deplasareDecont.denumireComitet = ?");
			queryParams.add(filter.getDenumireComitet());
		}
		if (filter.getOras() != null) {
			queryBuilder.append(" AND deplasareDecont.oras = ?");
			queryParams.add(filter.getOras());
		}
		if (filter.getDataPlecareDeLa() != null) {
			queryBuilder.append(" AND deplasareDecont.dataPlecare >= ?");
			queryParams.add(filter.getDataPlecareDeLa());
		}
		if (filter.getDataPlecarePanaLa() != null) {
			queryBuilder.append(" AND deplasareDecont.dataPlecare <= ?");
			queryParams.add(filter.getDataPlecarePanaLa());
		}
		if (filter.getDataSosireDeLa() != null) {
			queryBuilder.append(" AND deplasareDecont.dataSosire >= ?");
			queryParams.add(filter.getDataSosireDeLa());
		}
		if (filter.getDataSosirePanaLa() != null) {
			queryBuilder.append(" AND deplasareDecont.dataSosire <= ?");
			queryParams.add(filter.getDataSosirePanaLa());
		}

		if (filter.getTitularDecont() != null) {
			queryBuilder.append(" AND deplasareDecont.cheltuieliArbTitularDecont = ?");
			queryParams.add(filter.getTitularDecont());
		}
		
		List<DeplasareDecont> deplasariDeconturi = QueryUtils.executeQueryWithAppropiateParameters(getHibernateTemplate(), queryBuilder.toString(), queryParams.toArray());
		return deplasariDeconturi;
	}

	@Override
	public Set<String> getAllDistinctOrase() {
		String query = "SELECT DISTINCT decont.oras FROM DeplasareDecont decont";
		Set<String> result = new HashSet<>();
		result.addAll((List<String>) getHibernateTemplate().find(query));
		return result;
	}

	@Override
	public Set<String> getAllDistinctDenumiriInstitutii() {
		String query = "SELECT DISTINCT decont.denumireInstitutie FROM DeplasareDecont decont";
		Set<String> result = new HashSet<>();
		result.addAll((List<String>) getHibernateTemplate().find(query));
		return result;
	}

	@Override
	public Set<String> getAllDistinctDenumiriComitete() {
		String query = "SELECT DISTINCT decont.denumireComitet FROM DeplasareDecont decont";
		Set<String> result = new HashSet<>();
		result.addAll((List<String>) getHibernateTemplate().find(query));
		return result;
	}


}
