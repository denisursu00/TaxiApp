-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Comisii/GL', 'comisii_sau_gl');

-- atributes	
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'),	'Categorie', 'attribute1', 'ATTRIBUTE1', 'NOMENCLATOR', (select ID from nomenclator where name='Categorii comisii/GL'), 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'),	'Denumire', 'attribute2', 'ATTRIBUTE2', 'TEXT', NULL, 2);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'),	'Abreviere', 'attribute3', 'ATTRIBUTE3', 'TEXT', NULL, 3);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'),	'Data infintarii', 'attribute4', 'ATTRIBUTE4', 'DATE', NULL, 4);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Comisii/GL'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Comisii/GL') and KEY='attribute2'),
	1
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Categorii comisii/GL') and  ATTRIBUTE1 = 'Comisie'), 'Comisia de afaceri documentare', 'AFDOC', '2018.03.12', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Categorii comisii/GL') and  ATTRIBUTE1 = 'Comisie'), 'Comisia de contabilitate', 'CONT', '2018.01.14', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Categorii comisii/GL') and  ATTRIBUTE1 = 'Grup de lucru'), 'Protocol DIICOT', 'DIICOT', '2017.02.15', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Comisii/GL'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Categorii comisii/GL') and  ATTRIBUTE1 = 'Grup de lucru'), 'Protocol de colaborare cu Inspectoratul General pentru Imigrari', 'PRIGI', '2016.01.21', 0);