package ro.taxiApp.docs.utils;

import java.util.Collection;

public class CollectionUtils {

	public static Collection<String> addObjectCollectionInStringCollection (Collection<? extends Object> objectCollection, Collection<String> stringCollection) {
		for (Object object : objectCollection) {
			stringCollection.add(object.toString());
		}
		return stringCollection;
	}
}
