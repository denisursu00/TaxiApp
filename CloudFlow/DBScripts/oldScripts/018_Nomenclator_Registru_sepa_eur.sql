----script pt stergere nomenclator de pe server-----
--SELECT * FROM NOMENCLATOR WHERE ID = 33519;
--SELECT * FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 33519;
--SELECT * FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 33519;
--SELECT * FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 33519;

--DELETE FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 33519; 
--DELETE FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 33519;
--DELETE FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 33519;
--DELETE FROM NOMENCLATOR WHERE ID = 33519;
----script pt stergere nomenclator de pe server-----


-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Registru SEPA EUR', 'registru_sepa_eur');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Schema', 'attribute1', 'ATTRIBUTE1', 'NOMENCLATOR', (select ID from nomenclator where code='scheme_sepa_eur'), 1, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Moneda', 'attribute2', 'ATTRIBUTE2', 'TEXT', NULL, 2, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Institutie', 'attribute3', 'ATTRIBUTE3', 'NOMENCLATOR', (select ID from nomenclator where code='institutii'), 3, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Adresa', 'attribute4', 'ATTRIBUTE4', 'TEXT', NULL, 4, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Localitate', 'attribute5', 'ATTRIBUTE5', 'TEXT', NULL, 5, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Cod BIC', 'attribute6', 'ATTRIBUTE6', 'TEXT', NULL, 6, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Data aderarii', 'attribute7', 'ATTRIBUTE7', 'DATE', NULL, 7, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='registru_sepa_eur'),	'Observatii', 'attribute8', 'ATTRIBUTE8', 'TEXT', NULL, 8, 0);  


--ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='registru_sepa_eur'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='registru_sepa_eur') and KEY='attribute1'),
	1
);

