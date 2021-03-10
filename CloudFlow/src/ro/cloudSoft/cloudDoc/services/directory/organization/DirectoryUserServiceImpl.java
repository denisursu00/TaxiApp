package ro.cloudSoft.cloudDoc.services.directory.organization;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.dao.directory.organization.DirectoryUserDao;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class DirectoryUserServiceImpl implements DirectoryUserService, InitializingBean {
	
	private DirectoryUserDao directoryUserDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			directoryUserDao
		);
	}

	@Override
	public List<DirectoryUser> findUsers(DirectoryUserSearchCriteria directoryUserSearchCriteria) {
		return directoryUserDao.findUsers(directoryUserSearchCriteria);
	}
	
	public void setDirectoryUserDao(DirectoryUserDao directoryUserDao) {
		this.directoryUserDao = directoryUserDao;
	}
}