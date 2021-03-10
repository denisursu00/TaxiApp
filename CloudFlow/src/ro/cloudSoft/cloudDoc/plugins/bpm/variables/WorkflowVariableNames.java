package ro.cloudSoft.cloudDoc.plugins.bpm.variables;

/**
 * Contine constante pentru numele variabilelor ce vor fi puse pe flux.
 * 
 * 
 */
public class WorkflowVariableNames {
	
	/** Variabila de tip String ce reprezinta ID-ul expeditorului */
	public static final String SENDER_USER_ID = "SENDER_USER_ID";
	
	/** Variabila de tip String ce reprezinta ID-ul documentului trimis pe flux */
	public static final String DOCUMENT_ID = "DOCUMENT_ID";
	
	/** Variabila de tip String ce reprezinta numele 'real' al spatiului de lucru in care se gaseste documentul trimis pe flux */
	public static final String DOCUMENT_LOCATION_REAL_NAME = "DOCUMENT_LOCATION_REAL_NAME";
	
	/** Variabila de tip Long ce reprezinta ID-ul tipului de document al documentului trimis pe flux */
	public static final String DOCUMENT_TYPE_ID = "DOCUMENT_TYPE_ID";
	
	/** Variabila de tip Long ce reprezinta ID-ul utilizatorului care este autorul documentului trimis pe flux */
	public static final String DOCUMENT_AUTHOR_ID = "DOCUMENT_AUTHOR_ID";
	
	/** Variabila de tip String ce reprezinta numele documentului trimis pe flux */
	public static final String DOCUMENT_NAME = "DOCUMENT_NAME";
	
	/** Variabila de tip Date ce reprezinta data crearii documentului trimis pe flux */
	public static final String DOCUMENT_CREATED_DATE = "DOCUMENT_CREATED_DATE";
	
	/** Variabila de tip String ce reprezinta ID-ul initiatorului fluxului */
	public static final String INITIATOR_ID = "INITIATOR";
	
	/** Prefix pentru numele variabilelor de tip String ce reprezinta ID-urile utilizatorilor care au fost asignati manual */
	public static final String PREFIX_MANUAL_ASSIGNMENT_DESTINATION_USER_ID = "MANUAL_ASSIGNMENT_DESTINATION_ID_";
	
	public static final String SEPARATOR_MANUAL_ASSIGNMENT_DESTINATION_USER_ID_TRANSITION_NAME_FROM_FINAL_STATE_CODE = "_";
	
	/** Variabila de tip List(String) ce reprezinta ultimele asignari ale fluxului */
	public static final String LAST_ASSIGNED_ASSIGNEES = "LAST_ASSIGNED";
	
	/** Variabila de tip String ce reprezinta numele ultimei tranzitii prin care a trecut fluxul */
	public static final String LAST_TRANSITION_NAME = "LAST_TRANSITION";
}