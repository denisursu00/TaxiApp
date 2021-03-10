package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;

public interface CheltuieliReprezentantArbDao {

	CheltuialaReprezentantArb findById(Long cheltuialaReprezentantArbId);
	
	void deleteCheltuieliReprezentatiArb(List<Long> cheltuieliReprezentantiArbIds);
}
