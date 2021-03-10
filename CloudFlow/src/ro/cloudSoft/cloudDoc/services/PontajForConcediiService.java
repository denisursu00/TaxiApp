package ro.cloudSoft.cloudDoc.services;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.Concediu;

/**
 * 
 */
public interface PontajForConcediiService {

	/**
	 * Adauga pontaje pentru concediile care contin ziua de ieri.
	 * Daca exista pentru zile de concediu din trecut pontaje care nu au putut fi create, se va reincerca crearea lor.
	 * Pontajul se va efectua doar pe ziua de ieri.
	 */
	void createPontajeForConcediiForYesterday() throws AppException;
	
	boolean hasPontajeAprobate(Concediu concediu) throws AppException;
	
	void deletePendingPontajeForConcediu(Concediu concediu);
}