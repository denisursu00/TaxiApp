DELETE FROM GROUP_USER where GROUP_ID = (select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Logistica');
INSERT INTO GROUP_USER (
	GROUP_ID,
	USER_ID
) SELECT
	(select ORG_ENTITY_ID from EDOCGROUP where NAME = 'Logistica'),
	ORG_ENTITY_ID
FROM EDOCUSER WHERE USERNAME IN ('adriana.tudor', 'andreea.rosu', 'cati.ursu', 'florin.ninu', 'gabriela.folcut', 'lina.visan', 'liviu.chitu', 'rodica.tuchila', 'sorina.sirbu', 'vlad.barabas');
