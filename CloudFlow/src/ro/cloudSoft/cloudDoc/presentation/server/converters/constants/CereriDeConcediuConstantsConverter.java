package ro.cloudSoft.cloudDoc.presentation.server.converters.constants;

import java.util.Map;
import java.util.Map.Entry;

import ro.cloudSoft.cloudDoc.core.constants.CerereDeConcediuConstants;
import ro.cloudSoft.cloudDoc.core.constants.CereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCerereDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCereriDeConcediuConstants;

import com.google.common.collect.Maps;

/**
 * 
 */
public class CereriDeConcediuConstantsConverter {

	public static GwtCereriDeConcediuConstants getForGwt(CereriDeConcediuConstants constants) {
		
		Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId = constants.getConstantsByDocumentTypeId();
		Map<Long, GwtCerereDeConcediuConstants> gwtConstantsByDocumentTypeId = Maps.newHashMap();
		
		for (Entry<Long, CerereDeConcediuConstants> constantsByDocumentTypeIdEntry : constantsByDocumentTypeId.entrySet()) {
			
			Long documentTypeId = constantsByDocumentTypeIdEntry.getKey();
			CerereDeConcediuConstants constantsForCerere = constantsByDocumentTypeIdEntry.getValue();
			
			GwtCerereDeConcediuConstants gwtConstantsForCerere = new GwtCerereDeConcediuConstants();
			
			gwtConstantsForCerere.setSolicitantMetadataId(constantsForCerere.getSolicitantMetadataId());
			gwtConstantsForCerere.setInlocuitorMetadataId(constantsForCerere.getInlocuitorMetadataId());
			gwtConstantsForCerere.setDataInceputMetadataId(constantsForCerere.getDataInceputMetadataId());
			gwtConstantsForCerere.setDataSfarsitMetadataId(constantsForCerere.getDataSfarsitMetadataId());
			gwtConstantsForCerere.setAnulataMetadataId(constantsForCerere.getAnulataMetadataId());
			gwtConstantsForCerere.setAnulataMetadataPositiveValue(constantsForCerere.getAnulataMetadataPositiveValue());
			gwtConstantsForCerere.setAnulataMetadataNegativeValue(constantsForCerere.getAnulataMetadataNegativeValue());
			gwtConstantsForCerere.setAprobareDecisivaMetadataId(constantsForCerere.getAprobareDecisivaMetadataId());
			gwtConstantsForCerere.setAprobareDecisivaMetadataPositiveValue(constantsForCerere.getAprobareDecisivaMetadataPositiveValue());
			gwtConstantsForCerere.setAprobareDecisivaMetadataNegativeValue(constantsForCerere.getAprobareDecisivaMetadataNegativeValue());
			
			gwtConstantsForCerere.setAnulareTransitionNamePrefix(constantsForCerere.getAnulareTransitionNamePrefix());
			
			gwtConstantsByDocumentTypeId.put(documentTypeId, gwtConstantsForCerere);
		}
		
		return new GwtCereriDeConcediuConstants(gwtConstantsByDocumentTypeId);
	}
}