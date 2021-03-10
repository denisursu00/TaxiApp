package ro.cloudSoft.common.utils;

import java.util.Map;

/**
 * 
 */
public class MapUtils {
	
	/**
	 * Returneaza valoarea corespunzatoare cheii din Map.
	 * Daca nu exista valoare pentru cheia data, atunci
	 * se va introduce in map valoarea data si se va returna.
	 */
	public static <K, V> V getAndInitIfNull(Map<K, V> map, K key, V valueIfNull) {
        V value = map.get(key);
        if (value == null) {
            map.put(key, valueIfNull);
            value = valueIfNull;
        }
        return value;
    }
}