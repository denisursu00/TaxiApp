package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Calendar;

public class DocumentHistory {
	

	private Long actorId;
	private String transitionName;
	private Calendar transitionDate;
	
	public Long getActorId() {
		return actorId;
	}
	public String getTransitionName() {
		return transitionName;
	}
	public Calendar getTransitionDate() {
		return transitionDate;
	}
	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}
	public void setTransitionDate(Calendar transitionDate) {
		this.transitionDate = transitionDate;
	}
}
