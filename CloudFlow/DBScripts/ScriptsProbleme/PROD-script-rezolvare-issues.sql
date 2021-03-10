-- ----------------------------------- script pt update radiat -----------------------------------
SELECT ATTRIBUTE13, ATTRIBUTE14  
FROM nomenclator_values WHERE NOMENCLATOR_ID = (SELECT ID FROM nomenclator WHERE CODE = 'institutii')
AND ATTRIBUTE14 IS NOT NULL AND ATTRIBUTE13 = 'false';

UPDATE NOMENCLATOR_VALUES
SET ATTRIBUTE13 = 'true'
WHERE NOMENCLATOR_ID = (SELECT ID FROM nomenclator WHERE CODE = 'institutii') 
	AND ATTRIBUTE14 IS NOT NULL AND ATTRIBUTE13 = 'false';
	
---------------------------script pt stergere task si eveniment------------------------------------

-- PROIECT/TASK-URI
SELECT * FROM project WHERE NAME = 'Task Force Analiza si Conceptie';

SELECT * FROM task 
WHERE PROJECT_ID = (SELECT ID FROM project WHERE NAME = 'Task Force Analiza si Conceptie') AND NAME = 'Actualizare date';

SELECT * FROM task_assignment_oe 
WHERE TASK_ID = (SELECT ID FROM task WHERE PROJECT_ID = (SELECT ID FROM project WHERE NAME = 'Task Force Analiza si Conceptie') AND NAME = 'Actualizare date');

SELECT * FROM task_attachment 
WHERE TASK_ID = (SELECT ID FROM task WHERE PROJECT_ID = (SELECT ID FROM project WHERE NAME = 'Task Force Analiza si Conceptie') AND NAME = 'Actualizare date');

-- CALENDAR
SELECT * FROM calendar WHERE NAME = 'Evenimente/intalniri';

SELECT * FROM calendar_event 
WHERE CALENDAR_ID = (SELECT ID FROM calendar WHERE NAME = 'Evenimente/intalniri') AND SUBJECT = 'Participare la Evenimente ARB'; ---- aici data_inceput=11/01/2018 12:00 si data_sfarsit=12.31.2032 17:00

SELECT * FROM calendar_event_oe 
WHERE CALENDAR_EVENT_ID = (SELECT ID FROM calendar_event 
									WHERE CALENDAR_ID = (SELECT ID FROM calendar WHERE NAME = 'Evenimente/intalniri')
										AND SUBJECT = 'Participare la Evenimente ARB');
---------
DELETE FROM calendar_event_oe
WHERE CALENDAR_EVENT_ID = (SELECT ID FROM calendar_event 
									WHERE CALENDAR_ID = (SELECT ID FROM calendar WHERE NAME = 'Evenimente/intalniri')
										AND SUBJECT = 'Participare la Evenimente ARB');---- aici data_inceput=11/01/2018 12:00 si data_sfarsit=12.31.2032 17:00

DELETE FROM calendar_event
WHERE CALENDAR_ID = (SELECT ID FROM calendar WHERE NAME = 'Evenimente/intalniri') AND SUBJECT = 'Participare la Evenimente ARB'; ---- aici data_inceput=11/01/2018 12:00 si data_sfarsit=12.31.2032 17:00


----
DELETE FROM task_assignment_oe
WHERE TASK_ID = (SELECT ID FROM task WHERE PROJECT_ID = (SELECT ID FROM project WHERE NAME = 'Task Force Analiza si Conceptie') AND NAME = 'Actualizare date');

DELETE FROM task 
WHERE PROJECT_ID = (SELECT ID FROM project WHERE NAME = 'Task Force Analiza si Conceptie') AND NAME = 'Actualizare date';
