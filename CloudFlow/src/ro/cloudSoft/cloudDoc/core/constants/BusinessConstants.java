package ro.cloudSoft.cloudDoc.core.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class BusinessConstants implements InitializingBean {
	
	/**
	 * Instanta statica ce va fi setata la rulearea constructorului
	 * <br><br>
	 * NOTA: Aceasta instanta a fost creata pentru ca exista multe zone care folosesc valorile, dar nu sunt
	 * bean-uri Spring.
	 */
	private static BusinessConstants instance;

	private final String groupNameAdmins;

	private final String applicationUserName;
	
	/**
	 * Construieste o instanta cu valorile constantelor.
	 * Acest constructor va fi apelat de Spring.
	 */
	public BusinessConstants(String groupNameAdmins, String applicationUserName) {
		
		this.groupNameAdmins = groupNameAdmins;
		this.applicationUserName = applicationUserName;
		/*
		 * Trebuie sa tin instanta intr-o variabila statica intrucat exista zone non-Spring care depind de valorile
		 * constantelor.
		 */
		instance = this;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			groupNameAdmins,
			applicationUserName
		);
	}
	
	/**
	 * Obtine instanta cu valorile completate.
	 * <br><br>
	 * NOTA: Metoda trebuie apelata dupa ce s-a initializat instanta prin intermediul Spring.
	 * 
	 * @throws IllegalStateException daca s-a apelat inainte de / fara a se initializa de catre Spring
	 */
	public static BusinessConstants get() {
		if (instance == null) {
			throw new IllegalStateException("Constantele nu au fost initializate (prin intermediul Spring).");
		}
		return instance;
	}
	
	public String getGroupNameAdmins() {
		return groupNameAdmins;
	}

	public String getApplicationUserName() {
		return applicationUserName;
	}
}