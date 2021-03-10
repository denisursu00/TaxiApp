-- nomenclator
INSERT INTO NOMENCLATOR (ID, NAME, CODE) VALUES (HIBERNATE_SEQUENCE.nextval, 'Tip Institutii', 'tip_institutii');

-- atributes
INSERT INTO NOMENCLATOR_ATTRIBUTE (ID, NOMENCLATOR_ID, NAME, KEY, COLUMN_NAME, TYPE,TYPE_NOMENCLATOR_ID, UI_ORDER, REQUIRED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Tip Institutii'),	'Tip institutie', 'attribute1', 'ATTRIBUTE1', 'TEXT', NULL, 1, 1);

-- ui attributes
INSERT INTO NOMENCLATOR_UI_ATTRIBUTE (
	ID, 
	NOMENCLATOR_ID, 
	ATTRIBUTE_ID, 
	UI_ORDER
) VALUES (
	HIBERNATE_SEQUENCE.nextval, 
	(select ID from NOMENCLATOR where NAME='Tip Institutii'),
	(select ID from NOMENCLATOR_ATTRIBUTE where NOMENCLATOR_ID=(select ID from NOMENCLATOR where NAME='Tip Institutii') and KEY='attribute1'),
	1
);

-- valori
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Tip Institutii'), 'Membru ARB (B)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED) 
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Tip Institutii'), 'Membru Afiliat (A)', 0);
INSERT INTO NOMENCLATOR_VALUES (ID, NOMENCLATOR_ID, ATTRIBUTE1, DELETED)  
    VALUES (HIBERNATE_SEQUENCE.nextval, (select ID from nomenclator where name='Tip Institutii'), 'Alte Institutii (I)', 0);
	

