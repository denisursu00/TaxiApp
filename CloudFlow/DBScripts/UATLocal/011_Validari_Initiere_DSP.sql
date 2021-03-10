insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	METADATA_COLLECTION_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from METADATACOLLECTION where documenttype_id=(select id from DOCUMENTTYPE where name = 'Initiere DSP') and name = 'activitati_proiect'),
	'(empty({data_sfarsit_eveniment}) or empty({data_inceput_eveniment})) ? true : {data_sfarsit_eveniment} > {data_inceput_eveniment}',
	'adaugare_participanti_si_actiuni_viitoare;validare_participanti_desemnati;corectare_participanti_desemnati',
	'Data sfarsit eveniment trebuie sa fie mai mare decat Data inceput eveniment.',
	4
);