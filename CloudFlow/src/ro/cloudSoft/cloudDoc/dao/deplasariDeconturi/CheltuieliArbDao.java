package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;

public interface CheltuieliArbDao {

	CheltuialaArb findById(Long cheltuialaArbId);
	
	void deleteCheltuieliArb(List<Long> cheltuieliArbIds);
}
