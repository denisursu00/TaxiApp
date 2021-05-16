package ro.taxiApp.docs.presentation.client.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentationException extends Exception implements IsSerializable {
	
	private static final long serialVersionUID = 1L;

	private String code;
	
	@SuppressWarnings("unused")
	private PresentationException() {}
	
	public PresentationException(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "[" + code + "]";
	}
}