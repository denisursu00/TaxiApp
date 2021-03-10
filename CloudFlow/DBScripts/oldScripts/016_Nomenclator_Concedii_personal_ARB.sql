------scriptul de stergere de pe server ------------
--SELECT * FROM NOMENCLATOR WHERE ID = 29567;
--SELECT * FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 29567;
--SELECT * FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 29567;
--SELECT * FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 29567;

--DELETE FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID = 29567; 
--DELETE FROM NOMENCLATOR_UI_ATTRIBUTE WHERE NOMENCLATOR_ID = 29567;
--DELETE FROM NOMENCLATOR_ATTRIBUTE WHERE NOMENCLATOR_ID = 29567;
--DELETE FROM NOMENCLATOR WHERE ID = 29567;
------scriptul de stergere de pe server ------------


-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Concedii personal ARB', 'concedii_personal_arb');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'),	'An', 'attribute1', 'ATTRIBUTE1', 'TEXT', NULL, 1, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'),	'Nume', 'attribute2', 'ATTRIBUTE2', 'NOMENCLATOR', (select ID from nomenclator where code='persoane'), 2, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Odihna conform contract', 'attribute3', 'ATTRIBUTE3', 'NUMERIC', NULL, 3, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Odihna neefectuate an anterior', 'attribute4', 'ATTRIBUTE4', 'NUMERIC', NULL, 4, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Odihna efectuate an anterior', 'attribute5', 'ATTRIBUTE5', 'NUMERIC', NULL, 5, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numãr zile Concediu Odihna neefectuate an în curs', 'attribute6', 'ATTRIBUTE6', 'NUMERIC', NULL, 6, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numãr zile Concediu Odihna efectuate an în curs', 'attribute7', 'ATTRIBUTE7', 'NUMERIC', NULL, 7, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Concediu Odihna estimat conform cod munca de la data', 'attribute8', 'ATTRIBUTE8', 'DATE', NULL, 8, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Concediu Odihna estimat conform cod munca pana la data', 'attribute9', 'ATTRIBUTE9', 'DATE', NULL, 9, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Concediu Odihna realizat conform cod munca de la data', 'attribute10', 'ATTRIBUTE10', 'DATE', NULL, 10, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Concediu Odihna realizat conform cod munca pana la data', 'attribute11', 'ATTRIBUTE11', 'DATE', NULL, 11, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Medical', 'attribute12', 'ATTRIBUTE12', 'NUMERIC', NULL, 12, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Fara Plata', 'attribute13', 'ATTRIBUTE13', 'NUMERIC', NULL, 13, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile Concediu Situatii Speciale', 'attribute14', 'ATTRIBUTE14', 'NUMERIC', NULL, 14, 1);  
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='concedii_personal_arb'), 'Numar zile rechemare', 'attribute15', 'ATTRIBUTE15', 'NUMERIC', NULL, 15, 1);  

--ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='concedii_personal_arb'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='concedii_personal_arb') and KEY='attribute2'),
	1
);
