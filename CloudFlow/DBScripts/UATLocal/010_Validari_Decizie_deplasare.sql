-- UAT Local - Decizie deplasare
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from DOCUMENTTYPE where name = 'UAT Local - Decizie deplasare'),
	'{data_sfarsit} > {data_inceput}',
	'completare_metadate_decizie_deplasare',
	'Data sosire trebuie sa fie mai mare decat Data plecare.',
	1
);
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from DOCUMENTTYPE where name = 'UAT Local - Decizie deplasare'),
	'date:dateAsMetadataFormatFromMetadataDateTime({data_inceput}) > {data_decizie}',
	'completare_metadate_decizie_deplasare',
	'Data plecare trebuie sa fie mai mare decat Data decizie.',
	2
);
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from DOCUMENTTYPE where name = 'UAT Local - Decizie deplasare'),
	'{data_sfarsit_conferinta} > {data_inceput_conferinta}',
	'completare_metadate_decizie_deplasare',
	'Data sfarsit conferinta trebuie sa fie mai mare decat Data inceput conferinta.',
	3
);
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from DOCUMENTTYPE where name = 'UAT Local - Decizie deplasare'),
	'{data_sfarsit} > {data_sfarsit_conferinta}',
	'completare_metadate_decizie_deplasare',
	'Data sosire trebuie sa fie mai mare decat Data sfarsit conferinta.',
	4
);
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	nextval('HIBERNATE_SEQUENCE'), 
	(select id from DOCUMENTTYPE where name = 'UAT Local - Decizie deplasare'),
	'{data_inceput_conferinta} > {data_inceput}',
	'completare_metadate_decizie_deplasare',
	'Data inceput conferinta trebuie sa fie mai mare decat Data plecare.',
	5
);

