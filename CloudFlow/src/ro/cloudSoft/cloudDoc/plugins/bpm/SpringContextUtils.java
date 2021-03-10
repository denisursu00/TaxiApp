package ro.cloudSoft.cloudDoc.plugins.bpm;

import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.env.SpringContext;

public class SpringContextUtils
{
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String id) 
	{
		EnvironmentImpl env = EnvironmentImpl.getCurrent();
		SpringContext sc = (SpringContext) env.getContext("spring");
		return (T)sc.get( id );
    }
}
