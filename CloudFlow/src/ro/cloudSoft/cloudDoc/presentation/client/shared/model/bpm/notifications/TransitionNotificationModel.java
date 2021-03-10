package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class TransitionNotificationModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_EMAIL_SUBJECT_TEMPLATE = "emailSubjectTemplate";
	public static final String PROPERTY_EMAIL_CONTENT_TEMPLATE = "emailContentTemplate";
	
	private TransitionNotificationModelType type;
	
	protected TransitionNotificationModel(TransitionNotificationModelType type) {
		this.type = type;
	}
	
	public TransitionNotificationModelType getType() {
		return type;
	}
	
	public String getTypeLabel() {
		return type.getLabel();
	}
	
	public Long getId() {
		return get(PROPERTY_ID);
	}
	
	public String getEmailSubjectTemplate() {
		return get(PROPERTY_EMAIL_SUBJECT_TEMPLATE);
	}
	
	public String getEmailContentTemplate() {
		return get(PROPERTY_EMAIL_CONTENT_TEMPLATE);
	}

	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}
	
	public void setEmailSubjectTemplate(String emailSubjectTemplate) {
		set(PROPERTY_EMAIL_SUBJECT_TEMPLATE, emailSubjectTemplate);
	}
	
	public void setEmailContentTemplate(String emailContentTemplate) {
		set(PROPERTY_EMAIL_CONTENT_TEMPLATE, emailContentTemplate);
	}
}