package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeSelectionFilter;

public class NomenclatorAttributeDaoImpl extends HibernateDaoSupport implements NomenclatorAttributeDao {

	@Override
	public Long save(NomenclatorAttribute nomenclatorAttribute) {
		if (nomenclatorAttribute.getId() == null) {
			return (Long) getHibernateTemplate().save(nomenclatorAttribute);
		} else {
			getHibernateTemplate().saveOrUpdate(nomenclatorAttribute);
			return nomenclatorAttribute.getId();
		}
	}

	@Override
	public NomenclatorAttribute find(Long id) {
		return (NomenclatorAttribute) getHibernateTemplate().get(NomenclatorAttribute.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NomenclatorAttribute> getAllByNomenclatorId(Long nomenclatorId) {
		String query = "FROM NomenclatorAttribute nomenclatorAttribute WHERE nomenclatorAttribute.nomenclator.id = ? order by nomenclatorAttribute.uiOrder";
		List<NomenclatorAttribute> nomenclatorAttributes = getHibernateTemplate().find(query, nomenclatorId);
		return nomenclatorAttributes;
	}

	@Override
	public void saveAttributes(List<NomenclatorAttribute> nomenclatorAttributes) {
		getHibernateTemplate().saveOrUpdateAll(nomenclatorAttributes);
	}

	@Override
	@SuppressWarnings("unchecked")
	public NomenclatorAttribute findByNomenclatorIdAndKey(Long nomenclatorId, String key) {
		String query = "FROM NomenclatorAttribute nomenclatorAttribute WHERE nomenclatorAttribute.nomenclator.id = ? AND key = ?";
		
		Object[] queryParameters = new Object[] {
			nomenclatorId,
			key
		};

		List<NomenclatorAttribute> nomenclatorAttributes = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(nomenclatorAttributes, null);
	}
	
	@Override
	public void deleteAll(List<NomenclatorAttribute> nomenclatorAttributes) {
		getHibernateTemplate().deleteAll(nomenclatorAttributes);
	}
	
	@Override
	public void deleteByIds(List<Long> attributeIds) {
		List<NomenclatorAttribute> orphanNomenclatorAttributes = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
						" SELECT a " +
						" FROM NomenclatorAttribute a " +
						" WHERE a.id IN (:attributesIds)";
				return session.createQuery(query)
						.setParameterList("attributesIds", attributeIds)
						.list();
			}
		});
		getHibernateTemplate().deleteAll(orphanNomenclatorAttributes);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deleteOrphanAttributes(List<NomenclatorAttribute> nomenclatorAttributes, Nomenclator entity) {
		
		if (entity.getId() == null) {
			return;
		}
		
		if (!nomenclatorAttributes.isEmpty()) {
			final List<Long> attributesIds = new ArrayList<Long>();
			final Long nomenclatorId = entity.getId();
			
			for (NomenclatorAttribute nomenclatorAttribute : nomenclatorAttributes) {
				attributesIds.add(nomenclatorAttribute.getId());
			}
			
			List<NomenclatorAttribute> orphanNomenclatorAttributes = getHibernateTemplate().executeFind(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String query =
							" SELECT na " +
							" FROM NomenclatorAttribute na " +
							" JOIN na.nomenclator n " +
							" WHERE n.id = :nomenclatorId AND na.id NOT IN (:attributesIds)";
					return session.createQuery(query)
							.setParameter("nomenclatorId", nomenclatorId)
							.setParameterList("attributesIds", attributesIds)
							.list();
				}
			});
			
			getHibernateTemplate().deleteAll(orphanNomenclatorAttributes);
		}
	}
	
	@Override
	public void deleteNomenclatorAttributeSelectionFiltersByIds(List<Long> ids) {
		List<NomenclatorAttributeSelectionFilter> nomenclatorAttributeSelectionFilters = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
						" SELECT nasf " +
						" FROM NomenclatorAttributeSelectionFilter nasf " +
						" WHERE nasf.id IN (:ids)";
				return session.createQuery(query)
						.setParameterList("ids", ids)
						.list();
			}
		});	
		
		getHibernateTemplate().deleteAll(nomenclatorAttributeSelectionFilters);
	}
	
	@Override
	public NomenclatorAttributeSelectionFilter findNomenclatorAttributeSelectionFilter(Long id) {
		return (NomenclatorAttributeSelectionFilter) getHibernateTemplate().get(NomenclatorAttributeSelectionFilter.class, id);
	}
}
