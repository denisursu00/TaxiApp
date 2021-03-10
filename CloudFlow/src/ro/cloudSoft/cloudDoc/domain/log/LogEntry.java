package ro.cloudSoft.cloudDoc.domain.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 */
@Entity
@Table(name="log_entries")
public class LogEntry {
	
	private Long id;
	
	private Date time;
	private LogLevel level;
	
	private String module;
	private String operation;
	
	private LogActorType actorType;
	private String actorDisplayName;
	private Long userId;
	
	private String message;
	private String exception;
	
	@Id
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "log_time", nullable = false)
	public Date getTime() {
		return time;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "log_level", nullable = false)
	public LogLevel getLevel() {
		return level;
	}
	
	@Column(name = "module", nullable = false)
	public String getModule() {
		return module;
	}
	
	@Column(name = "operation")
	public String getOperation() {
		return operation;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "actor_type", nullable = false)
	public LogActorType getActorType() {
		return actorType;
	}
	
	@Column(name = "actor_display_name", nullable = false)
	public String getActorDisplayName() {
		return actorDisplayName;
	}
	
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}
	
	@Column(name = "message", nullable = false)
	public String getMessage() {
		return message;
	}
	
	@Column(name = "exception")
	public String getException() {
		return exception;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public void setLevel(LogLevel level) {
		this.level = level;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public void setActorType(LogActorType actorType) {
		this.actorType = actorType;
	}
	public void setActorDisplayName(String actorDisplayName) {
		this.actorDisplayName = actorDisplayName;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
}