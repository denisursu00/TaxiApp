package ro.taxiApp.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Manager pentru o colectie de entitati tinute in sesiunea HTTP
 * 
 * 
 *
 * @param <I> tipul identificatorilor (trebuie sa fie serializabil)
 * @param <E> tipul entitatilor (trebuie sa fie serializabil)
 */
public class HttpSessionEntityManager<I extends Serializable, E extends Serializable> {

	private final String sessionAttributeName;
	
	/**
	 * @param sessionAttributeName numele atributului de sesiune HTTP unde va fi tinuta colectia de entitati
	 */
	public HttpSessionEntityManager(String sessionAttributeName) {
		this.sessionAttributeName = sessionAttributeName;
	}

	@SuppressWarnings("unchecked")
	private Map<I, E> getEntityMap(HttpSession session) {
		return (Map<I, E>) session.getAttribute(this.sessionAttributeName);
	}
	
	public void addEntity(HttpSession session, I identifier, E entity) {

		Map<I, E> entityMap = getEntityMap(session);
		
		if (entityMap == null) {
			entityMap = new HashMap<I, E>();
		}
		
		entityMap.put(identifier, entity);
		
		session.setAttribute(sessionAttributeName, entityMap);
	}

	public void removeEntities(HttpSession session, List<? extends I> identifiers) {

		Map<I, E> entityMap = getEntityMap(session);
		
		if (entityMap == null) {
			return;
		}
		
		for (I identifier : identifiers) {
			entityMap.remove(identifier);
		}
	}
	
	public void removeAllEntities(HttpSession session) {

		Map<I, E> entityMap = getEntityMap(session);
		
		if (entityMap == null) {
			return;
		}
		
		entityMap.clear();
	}
	
	/**
	 * Returneaza colectia de entitati existente pe sesiune, eliminandu-le de pe sesiune.
	 */
	public List<E> getEntities(HttpSession session) {
		
		List<E> entities = new ArrayList<E>();

		Map<I, E> entityMap = getEntityMap(session);
		
		if (entityMap != null) {
			entities.addAll(entityMap.values());
			entityMap.clear();
		}
		
		return entities;
	}
	
	/**
	 * Returneaza entitatea corespunzatoare identificatorului dat. Entitatea NU va fi eliminata de pe sesiune.
	 */
	public E getEntity(HttpSession session, I identifier) {
		
		Map<I, E> entityMap = getEntityMap(session);
		
		if (entityMap != null) {
			return entityMap.get(identifier);
		}
		
		return null;
	}
}