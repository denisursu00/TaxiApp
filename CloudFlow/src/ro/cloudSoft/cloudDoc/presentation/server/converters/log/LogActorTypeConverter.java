package ro.cloudSoft.cloudDoc.presentation.server.converters.log;

import ro.cloudSoft.cloudDoc.domain.log.LogActorType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogActorType;

public class LogActorTypeConverter {

	public static LogActorType getFromGwt(GwtLogActorType gwtLogActorType) {
		
		if (gwtLogActorType == null) {
			return null;
		}
		
		String gwtLogActorTypeName = gwtLogActorType.name();
		return LogActorType.valueOf(gwtLogActorTypeName);
	}
	
	public static GwtLogActorType getForGwt(LogActorType logActorType) {
		
		if (logActorType == null) {
			return null;
		}
		
		String logActorTypeName = logActorType.name();
		return GwtLogActorType.valueOf(logActorTypeName);
	}
}