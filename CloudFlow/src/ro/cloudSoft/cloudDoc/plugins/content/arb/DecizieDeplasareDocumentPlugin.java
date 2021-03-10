package ro.cloudSoft.cloudDoc.plugins.content.arb;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;

public interface DecizieDeplasareDocumentPlugin {
	
	List<NumarDecizieDeplasareModel> getNumarDeciziiDeplasariAprobateByReprezentant(Long reprezentantId, SecurityManager userSecurity);
}
