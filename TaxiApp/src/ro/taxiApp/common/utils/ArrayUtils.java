package ro.taxiApp.common.utils;

import java.lang.reflect.Array;

public class ArrayUtils extends org.apache.commons.lang.ArrayUtils {

	public static <E> E[] union(Class<E> elementClass, E[]... arrays) {
		int unionLength = 0;
		for (E[] array: arrays) {
			unionLength += array.length;
		}
		@SuppressWarnings("unchecked")
		E[] union = (E[]) Array.newInstance(elementClass, unionLength);
		int index = 0;
		for (E[] array: arrays) {
			for (E element : array) {
				union[index] = element;
				index++;
			}
		}
		return union;
	}
}
