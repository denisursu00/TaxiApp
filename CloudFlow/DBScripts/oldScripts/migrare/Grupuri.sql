WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'ALL'
);
WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'ADMIN'
);
WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'Directori'
);
WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'Director DEOP'
);
WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'Director DEIC'
);
WITH groupRow AS (
	INSERT INTO ORGANIZATIONENTITY (ORG_ENTITY_ID) VALUES (nextval('HIBERNATE_SEQUENCE'))
    RETURNING ORG_ENTITY_ID
) INSERT INTO EDOCGROUP (
	ORG_ENTITY_ID, 
	NAME
) VALUES (
	(select ORG_ENTITY_ID from groupRow), 
	'Consilieri Financiari Bancari(DAS)'
);