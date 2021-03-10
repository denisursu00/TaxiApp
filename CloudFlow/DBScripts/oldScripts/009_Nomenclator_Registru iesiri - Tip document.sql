-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Registru iesiri - Tip document', 'registru_iesiri_tip_document');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'),	'Cod', 'attribute1', 'ATTRIBUTE1', 'TEXT', 1, 1);
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'),	'Denumire', 'attribute2', 'ATTRIBUTE2', 'TEXT', 2, 1);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Registru iesiri - Tip document'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Registru iesiri - Tip document') and KEY='attribute2'),
	1
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'DA', 'Document intern ARB', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'CB', 'Consultare banci', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'SR', 'Scrisoare externa transmisa de ARB la care se asteapta raspuns', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'SN', 'Scrisoare externa transmisa de ARB la care nu se asteapta raspuns', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'RE', 'Raspuns ARB la o scrisoare primita din exterior', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'RI', 'Raspuns Invitatie primita', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'HR', 'Adeverinta/documente HR', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, ATTRIBUTE2, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Registru iesiri - Tip document'), 'AL', 'Altele', 0);