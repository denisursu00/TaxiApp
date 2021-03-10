package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private byte[] data;
	
	@SuppressWarnings("unused")
	private Attachment() {}
	
	public Attachment(String name) {
		this.name = name;
	}
	
	public Attachment(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	public byte[] getData() {
		return data;
	}
}