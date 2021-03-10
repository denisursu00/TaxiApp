package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import ro.cloudSoft.cloudDoc.core.AppException;

/**
 * Clasa ajutatoare pentru logica operatiilor legate de asignari
 * 
 * 
 */
public interface AssignmentHelper {

	/**
	 * Se ocupa de toate operatiile legate de asignarile pt. un task.
	 */
	void handleAssignment() throws AppException;
}