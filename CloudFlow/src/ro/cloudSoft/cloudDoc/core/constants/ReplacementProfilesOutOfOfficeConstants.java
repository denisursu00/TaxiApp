package ro.cloudSoft.cloudDoc.core.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class ReplacementProfilesOutOfOfficeConstants implements InitializingBean {
	
	public static final String DATE_FORMAT = "dd.MM.yyyy";

	private String templatingPlaceholderForRequesterName;
	private String templatingPlaceholderForStartDate;
	private String templatingPlaceholderForEndDate;
	
	private String defaultTemplateForEmailSubject;
	private String defaultTemplateForEmailBody;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			
			templatingPlaceholderForRequesterName,
			templatingPlaceholderForStartDate,
			templatingPlaceholderForEndDate,
			
			defaultTemplateForEmailSubject,
			defaultTemplateForEmailBody
		);
	}

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