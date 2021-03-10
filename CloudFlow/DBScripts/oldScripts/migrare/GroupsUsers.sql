INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'ALL'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME != 'application.user';
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'ADMIN'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME = 'vlad.barabas';
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Directori'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME IN ('rodica.tuchila', 'cati.ursu', 'gabriela.folcut');
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Director DEOP'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME IN ('rodica.tuchila');
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Director DEIC'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME IN ('gabriela.folcut');
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Consilieri Financiari Bancari(DAS)'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME IN ('rodica.tuchila', 'constantin.rotaru', 'tatiana.grafcenco', 'cristina.dumitrescu', 'luminita.soitu', 'stefan.dina');


