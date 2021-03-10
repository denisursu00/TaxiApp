-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME) VALUES (HIBERNATE_SEQUENCE.nextval, 'Categorii comisii/GL');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Categorii comisii/GL'),	'Categorie', 'attribute1', 'ATTRIBUTE1', 'TEXT', 1);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Categorii comisii/GL'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Categorii comisii/GL') and KEY='attribute1'),
	1
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Categorii comisii/GL'), 'Comisie', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Categorii comisii/GL'), 'Grup de lucru', 0);
	

