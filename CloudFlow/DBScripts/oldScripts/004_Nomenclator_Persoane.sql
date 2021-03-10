-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Persoane', 'persoane');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Nume', 'attribute1', 'ATTRIBUTE1', 'TEXT', NULL, 1, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED)  
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Prenume', 'attribute2', 'ATTRIBUTE2', 'TEXT', NULL, 2, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Email', 'attribute3', 'ATTRIBUTE3', 'TEXT', NULL, 3, 0);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Telefon', 'attribute4', 'ATTRIBUTE4', 'TEXT', NULL, 4, 0);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED)  
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Fax', 'attribute5', 'ATTRIBUTE5', 'TEXT', NULL, 5, 0);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Mobil', 'attribute6', 'ATTRIBUTE6', 'TEXT', NULL, 6, 0);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Departament', 'attribute7', 'ATTRIBUTE7', 'TEXT', NULL, 7, 0);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED)  
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Functie', 'attribute8', 'ATTRIBUTE8', 'TEXT', NULL, 8, 0);


-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Persoane'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Persoane') and KEY='attribute1'),
	1
);
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Persoane'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Persoane') and KEY='attribute2'),
	2
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, ATTRIBUTE5, ATTRIBUTE6, ATTRIBUTE7, ATTRIBUTE8, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'), 'Gheorghe', 'Alexandru', 'alexandru.gheorghe@arb.ro', '0213212078', '0213212095', '0725216225', 'IT', 'Administrator de sistem', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, ATTRIBUTE5, ATTRIBUTE6, ATTRIBUTE7, ATTRIBUTE8, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'), 'Soitu', 'Luminita', 'luminita.soitu@arb.ro', '0213212012', '0213212021', '0725216215', 'CFB', 'Consultant Financiar Bancar', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, ATTRIBUTE5, ATTRIBUTE6, ATTRIBUTE7, ATTRIBUTE8, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'), 'Grafcenco', 'Tatiana', 'tatiana.grafcenco@arb.ro', '0213212033', '0213212044', '0725216223', 'CFB', 'Consultant Financiar Bancar', 0);
    
-- Adaugare atribut institutie
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED)  
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Persoane'),	'Institutie', 'attribute9', 'ATTRIBUTE9', 'NOMENCLATOR', (select ID from nomenclator where name='Institutii'), 9, 1);
    
	





























	

