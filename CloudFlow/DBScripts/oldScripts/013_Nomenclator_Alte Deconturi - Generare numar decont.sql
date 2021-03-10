-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Alte deconturi - Generare numar decont', 'alte_deconturi_generare_numar_decont');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='alte_deconturi_generare_numar_decont'),	'Ultimul numar de decont', 'attribute1', 'ATTRIBUTE1', 'NUMERIC', 1, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='alte_deconturi_generare_numar_decont'),	'An', 'attribute2', 'ATTRIBUTE2', 'NUMERIC', 2, 1);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='alte_deconturi_generare_numar_decont'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='alte_deconturi_generare_numar_decont') and KEY='attribute1'),
	1
);
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where code='alte_deconturi_generare_numar_decont'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where code='alte_deconturi_generare_numar_decont') and KEY='attribute2'),
	2
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where code='alte_deconturi_generare_numar_decont'), 0, 2018, 0);