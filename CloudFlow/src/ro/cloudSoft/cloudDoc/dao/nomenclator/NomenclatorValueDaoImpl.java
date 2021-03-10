package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValueSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorMultipleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortedAttribute;
import ro.cloudSoft.common.utils.PagingList;

public class NomenclatorValueDaoImpl extends HibernateDaoSupport implements NomenclatorValueDao {

	@Override
	public Long save(NomenclatorValue nomenclatorValue) {
		if (nomenclatorValue.getId() == null) {
			return (Long) getHibernateTemplate().save(nomenclatorValue);
		} else {
			getHibernateTemplate().saveOrUpdate(nomenclatorValue);
			return nomenclatorValue.getId();
		}
	}

	@Override
	public NomenclatorValue find(Long id) {
		return (NomenclatorValue) getHibernateTemplate().get(NomenclatorValue.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> getAll(Long nomenclatorId) {
		String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.nomenclator.id = ? and nomenclatorValue.deleted = ? ";
		Object[] params = new Object[2];
		params[0] = nomenclatorId;
		params[1] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> getAllByNomenclatorCode(String nomenclatorCode) {
		String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.nomenclator.code = ? and nomenclatorValue.deleted = ? ";
		Object[] params = new Object[2];
		params[0] = nomenclatorCode;
		params[1] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public PagingList<NomenclatorValue> searchValues(final int offset, final int pageSize, NomenclatorValueSearchCriteria searchCriteria) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NomenclatorValue.class);
		detachedCriteria.add(Restrictions.eq("deleted", false));		
		if (searchCriteria.getNomenclatorId() != null) {
			detachedCriteria.add(Restrictions.eq("nomenclator.id", searchCriteria.getNomenclatorId()));
		}
		if (CollectionUtils.isNotEmpty(searchCriteria.getValueIds())) {
			detachedCriteria.add(Restrictions.in("id", searchCriteria.getValueIds()));
		}
		if (CollectionUtils.isNotEmpty(searchCriteria.getFilterValues())) {
			for (NomenclatorFilter filter : searchCriteria.getFilterValues()) {
				if (filter instanceof NomenclatorSimpleFilter) {
					if (((NomenclatorSimpleFilter)filter).getValue() == null) {
						detachedCriteria.add(Restrictions.isNull(filter.getAttributeKey()));
					} else {
					detachedCriteria.add(Restrictions.ilike(filter.getAttributeKey(), ((NomenclatorSimpleFilter)filter).getValue(), filter.getMatchMode()));
					}
				} else if (filter instanceof NomenclatorMultipleFilter) {
					Disjunction objDisjunction = Restrictions.disjunction();
					NomenclatorMultipleFilter multipleFilter = (NomenclatorMultipleFilter) filter;
					for (String value : multipleFilter.getValues()) {
						if (value == null) {
							objDisjunction.add(Restrictions.isNull(filter.getAttributeKey()));
						} else {
							objDisjunction.add(Restrictions.ilike(filter.getAttributeKey(), value, filter.getMatchMode()));
						}
					}
					detachedCriteria.add(objDisjunction);
				}
			}
		}
		
		final DetachedCriteria detachedCriteriaForCount = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		int foundNomenclatorValuesCount = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForCount.getExecutableCriteria(session)
					.setProjection(Projections.rowCount())
					.uniqueResult();
			}
		});
		
		final DetachedCriteria detachedCriteriaForListing = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		List<NomenclatorValue> foundNomenclatorValuesInRange = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForListing.getExecutableCriteria(session)
					.list();
			}
		});
		
		return new PagingList<NomenclatorValue>(foundNomenclatorValuesCount, offset, foundNomenclatorValuesInRange);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> getAllByIds(List<Long> ids) {
		String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.id IN (:ids)";
		List<NomenclatorValue> nomenclatorValues = getHibernateTemplate().findByNamedParam(query, "ids", ids);
		return nomenclatorValues;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> findByNomenclatorId(Long nomenclatorId) {
		String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.nomenclator.id = ? and nomenclatorValue.deleted = ?";
		Object[] params = new Object[2];
		params[0] = nomenclatorId;
		params[1] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public NomenclatorValue getRegistruFacturiCuUltimulNumarDeInregistrareByTipDocumentAndCurrentYear(final String tipDocumentId, final String year) {
		
		List<NomenclatorValue> registruFacturiCuUltimulNumarInregistrare = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.attribute1 = :tipDocumentId and nomenclatorValue.attribute4 = :year";
				
				return session.createQuery(query)
					.setLockMode("nomenclatorValue", LockMode.UPGRADE)
					.setParameter("tipDocumentId", tipDocumentId)
					.setParameter("year", year)
					.list();
			}
		});
		
		if (CollectionUtils.isEmpty(registruFacturiCuUltimulNumarInregistrare)) {
			return null;
		}
		return registruFacturiCuUltimulNumarInregistrare.get(0);
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public NomenclatorValue getRegistruIntrariIesiriCuUltimulNumarDeInregistrareByTipRegistruAndCurrentYear(final String tipRegistru, final String year) {
		
		List<NomenclatorValue> registruIntrariIesiriCuUltimulNumarInregistrare = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.attribute1 = :tipRegistru and nomenclatorValue.attribute3 = :year";
				
				return session.createQuery(query)
					.setLockMode("nomenclatorValue", LockMode.UPGRADE)
					.setParameter("tipRegistru", tipRegistru)
					.setParameter("year", year)
					.list();
			}
		});
		
		if (CollectionUtils.isEmpty(registruIntrariIesiriCuUltimulNumarInregistrare)) {
			return null;
		}
		return (NomenclatorValue) DataAccessUtils.singleResult(registruIntrariIesiriCuUltimulNumarInregistrare);
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public NomenclatorValue getNomenclatorValueCuUltimulNumarInregistrareByNomenclatorIdAndCurrentYear(final Long nomenclatorId, final String year) {
		
		List<NomenclatorValue> nomenclatorValues = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.nomenclator.id = :nomenclatorId AND nomenclatorValue.attribute2 = :year";
				
				return session.createQuery(query)
					.setLockMode("nomenclatorValue", LockMode.UPGRADE)
					.setParameter("nomenclatorId", nomenclatorId)
					.setParameter("year", year)
					.list();
			}
		});
		
		if (CollectionUtils.isEmpty(nomenclatorValues)) {
			return null;
		}
		return (NomenclatorValue) DataAccessUtils.singleResult(nomenclatorValues);
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public NomenclatorValue getDetaliiNumarDeplasariBugetateNomenclatorValueByOrganismIdAndCurrentYear(Long nomenclatorId, String organismId,
			String year) {

		List<NomenclatorValue> nomenclatorValues = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query = "FROM NomenclatorValue nomenclatorValue WHERE nomenclatorValue.nomenclator.id = :nomenclatorId AND "
						+ " nomenclatorValue." + NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM + " = :organismId AND "
						+ " EXTRACT(YEAR from TO_DATE(nomenclatorValue." + NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_DE_LA_DATA + ", 'YYYY.MM.DD')) = :year";
				
				return session.createQuery(query)
					.setLockMode("nomenclatorValue", LockMode.UPGRADE)
					.setParameter("nomenclatorId", nomenclatorId)
					.setParameter("organismId", organismId)
					.setParameter("year", Integer.valueOf(year))
					.list();
			}
		});
		
		if (CollectionUtils.isEmpty(nomenclatorValues)) {
			return null;
		}
		return (NomenclatorValue) DataAccessUtils.singleResult(nomenclatorValues);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> findByNomenclatorIdAndAttribute(Long nomenclatorId, String attributeKey, String attributeValue) {
		String query = "FROM NomenclatorValue nv WHERE nv.nomenclator.id = ? and nv." + attributeKey + " = ? and nv.deleted = ?";
		Object[] params = new Object[3];
		params[0] = nomenclatorId;
		params[1] = attributeValue;
		params[2] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> findByNomenclatorCodeAndAttribute(String nomenclatorCode, String attributeKey, String attributeValue) {
		String query = "FROM NomenclatorValue nv WHERE nv.nomenclator.code = ? and nv." + attributeKey + " = ? and nv.deleted = ?";
		Object[] params = new Object[3];
		params[0] = nomenclatorCode;
		params[1] = attributeValue;
		params[2] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> findByNomenclatorCodeAndAttributes(String nomenclatorCode, String attributeKey1,
			String attributeValue1, String attributeKey2, String attributeValue2) {
		String query = "FROM NomenclatorValue nv WHERE nv.nomenclator.code = ? and nv." + attributeKey1 + " = ? and nv." + attributeKey2 + " = ? and nv.deleted = ?";
		Object[] params = new Object[4];
		params[0] = nomenclatorCode;
		params[1] = attributeValue1;
		params[2] = attributeValue2;
		params[3] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorValue> findByNomenclatorCode(String nomenclatorCode) {
		String query = "FROM NomenclatorValue nv WHERE nv.nomenclator.code = ? and nv.deleted = ?";
		Object[] params = new Object[2];
		params[0] = nomenclatorCode;
		params[1] = Boolean.FALSE;
		return getHibernateTemplate().find(query, params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NomenclatorValue> getNomenclatorValues(GetNomenclatorValuesRequestModel requestModel) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NomenclatorValue.class);
		detachedCriteria.add(Restrictions.eq("deleted", false));		
		if (requestModel.getNomenclatorId() != null) {
			detachedCriteria.add(Restrictions.eq("nomenclator.id", requestModel.getNomenclatorId()));
		}

		if (CollectionUtils.isNotEmpty(requestModel.getFilters())) {
			for (NomenclatorFilter filter : requestModel.getFilters()) {
				if (filter instanceof NomenclatorSimpleFilter) {
					detachedCriteria.add(Restrictions.ilike(filter.getAttributeKey(), ((NomenclatorSimpleFilter)filter).getValue(), MatchMode.EXACT));
				} else if (filter instanceof NomenclatorMultipleFilter) {
					Disjunction objDisjunction = Restrictions.disjunction();
					NomenclatorMultipleFilter multipleFilter = (NomenclatorMultipleFilter) filter;
					for (String value : multipleFilter.getValues()) {
						objDisjunction.add(Restrictions.ilike(filter.getAttributeKey(), value, MatchMode.EXACT));
					}
					detachedCriteria.add(objDisjunction);
				}
			}
		}
			
		final DetachedCriteria detachedCriteriaForListing = (DetachedCriteria) SerializationUtils.clone(detachedCriteria);
		@SuppressWarnings("deprecation")
		List<NomenclatorValue> foundNomenclatorValues = getHibernateTemplate().executeFind(new HibernateCallback() {			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return detachedCriteriaForListing.getExecutableCriteria(session).list();
			}
		});
		
		return foundNomenclatorValues;
	}
	
	@Override
	public boolean nomenclatorHasValuesByNomenclatorId(Long nomenclatorId) {
		String query = "SELECT COUNT(*) FROM NomenclatorValue nv "
				+ " WHERE nv.nomenclator.id = ?";
		List  find = getHibernateTemplate().find(query, nomenclatorId);
		long countResult = (long) find.get(0);
		return countResult > 0;
	}	
	
	@Override
	public List<NomenclatorValue> getDistinctNomenclatorValuesByNomenclatorAndOtherNomenclatorAndAttributeThatUseIt(Long nomenclatorId, Long nomenclatorIdThatUseIt, String nomenclatorAttributeKeyThatUseIt) {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT nv FROM NomenclatorValue nv, NomenclatorValue nvThatUseIt ");
		query.append(" WHERE nv.nomenclator.id = ? AND nv.deleted = ? ");
		query.append("    AND nvThatUseIt.nomenclator.id = ? AND nvThatUseIt.deleted = ? ");
		query.append("    AND cast(nv.id as text) = nvThatUseIt." + nomenclatorAttributeKeyThatUseIt);
		
		Object[] params = new Object[4];
		params[0] = nomenclatorId;
		params[1] = Boolean.FALSE;
		params[2] = nomenclatorIdThatUseIt;
		params[3] = Boolean.FALSE;
		
		return getHibernateTemplate().find(query.toString(), params);
	}
	
	@Override
	public Long countInstitutiiMembreARBNeradiateWithoutARB() {
			
		List<NomenclatorValue> values = findByNomenclatorCodeAndAttribute(NomenclatorConstants.TIP_INSTITUTII_NOMENCLATOR_CODE, NomenclatorConstants.TIP_INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB);
		if (CollectionUtils.isEmpty(values)) {
			throw new java.lang.RuntimeException("valori pentru tip institutie membru ARB empty");
		}
		Long idTipInstitutieMembruArb = values.get(0).getId();
		
		String query = ""
				+ "	SELECT count(id)"
				+ "	FROM NomenclatorValue nomValueInst"
				+ "	WHERE nomValueInst.nomenclator.code = ? "
				+ "		AND nomValueInst." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE + " = ? "
				+ "		AND nomValueInst." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT + " = ? "
				+ "		AND nomValueInst.deleted = ? ";
		
		List result = getHibernateTemplate().find(query, NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE, String.valueOf(idTipInstitutieMembruArb), NomenclatorConstants.NOMENCLATOR_ATTR_BOOLEAN_VALUE_AS_FALSE, Boolean.FALSE);
		
		Long count = (Long) (result).get(0);
		Long countFinal = count;
		if (count.intValue() >= 1) {
			// Excludem din count pe ARB care si el e trecut ca si Membru ARB.
			countFinal = Long.valueOf(count.intValue() - 1); 
		}
		return countFinal;
	}
}
