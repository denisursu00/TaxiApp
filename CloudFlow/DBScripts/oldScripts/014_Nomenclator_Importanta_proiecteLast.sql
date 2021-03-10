-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Importanta proiecte', 'importanta_proiecte');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'),	'Grad de importanta', 'attribute1', 'ATTRIBUTE1', 'TEXT', NULL, 1, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'),	'Valoare grad importanta', 'attribute2', 'ATTRIBUTE2', 'NUMERIC', NULL, 2, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'),	'Culoare grad de importanta', 'attribute3', 'ATTRIBUTE3', 'TEXT', NULL, 3, 1);  
    
--ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='importanta_proiecte'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='importanta_proiecte') and KEY='attribute1'),
	1
);

--- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'strategic', 0, '#6699ff', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'critic', 1, '#ff0000', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'important', 2, '#ffff00', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'important', 3, '#ffff00', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'in asteptare', 4, '#99cc00', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3,  DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='importanta_proiecte'), 'in asteptare', 5, '#99cc00', 0);
    