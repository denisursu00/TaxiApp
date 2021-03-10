package ro.cloudSoft.cloudDoc.internal.monitor.jamon;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.internal.monitor.GlobalMonitorStatistics;
import ro.cloudSoft.cloudDoc.internal.monitor.Monitor;
import ro.cloudSoft.cloudDoc.internal.monitor.MonitorFactory;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.jamonapi.MonitorComposite;

public class JamonMonitorFactory implements MonitorFactory, GlobalMonitorStatistics, InitializingBean
{

	private com.jamonapi.MonitorFactoryInterface monitorFactory = com.jamonapi.MonitorFactory.getFactory();
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies();
	}

	public Monitor start(String name) {
		return new JamonMonitor(monitorFactory.start(name));
	}

	public long getCallsCount() {
		return (long) getMonitors().getHits();
	}

	public long getTotalCallTime() {
		return (long) getMonitors().getTotal();
	}

	public Date getLastAccessTime() {
		return getMonitors().getLastAccess();
	}

	public MonitorComposite getMonitors() {
		return monitorFactory.getRootMonitor();
	}

	public long averageCallTime(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getAvg();
	}

	public long callCount(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getHits();
	}

	public long lastCallTime(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getLastValue();
	}

	public long maximumCallTime(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getMax();
	}

	public long minimumCallTime(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getMin();
	}

	public long totalCallTime(String methodName) {
		return (long) monitorFactory.getMonitor(methodName, "ms.").getTotal();
	}

}
