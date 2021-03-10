----script pt stergere nomenclator de pe server-----
--SELECT * FROM NOMENCLATOR WHERE ID = 23887;
--SELECT * FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 23887;
--SELECT * FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 23887;
--SELECT * FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 23887;

--DELETE FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 23887; 
--DELETE FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 23887;
--DELETE FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 23887;
--DELETE FROM NOMENCLATOR WHERE ID = 23887;
----script pt stergere nomenclator de pe server-----



-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Domenii bancare', 'domenii_bancare');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'),	'Denumire domeniu bancar', 'attribute1', 'ATTRIBUTE1', 'TEXT', NULL, 1, 1);

--ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='domenii_bancare'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='domenii_bancare') and KEY='attribute1'),
	1
);

INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Piete de capital', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Operatiuni (conturi)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Operatiuni (popriri)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Operatiuni (carduri si ATM-uri)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Operatiuni (numerar)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Conformitate', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Antifrauda', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Resurse Umane', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Securitate IT', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Securitate bancara', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Fiscalitate', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Recuperare credite neperformante', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Intern', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Contabilitate', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Trezorerie', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Risc', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Plati', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Comunicare', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Juridic', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Creditare', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='domenii_bancare'), 'Marketing', 0);
    