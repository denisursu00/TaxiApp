package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtReplacementProfilesOutOfOfficeConstants implements IsSerializable {

	private String templatingPlaceholderForRequesterName;
	private String templatingPlaceholderForStartDate;
	private String templatingPlaceholderForEndDate;
	
	private String defaultTemplateForEmailSubject;
	private String defaultTemplateForEmailBody;

	public String getTemplatingPlaceholderForRequesterName() {
		return templatingPlaceholderForRequesterName;
	}

	public String getTemplatingPlaceholderForStartDate() {
		return templatingPlaceholderForStartDate;
	}

	public String getTemplatingPlaceholderForEndDate() {
		return templatingPlaceholderForEndDate;
	}

	public String getDefaultTemplateForEmailSubject() {
		return defaultTemplateForEmailSubject;
	}

	public String getDefaultTemplateForEmailBody() {
		return defaultTemplateForEmailBody;
	}

	public void setTemplatingPlaceholderForRequesterName(String templatingPlaceholderForRequesterName) {
		this.templatingPlaceholderForRequesterName = templatingPlaceholderForRequesterName;
	}

	public void setTemplatingPlaceholderForStartDate(String templatingPlaceholderForStartDate) {
		this.templatingPlaceholderForStartDate = templatingPlaceholderForStartDate;
	}

	public void setTemplatingPlaceholderForEndDate(String templatingPlaceholderForEndDate) {
		this.templatingPlaceholderForEndDate = templatingPlaceholderForEndDate;
	}

	public void setDefaultTemplateForEmailSubject(String defaultTemplateForEmailSubject) {
		this.defaultTemplateForEmailSubject = defaultTemplateForEmailSubject;
	}

	public void setDefaultTemplateForEmailBody(String defaultTemplateForEmailBody) {
		this.defaultTemplateForEmailBody = defaultTemplateForEmailBody;
	}
}