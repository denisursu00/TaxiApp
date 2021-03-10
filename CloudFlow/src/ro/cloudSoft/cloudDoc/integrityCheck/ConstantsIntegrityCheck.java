package ro.cloudSoft.cloudDoc.integrityCheck;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Verifica daca exista constantele necesare aplicatiei.
 * 
 * 
 */
public class ConstantsIntegrityCheck implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(ConstantsIntegrityCheck.class);
	
	private final boolean throwExceptionOnFailure;
	private final BusinessConstants businessConstants;
	
	public ConstantsIntegrityCheck(boolean throwExceptionOnFailure,
			BusinessConstants businessConstants) {
		
		this.throwExceptionOnFailure = throwExceptionOnFailure;		
		this.businessConstants = businessConstants;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			throwExceptionOnFailure,
			businessConstants
		);
	}
	
	/** Returneaza da sau nu in functie de conditia data. */
	private static String yesOrNo(boolean condition) {
		return (condition) ? "DA" : "NU";
	}
	
	public void check() {
		checkBusinessConstants();
	}
	
	private void checkBusinessConstants() {		
		String groupNameAdmins = businessConstants.getGroupNameAdmins();
	}
	
	/** Notifica aplicatia ca s-a gasit o constanta. */
	private void notifyOk(String message) {
		LOGGER.info(message, "verificarea constantelor");
	}
	
	/** Executa actiunea corespunzatoare daca nu se gaseste o constanta, afisand mesajul dat. */
	private void fail(String message) {
		if (throwExceptionOnFailure) {
			throw new IllegalStateException(message);
		} else {
			LOGGER.error(message, "verificarea constantelor");
		}
	}
}