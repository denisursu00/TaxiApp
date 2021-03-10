package ro.cloudSoft.cloudDoc.scheduledTasks;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public abstract class QuartzScheduledJobBean extends QuartzJobBean implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(QuartzScheduledJobBean.class);
	
	public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";
	public static final boolean DEFAULT_HIBERNATE_OPEN_SESSION_ENABLED = true;
	public static final boolean DEFAULT_SECURITY_MANAGER_EXPOSED = false;
	
	private boolean hibernateOpenSessionEnabled = DEFAULT_HIBERNATE_OPEN_SESSION_ENABLED;
	private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;
	
	private boolean securityManagerExposed = DEFAULT_SECURITY_MANAGER_EXPOSED;	
	private BusinessConstants businessConstants;	
	private SecurityManagerFactory securityManagerFactory;	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (isHibernateOpenSessionEnabled()) {
			DependencyInjectionUtils.checkRequiredDependencies(sessionFactoryBeanName);
		}
		if (isSecurityManagerExposed()) {
			DependencyInjectionUtils.checkRequiredDependencies(businessConstants, securityManagerFactory);
		}
	}
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		SessionFactory sessionFactory = null;
		try {				
			if (isHibernateOpenSessionEnabled()) {
				sessionFactory = lookupSessionFactory(jobExecutionContext);
				LOGGER.debug("Opening single Hibernate Session", "executeInternal");
				
				Session session = getSession(sessionFactory);
				SessionHolder sessionHolder = new SessionHolder(session);
				TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
			}
			if (isSecurityManagerExposed()) {
				SecurityManager securityManager = securityManagerFactory.getSecurityManager(businessConstants.getApplicationUserName());
				SecurityManagerHolder.setSecurityManager(securityManager);
			}			
			doExecuteInternal(jobExecutionContext);
			
		} catch (Exception e) {	
			throw new JobExecutionException(e);
		} finally {
			if (isHibernateOpenSessionEnabled()) {
				try {
					SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
					closeSession(sessionHolder.getSession(), sessionFactory);
				} catch (Exception e) {
					LOGGER.error("Exception while trying to obtain and closing Hibernate Session", "executeInternal");	
				}
			}
			if (isSecurityManagerExposed()) {
				SecurityManagerHolder.clearSecurityManager();
			}
		}
	}
	
	public abstract void doExecuteInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException;
	
	private SessionFactory lookupSessionFactory(JobExecutionContext jobExecutionContext) {
		try {
			ApplicationContext ctx = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
			return ctx.getBean(getSessionFactoryBeanName(), SessionFactory.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}

	protected void closeSession(Session session, SessionFactory sessionFactory) {
		try {
			if (session != null && session.isOpen() && session.isConnected()) {
				session.flush();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Hibernate Session flushed", "closeSession");
				}				
			}
		} finally {
			try {
				SessionFactoryUtils.closeSession(session);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Hibernate Session closed", "closeSession");
				}
			} catch (Exception e) {
				LOGGER.error("Exception while closing Hibernate Session", e, "closeSession");
			}			
		}
	}

	public boolean isHibernateOpenSessionEnabled() {
		return hibernateOpenSessionEnabled;
	}

	public void setHibernateOpenSessionEnabled(boolean hibernateOpenSessionEnabled) {
		this.hibernateOpenSessionEnabled = hibernateOpenSessionEnabled;
	}

	public String getSessionFactoryBeanName() {
		return sessionFactoryBeanName;
	}

	public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
		this.sessionFactoryBeanName = sessionFactoryBeanName;
	}

	public boolean isSecurityManagerExposed() {
		return securityManagerExposed;
	}

	public void setSecurityManagerExposed(boolean securityManagerExposed) {
		this.securityManagerExposed = securityManagerExposed;
	}

	public BusinessConstants getBusinessConstants() {
		return businessConstants;
	}

	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}

	public SecurityManagerFactory getSecurityManagerFactory() {
		return securityManagerFactory;
	}

	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}	
}
