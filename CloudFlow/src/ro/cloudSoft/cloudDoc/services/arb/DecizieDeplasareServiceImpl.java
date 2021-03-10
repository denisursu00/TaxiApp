package ro.cloudSoft.cloudDoc.services.arb;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.arb.DecizieDeplasareDocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DecizieDeplasareServiceImpl implements DecizieDeplasareService, InitializingBean {
	
	private DecizieDeplasareDocumentPlugin decizieDeplasareDocumentPlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(decizieDeplasareDocumentPlugin);
	}
	
	@Override
	public List<NumarDecizieDeplasareModel> getNumarDeciziiDeplasariAprobateByReprezentant(Long reprezentantId, SecurityManager userSecurity) {
		return decizieDeplasareDocumentPlugin.getNumarDeciziiDeplasariAprobateByReprezentant(reprezentantId, userSecurity);
	}
	
	public void setDecizieDeplasareDocumentPlugin(DecizieDeplasareDocumentPlugin decizieDeplasareDocumentPlugin) {
		this.decizieDeplasareDocumentPlugin = decizieDeplasareDocumentPlugin;
	}
}
