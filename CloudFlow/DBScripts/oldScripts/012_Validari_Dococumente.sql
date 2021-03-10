
-----Nota: In cazul reutilizarii acestor scripturi pe mediul de test/productie, se vor elimina de la valorile Name din DOCUMENTTYPE urmatoarele: "Uat Local - " si "Demo " la nivel de script.
-----      Aceasta este necesara pt ca tipurile de documente vor fi redenumite, vor avea doar numele lor fara prefix.

-- Uat Local - Investigatie
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Investigatie'),
	'{data_solutionare} >= {data_investigatie}',
	'completare_finala_metadate',
	'Data solutionare trebuie sa fie mai mare sau egala cu Data investigatie.',
	1
);

-- Uat Local - Conciliere
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Conciliere'),
	'{data_conciliere} >= date:currentDateAsMetadataFormat()',
	'completare_initiala_metadate',
	'Data conciliere trebuie sa fie mai mare sau egala cu data curenta.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Conciliere'),
	'{data_solutionare} >= {data_conciliere}',
	'completare_finala_metadate',
	'Data solutionare trebuie sa fie mai mare sau egala cu Data conciliere.',
	2
);

------------------------------------------------------------------------------------------------------------------------------------------------------

-- Demo Misiune de audit si control
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Misiune de audit si control'),
	'{data_sfarsit} >= {data_inceput}',
	'completare_metadate',
	'Data sfarsit trebuie sa fie mai mare sau egala cu Data inceput.',
	1
);
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Misiune de audit si control'),
	'{data_inceput} >= date:currentDateAsMetadataFormat()',
	'completare_metadate',
	'Data inceput trebuie sa fie mai mare sau egala cu data curenta.',
	2
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Misiune de audit si control'),
	'{fisa_neconformitate} == "#da#" ? {termen_remediere_fisa_neconformitate} > {data_inceput} : true',
	'completare_metadate',
	'Termen remediere fisa neconformitate trebuie sa fie mai mare decat Data inceput',
	3
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Misiune de audit si control'),
	'{fisa_neregularitate} == "#da#" ? {termen_remediere_fisa_neregularitate} > {data_inceput} : true',
	'completare_metadate',	
	'Termen remediere fisa neregularitate trebuie sa fie mai mare decat Data inceput',
	4
);

-- Uat Local - Nota CD - Consilieri Financiari Bancari DAS
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Nota CD - Consilieri Financiari Bancari DAS'),
	'{data_intocmire_nota} < {data_cd_arb}',
	'completare_metadate;corectie_nota_cd;verificare_nota_cd',
	'Data intocmire nota trebuie sa fie mai mica decat Data CD ARB.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Nota CD - Consilieri Financiari Bancari DAS'),
	'{data_intocmire_nota} >= date:currentDateAsMetadataFormat()',
	'completare_metadate;corectie_nota_cd;',
	'Data intocmire nota trebuie sa fie mai mare sau egala cu data curenta.',
	2
);

-- Demo Nota CD - Directori
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Directori'),
	'{data_intocmire_nota} < {data_cd_arb}',
	'completare_metadate;corectie_nota_cd;verificare_nota_cd',
	'Data intocmire nota trebuie sa fie mai mica decat Data CD ARB.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Directori'),
	'{data_intocmire_nota} >= date:currentDateAsMetadataFormat()',
	'completare_metadate;corectie_nota_cd',
	'Data intocmire nota trebuie sa fie mai mare sau egala cu data curenta.',
	2
);

-- Demo Nota CD - Useri ARB
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Useri ARB'),
	'{data_intocmire_nota} < {data_cd_arb}',
	'completare_metadate;corectie_nota_cd;verificare_nota_cd',
	'Data intocmire nota trebuie sa fie mai mica decat Data CD ARB.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Useri ARB'),
	'{data_intocmire_nota} >= date:currentDateAsMetadataFormat()',
	'completare_metadate;corectie_nota_cd',
	'Data intocmire nota trebuie sa fie mai mare sau egala cu data curenta.',
	2
);

-- Demo Agenda sedinta PVG
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Agenda sedinta PVG'),
	'{data_sfarsit} >= {data_inceput}',
	'completare_initiala_metadate',
	'Data sfarsit intalnire trebuie sa fie mai mare sau egala cu Data inceput intalnire.',
	1
);

-- Demo Ordine de zi CD
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Ordine de zi CD'),
	'{data_sfarsit} >= {data_inceput}',
	'completare_initiala_metadate',
	'Data sfarsit sedinta trebuie sa fie mai mare sau egala cu Data inceput sedinta.',
	1
);

-- Demo Nota PVG
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota PVG'),
	'{data_intocmire_nota_pvg} < {data_nota_pvg}',
	'completare_metadate',
	'Data intocmire nota PVG trebuie sa fie mai mica decat Data nota PVG.',
	1
);

-- Demo Nota CD - Responsabil
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Responsabil'),
	'{data_intocmire_nota} < {data_cd_arb}',
	'completare_metadate;corectie_nota_cd;verificare_nota_cd',
	'Data intocmire nota trebuie sa fie mai mica decat Data CD ARB.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Nota CD - Responsabil'),
	'{data_intocmire_nota} >= date:currentDateAsMetadataFormat()',
	'completare_metadate;corectie_nota_cd',
	'Data intocmire nota trebuie sa fie mai mare sau egala cu data curenta ',
	2
);

-- Demo Minuta sedinta Comisie GL
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Minuta sedinta Comisie GL'),
	'{data_redactare} >= {data_sedinta}',
	'completare_metadate_minuta',
	'Data redactare trebuie sa fie mai mare sau egala cu Data sedinta.',
	1
);

---=============================================================================
-- Uat Local - DSP
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Initiere DSP'),
	'{data_sfarsit} > {data_inceput}',
	'completare_metadate_dsp;validare_dsp;corectare_dsp',
	'Data sfarsit proiect trebuie sa fie mai mare decat Data inceput proiect.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Initiere DSP'),
	'{data_implementarii} > {data_inceput}',
	'completare_metadate_dsp;validare_dsp;corectare_dsp',
	'Data implementarii trebuie sa fie mai mare decat Data inceput proiect.',
	2
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	METADATA_COLLECTION_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from METADATACOLLECTION where documenttype_id=(select id from DOCUMENTTYPE where name = 'Uat Local - Initiere DSP') and name = 'activitati_proiect'),
	'{data_sfarsit} >= {data_inceput}',
	'adaugare_participanti_si_actiuni_viitoare;validare_participanti_desemnati;corectare_participanti_desemnati',
	'Data sfarsit activitate trebuie sa fie mai mare sau egala cu Data inceput activitate.',
	3
);
----============================================================================
-- Demo Cerere concediu
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Cerere concediu'),
	'{data_sfarsit} >= {data_inceput}',
	'completare_metadate_cfp_concediu_nepersonal;completare_metadate_cfp_concediu_personal;completare_metadate_cm_concediu_nepersonal;completare_metadate_cm_concediu_personal;completare_metadate_co_concediu_personal;completare_metadate_css_concediu_nepersonal;completare_metadate_css_concediu_personal',
	'Data sfarsit concediu trebuie sa fie mai mare sau egala cu Data inceput concediu.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Cerere concediu'),
	'{data_initiere_cerere_concediu} < {data_inceput}',
	'completare_metadate_cfp_concediu_nepersonal;completare_metadate_cfp_concediu_personal;completare_metadate_cm_concediu_nepersonal;completare_metadate_cm_concediu_personal;completare_metadate_co_concediu_personal;completare_metadate_css_concediu_nepersonal;completare_metadate_css_concediu_personal',
	'Data initiere cerere concediu trebuie sa fie mai mica decat Data inceput concediu.',
	2
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Cerere concediu'),
	'{data_initiere_cerere_concediu} < {data_sfarsit}',
	'completare_metadate_cfp_concediu_nepersonal;completare_metadate_cfp_concediu_personal;completare_metadate_cm_concediu_nepersonal;completare_metadate_cm_concediu_personal;completare_metadate_co_concediu_personal;completare_metadate_css_concediu_nepersonal;completare_metadate_css_concediu_personal',
	'Data initiere cerere concediu trebuie sa fie mai mica decat Data sfarsit concediu.',
	3
);

-- Demo Cerere rechemare
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Cerere rechemare'),
	'{data_cerere_rechemare} < {data_intrarii_vigoare_rechemarii}',
	'initiere_cerere_rechemare_si_completare_metadate',
	'Data cerere rechemare trebuie sa fie mai mica decat Data intrarii in vigoare a rechemarii.',
	1
);

-- Demo Decizie rechemare
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie rechemare'),
	'{data_decizie_rechemare} < {data_intrarii_vigoare_rechemarii}',
	'initiere_decizie_rechemare_completare_metadate',
	'Data decizie rechemare trebuie sa fie mai mica decat Data intrarii in vigoare a rechemarii.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie rechemare'),
	'{data_sfarsit_zile_neefectuate} >= {data_inceput_zile_neefectuate}',
	'initiere_decizie_rechemare_completare_metadate',
	'Data sfarsit zile neefectuate trebuie sa fie mai mare sau egala cu Data inceput zile neefectuate.',
	2
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie rechemare'),
	'{data_sfarsit_concediu_aprobat} >= {data_inceput_concediu_aprobat}',
	'initiere_decizie_rechemare_completare_metadate',
	'Data sfarsit concediu aprobat trebuie sa fie mai mare sau egala cu Data inceput concediu aprobat.',
	3
);

-- Demo Decizie deplasare
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_sfarsit} >= {data_inceput}',
	'completare_metadate_decizie_deplasare',
	'Data sosire trebuie sa fie mai mare sau egala cu Data plecare.',
	1
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_conferinta} >= {data_inceput}',
	'completare_metadate_decizie_deplasare',
	'Data conferinta trebuie sa fie mai mare sau egala cu Data plecare.',
	2
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_conferinta} <= {data_sfarsit}',
	'completare_metadate_decizie_deplasare',
	'Data conferinta trebuie sa fie mai mica sau egala cu Data sosire.',
	3
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_decizie} < {data_inceput}',
	'completare_metadate_decizie_deplasare',
	'Data decizie trebuie sa fie mai mica decat Data plecare.',
	4
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_decizie} < {data_conferinta}',
	'completare_metadate_decizie_deplasare',
	'Data decizie trebuie sa fie mai mica decat Data conferinta.',
	5
);

insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
	HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Demo Decizie deplasare'),
	'{data_decizie} < {data_sfarsit}',
	'completare_metadate_decizie_deplasare',
	'Data decizie trebuie sa fie mai mica decat Data sosire.',
	6
);

-- Uat Local - Avans Salarial
insert into DOCUMENT_VALIDATION_DEFINITION (
	ID, 
	DOCUMENT_TYPE_ID, 
	CONDITION_EXPRESSION, 
	VALIDATION_IN_STATES, 
	MESSAGE, 
	VALIDATION_ORDER
) values (
HIBERNATE_SEQUENCE.nextval, 
	(select id from DOCUMENTTYPE where name = 'Uat Local - Avans Salarial'),
	'{valoare_avans} > 0',
	'initiere_completare_metadate',
	'Valoare avans trebuie sa fie mai mare ca 0.',
	1
);

