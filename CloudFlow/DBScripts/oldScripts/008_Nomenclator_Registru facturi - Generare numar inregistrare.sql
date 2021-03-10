-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Registru facturi - Generare numar inregistrare', 'registru_facturi_generare_numar_inregistrare');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'),	'Tip document', 'attribute1', 'ATTRIBUTE1', 'NOMENCLATOR', (select ID from nomenclator where name='Registru facturi - Tip document'), 1, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'),	'Prefix', 'attribute2', 'ATTRIBUTE2', 'TEXT', 2, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'),	'Ultimul numar de inregistrare', 'attribute3', 'ATTRIBUTE3', 'NUMERIC', 3, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'),	'An', 'attribute4', 'ATTRIBUTE4', 'NUMERIC', 4, 1);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare') and KEY='attribute2'),
	1
);
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare') and KEY='attribute3'),
	2
);
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Registru facturi - Generare numar inregistrare') and KEY='attribute4'),
	3
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Tip document - Registru facturi') and  ATTRIBUTE1 = 'Factura'), 'F', 0, 2018, 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Tip document - Registru facturi') and  ATTRIBUTE1 = 'Proforma'), 'FP', 0, 2018, 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Tip document - Registru facturi') and  ATTRIBUTE1 = 'Instiintare de plata'), 'I', 0, 2018, 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Tip document - Registru facturi') and  ATTRIBUTE1 = 'Chitanta'), 'C', 0, 2018, 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru facturi - Generare numar inregistrare'), (SELECT ID FROM NOMENCLATOR_VALUES WHERE NOMENCLATOR_ID=(select ID from nomenclator where name='Tip document - Registru facturi') and  ATTRIBUTE1 = 'Bon fiscal'), 'BF', 0, 2018, 0);
