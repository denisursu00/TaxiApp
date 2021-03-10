package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

/**
 * Clasa contine metode care, spre deosebire de metodele din API-ul Java,
 * nu arunca <code>NullPointerException</code> daca o valoare comparata este
 * nula.
 */
public class GwtCompareUtils {
	
	/**
	 * Verifica daca doua obiecte sunt egale, tinand cont de valoarea null.
	 * Daca amandoua sunt nule, atunci sunt egale.
	 * Daca doar unul dintre ele este null, atunci nu sunt egale.
	 */
	public static boolean areEqual(Object object1, Object object2) {
		if ((object1 != null) && (object2 != null)) {
			return object1.equals(object2);
		}
		if ((object1 != null) && (object2 == null)) {
			return false;
		}
		if ((object1 == null) && (object2 != null)) {
			return false;
		}
		return true;
	}

    /**
     * Compara doua obiecte care pot avea valoare nula.
     * @param <T> tipul obiectelor (trebuie sa fie comparabile)
     * @param obj1 primul obiect
     * @param obj2 al doilea obiect
     * @return rezultatul comparatiei directe intre cele doua obiecte, sau
     * o valoare in functie de valoarea nula a fiecaruia:
     * 1, daca al doilea obiect este nul;
     * -1, daca primul obiect este nul;
     * 0, daca amandoua obiectele sunt nule
     */
    public static <T extends Comparable<T>> int compare(T obj1, T obj2) {
        if ((obj1 != null) && (obj2 != null)) {
            return obj1.compareTo(obj2);
        } else if ((obj1 != null) && (obj2 == null)) {
            return 1;
        } else if ((obj1 == null) && (obj2 != null)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Compara doua String-uri ignorand case-ul (litere mari / mici),
     * fara a arunca <code>NullPointerException</code> cand una / doua valori
     * sunt nule.
     * @param str1
     * @param str2
     * @return rezultatul comparatiei directe intre cele doua obiecte, sau
     * o valoare in functie de valoarea nula a fiecaruia:
     * 1, daca al doilea obiect este nul;
     * -1, daca primul obiect este nul;
     * 0, daca amandoua obiectele sunt nule
     */
    public static int compareIgnoreCase(String str1, String str2) {
        if ((str1 != null) && (str2 != null)) {
            return str1.compareToIgnoreCase(str2);
        } else if ((str1 != null) && (str2 == null)) {
            return 1;
        } else if ((str1 == null) && (str2 != null)) {
            return -1;
        } else {
            return 0;
        }
    }
}