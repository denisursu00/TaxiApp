package ro.cloudSoft.cloudDoc.internal.monitor;

public interface MonitorStatistics {

	String getName();

	long getLastCallTime();

	long getCallCount();

	long getAverageCallTime();

	long getTotalCallTime();

	long getMinimumCallTime();

	long getMaximumCallTime();

}
