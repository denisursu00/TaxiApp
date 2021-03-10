package ro.cloudSoft.cloudDoc.plugins.bpm.assignment;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class OrganizationUnitAssignmentHandler extends BaseAssignmentHandler {
	
	private static final long serialVersionUID = 1L;

	private final static LogHelper LOGGER = LogHelper.getInstance(OrganizationUnitAssignmentHandler.class);

	private String organizationUnitId;
	
	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {

		if (StringUtils.isBlank(organizationUnitId)) {
			
			String logMessage = "S-a incercat asignarea catre o unitate organizatorica, insa ID-ul unitatii NU este " +
				"specificat in handler. ID-ul definitiei de proces este [" + execution.getProcessDefinitionId() + "].";
			LOGGER.error(logMessage, "asignarea unui grup");
			
			throw new AppException();
		}
		Long organizationUnitIdAsLong = Long.valueOf(organizationUnitId);
		
		assignToOrganizationUnit(execution, assignable, organizationUnitIdAsLong);
	}
	
	public void setOrganizationUnitId(String organizationUnitId) {
		this.organizationUnitId = organizationUnitId;
	}
}