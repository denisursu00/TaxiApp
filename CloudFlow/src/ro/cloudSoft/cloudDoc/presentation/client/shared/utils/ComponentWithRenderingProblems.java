package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

/**
 * Reprezinta o componenta din interfata grafica ce are probleme cu afisarea,
 * in special cand se schimba niste valori sau cand devine vizibila.
 * 
 * 
 */
public interface ComponentWithRenderingProblems {

	/**
	 * Spune daca trebuie sa se asigure ca , componenta este afisata corect ACUM.
	 */
	boolean needsEnsuringIsProperlyRenderedNow();
	
	/**
	 * Metoda de tip callback prin care componenta se asigura ca este afisata corect.
	 */
	void ensureIsProperlyRendered();
}