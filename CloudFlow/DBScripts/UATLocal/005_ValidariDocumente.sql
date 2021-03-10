
DELETE FROM document_validation_definition where document_type_id = (select id from documenttype where name='UAT Local - Nota PVG');
INSERT INTO document_validation_definition (id, document_type_id, metadata_collection_id, condition_expression, validation_in_states, message, validation_order) 
VALUES (nextval('HIBERNATE_SEQUENCE'), (select id from documenttype where name='UAT Local - Nota PVG'), null, '{data_intocmire_nota_pvg} < {data_nota_pvg}', 'completare_metadate;verificare_nota_pvg;corectie_nota_pvg', 'Data intocmire nota PVG trebuie sa fie mai mica decat Data nota PVG.', 1);

INSERT INTO document_validation_definition (id, document_type_id, metadata_collection_id, condition_expression, validation_in_states, message, validation_order)
VALUES (nextval('HIBERNATE_SEQUENCE'), (select id from documenttype where name='UAT Local - Prezenta Comisie GL'), null, '{data_sfarsit} > {data_inceput}', 'completare_metadate_prezenta;planificare_sedinta', 'Data sfarsit trebuie sa fie mai mare decat Data inceput.', 1);

DELETE FROM document_validation_definition where document_type_id = (select id from documenttype where name='UAT Local - Ordine de zi CD');
INSERT INTO document_validation_definition (id, document_type_id, metadata_collection_id, condition_expression, validation_in_states, message, validation_order) 
VALUES (nextval('HIBERNATE_SEQUENCE'), (select id from documenttype where name='UAT Local - Ordine de zi CD'), null, '{data_sfarsit} > {data_inceput}', 'completare_initiala_metadate;propunere_definitivare;definitivare_ordine_de_zi', 'Data sfarsit trebuie sa fie mai mare decat Data inceput.', 1);

DELETE FROM document_validation_definition where document_type_id = (select id from documenttype where name='UAT Local - Agenda sedinta PVG');
INSERT INTO document_validation_definition (id, document_type_id, metadata_collection_id, condition_expression, validation_in_states, message, validation_order) 
VALUES (nextval('HIBERNATE_SEQUENCE'), (select id from documenttype where name='UAT Local - Agenda sedinta PVG'), null, '{data_sfarsit} > {data_inceput}', 'completare_initiala_metadate;definitivare_agenda_pvg;propunere_definitivare', 'Data sfarsit trebuie sa fie mai mare decat Data inceput.', 1);


