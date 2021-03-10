package ro.cloudSoft.cloudDoc.core.constants;

import java.util.Map;

/**
 * 
 */
public class CereriDeConcediuConstants {

	private final Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId;

	public CereriDeConcediuConstants(Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId) {
		this.constantsByDocumentTypeId = constantsByDocumentTypeId;
	}
	
	public boolean isCerereDeConcediu(Long documentTypeId) {
		return constantsByDocumentTypeId.keySet().contains(documentTypeId);
	}
	
	public CerereDeConcediuConstants getFor(Long documentTypeId) {
		return constantsByDocumentTypeId.get(documentTypeId);
	}
	
	public Map<Long, CerereDeConcediuConstants> getConstantsByDocumentTypeId() {
		return constantsByDocumentTypeId;
	}
}