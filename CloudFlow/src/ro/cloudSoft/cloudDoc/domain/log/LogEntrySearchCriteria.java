package ro.cloudSoft.cloudDoc.domain.log;

import java.util.Date;

public class LogEntrySearchCriteria {

	private LogLevel level;
	
	private Date timeStartDate;
	private Date timeEndDate;
	
	private String moduleText;
	private String operationText;
	
	private LogActorType actorType;
	private String actorDisplayNameText;
	private Long userId;
	
	private String messageText;
	private String exceptionText;
	
	public LogLevel getLevel() {
		return level;
	}
	public Date getTimeStartDate() {
		return timeStartDate;
	}
	public Date getTimeEndDate() {
		return timeEndDate;
	}
	public String getModuleText() {
		return moduleText;
	}
	public String getOperationText() {
		return operationText;
	}
	public LogActorType getActorType() {
		return actorType;
	}
	public String getActorDisplayNameText() {
		return actorDisplayNameText;
	}
	public Long getUserId() {
		return userId;
	}
	public String getMessageText() {
		return messageText;
	}
	public String getExceptionText() {
		return exceptionText;
	}
	
	public void setLevel(LogLevel level) {
		this.level = level;
	}
	public void setTimeStartDate(Date timeStartDate) {
		this.timeStartDate = timeStartDate;
	}
	public void setTimeEndDate(Date timeEndDate) {
		this.timeEndDate = timeEndDate;
	}
	public void setModuleText(String moduleText) {
		this.moduleText = moduleText;
	}
	public void setOperationText(String operationText) {
		this.operationText = operationText;
	}
	public void setActorType(LogActorType actorType) {
		this.actorType = actorType;
	}
	public void setActorDisplayNameText(String actorDisplayNameText) {
		this.actorDisplayNameText = actorDisplayNameText;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setExceptionText(String exceptionText) {
		this.exceptionText = exceptionText;
	}
}