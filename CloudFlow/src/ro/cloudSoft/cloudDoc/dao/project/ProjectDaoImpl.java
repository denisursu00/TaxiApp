package ro.cloudSoft.cloudDoc.dao.project;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.ProjectType;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilter;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterApplicability;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterMatchMode;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterValueType;
import ro.cloudSoft.cloudDoc.domain.project.TaskSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel;
import ro.cloudSoft.cloudDoc.domain.project.Project.ArieDeCuprindereEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterModel;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.PagingList;

public class ProjectDaoImpl extends HibernateDaoSupport implements ProjectDao {

	@Override
	public Long save(Project project) {
		if (project.getId() == null) {
			return (Long) getHibernateTemplate().save(project);
		} else {
			getHibernateTemplate().saveOrUpdate(project);
			return project.getId();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAll() {
		String query = "FROM Project";
		List<Project> projects = getHibernateTemplate().find(query);
		return projects;
	}

	@Override
	public Project getById(Long id) {
		return (Project) getHibernateTemplate().get(Project.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Project getByAbreviere(String abreviere) {
		String query = " SELECT project FROM Project project WHERE project.projectAbbreviation = ? ";
		List<Project> results = getHibernateTemplate().find(query, abreviere);
		return Iterables.getOnlyElement(results);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Task> getProjectTasks(Long projectId) {
		String query = "" + " SELECT task FROM Task task " + " WHERE task.project.id = ? ORDER BY endDate ASC";
		return getHibernateTemplate().find(query, projectId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getUserProjectsByStatus(Long userId, ProjectStatus status) {
		Date currentDate = new Date();
		
		String query = ""
				+ " SELECT DISTINCT project, gradImportanta." + NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA + "  FROM Project project "
				+ " LEFT JOIN project.participants participants "
				+ " LEFT JOIN project.gradImportanta gradImportanta "
				+ " WHERE "
				+ " 	( "
				+ " 		project.responsibleUser.id = ? "
				+ " 		OR "
				+ " 		participants.id = ? "
				+ " 	) "
				+ " 	AND "
				+ " 	project.endDate >= ? "
				+ " 	AND "
				+ " 	project.status = ? "
				+ " ORDER BY gradImportanta." + NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA + " ASC "
				;
		
		Object[] queryParams = new Object[] {
				userId,
				userId,
				currentDate,
				status
		};

		List<Object[]> resultList = getHibernateTemplate().find(query, queryParams);
		List<Project> projects = new ArrayList<>();
		for (Object[] objects : resultList) {
			projects.add((Project) objects[0]);
		}
		return projects;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<User> getProjectParticipants(Long projectId) {

		Set<User> participants = new HashSet<User>();

		String responsibleUserQuery = ""
				+ " SELECT project.responsibleUser FROM Project project "
				+ " WHERE project.id = ?";
		participants.addAll(getHibernateTemplate().find(responsibleUserQuery, projectId));
		
		String participantsQuery = ""
				+ " SELECT project.participants FROM Project project "
				+ " WHERE project.id = ?";
		participants.addAll(getHibernateTemplate().find(participantsQuery, projectId));

		return participants;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PagingList<Task> getPagedProjectTasks(final int offset, final int pageSize,
			final TaskSearchCriteria searchCriteria) {
		
		StringBuilder queryStringPart = new StringBuilder();
		queryStringPart.append(" FROM Task task ");
		queryStringPart.append(" JOIN task.project project ");
		queryStringPart.append(" LEFT JOIN task.assignments assignment ");
		queryStringPart.append(" LEFT JOIN task.subactivity subactivity ");
		queryStringPart.append(" LEFT JOIN project.participants participant ");
		queryStringPart.append(" LEFT JOIN project.responsibleUser responsibleUser ");
		queryStringPart.append(" WHERE ( ");
		queryStringPart.append(" 	project.id = :projectId ");
		queryStringPart.append(" 	AND ");
		queryStringPart.append(" 	( ");
		queryStringPart.append(" 		participant.id = :userId ");
		queryStringPart.append(" 		OR ");
		queryStringPart.append(" 		responsibleUser.id = :userId ");
		queryStringPart.append(" 	) ");
		
		if (CollectionUtils.isNotEmpty(searchCriteria.getFilters())) {
			addFiltersToQueryStringPart(queryStringPart, searchCriteria.getFilters());
		}
		
		queryStringPart.append(" ) ");
		
		final StringBuilder countQueryString = new StringBuilder();
		countQueryString.append(" SELECT COUNT(DISTINCT task) ");
		countQueryString.append(queryStringPart);
		
		int count = getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(countQueryString.toString())
						.setParameter("projectId", searchCriteria.getProjectId())
						.setParameter("userId", searchCriteria.getUserId());

				addParametersToQuery(query, searchCriteria.getFilters());
				
				Number count = (Number) query.uniqueResult();
				return count.intValue();
			}
		});
		
		final StringBuilder pageElementsQueryString = new StringBuilder();
		pageElementsQueryString.append(" SELECT DISTINCT task ");
		pageElementsQueryString.append(queryStringPart);
		
		if (searchCriteria.getSortedAttribute() != null) {
			pageElementsQueryString.append(" ORDER BY task.subactivity, task." + searchCriteria.getSortedAttribute().getPropertyName() + " " + searchCriteria.getSortedAttribute().getOrder().name());
		} else {
			pageElementsQueryString.append(" ORDER BY task.subactivity, task.endDate DESC");
		}
		
		List<Task> pageElements = getHibernateTemplate().execute(new HibernateCallback<List<Task>>() {
			@Override
			public List<Task> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(pageElementsQueryString.toString())
						.setParameter("projectId", searchCriteria.getProjectId())
						.setParameter("userId", searchCriteria.getUserId())
						.setFirstResult(offset)
						.setMaxResults(pageSize);
				
				addParametersToQuery(query, searchCriteria.getFilters());
				
				return query.list();
			}
		});
		
		return new PagingList<>(count, offset, pageElements);
	}
	
	private void addFiltersToQueryStringPart(StringBuilder queryStringPart, List<TaskFilter> filters) {
		for (TaskFilter filter : filters) {
			
			String entity = null;
			if (filter.getAplicability().equals(TaskFilterApplicability.TASK)) {
				entity = "task";
			} else if (filter.getAplicability().equals(TaskFilterApplicability.ASSIGNMENTS)) {
				entity = "assignment";
			} else if (filter.getAplicability().equals(TaskFilterApplicability.SUBACTIVITY)) {
				entity = "subactivity";
			} else {
				throw new RuntimeException("Unimplemented for filterType [" + filter.getAplicability() + "].");
			}
			
			TaskFilterMatchMode matchMode = filter.getMatchMode();
			if (matchMode.equals(TaskFilterMatchMode.LIKE)) {
				queryStringPart.append(" AND UPPER(" + entity + "." + filter.getPropertyName() + ") LIKE UPPER(:" + filter.getPropertyName() + ") "); 
			} else if (matchMode.equals(TaskFilterMatchMode.EQUAL)) {
				queryStringPart.append(" AND " + entity + "." +  filter.getPropertyName() + " = :" + filter.getPropertyName() + " ");
			} else if (matchMode.equals(TaskFilterMatchMode.IN)) {
				queryStringPart.append(" AND (" + entity + "." +  filter.getPropertyName() + ") IN (:" + filter.getPropertyName() + ") ");
			} else if (matchMode.equals(TaskFilterMatchMode.BETWEEN)) {
				queryStringPart.append(" AND ");
				queryStringPart.append(" ( ");
				queryStringPart.append(" 	" + entity + "." +  filter.getPropertyName() + " between  :" + filter.getPropertyName() + "_0 AND :" + filter.getPropertyName() + "_1");
				queryStringPart.append(" ) ");
			} else if (matchMode.equals(TaskFilterMatchMode.GREATER)) {
				queryStringPart.append(" AND " + entity + "." +  filter.getPropertyName() + " > :" + filter.getPropertyName());
			} else if (matchMode.equals(TaskFilterMatchMode.LOWER)) {
				queryStringPart.append(" AND " + entity + "." +  filter.getPropertyName() + " < :" + filter.getPropertyName());
			} else {
				throw new RuntimeException("Unimplemented for matchMode [" + filter.getMatchMode() + "].");
			}
		}
	}
	
	private void addParametersToQuery(Query query, List<TaskFilter> filters) throws HibernateException {
		if (CollectionUtils.isNotEmpty(filters)) {
			for (TaskFilter filter : filters) {
				TaskFilterMatchMode matchMode = filter.getMatchMode();
				if (matchMode.equals(TaskFilterMatchMode.LIKE)) {
					query.setParameter(filter.getPropertyName(), getTaskFilterValue(filter.getValueType(), "%" + filter.getValues().get(0) + "%"));
				} else if (matchMode.equals(TaskFilterMatchMode.EQUAL)) {
					query.setParameter(filter.getPropertyName(), getTaskFilterValue(filter.getValueType(), filter.getValues().get(0)));
				} else if (matchMode.equals(TaskFilterMatchMode.IN)) {
					List<Object> values = new ArrayList<>();
					for (String value : filter.getValues()) {
						values.add(getTaskFilterValue(filter.getValueType(), value));
					}
					query.setParameterList(filter.getPropertyName(), values);
				} else if (matchMode.equals(TaskFilterMatchMode.BETWEEN)) {
					query.setParameter(filter.getPropertyName() + "_0", getTaskFilterValue(filter.getValueType(), filter.getValues().get(0)));
					query.setParameter(filter.getPropertyName() + "_1", getTaskFilterValue(filter.getValueType(), filter.getValues().get(1)));
				} else if (matchMode.equals(TaskFilterMatchMode.GREATER)) {
					query.setParameter(filter.getPropertyName(), getTaskFilterValue(filter.getValueType(), filter.getValues().get(0)));
				} else if (matchMode.equals(TaskFilterMatchMode.LOWER)) {
					query.setParameter(filter.getPropertyName(), getTaskFilterValue(filter.getValueType(), filter.getValues().get(0)));
				} else {
					throw new RuntimeException("Unimplemented for matchMode [" + filter.getMatchMode() + "].");
				}
			}
		}
	}
	
	private Object getTaskFilterValue(TaskFilterValueType valueType, String value) {
		if (valueType.equals(TaskFilterValueType.STRING)) {
			return value;
		} else if (valueType.equals(TaskFilterValueType.INTEGER)) {
			return new Long(value);
		} else if (valueType.equals(TaskFilterValueType.DECIMAL)) {
			return new BigDecimal(value);
		} else if (valueType.equals(TaskFilterValueType.DATE)) {
			try {
				return DateUtils.parseDate(value, new String[] { FormatConstants.TASK_FILTER_VALUE_DATE_FORMAT });
			} catch (ParseException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else if (valueType.equals(TaskFilterValueType.BOOLEAN)) {
			return BooleanUtils.toBoolean(value);
		}
		throw new RuntimeException("Unknown valueType.");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getUserProjectsWithDspByStatus(Long userId, ProjectStatus status) {
		
		String responsibleUserQuery = ""
				+ " SELECT DISTINCT project "
				+ " FROM Project project "
				+ " LEFT JOIN project.participants participants "
				+ " WHERE "
				+ " 	project.status = ? "
				+ " 	AND "
				+ "		project.type = ? "
				+ " 	AND "
				+ " 	( "
				+ " 		project.responsibleUser.id = ? "
				+ " 		OR "
				+ " 		participants.id = ? "
				+ " 	) ";
				

		List<Project> responsibleUserProjects = getHibernateTemplate().find(responsibleUserQuery, new Object[] {status, ProjectType.DSP, userId, userId});
		
		return responsibleUserProjects;
	}
	
	@Override
	public ProjectEstimation findProjectEstimationById(Long projectEstimationId) {
		return getHibernateTemplate().get(ProjectEstimation.class, projectEstimationId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Task> getProjectTasksByStatus(Long projectId, TaskStatus status) {
		String query = ""
				+ " SELECT task FROM Task task "
				+ " WHERE "
				+ " 	task.project.id = ? "
				+ " 	AND "
				+ " 	task.status = ? ";
		
		Object[] queryParams = new Object[] {
				projectId,
				status
		};
		
		List<Task> tasks = getHibernateTemplate().find(query, queryParams);
		return tasks;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Subactivity> getProjectSubactivities(Long projectId){
		String query = "FROM Subactivity s\n"
				+ "WHERE s.project.id = ?";
		return getHibernateTemplate().find(query,projectId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAllOpenedWithDsp() {
		String query = "FROM Project p "
				+ "		WHERE p.type = ? "
				+ "			AND p.status = ?";
		return getHibernateTemplate().find(query, ProjectType.DSP, ProjectStatus.INITIATED);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getProjectNameById(Long projectId) {
		String query = "SELECT p.name FROM Project p WHERE p.id = ?";		
		List<String> results = getHibernateTemplate().find(query, projectId);
		return Iterables.getOnlyElement(results);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getByReportFilter(ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel filter) {
		
		List<Object> queryParameters = new ArrayList<>(); 
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT  proiect ");
		query.append(" FROM Project proiect");
		query.append(" WHERE proiect.status = ? ");
		
		queryParameters.add(ProjectStatus.INITIATED);
		
		query.append(" AND ( ");
		query.append(" EXISTS ( ");
		query.append("	 SELECT  registru");
		query.append("	 FROM RegistruIesiri registru ");
		query.append("	 JOIN registru.proiecte registruProiect ");
		query.append("	 WHERE registruProiect  = proiect ");
		
		if (filter.getDataInceput() != null) {
			query.append(" AND " + " registru.dataInregistrare >= ? ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}		
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + " registru.dataInregistrare <= ? ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}
		
		query.append(" )");
		
		query.append(" OR EXISTS ( ");
		query.append("	 SELECT  registru");
		query.append("	 FROM RegistruIntrari registru ");
		query.append("	 JOIN registru.proiecte registruProiect ");
		query.append("	 WHERE registruProiect = proiect ");
		
		if (filter.getDataInceput() != null) {
			query.append(" AND " + " registru.dataInregistrare >= ? ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}	
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + " registru.dataInregistrare <= ? ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}
		
		query.append(" )");
		
		query.append(" OR EXISTS ( ");
		query.append("	 SELECT  task");
		query.append("	 FROM Task task ");
		query.append("	 WHERE task.project  = proiect ");
		query.append("	 AND task.status  = ?");
		
		queryParameters.add(TaskStatus.FINALIZED);
		
		if (filter.getDataInceput() != null) {
			query.append(" AND " + " task.finalizedDate >= ? ");
			queryParameters.add(filter.getDataInceput());
		}	
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + " task.finalizedDate <= ? ");
			queryParameters.add(filter.getDataSfarsit());
		}
		
		query.append(" )");
		
		query.append(" )");
		
		List<Project> projects = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		
		return projects;
	}


	public int getNrTaskuriProiectFinalizateInTermen(final Long projectId) {
		Number nrTaskuri =  getHibernateTemplate().execute(new HibernateCallback<Number>() {

			@Override
			public Number doInHibernate(Session session) throws HibernateException, SQLException {
				
				return (Number) session.createQuery(""
						+ "SELECT count(*) "
						+ "FROM Task task "
						+ "WHERE "
						+ "	task.project.id = :projectId "
						+ "		AND"
						+ "	task.status = :taskStatus "
						+ "		AND"
						+ "	task.finalizedDate <= task.endDate ")
						.setParameter("projectId", projectId)
						.setParameter("taskStatus", TaskStatus.FINALIZED).iterate().next();
			}
			
		});
		return nrTaskuri.intValue();
	}

	@Override
	public List<Project> getDspProjectsByDashboardProiecteFilterModel(DashboardProiecteFilterModel filter) {
		
		List<Object> queryParams = new ArrayList<>();
		StringBuilder query = new StringBuilder(""
						+ "SELECT DISTINCT project "
						+ "FROM Project project "
						+ "LEFT JOIN project.responsibleUser responsibleUser "
						+ "LEFT JOIN project.tasks task "
						+ "LEFT JOIN project.domeniuBancar domeniuBancar "
						+ "LEFT JOIN project.gradImportanta gradImportanta "
						+ "LEFT JOIN project.incadrareProiect incadrareProiect "
						+ "WHERE (project.type = ?) ");
		queryParams.add( ProjectType.DSP);
		
		if (filter.getAbreviereProiect() != null) {
			query.append(" AND  (project.projectAbbreviation = ?) ");
			queryParams.add( filter.getAbreviereProiect());
		}
		if (filter.getIdUserResponsabil() != null) {
			query.append(" AND  (responsibleUser.id = ?) ");
			queryParams.add( filter.getIdUserResponsabil());
		}
		if (filter.getTermenFinalizareDeLa() != null) {
			query.append(" AND  (project.endDate >= ?) ");
			queryParams.add( filter.getTermenFinalizareDeLa());
		}
		if (filter.getTermenFinalizarePanaLa() != null) {
			query.append(" AND  (project.endDate <= ?) ");
			queryParams.add( filter.getTermenFinalizarePanaLa());
		}
		if (filter.getActiuneDeUrmat() != null) {
			query.append(" AND  (task.name = ?) ");
			queryParams.add( filter.getActiuneDeUrmat());
		}
		if (filter.getDeadlineActiuniDeUrmatDeLa() != null) {
			query.append(" AND  (task.endDate >= ?) ");
			queryParams.add( filter.getDeadlineActiuniDeUrmatDeLa());
		}
		if (filter.getDeadlineActiuniDeUrmatPanaLa() != null) {
			query.append(" AND  (task.endDate <= ?) ");
			queryParams.add( filter.getDeadlineActiuniDeUrmatPanaLa());
		}
		if (filter.getArieActivitateBancaraId() != null) {			
			query.append(" AND  (domeniuBancar.id = ?) ");
			queryParams.add( filter.getArieActivitateBancaraId());
		}
		if (filter.getImportantaId() != null) {			
			query.append(" AND  (gradImportanta.id = ?) ");
			queryParams.add( filter.getImportantaId());
		}
		if (filter.getIncadrareProiectId() != null) {			
			query.append(" AND  (incadrareProiect.id = ?) ");
			queryParams.add( filter.getIncadrareProiectId());
		}
		if (filter.isProiectInitiatDeARB() != null) {			
			query.append(" AND  (project.proiectInitiatArb = ?) ");
			queryParams.add( filter.isProiectInitiatDeARB());
		}
		if (filter.getAriaDeCuprindere() != null) {			
			query.append(" AND  (project.arieDeCuprindere = ?) ");
			queryParams.add( ArieDeCuprindereEnum.valueOf(filter.getAriaDeCuprindere().toUpperCase()));
		}
		List<Project> result ;
		if (queryParams.size() == 1) {
			result = getHibernateTemplate().find(query.toString(), queryParams.get(0));
		} else {
			result = getHibernateTemplate().find(query.toString(), queryParams.toArray());
		}
		return (List<Project>)result;
	}

	@Override
	public List<String> getAllAbrevieriProiecte() {
		String query = ""
				+ " SELECT DISTINCT project.projectAbbreviation "
				+ "	FROM Project project ";
		
		return getHibernateTemplate().find(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean existsAbbreviation(String projectAbbreviation) {
		String query = ""
				+ " SELECT project FROM Project project "
				+ " WHERE project.projectAbbreviation = ? ";
		Object[] queryParameters = new Object[] { projectAbbreviation };
		List<Project> projects = getHibernateTemplate().find(query, queryParameters);
		return CollectionUtils.isNotEmpty(projects);
	}
	
	

	@Override
	@SuppressWarnings("unchecked")
	public boolean existsName(String name) {
		String query = ""
				+ " SELECT project FROM Project project "
				+ " WHERE project.name = ? ";
		Object[] queryParameters = new Object[] { name };
		List<Project> projects = getHibernateTemplate().find(query, queryParameters);
		return CollectionUtils.isNotEmpty(projects);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAllByEndDate(Date endDate) {
		String query = "FROM Project p"
				+ "		LEFT JOIN FETCH p.responsibleUser"
				+ "		WHERE p.endDate=?"
				+ "			AND p.status = ?";
		return getHibernateTemplate().find(query, endDate, ProjectStatus.INITIATED);
	}

}