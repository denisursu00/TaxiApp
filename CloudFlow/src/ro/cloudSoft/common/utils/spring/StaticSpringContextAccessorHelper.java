package ro.cloudSoft.common.utils.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Permite accesarea contextului Spring din afara bean-urilor Spring (de exemplu, dintr-o metoda statica).
 * ATENTIE: Pentru a se putea folosi, trebuie initializat contextul prin inregistrarea unui bean in Spring de tipul clasei curente.
 * 
 * 
 */
public class StaticSpringContextAccessorHelper implements ApplicationListener, DisposableBean {
	
	private static State currentApplicationContextState = State.NOT_INITIALIZED;
	private static ApplicationContext currentApplicationContext;
	
	public static ApplicationContext getApplicationContext() {
		
		if (!currentApplicationContextState.equals(State.INITIALIZED)) {
			if (currentApplicationContextState.equals(State.NOT_INITIALIZED)) {
				throw new IllegalStateException("S-a incercat luarea contextului INAINTE de initializarea acestuia.");
			} else if (currentApplicationContextState.equals(State.CLOSED)) {
				throw new IllegalStateException("S-a incercat luarea contextului DUPA inchiderea acestuia.");
			}
		}
		
		if (currentApplicationContext == null) {
			throw new IllegalStateException("Desi starea e initializata, contextul a fost gasit null.");
		}
		
		return currentApplicationContext;
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		/*
		 * In initializarea Spring exista 3 stadii:
		 * 1. Crearea bean-urilor si setarea proprietatilor lor
		 * 2. Initializarea bean-urilor (care contin logica de initializare - vezi InitializingBean)
		 * 3. Sfarsitul initializarii tuturor bean-urilor
		 * 
		 * Daca se iau si se folosesc bean-uri din contextul Spring intre 1 si 2,
		 * iar bean-urile aveau logica de initializare, aplicatia nu va functiona corect.
		 * Daca se iau si se folosesc bean-uri din contextul Spring intre 2 si 3,
		 * iar oricare din bean-urile folosite nu au apucat sa fie initializate, , aplicatia nu va functiona corect.
		 * Doar dupa 3 se poate garanta ca bean-urile pot fi folosite fara probleme.
		 * 
		 * Asadar, eu trebuie sa am contextul disponibil doar dupa stadiul 3.
		 * Pentru a detecta sfarsitul initializarii tuturor bean-urilor,
		 * ascult dupa evenimente si astept pe cel potrivit - ContextRefreshedEvent este acela.
		 * 
		 */
		
		if (!(event instanceof ContextRefreshedEvent)) {
			return;
		}
		
		ContextRefreshedEvent contextRefreshedEvent = (ContextRefreshedEvent) event;
		
		ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
		if (!isTopLevelApplicationContext(applicationContext)) {
			/*
			 * TODO Daca sunt mai multe context-uri, se va declansa cate un eveniment pentru fiecare.
			 * In forma actuala, metoda va tot inlocui contextul curent.
			 * Exista 2 solutii: sa tin cont doar de contextul principal SAU sa tin referinte spre toate contextele.
			 * Deocamdata, tin cont doar de contextul principal, insa ar fi de luat in calcul a doua varianta.
			 */
			return;
		}

		currentApplicationContext = applicationContext;
		currentApplicationContextState = State.INITIALIZED;
	}
	
	private boolean isTopLevelApplicationContext(ApplicationContext applicationContext) {
		return (applicationContext.getParent() == null);
	}
	
	@Override
	public void destroy() throws Exception {
		
		/*
		 * Dupa ce a fost inchis contextul Spring,
		 * acesta NU mai trebuie sa poata fi folosit.
		 */
		
		currentApplicationContextState = State.CLOSED;
		currentApplicationContext = null;
	}
	
	private static enum State {
		NOT_INITIALIZED, INITIALIZED, CLOSED
	}
}