package ro.cloudSoft.cloudDoc.plugins;

import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class JexlProjectFunctions {

	private ProjectDao getProjectDao() {
		return SpringUtils.getBean(ProjectDao.class);
	}
	
	public boolean projectNameExistsValidation(String projectName) {
		return !getProjectDao().existsName(projectName);
	}
	
	public boolean projectAbbreviationExistsValidation(String projectAbbreviation) {
		return !getProjectDao().existsAbbreviation(projectAbbreviation);
	}
}
