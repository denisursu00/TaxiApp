package ro.cloudSoft.cloudDoc.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.PendingPontajForConcediu;

/**
 * 
 */
public interface PendingPontajForConcediuDao {

	Set<PendingPontajForConcediu> getAll();
	
	void deleteAllForDay(Date day);
	
	void deleteAllInIntervalForSolicitant(String solicitantEmail, Date dataInceput, Date dataSfarsit);
	
	void delete(Collection<PendingPontajForConcediu> pendingPontajeForConcediiToDelete);
	
	void save(Collection<PendingPontajForConcediu> pendingPontajeForConcediiToSave);
}