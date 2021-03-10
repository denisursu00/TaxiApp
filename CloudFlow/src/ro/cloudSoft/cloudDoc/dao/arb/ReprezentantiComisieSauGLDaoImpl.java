package ro.cloudSoft.cloudDoc.dao.arb;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.arb.DiplomaMembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.common.utils.DateUtils;

public class ReprezentantiComisieSauGLDaoImpl extends HibernateDaoSupport implements ReprezentantiComisieSauGLDao {

	private static final int TERMEN_EXPIRARE_MANDAT_NR_ZILE = 60;
	@Override
	public ReprezentantiComisieSauGL getById(Long id) {
		return (ReprezentantiComisieSauGL) getHibernateTemplate().get(ReprezentantiComisieSauGL.class, id);
	}

	@Override
	public Long save(ReprezentantiComisieSauGL entity) {
		if (entity.getId() == null) {
			return (Long) getHibernateTemplate().save(entity);
		} else {
			getHibernateTemplate().saveOrUpdate(entity);			
			return entity.getId();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ReprezentantiComisieSauGL getByComisieSauGLId(Long comisieSauGLId) {
		
		StringBuilder query = new StringBuilder();
		query.append(" FROM ReprezentantiComisieSauGL r ");
		query.append(" WHERE r.comisieSauGL.id = ? ");
		
		List<ReprezentantiComisieSauGL> results = getHibernateTemplate().find(query.toString(), comisieSauGLId);
		if (CollectionUtils.isNotEmpty(results)) {
			if (results.size() > 1) {
				throw new IllegalStateException("o comisie/gl are mai mult de o inregistrare pentru reprezentanti");
			}
			return results.get(0);
		}
		return null;
	}
	
	@Override
	public MembruReprezentantiComisieSauGL getMembruById(Long id) {		
		return (MembruReprezentantiComisieSauGL) getHibernateTemplate().get(MembruReprezentantiComisieSauGL.class, id);
	}
	
	@Override
	public void deleteMembri(List<Long> membriIds) {
		for (Long id : membriIds) {
			MembruReprezentantiComisieSauGL membru = getMembruById(id);
			getHibernateTemplate().delete(membru);
		}
	}
	
	@Override
	public DiplomaMembruReprezentantiComisieSauGL getDiplomaMembruById(Long id) {
		return (DiplomaMembruReprezentantiComisieSauGL) getHibernateTemplate().get(DiplomaMembruReprezentantiComisieSauGL.class, id);
	}
	
	@Override
	public void deleteDiplomeMembri(List<Long> diplomeIds) {
		for (Long id : diplomeIds) {
			DiplomaMembruReprezentantiComisieSauGL diploma = getDiplomaMembruById(id);
			getHibernateTemplate().delete(diploma);
		}
	}

	@Override
	public Boolean existsResponsabilArbInAllReprezentanti(Long oldResponsabilArbId) {
		String query = "SELECT COUNT(*)"
				+ "		FROM ReprezentantiComisieSauGL reprezentant"
				+ "		INNER JOIN reprezentant.responsabilARB responsabilARB"
				+ "		WHERE responsabilARB.id = ?";
		List  find = getHibernateTemplate().find(query, oldResponsabilArbId);
		long countResult = (long) find.get(0);
		if (countResult > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NomenclatorValue> getAllInstitutiiOfMembriiComisieSauGL(Long comisieSauGLId) {
		StringBuilder query = new StringBuilder();
		query.append(" select distinct m.institutie from ReprezentantiComisieSauGL r ");
		query.append(" join r.membri m ");
		query.append(" where r.comisieSauGL.id = ? and m.institutie.deleted = ? ");
		return getHibernateTemplate().find(query.toString(), comisieSauGLId, Boolean.FALSE);
	}
	
	@Override
	public List<MembruReprezentantiComisieSauGL> getMembriiReprezentantiComisieSauGLByInstitutie(Long comisieSauGLId, Long institutieId) {
		StringBuilder query = new StringBuilder();
		query.append(" select m from ReprezentantiComisieSauGL r ");
		query.append(" join r.membri m ");
		query.append(" where r.comisieSauGL.id = ? ");
		query.append(" 		and m.institutie.id = ? ");
		query.append(" 		and m.institutie.deleted = ? ");
		return getHibernateTemplate().find(query.toString(), comisieSauGLId, institutieId, Boolean.FALSE);
	}

	@Override
	public List<ReprezentantiComisieSauGL> getAllWithCategorieComisie() {
		String getIdQuery ="	SELECT id"
					+ "			FROM NomenclatorValue "
					+ " 		WHERE nomenclator.id = (SELECT id "
					+ "									FROM Nomenclator "
					+ "									WHERE code = ?)"
					+ "				AND " + NomenclatorConstants.CATEGORII_COMISII_SAU_GL_ATTRIBUTE_KEY_CATEGORIE + "= ?";
		List  result = getHibernateTemplate().find(getIdQuery, NomenclatorConstants.CATEGORII_COMISII_SAU_GL_NOMENCLATOR_CODE, NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_CATEGORIE_VALUE_FOR_COMISIE);
		long idCategorieValueComisie = (long) result.get(0);
		
		String query = "SELECT reprezentatiComisieSauGl "
				+ "		FROM ReprezentantiComisieSauGL reprezentatiComisieSauGl"
				+ "		WHERE reprezentatiComisieSauGl.comisieSauGL." + NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_CATEGORIE + " = ?";
		return getHibernateTemplate().find(query, "" + idCategorieValueComisie);
	}

	@Override
	public List<ReprezentantiComisieSauGL> getAllWithExpiredMandatsSince60Days() {
		Date today = new Date();
		Date after60DaysDate = DateUtils.addDays(today, TERMEN_EXPIRARE_MANDAT_NR_ZILE);
		after60DaysDate = DateUtils.maximizeHourMinutesSeconds(after60DaysDate);
		String query = "SELECT reprezentatiComisieSauGl "
				+ "		FROM ReprezentantiComisieSauGL reprezentatiComisieSauGl"
				+ "		WHERE (reprezentatiComisieSauGl.dataExpirareMandatPresedinte IS NOT NULL AND reprezentatiComisieSauGl.dataExpirareMandatPresedinte <= ?)"
				+ "			OR (reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte1 IS NOT NULL AND reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte1 <= ?)"
				+ "			OR (reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte2 IS NOT NULL AND reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte2 <= ?)"
				+ "			OR (reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte3 IS NOT NULL AND reprezentatiComisieSauGl.dataExpirareMandatVicepresedinte3 <= ?)";
		return getHibernateTemplate().find(query, after60DaysDate, after60DaysDate, after60DaysDate, after60DaysDate);
	}
}
