package ro.cloudSoft.cloudDoc.internal.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.internal.monitor.Monitor;
import ro.cloudSoft.cloudDoc.internal.monitor.MonitorFactory;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;



/**
 * An aspect that monitors the performance of all three repositories used in the application.
 * @see AccountRepository
 * @see RestaurantRepository
 * @see RewardRepository
 */
@Aspect
public class RepositoryPerformanceMonitor implements InitializingBean {

	private static final LogHelper logger = LogHelper.getInstance(RepositoryPerformanceMonitor.class);

	private MonitorFactory monitorFactory;

	public RepositoryPerformanceMonitor(MonitorFactory monitorFactory) {
		this.monitorFactory = monitorFactory;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			monitorFactory
		);
	}

	/**
	 * Times repository method invocations and outputs performance results to a Log4J logger.
	 * @param repositoryMethod The join point representing the intercepted repository method
	 * @return The object returned by the target method
	 * @throws Throwable if thrown by the target method
	 */
	@Around("execution(public * ro.cloudSoft.cloudDoc.plugins.content.*.*Plugin.*(..))")
	public Object monitor(ProceedingJoinPoint repositoryMethod) throws Throwable {
		String name = createJoinPointTraceName(repositoryMethod);
		Monitor monitor = monitorFactory.start(name);
		try
        {
			return repositoryMethod.proceed();
		} 
        finally
        {
			monitor.stop();
			logger.info(monitor.toString(), null);
		}
	}

	private String createJoinPointTraceName(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		StringBuilder sb = new StringBuilder();
		sb.append(signature.getDeclaringType().getSimpleName());
		sb.append('.').append(signature.getName());
		return sb.toString();
	}
}