package ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriAtasament;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrariAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.utils.hibernate.QueryUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class RegistruIntrariIesiriDaoImpl extends HibernateDaoSupport implements RegistruIntrariIesiriDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIntrari> getAllIntrari() {
		String query = "FROM RegistruIntrari ORDER BY dataInregistrare";
		List<RegistruIntrari> registruIntrari = getHibernateTemplate().find(query);
		return registruIntrari;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsOfExistingIntrari() {
		String query = "SELECT DISTINCT EXTRACT(YEAR from registru.dataInregistrare) FROM RegistruIntrari registru";
		List<Integer> years = (List<Integer>) getHibernateTemplate().find(query);
		return years;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsOfExistingIesiri() {
		String query = "SELECT DISTINCT EXTRACT(YEAR from registru.dataInregistrare) FROM RegistruIesiri registru";
		List<Integer> years = (List<Integer>) getHibernateTemplate().find(query);
		return years;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIntrari> getAllIntrariByYear(Integer year) {
		String query = "FROM RegistruIntrari registru WHERE EXTRACT (YEAR from registru.dataInregistrare) = :year ORDER BY registru.dataInregistrare DESC";
		List<RegistruIntrari> intrari = getHibernateTemplate().findByNamedParam(query, "year", year);
		return intrari;
	}

	@Override
	public List<RegistruIesiri> getAllIesiriByYear(Integer year) {
		String query = "FROM RegistruIesiri registru WHERE EXTRACT (YEAR from registru.dataInregistrare) = :year ORDER BY registru.dataInregistrare DESC";
		List<RegistruIesiri> iesiri = getHibernateTemplate().findByNamedParam(query, "year", year);
		return iesiri;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIesiri> getIesiriByFilter(RegistruIesiriFilterModel filter, String docTypeCodEchivalent) {
		StringBuilder query = new StringBuilder();
		List<String> paramList = new ArrayList<>();
		List<Object> paramValues = new ArrayList<>();
		query.append("SELECT DISTINCT iesiri FROM RegistruIesiri iesiri JOIN iesiri.proiecte pr LEFT JOIN iesiri.subactivity subactivity JOIN iesiri.destinatari dest"
				+ " WHERE 1 = 1\n");
		if (filter.getYear() != null) {
			paramList.add("year");
			paramValues.add(filter.getYear());
			query.append("AND EXTRACT (YEAR FROM iesiri.dataInregistrare) = :year\n");
		}
		if (StringUtils.isNotBlank(filter.getNrInregistrare())) {
			paramList.add("nrInregistrare");
			paramValues.add("%"+filter.getNrInregistrare()+"%");
			query.append("AND SUBSTR(iesiri.numarInregistrare,1,LOCATE(' ~',iesiri.numarInregistrare) - 1) LIKE :nrInregistrare\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getSelectedMonths())) {
			paramList.add("selectedMonths");
			paramValues.add(filter.getSelectedMonths().stream().map(elem -> ++elem).toArray());// in db JAN = 1 but in presentation layer, JAN = 0
			query.append("AND EXTRACT (MONTH from iesiri.dataInregistrare) IN (:selectedMonths)\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getDocumentTypeIds())) {
			paramList.add("documentTypeIds");
			paramValues.add(filter.getDocumentTypeIds().toArray());
			query.append("AND iesiri.tipDocument.id IN (:documentTypeIds)\n");
		}
		if (filter.getIsMailed() != null) {
			paramList.add("isMailed");
			paramValues.add(filter.getIsMailed());
			query.append("AND iesiri.trimisPeMail IS :isMailed\n");
		}
		if (StringUtils.isNotBlank(filter.getDevelopingUser())) {
			paramList.add("developingUser");
			paramValues.add("%"+filter.getDevelopingUser().toUpperCase()+"%");
			query.append("AND UPPER(CONCAT(iesiri.intocmitDe.firstName, ' ',iesiri.intocmitDe.lastName)) LIKE :developingUser\n");
		}
		if (filter.getIsAwaitingResponse() != null) {
			paramList.add("isAwaitingResponse");
			paramValues.add(filter.getIsAwaitingResponse());
			query.append("AND iesiri.asteptamRaspuns IS :isAwaitingResponse\n");
		}
		if (filter.getIsCanceled() != null) {
			paramList.add("isCanceled");
			paramValues.add(filter.getIsCanceled());
			query.append("AND iesiri.anulat IS :isCanceled\n");
		}
		if (filter.getIsFinished() != null) {
			paramList.add("isFinished");
			paramValues.add(filter.getIsFinished());
			query.append("AND iesiri.inchis IS :isFinished\n");
		}
		if (filter.getRegistrationDate() != null) {
			paramList.add("registrationDate");
			paramValues.add(filter.getRegistrationDate());
			query.append("AND DATE(iesiri.dataInregistrare) = :registrationDate\n");
		}
		if (StringUtils.isNotBlank(filter.getDocumentTypeCode())) {
			paramList.add("documentTypeCode");
			paramValues.add("%"+filter.getDocumentTypeCode().toUpperCase()+"%");
			query.append("AND UPPER(iesiri.tipDocument."+NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD+") LIKE :documentTypeCode\n");
		}
		if (StringUtils.isNotBlank(filter.getContent())) {
			paramList.add("content");
			paramValues.add("%"+filter.getContent().toUpperCase()+"%");
			query.append("AND UPPER(iesiri.continut) LIKE :content\n");
		}
		if (filter.getNumberOfPages() != null) {
			paramList.add("numberOfPages");
			paramValues.add("%"+filter.getNumberOfPages().toString()+"%");
			query.append("AND CAST(iesiri.numarPagini AS string) LIKE :numberOfPages\n");
		}
		if (filter.getNumberOfAnnexes() != null) {
			paramList.add("numberOfAnnexes");
			paramValues.add("%"+filter.getNumberOfAnnexes().toString()+"%");
			query.append("AND CAST(iesiri.numarAnexe AS string) LIKE :numberOfAnnexes\n");
		}
		if (StringUtils.isNotBlank(filter.getProjectName())) {
			paramList.add("projectName");
			paramValues.add("%"+filter.getProjectName().toUpperCase()+"%");
			query.append("AND UPPER(pr.name) LIKE :projectName\n");
		}
		if (filter.getResponseDeadline() != null) {
			paramList.add("responseDeadline");
			paramValues.add(filter.getResponseDeadline());
			query.append("AND DATE(iesiri.termenRaspuns) = :responseDeadline\n");
		}
		if (StringUtils.isNotBlank(filter.getCancellationReason())) {
			paramList.add("cancellationReason");
			paramValues.add("%"+filter.getCancellationReason()+"%");
			query.append("AND iesiri.motivAnulare LIKE :cancellationReason\n");
		}
		if (StringUtils.isNotBlank(filter.getDepartamentDestinatar())) {
			paramList.add("departamentDestinatar");
			paramValues.add("%"+filter.getDepartamentDestinatar().toUpperCase()+"%");
			query.append("AND UPPER(dest.departament) LIKE :departamentDestinatar\n");
		}
		if (StringUtils.isNotBlank(filter.getNumarInregistrareDestinatar())) {
			paramList.add("numarInregistrareDestinatar");
			paramValues.add(filter.getNumarInregistrareDestinatar()+"%");
			query.append("AND dest.numarInregistrare LIKE :numarInregistrareDestinatar\n");
		}
		if (filter.getDataInregistrareDestinatar() != null) {
			paramList.add("dataInregistrareDestinatar");
			paramValues.add(filter.getDataInregistrareDestinatar());
			query.append("AND DATE(dest.dataInregistrare) = :dataInregistrareDestinatar\n");
		}
		if (filter.getComisieGlDestinatar() != null) {
			paramList.add("comisieGlDestinatar");
			paramValues.add(filter.getComisieGlDestinatar());
			query.append("AND dest.comisieGl.id = :comisieGlDestinatar\n");
		}
		if (StringUtils.isNotBlank(filter.getObservatiiDestinatar())) {
			paramList.add("observatiiDestinatar");
			paramValues.add(filter.getObservatiiDestinatar()+"%");
			query.append("AND dest.observatii LIKE :observatiiDestinatar\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getInstitutiiIdsDestinatar())) {
			paramList.add("institutiiIds");
			paramValues.add(filter.getInstitutiiIdsDestinatar());
			query.append("AND dest.institutie.id IN (:institutiiIds)\n");
		}
		if (StringUtils.isNotBlank(filter.getNumeDestinatarNou())) {
			paramList.add("numeDestinatarNou");
			paramValues.add("%"+filter.getNumeDestinatarNou().toUpperCase()+"%");
			query.append("AND UPPER(dest.nume) LIKE :numeDestinatarNou\n");
		}
		if (StringUtils.isNotBlank(filter.getSubactivityName())) {
			paramList.add("subactivityName");
			paramValues.add("%"+filter.getSubactivityName().toUpperCase()+"%");
			query.append("AND UPPER(subactivity.name) LIKE :subactivityName\n");
		}
		if (StringUtils.isNotBlank(docTypeCodEchivalent)) {
			query.append("AND iesiri.anulat = false\n");
			query.append("AND iesiri.tipDocument."+NomenclatorConstants.RESGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD+" = '"+docTypeCodEchivalent+"'\n");
			if (filter.getEmitentId() != null) {
				query.append(""
						+ " AND EXISTS ("
						+ "		SELECT destinatar.id"
						+ "		FROM RegistruIesiriDestinatar destinatar"
						+ "		INNER JOIN destinatar.institutie as institutie"
						+ "		WHERE institutie.id = '" + filter.getEmitentId() + "'"
						+ "		AND destinatar.registruIesiri = iesiri"
						+ "		AND destinatar.registruIntrari IS NULL"
						+ " )\n");
			} else {
				query.append(""
						+ " AND EXISTS ("
						+ "		SELECT destinatar.nume"
						+ "		FROM RegistruIesiriDestinatar destinatar"
						+ "		WHERE destinatar.nume = '" + filter.getNumeEmitent() + "'"
						+ "		AND destinatar.registruIesiri = iesiri"
						+ "		AND destinatar.registruIntrari IS NULL"
						+ " )\n");
			}
		}
		
		String[] params = new String[paramList.size()];
		params = paramList.toArray(params);
		List<RegistruIesiri> iesiri = getHibernateTemplate().findByNamedParam(query.toString(), params, paramValues.toArray());
		return iesiri;	
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistruIntrari> getAllRegistruIntrariByFilter(RegistruIntrariFilter filter, String docTypeCodEchivalent){
		
		StringBuilder query = new StringBuilder();
		List<String> paramList = new ArrayList<>();
		List<Object> paramValues = new ArrayList<>();
		query.append("SELECT DISTINCT intrari FROM RegistruIntrari intrari JOIN intrari.proiecte pr LEFT JOIN intrari.comisiiSauGL cogl LEFT JOIN intrari.subactivity subactivity"
				+ " WHERE 1 = 1\n");
		if (filter.getYear() != null) {
			paramList.add("year");
			paramValues.add(filter.getYear());
			query.append("AND EXTRACT (YEAR FROM intrari.dataInregistrare) = :year\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getSelectedMonths())) {
			paramList.add("selectedMonths");
			paramValues.add(filter.getSelectedMonths().stream().map(elem -> ++elem).toArray());// in db JAN = 1 but in presentation layer, JAN = 0
			query.append("AND EXTRACT (MONTH from intrari.dataInregistrare) IN (:selectedMonths)\n");
		}
		if (StringUtils.isNotBlank(filter.getRegistrationNumber())) {
			paramList.add("regNumber");
			paramValues.add("%"+filter.getRegistrationNumber()+"%");
			query.append("AND SUBSTR(intrari.numarInregistrare,1,LOCATE(' ~',intrari.numarInregistrare) - 1) LIKE :regNumber\n");
		}
		if (filter.getRegistrationDate() != null) {
			paramList.add("registrationDate");
			paramValues.add(filter.getRegistrationDate());
			query.append("AND DATE(intrari.dataInregistrare) = :registrationDate\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getSenderIds())) {
			paramList.add("senderIds");
			paramValues.add(filter.getSenderIds().toArray());
			query.append("AND intrari.emitent.id IN (:senderIds)\n");
		}
		if (StringUtils.isNotBlank(filter.getSenderDepartment())) {
			paramList.add("senderDep");
			paramValues.add("%"+filter.getSenderDepartment().toUpperCase()+"%");
			query.append("AND UPPER(intrari.departamentEmitent) LIKE :senderDep\n");
		}
		if (StringUtils.isNotBlank(filter.getSenderDocumentNr())) {
			paramList.add("senderDocNr");
			paramValues.add("%"+filter.getSenderDocumentNr().toUpperCase()+"%");
			query.append("AND UPPER(intrari.numarDocumentEmitent) LIKE :senderDocNr\n");
		}
		if (filter.getSenderDocumentDate() != null) {
			paramList.add("senderDocDate");
			paramValues.add(filter.getSenderDocumentDate());
			query.append("AND DATE(intrari.dataDocumentEmitent) = :senderDocDate\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getDocumentTypeIds())) {
			paramList.add("docTypeIds");
			paramValues.add(filter.getDocumentTypeIds().toArray());
			query.append("AND intrari.tipDocument.id IN (:docTypeIds)\n");
		}
		if (StringUtils.isNotBlank(filter.getDocumentTypeCode())) {
			paramList.add("documentTypeCode");
			paramValues.add("%"+filter.getDocumentTypeCode().toUpperCase()+"%");
			query.append("AND UPPER(intrari.tipDocument."+NomenclatorConstants.REGISTRU_INTRARI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD+") LIKE :documentTypeCode\n");
		}
		if (filter.getIsMailed() != null) {
			paramList.add("isMailed");
			paramValues.add(filter.getIsMailed());
			query.append("AND intrari.trimisPeMail IS :isMailed\n");
		}
		if (StringUtils.isNotBlank(filter.getContent())) {
			paramList.add("content");
			paramValues.add("%"+filter.getContent().toUpperCase()+"%");
			query.append("AND UPPER(intrari.continut) LIKE :content\n");
		}
		if (filter.getNumberOfPages() != null) {
			paramList.add("numberOfPages");
			paramValues.add("%"+filter.getNumberOfPages().toString()+"%");
			query.append("AND CAST(intrari.numarPagini AS string) LIKE :numberOfPages\n");
		}
		if (filter.getNumberOfAnnexes() != null) {
			paramList.add("numberOfAnnexes");
			paramValues.add("%"+filter.getNumberOfAnnexes().toString()+"%");
			query.append("AND CAST(intrari.numarAnexe AS string) LIKE :numberOfAnnexes\n");
		}
		if (StringUtils.isNotBlank(filter.getAssignedUser())) {
			paramList.add("assignedUser");
			paramValues.add("%"+filter.getAssignedUser().toUpperCase()+"%");
			query.append("AND UPPER(CONCAT(intrari.repartizatCatre.firstName, ' ',intrari.repartizatCatre.lastName)) LIKE :assignedUser\n");
		}
		if (StringUtils.isNotBlank(filter.getCommitteeWgName())) {
			paramList.add("comisieGl");
			paramValues.add("%"+filter.getCommitteeWgName().toUpperCase()+"%");
			query.append("AND UPPER(cogl."+NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE+") LIKE :comisieGl\n");
		}
		if (StringUtils.isNotBlank(filter.getProjectName())) {
			paramList.add("projectName");
			paramValues.add("%"+filter.getProjectName().toUpperCase()+"%");
			query.append("AND UPPER(pr.name) LIKE :projectName\n");
		}
		if (filter.getIsAwaitingResponse() != null) {
			paramList.add("isAwaitingResponse");
			paramValues.add(filter.getIsAwaitingResponse());
			query.append("AND intrari.necesitaRaspuns IS :isAwaitingResponse\n");
		}
		if (filter.getResponseDeadline() != null) {
			paramList.add("responseDeadline");
			paramValues.add(filter.getResponseDeadline());
			query.append("AND DATE(intrari.termenRaspuns) = :responseDeadline\n");
		}
		if (StringUtils.isNotBlank(filter.getIesireRegistrationNumber())) {
			paramList.add("iesireRegNr");
			paramValues.add("%"+filter.getIesireRegistrationNumber()+"%");
			query.append("AND EXISTS (FROM RegistruIesiri iesire WHERE iesire.id = intrari.registruIesiriId AND iesire.numarInregistrare LIKE :iesireRegNr)\n");
		}
		if (StringUtils.isNotBlank(filter.getRemarks())) {
			paramList.add("remarks");
			paramValues.add("%"+filter.getRemarks().toUpperCase()+"%");
			query.append("AND UPPER(intrari.observatii) LIKE :remarks\n");
		}
		if (CollectionUtils.isNotEmpty(filter.getBankResponseProposal())) {
			paramList.add("raspunsuriBanci");
			paramValues.add(filter.getBankResponseProposal().toArray());
			query.append("AND intrari.raspunsuriBanciCuPropuneri IN (:raspunsuriBanci)\n");
		}
		if (filter.getNrZileIntrareEmitent() != null) {
			paramList.add("nrZileIntrareEm");
			paramValues.add(filter.getNrZileIntrareEmitent());
			query.append("AND intrari.nrZileIntrareEmitent = :nrZileIntrareEm\n");
		}
		if (filter.getNrZileRaspunsIntrare() != null) {
			paramList.add("nrZileRaspIntr");
			paramValues.add(filter.getNrZileRaspunsIntrare());
			query.append("AND intrari.nrZileRaspunsIntrare = :nrZileRaspIntr\n");
		}
		if (filter.getNrZileRaspunsEmitent() != null) {
			paramList.add("nrZileRaspEmit");
			paramValues.add(filter.getNrZileRaspunsEmitent());
			query.append("AND intrari.nrZileRaspunsEmitent = :nrZileRaspEmit\n");
		}
		if (filter.getNrZileTermenDataRaspuns() != null) {
			paramList.add("nrZileTermentRasp");
			paramValues.add(filter.getNrZileTermenDataRaspuns());
			query.append("AND intrari.nrZileTermenDataRaspuns = :nrZileTermentRasp\n");
		}
		if (filter.getIsCanceled() != null) {
			paramList.add("isCanceled");
			paramValues.add(filter.getIsCanceled());
			query.append("AND intrari.anulat IS :isCanceled\n");
		}
		if (StringUtils.isNotBlank(filter.getCancellationReason())) {
			paramList.add("cancellationReason");
			paramValues.add("%"+filter.getCancellationReason()+"%");
			query.append("AND intrari.motivAnulare LIKE :cancellationReason\n");
		}
		if (filter.getIsFinished() != null) {
			paramList.add("isFinished");
			paramValues.add(filter.getIsFinished());
			query.append("AND intrari.inchis IS :isFinished\n");
		}
		if (StringUtils.isNotBlank(filter.getSubactivityName())) {
			paramList.add("subactivityName");
			paramValues.add("%"+filter.getSubactivityName().toUpperCase()+"%");
			query.append("AND UPPER(subactivity.name) LIKE :subactivityName\n");
		}
		if (docTypeCodEchivalent != null) {
			query.append("AND intrari.anulat = false\n");
			query.append("AND intrari.tipDocument."+NomenclatorConstants.RESGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD+" = '"+docTypeCodEchivalent+"'\n");
			query.append("AND NOT EXISTS (FROM RegistruIesiriDestinatar dest WHERE intrari = dest.registruIntrari)\n");
			if (filter.getDestinatarId() != null) {
				query.append("AND intrari.emitent.id = '"+filter.getDestinatarId()+"'\n");
			} else {
				query.append("AND intrari.numeEmitent = '"+filter.getNumeDestinatar()+"'\n");
			}
		}
		
		String[] params = new String[paramList.size()];
		params = paramList.toArray(params);
		List<RegistruIntrari> intrari = getHibernateTemplate().findByNamedParam(query.toString(), params, paramValues.toArray());
		return intrari;
	}
	
	@Override
	public RegistruIntrari findIntrare(Long registruIntrariId) {
		return (RegistruIntrari) getHibernateTemplate().get(RegistruIntrari.class, registruIntrariId);
	}

	@Override
	public RegistruIesiri findIesire(Long registruIesiriId) {
		return (RegistruIesiri) getHibernateTemplate().get(RegistruIesiri.class, registruIesiriId);
	}

	@Override
	public Long saveRegistruIntrari(RegistruIntrari registruIntrari) {
		if (registruIntrari.getId() == null) {
			Long id = (Long) getHibernateTemplate().save(registruIntrari);
			getHibernateTemplate().flush();
			return id;
		} else {
			getHibernateTemplate().saveOrUpdate(registruIntrari);
			return registruIntrari.getId();
		}
	}

	@Override
	public Long saveRegistruIesiri(RegistruIesiri registruIesiri) {
		if (registruIesiri.getId() == null) {
			return (Long) getHibernateTemplate().save(registruIesiri);
		} else {
			getHibernateTemplate().saveOrUpdate(registruIesiri);
			return registruIesiri.getId();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIesiri> getAllRegistruIesiri() {
		String query = "FROM RegistruIesiri ORDER BY dataInregistrare";
		List<RegistruIesiri> registruIntrari = getHibernateTemplate().find(query);
		return registruIntrari;
	}

	@Override
	public RegistruIntrari getById(Long registruId) {
		return (RegistruIntrari) getHibernateTemplate().get(RegistruIntrari.class, registruId);
	}

	@Override
	public RegistruIesiriDestinatar findRegistruIesireDestinatar(Long destinatarId) {
		return getHibernateTemplate().get(RegistruIesiriDestinatar.class, destinatarId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public RegistruIesiriDestinatar getRegistruIesireDestinatarByIntrareId(Long intrareId) {
		List<RegistruIesiriDestinatar> destinatari = getHibernateTemplate().find("FROM RegistruIesiriDestinatar red\n"
				+ "WHERE red.registruIntrari = "+intrareId);
		if (CollectionUtils.isEmpty(destinatari)) {
			return null;
		}
		return destinatari.get(0);
	}

	@Override
	public void deleteRegistruIesiriDestinatari(List<Long> destinatariIds) {
		for (Long id : destinatariIds) {
			RegistruIesiriDestinatar registruIesiriDestinatar = findRegistruIesireDestinatar(id);
			getHibernateTemplate().delete(registruIesiriDestinatar);
		}
	}

	@Override
	public void deleteRegistruIntrariAtasamente(List<Long> atasamenteIdsToDelete) {
		for (Long id : atasamenteIdsToDelete) {
			RegistruIntrariAtasament registruIntrariAtasamente = findRegistruIntrariAtasamentById(id);
			getHibernateTemplate().delete(registruIntrariAtasamente);
		}
	}

	@Override
	public void deleteRegistruIesiriAtasamente(List<Long> atasamenteIdsToDelete) {
		for (Long id : atasamenteIdsToDelete) {
			RegistruIesiriAtasament registruIesiriAtasamente = findRegistruIesiriAtasamentById(id);
			getHibernateTemplate().delete(registruIesiriAtasamente);
		}

	}

	@Override
	public Long saveRegistruIesiriAtasamente(RegistruIesiriAtasament regIesiriAtasament) {
		if (regIesiriAtasament.getId() == null) {
			return (Long) getHibernateTemplate().save(regIesiriAtasament);
		} else {
			getHibernateTemplate().saveOrUpdate(regIesiriAtasament);
			return regIesiriAtasament.getId();
		}

	}

	@Override
	public Long saveRegistruIntrariAtasamente(RegistruIntrariAtasament regIntrariAtasament) {
		if (regIntrariAtasament.getId() == null) {
			return (Long) getHibernateTemplate().save(regIntrariAtasament);
		} else {
			getHibernateTemplate().saveOrUpdate(regIntrariAtasament);
			return regIntrariAtasament.getId();
		}

	}

	@Override
	public RegistruIntrariAtasament findRegistruIntrariAtasamentById(Long id) {
		return (RegistruIntrariAtasament) getHibernateTemplate().get(RegistruIntrariAtasament.class, id);
	}

	@Override
	public RegistruIesiriAtasament findRegistruIesiriAtasamentById(Long id) {
		return (RegistruIesiriAtasament) getHibernateTemplate().get(RegistruIesiriAtasament.class, id);
	}

	@Override
	public List<RegistruIesiri> getIesiriByDocumenteTrimiseDeArbReportFilterModel(
			DocumenteTrimiseDeArbReportFilterModel filter) {

		List<Object> queryParameters = new ArrayList<>(); 
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT registru FROM RegistruIesiri registru ");
		
		String trueCondition = " 1 = 1 ";
		query.append("WHERE " + trueCondition);

		if (filter.getDataInceput() != null) {
			query.append(" AND " + " registru.dataInregistrare >= ? ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}
		
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + " registru.dataInregistrare <= ? ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}
		
		query.append(" ORDER BY registru.dataInregistrare ");
		
		List<RegistruIesiri> iesiri = new ArrayList<RegistruIesiri>();

		if (queryParameters.size() == 0) {
			iesiri = getHibernateTemplate().find(query.toString());
		}if (queryParameters.size() == 1) {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return iesiri;
	}
	
	public Long saveRegistruIesiriDestinatari(RegistruIesiriDestinatar regIesiriDestinatar) {
		if (regIesiriDestinatar.getId() == null) {
			return (Long) getHibernateTemplate().save(regIesiriDestinatar);
		} else {
			getHibernateTemplate().saveOrUpdate(regIesiriDestinatar);
			return regIesiriDestinatar.getId();
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ActiuniPeProiectRegistruIntrariIesiriReportModel> getAllRegistruIntrariByActiuniPeProiectReportFilterModel(
			ActiuniPeProiectReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT new ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel ("
				+ "proiect.name, subactivity.name, registru.tipDocument." + NomenclatorConstants.REGISTRU_INTRARI_TIP_DOCUMENT_ATTRIBUTE_KEY_DENUMIRE + ", registru.continut, registru.dataInregistrare, registru.numarInregistrare ) ");
		query.append(" FROM RegistruIntrari registru ");
		query.append(" JOIN registru.proiecte proiect ");
		query.append(" LEFT JOIN registru.subactivity subactivity ");
		
		String trueCondition = " 1 = 1 ";
		query.append("WHERE " + trueCondition);

		List<Object> queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND proiect.id = ? ");
			queryParameters.add(filter.getProiectId());
			if (!CollectionUtils.isEmpty(filter.getSubprojectIds())) {
				String subListString = "";
				for (int i = 0; i < filter.getSubprojectIds().size(); i++) {
					subListString += "?";
					queryParameters.add(filter.getSubprojectIds().get(i));
					if (i == filter.getSubprojectIds().size() - 1) break;
					subListString += ",";
				}
				query.append(" AND (subactivity.id IN ("+subListString+"))");
			}
		}
		if (filter.getDataInceput() != null) {
			query.append(" AND " + "(registru.dataInregistrare >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + "(registru.dataInregistrare <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}

		query.append(" ORDER BY proiect.name, subactivity.name, registru.dataInregistrare ");
		
		List<ActiuniPeProiectRegistruIntrariIesiriReportModel> intrari = new ArrayList<>();
		
		if (queryParameters.size() == 0) {
			intrari = getHibernateTemplate().find(query.toString());
		} if (queryParameters.size() == 1) {
			intrari = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			intrari = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return intrari;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiuniPeProiectRegistruIntrariIesiriReportModel> getAllRegistruIesiriByActiuniPeProiectReportFilterModel(
			ActiuniPeProiectReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT new ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel ("
				+ "proiect.name, subactivity.name, registru.tipDocument." + NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_DENUMIRE + ", registru.continut, registru.dataInregistrare, registru.numarInregistrare ) ");
		query.append(" FROM RegistruIesiri registru ");
		query.append(" JOIN registru.proiecte proiect ");
		query.append(" LEFT JOIN registru.subactivity subactivity ");
		
		String trueCondition = " 1 = 1 ";
		query.append("WHERE " + trueCondition);

		List<Object> queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND proiect.id = ? ");
			queryParameters.add(filter.getProiectId());
			if (!CollectionUtils.isEmpty(filter.getSubprojectIds())) {
				String subListString = "";
				for (int i = 0; i < filter.getSubprojectIds().size(); i++) {
					subListString += "?";
					queryParameters.add(filter.getSubprojectIds().get(i));
					if (i == filter.getSubprojectIds().size() - 1) break;
					subListString += ",";
				}
				query.append(" AND (subactivity.id IN ("+subListString+"))");
			}
		}
		if (filter.getDataInceput() != null) {
			query.append(" AND " + "(registru.dataInregistrare >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + "(registru.dataInregistrare <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}

		query.append(" ORDER BY proiect.name, subactivity.name, registru.dataInregistrare ");
		
		List<ActiuniPeProiectRegistruIntrariIesiriReportModel> iesiri = new ArrayList<>();
		
		if (queryParameters.size() == 0) {
			iesiri = getHibernateTemplate().find(query.toString());
		} if (queryParameters.size() == 1) {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return iesiri;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Long> getNumarInterogariMembriiArbByBancaAndComisie(List<Long> institutiiMembriiArbIds) {
		Map<String, Long> interogariByBanca = new HashMap<String, Long>();
		StringBuilder query = new StringBuilder(""
				+ " SELECT  CONCAT(destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + ", destinatar.comisieGl), COUNT(registruIesire.id)"
				+ "	FROM RegistruIesiriDestinatar destinatar"
				+ "	INNER JOIN destinatar.registruIesiri registruIesire"
				+ "	WHERE registruIesire.asteptamRaspuns = true"
				+ "		AND destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " IS NOT NULL");
		if (CollectionUtils.isNotEmpty(institutiiMembriiArbIds)) {
			query.append("		AND destinatar.institutie.id IN " + QueryUtils.joinAsINValues(institutiiMembriiArbIds));
		}
		query.append("	GROUP BY destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " , destinatar.comisieGl");
		
		List result = getHibernateTemplate().find(query.toString());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			interogariByBanca.put("" + value[0], (Long) value[1]);
		});
		
		return interogariByBanca;
	}
	
	public List<RegistruIesiri> getAllRegistruIesiriByRaspunsuriBanciReportFilterModel(
			RaspunsuriBanciReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder(" "
				+ "	SELECT DISTINCT (registruIesire)"
				+ "	FROM RegistruIesiri registruIesire "
				+ " INNER JOIN registruIesire.proiecte proiect"
				+ " INNER JOIN registruIesire.destinatari destinatar"
				+ "	WHERE registruIesire.asteptamRaspuns = true");
		
		List<Object> queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND proiect.id = ? ");
			queryParameters.add(filter.getProiectId());
		}
		if (filter.getDenumireBanca() != null) {
			query.append(" AND " + "(destinatar.nume = ?  OR destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " = ?) ");
			queryParameters.add(filter.getDenumireBanca());
			queryParameters.add(filter.getDenumireBanca());
		}
		if (filter.getTermenRaspunsDela() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getTermenRaspunsDela()));
		}
		if (filter.getTermenRaspunsPanaLa() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getTermenRaspunsPanaLa()));
		}

		query.append(" ORDER BY registruIesire.dataInregistrare ");
		
		List<RegistruIesiri> iesiri = new ArrayList<>();
		
		if (queryParameters.size() == 0) {
			iesiri = getHibernateTemplate().find(query.toString());
		} if (queryParameters.size() == 1) {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			iesiri = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return iesiri;
	}

	@Override
	public List<SimpleListItemModel> getAllDenumiriBanciFromRegistruIesiriDestinatariAsSelectItems() {
		Set<SimpleListItemModel> denumiriBanciDistinct = new HashSet();
		String query = ""
				+ "	SELECT new ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel ( destinatar.nume) "
				+ " FROM RegistruIesiri registru "
				+ " JOIN registru.destinatari destinatar "
				+ "	WHERE registru.asteptamRaspuns = true "
				+ "		AND destinatar.nume IS NOT NULL"
				+ "	GROUP BY destinatar.nume"
				+ "	";
		
		denumiriBanciDistinct.addAll(getHibernateTemplate().find(query));
		
		query = ""
				+ "	SELECT new ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel ("
				+ "		destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " ) "
				+ "	FROM RegistruIesiri registru "
				+ " INNER JOIN registru.destinatari destinatar "
				+ "	WHERE registru.asteptamRaspuns = true "
				+ "		AND destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " IS NOT NULL"
				+ "	GROUP BY destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE ;
		
		denumiriBanciDistinct.addAll(getHibernateTemplate().find(query));
		return denumiriBanciDistinct.stream().collect(Collectors.toList());
	}

	@Override
	public List<SimpleListItemModel> getAllProjectsFromRegistruIesiriAsSelectItems() {
		String query = ""
				+ "	SELECT new ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel (proiect.id, proiect.name ) "
				+ " FROM RegistruIesiri registru " 
				+ " JOIN registru.proiecte proiect "
				+ "	GROUP BY proiect.id";

		
		return getHibernateTemplate().find(query.toString());
		
	}

	@Override
	public Map<String, Long> getNrInterogariByBancaAsMap() {
		Map<String, Long> interogariByBanca = new HashMap<String, Long>();
		String query = ""
				+ " SELECT destinatar.nume, COUNT(registruIesire.id)"
				+ "	FROM RegistruIesiriDestinatar destinatar"
				+ "	INNER JOIN destinatar.registruIesiri registruIesire"
				+ "	WHERE registruIesire.asteptamRaspuns = true "
				+ "		AND destinatar.nume IS NOT NULL"
				+ "	GROUP BY destinatar.nume";
		List result = getHibernateTemplate().find(query.toString());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			interogariByBanca.put("" + value[0], (Long) value[1]);
		});
		
		query = ""
				+ " SELECT destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + ", COUNT(registruIesire.id)"
				+ "	FROM RegistruIesiriDestinatar destinatar"
				+ "	INNER JOIN destinatar.registruIesiri registruIesire"
				+ "	WHERE registruIesire.asteptamRaspuns = true"
				+ "		AND destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " IS NOT NULL"
				+ "	GROUP BY destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE;
		
		result = getHibernateTemplate().find(query.toString());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			interogariByBanca.put("" + value[0], (Long) value[1]);
		});
		return interogariByBanca;
	}

	@Override
	public Map<Long, Long> getNrRaspunsuriByBancaAsMap() {
		Map<Long, Long> raspunsuriByBanca = new HashMap<Long, Long>();
		String query = ""
				+ " SELECT destinatar.institutie.id, COUNT(registruIntrare.id)"
				+ "	FROM RegistruIesiriDestinatar destinatar"
				+ "	INNER JOIN destinatar.registruIntrari registruIntrare"
				+ "	WHERE  destinatar.institutie.id IS NOT NULL"
				+ "	GROUP BY destinatar.institutie.id ";
		
		List result = getHibernateTemplate().find(query.toString());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			raspunsuriByBanca.put((Long) value[0], (Long) value[1]);
		});
		return raspunsuriByBanca;
	}

	@Override
	public Map<String, Long> getNrInterogariByBancaAsMapByRaspunsuriBanciFilter(RaspunsuriBanciReportFilterModel filter) {
		Map<String, Long> interogariByBanca = new HashMap<String, Long>();
		StringBuilder query = new StringBuilder("");
		query.append(" 	SELECT destinatar.nume, COUNT(registruIesire.id)");
		query.append("	FROM RegistruIesiriDestinatar destinatar");
		query.append(" 	INNER JOIN destinatar.registruIesiri registruIesire");
		query.append(" 	INNER JOIN registruIesire.proiecte proiect");
		query.append("	WHERE  registruIesire.asteptamRaspuns = true");
		query.append(" 		AND destinatar.nume IS NOT NULL");	
		
		List<Object> queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND proiect.id = ? ");
			queryParameters.add(filter.getProiectId());
		}
		if (filter.getDenumireBanca() != null) {
			query.append(" AND " + "(destinatar.nume = ?  OR destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " = ?) ");
			queryParameters.add(filter.getDenumireBanca());
			queryParameters.add(filter.getDenumireBanca());
		}
		if (filter.getTermenRaspunsDela() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getTermenRaspunsDela()));
		}
		if (filter.getTermenRaspunsPanaLa() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getTermenRaspunsPanaLa()));
		}

		query.append("	GROUP BY destinatar.nume ");
		
		List result = QueryUtils.executeQueryWithAppropiateParameters(getHibernateTemplate(), query.toString(), queryParameters.toArray());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			interogariByBanca.put("" + value[0], (Long) value[1]);
		});
		
		query = new StringBuilder("");
		query.append(" 	SELECT destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + ", COUNT(registruIesire.id)");
		query.append("	FROM RegistruIesiriDestinatar destinatar");
		query.append(" 	INNER JOIN destinatar.registruIesiri registruIesire");
		query.append(" 	INNER JOIN registruIesire.proiecte proiect");
		query.append("	WHERE registruIesire.asteptamRaspuns = true"); 
		query.append("		AND destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " IS NOT NULL");
		
		queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND proiect.id = ? ");
			queryParameters.add(filter.getProiectId());
		}
		if (filter.getDenumireBanca() != null) {
			query.append(" AND " + "(destinatar.nume = ?  OR destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " = ?) ");
			queryParameters.add(filter.getDenumireBanca());
			queryParameters.add(filter.getDenumireBanca());
		}
		if (filter.getTermenRaspunsDela() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getTermenRaspunsDela()));
		}
		if (filter.getTermenRaspunsPanaLa() != null) {
			query.append(" AND " + "(registruIesire.termenRaspuns <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getTermenRaspunsPanaLa()));
		}

		query.append("	GROUP BY destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE);
		
		result = QueryUtils.executeQueryWithAppropiateParameters(getHibernateTemplate(), query.toString(), queryParameters.toArray());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			interogariByBanca.put("" + value[0], (Long) value[1]);
		});
		return interogariByBanca;
	}

	@Override
	public Map<String, List<RegistruIesiriDestinatar>> getAllRegistruIesiriDestinatariByNotaGeneralaMembriiArbFilter(List<Long> institutiiMembriiArbIds) {
		
		Map<String, List<RegistruIesiriDestinatar>> destinatariByBancaComisie = new HashMap<String, List<RegistruIesiriDestinatar>>();
		StringBuilder query = new StringBuilder(""
				+ " SELECT  CONCAT(destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + ", destinatar.comisieGl), destinatar"
				+ "	FROM RegistruIesiriDestinatar destinatar"
				+ "	INNER JOIN destinatar.registruIesiri registruIesire"
				+ "	INNER JOIN destinatar.registruIntrari registruIntrare"
				+ "	WHERE registruIesire.asteptamRaspuns = true"
				+ "		AND destinatar.institutie." + NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE + " IS NOT NULL");
		if (CollectionUtils.isNotEmpty(institutiiMembriiArbIds)) {
			query.append("		AND destinatar.institutie.id IN " + QueryUtils.joinAsINValues(institutiiMembriiArbIds));
		}
		
		List result = getHibernateTemplate().find(query.toString());
		result.forEach(resultIterator -> {
			Object[] value = (Object[]) resultIterator;
			RegistruIesiriDestinatar regIesireDestinatar = (RegistruIesiriDestinatar) value[1];
			String key = (String) value[0];
			if (!destinatariByBancaComisie.containsKey(key)) {
				List<RegistruIesiriDestinatar> destinatarAsList = new ArrayList<>();
				destinatarAsList.add(regIesireDestinatar);
				destinatariByBancaComisie.put(key, destinatarAsList);
			} else {
				List<RegistruIesiriDestinatar> registreIesiriDestinatari = destinatariByBancaComisie.get(key);
				registreIesiriDestinatari.add(regIesireDestinatar);
			}
		});
		return destinatariByBancaComisie;
	}

	@Override
	public RegistruIntrari getIntrareByNrInregistrare(String nrInregistrare) {
		String query = "FROM RegistruIntrari WHERE numarInregistrare = '" + nrInregistrare + "'";
		List<RegistruIntrari> registreIntrari = getHibernateTemplate().find(query);

		if (CollectionUtils.isEmpty(registreIntrari)) {
			return null;
		} else {
			return registreIntrari.get(0);
		}
	}	
	
	@Override
	public RegistruIesiri getIesireByNrInregistrare(String nrInregistrare) {
		String query = "FROM RegistruIesiri WHERE numarInregistrare = '" + nrInregistrare + "'";
		List<RegistruIesiri> registreIesiri = getHibernateTemplate().find(query);
		
		if (CollectionUtils.isEmpty(registreIesiri)) {
			return null;
		} else {
			return registreIesiri.get(0);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIntrari> getMappedIntrariByIesireId(Long registruIesiriId) {
		List<RegistruIntrari> intrari = getHibernateTemplate().find("From RegistruIntrari reg\n"
				+ "WHERE reg.registruIesiriId = "+registruIesiriId+"");
		if (CollectionUtils.isEmpty(intrari)) {
			return null;
		}
		return intrari;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIesiri> getMappedIesiriByIntrareId(Long registruIntrariId) {
		List<RegistruIesiri> iesiri = getHibernateTemplate().find("SELECT dest.registruIesiri From RegistruIesiriDestinatar dest\n"
				+ "WHERE dest.registruIntrari.id = "+registruIntrariId);
		if (CollectionUtils.isEmpty(iesiri)) {
			return null;
		}
		return iesiri;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIesiri> getAllIesiriBySubactivityId(Long subactivityId) {
		return getHibernateTemplate().find("FROM RegistruIesiri r WHERE r.subactivity.id = ?", subactivityId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistruIntrari> getAllIntrariBySubactivityId(Long subactivityId) {
		return getHibernateTemplate().find("FROM RegistruIntrari r WHERE r.subactivity.id = ?", subactivityId);
	}
	
}
