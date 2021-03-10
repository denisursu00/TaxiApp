package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;


/**
 * Contine metode utilitare legate de fisiere, numele sau calea lor.
 * 
 * 
 */
public class GwtFileUtils {

	/** separatorul pentru cale folosit de Unix (Linux, Mac) */
	private static final char PATH_SEPARATOR_UNIX = '/';
	/** separatorul pentru cale folosit de Windows */
	private static final char PATH_SEPARATOR_WINDOWS = '\\';
	
	/**
	 * Returneaza extensia unui fisier. Daca fisierul nu are extensie, atunci
	 * returneaza un sir de caractere gol.
	 * @param path calea sau numele fisierului
	 * @return extensia unui fisier
	 */
	public static String getExtension(String path) {
		int lastIndexOfPoint = path.lastIndexOf('.');
		
		if (lastIndexOfPoint == -1) {
			return "";
		} else {
			return path.substring(lastIndexOfPoint + 1);
		}
	}
	
	/**
	 * Obtine numele fisierului din calea precizata.
	 * Calea fisierului include discul pe care se afla si ierarhia directoarelor
	 * pana la fisier.
	 * @param path calea fisierului
	 * @return numele fisierului din calea precizata
	 */
	public static String getFileNameFromPath(String path) {
		// Presupunem ca , calea fisierului este doar numele sau.
		int lastIndexOfPathSeparator = -1;
		// Verificam daca fisierul are cale de Windows.
		lastIndexOfPathSeparator = path.lastIndexOf(PATH_SEPARATOR_WINDOWS);
		if (lastIndexOfPathSeparator != -1) {
			return path.substring(lastIndexOfPathSeparator + 1);
		}
		// Verificam daca fisierul are cale de Unix (Linux, Mac).
		lastIndexOfPathSeparator = path.lastIndexOf(PATH_SEPARATOR_UNIX);
		if (lastIndexOfPathSeparator != -1) {
			return path.substring(lastIndexOfPathSeparator + 1);
		}
		// Nu are cale de Windows sau Unix, deci este doar numele fisierului.
		return path;
	}
}