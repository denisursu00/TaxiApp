package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Calendar;

public class DocumentVersionInfo {

	private Integer number;
	private String authorId;
	private Calendar date;
	
	public DocumentVersionInfo(Integer number, String authorId, Calendar date) {
		this.number = number;
		this.authorId = authorId;
		this.date = date;
	}
	
	public Integer getNumber() {
		return number;
	}
	public String getAuthorId() {
		return authorId;
	}
	public Calendar getDate() {
		return date;
	}
}