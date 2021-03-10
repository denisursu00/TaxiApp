package ro.cloudSoft.cloudDoc.services.arb;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;

public interface DecizieDeplasareService {
	
	List<NumarDecizieDeplasareModel> getNumarDeciziiDeplasariAprobateByReprezentant(Long reprezentantId, SecurityManager userSecurity);
}
