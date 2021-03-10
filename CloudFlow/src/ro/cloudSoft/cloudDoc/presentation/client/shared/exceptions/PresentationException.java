package ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Reprezinta o exceptie folosita pe partea de client.
 * Exceptii de acest tip vor fi trimise prin RPC utilizatorului, asadar nu
 * trebuie sa contina decat acele informatii care sunt relevante pentru
 * utilizator si interfata grafica.
 * 
 * 
 */
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