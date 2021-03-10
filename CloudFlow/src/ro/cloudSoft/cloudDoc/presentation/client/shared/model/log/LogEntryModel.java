package ro.cloudSoft.cloudDoc.presentation.client.shared.model.log;

import java.util.Date;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.additionalPropertyProviders.EnumAdditionalPropertyProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.AdditionalPropertyProviderForModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model.BaseModelWithAdditionalProperties;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogEntryModel extends BaseModelWithAdditionalProperties implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_TIME = "time";
	public static final String PROPERTY_LEVEL = "level";
	public static final String PROPERTY_MODULE = "module";
	public static final String PROPERTY_OPERATION = "operation";
	public static final String PROPERTY_ACTOR_TYPE = "actorType";
	public static final String PROPERTY_ACTOR_DISPLAY_NAME = "actorDisplayName";
	public static final String PROPERTY_USER_ID = "userId";
	public static final String PROPERTY_MESSAGE = "message";
	public static final String PROPERTY_EXCEPTION = "exception";
	
	public static final String PROPERTY_LEVEL_AS_STRING = "levelAsString";
	
	@Override
	public AdditionalPropertyProviderForModel[] getAdditionalPropertyProviders() {
		return new AdditionalPropertyProviderForModel[] {
			new EnumAdditionalPropertyProvider(PROPERTY_LEVEL_AS_STRING, PROPERTY_LEVEL, this)
		};
	}
	
	public Long getId() {
		return get(PROPERTY_ID);
	}
	
	public Date getTime() {
		return get(PROPERTY_TIME);
	}
	
	public GwtLogLevel getLevel() {
		return get(PROPERTY_LEVEL);
	}
	
	public String getModule() {
		return get(PROPERTY_MODULE);
	}
	
	public String getOperation() {
		return get(PROPERTY_OPERATION);
	}
	
	public GwtLogActorType getActorType() {
		return get(PROPERTY_ACTOR_TYPE);
	}
	
	public String getActorDisplayName() {
		return get(PROPERTY_ACTOR_DISPLAY_NAME);
	}
	
	public Long getUserId() {
		return get(PROPERTY_USER_ID);
	}
	
	public String getMessage() {
		return get(PROPERTY_MESSAGE);
	}
	
	public String getException() {
		return get(PROPERTY_EXCEPTION);
	}
	
	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}
	
	public void setTime(Date time) {
		set(PROPERTY_TIME, time);
	}
	
	public void setLevel(GwtLogLevel level) {
		set(PROPERTY_LEVEL, level);
	}
	
	public void setModule(String module) {
		set(PROPERTY_MODULE, module);
	}
	
	public void setOperation(String operation) {
		set(PROPERTY_OPERATION, operation);
	}
	
	public void setActorType(GwtLogActorType actorType) {
		set(PROPERTY_ACTOR_TYPE, actorType);
	}
	
	public void setActorDisplayName(String actorDisplayName) {
		set(PROPERTY_ACTOR_DISPLAY_NAME, actorDisplayName);
	}
	
	public void setUserId(Long userId) {
		set(PROPERTY_USER_ID, userId);
	}
	
	public void setMessage(String message) {
		set(PROPERTY_MESSAGE, message);
	}
	
	public void setException(String exception) {
		set(PROPERTY_EXCEPTION, exception);
	}
}