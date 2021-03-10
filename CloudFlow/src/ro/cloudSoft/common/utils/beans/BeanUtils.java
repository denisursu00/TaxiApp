package ro.cloudSoft.common.utils.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Contine metode utilitare legate de bean-uri Java.
 * 
 * 
 * @version 2011.01.19
 */
public class BeanUtils {
	
	/**
	 * Returneaza o lista avand valoarea proprietatii cu numele dat pentru fiecare element din colectie.
	 * Ordinea colectiei va fi pastrata.
	 * 
	 * @param <PROP> tipul proprietatii
	 * @param <BEAN> tipul fiecarui element din colectie
	 * @param beans colectia de bean-uri
	 * @param propertyName numele proprietatii
	 */
	@SuppressWarnings("unchecked")
	public static <PROP, BEAN> List<PROP> getPropertyValues(Collection<BEAN> beans, String propertyName) {
		List<PROP> list = Lists.newArrayListWithCapacity(beans.size());
		for (BEAN bean : beans) {
			list.add((PROP) getPropertyValue(bean, propertyName));
		}
		return list;
	}
	
	/**
	 * Returneaza colectia data ca un map in care cheia e valoarea proprietatii cu numele specificat
	 * pentru fiecare element. Map-ul va pastra ordinea elementelor din colectie.
	 * 
	 * @param <PROP> tipul proprietatii
	 * @param <BEAN> tipul fiecarui element din colectie
	 * @param collection colectia de elemente
	 * @param propertyName numele proprietatii
	 */
	public static <PROP, BEAN> Map<PROP, BEAN> getAsMap(Collection<BEAN> collection, String propertyName) {
		Map<PROP, BEAN> map = Maps.newLinkedHashMap();
		for (BEAN item : collection) {
			@SuppressWarnings("unchecked")
			// Must cast the property value or else the Maven compiler will complain.
			PROP propertyValue = (PROP) getPropertyValue(item, propertyName);
			map.put(propertyValue, item);
		}
		return map;
	}
	
	/**
	 * Obtine un map in care fiecare cheie si valoare sunt valorile proprietatilor 
	 * cu numele dat din bean-urile specificate.
	 */
	public static <B, K, V> Map<K, V> getPropertyMap(Collection<B> beans,
			String keyProperyName, String valuePropertyName) {
		
		Map<K, V> propertyMap = Maps.newHashMap();
		
		for (B bean : beans) {
			try {
				
				@SuppressWarnings("unchecked")
				K key = (K) PropertyUtils.getProperty(bean, keyProperyName);
				@SuppressWarnings("unchecked")
				V value = (V) PropertyUtils.getProperty(bean, valuePropertyName);
				
				propertyMap.put(key, value);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		return propertyMap;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyValue(Object bean, String name) {
		try {
			return (T) PropertyUtils.getProperty(bean, name);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}