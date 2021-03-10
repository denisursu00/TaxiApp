package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 */
public class GwtCereriDeConcediuConstants implements IsSerializable {

	private Map<Long, GwtCerereDeConcediuConstants> constantsByDocumentTypeId;

	@SuppressWarnings("unused")
	private GwtCereriDeConcediuConstants() {}
	
	public GwtCereriDeConcediuConstants(Map<Long, GwtCerereDeConcediuConstants> constantsByDocumentTypeId) {
		this.constantsByDocumentTypeId = constantsByDocumentTypeId;
	}
	
	public boolean isCerereDeConcediu(Long documentTypeId) {
		return constantsByDocumentTypeId.keySet().contains(documentTypeId);
	}
	
	public GwtCerereDeConcediuConstants getFor(Long documentTypeId) {
		return constantsByDocumentTypeId.get(documentTypeId);
	}
}