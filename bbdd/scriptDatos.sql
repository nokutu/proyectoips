
------ VACIAR TABLAS ------

DELETE FROM facilityBooking;
DELETE FROM member;
DELETE FROM facility;
DELETE FROM feeItem;
DELETE FROM fee;
DELETE FROM activity;
DELETE FROM activitybooking;
DELETE FROM activitymember;
DELETE FROM monitor;


------ CREACION DE SOCIOS ------

INSERT INTO member VALUES (1, 'Gabriel',true);
INSERT INTO member VALUES (2, 'Jorge',true);
INSERT INTO member VALUES (3, 'Sergio',true);
INSERT INTO member VALUES (4, 'Pepe',true);
INSERT INTO member VALUES (5, 'Roverto',true);
INSERT INTO member VALUES (6, 'Jorge',true);
INSERT INTO member VALUES (7, 'Ana',true);
INSERT INTO member VALUES (8, 'Maria',true);
INSERT INTO member VALUES (9, 'Pepa',true);
INSERT INTO member VALUES (10, 'Julio',true);
INSERT INTO member VALUES (11, 'Julia',true);
INSERT INTO member VALUES (12, 'Mario',true);
INSERT INTO member VALUES (13, 'Maria',true);
INSERT INTO member VALUES (14, 'Eugenio',false);
INSERT INTO member VALUES (15, 'Eugenia',false);


------ CREACION DE MONITORES ------

INSERT INTO monitor VALUES(1,'Jorge Monitor');
INSERT INTO monitor VALUES(2,'Pedro Monitor');
INSERT INTO monitor VALUES(3,'Pepa Monitora');
INSERT INTO monitor VALUES(4,'Sandra Monitora');
INSERT INTO monitor VALUES(5,'Rudolf Monitor');
INSERT INTO monitor VALUES(6,'Marta Monitora');
INSERT INTO monitor VALUES(7,'Jose Manuel Monitor');
INSERT INTO monitor VALUES(8,'Maria Jose Monitora');


------ CREACION DE INSTALACIONES ------

INSERT INTO facility VALUES (1, 5,'Cancha de Tenis peque�a');
INSERT INTO facility VALUES (2, 4,'Sala Tenis de Mesa');
INSERT INTO facility VALUES (3, 5,'Sala Libre 1');
INSERT INTO facility VALUES (4, 3,'Sala Libre 2');
INSERT INTO facility VALUES (5, 7,'Cancha Futbol ');
INSERT INTO facility VALUES (6, 2,'Tatami');
INSERT INTO facility VALUES (7, 5,'Cancha de Balonceto');
INSERT INTO facility VALUES (8, 15,'Piscina Adultos');
INSERT INTO facility VALUES (9, 9,'Piscina Mediana');
INSERT INTO facility VALUES (10, 7,'Piscina Peque�a');
INSERT INTO facility VALUES (11, 3,'Sauna');
INSERT INTO facility VALUES (12, 5,'Sala de badminton');

------ CREACION DE ACTIVIDADES (ID, NOMBRE, LIMITE, MONITOR) ------

INSERT INTO activity VALUES (1, 'Taller de Cocina', 15, false);
INSERT INTO activity VALUES (2, 'Taller de Manualidades', 15, false);
INSERT INTO activity VALUES (3, 'Clases de Natacion', 20, false);
INSERT INTO activity VALUES (4, 'Clases de Yoga', 30, false);
INSERT INTO activity VALUES (5, 'Iniciacion al Tenis', 15, false);
INSERT INTO activity VALUES (6, 'Concurso de Badminton', 30, false);
INSERT INTO activity VALUES (7, 'Clases de Canto', 20, false);
INSERT INTO activity VALUES (8, 'Clases de Natacion 2', 20, false);
INSERT INTO activity VALUES (9, 'Clases de Yoga 2', 30, false);
INSERT INTO activity VALUES (10, 'Iniciacion al Tenis 2', 15, false);
INSERT INTO activity VALUES (11, 'Clases de Canto 2', 20, false);


------ CREACION DE RESERVAS ------
/* *(FACILITYBOOKING_ID,FACILITY_ID,MEMBER_ID,TIME_START,TIME_END,PAYMENT_METHOD,PAID,FACILITYBOOKING_DELETED,ENTRANCE,ABANDON,STATE)* */

INSERT INTO "FACILITYBOOKING"  VALUES (1,1,1,{ts '2016-11-12 18:00:00.0'},{ts '2016-11-12 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (2,2,1,{ts '2016-11-12 15:00:00.0'},{ts '2016-11-12 16:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (3,1,2,{ts '2016-11-13 10:00:00.0'},{ts '2016-11-13 12:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (4,1,0,{ts '2016-11-15 10:00:00.0'},{ts '2016-11-15 18:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (5,1,0,{ts '2016-11-15 19:00:00.0'},{ts '2016-11-15 20:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (6,8,0,{ts '2016-11-07 10:00:00.0'},{ts '2016-11-07 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (8,8,1,{ts '2016-11-08 02:00:00.0'},{ts '2016-11-08 03:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (9,8,2,{ts '2016-11-12 08:00:00.0'},{ts '2016-11-12 09:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (10,8,3,{ts '2016-11-12 11:00:00.0'},{ts '2016-11-12 13:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (11,8,1,{ts '2016-11-10 16:00:00.0'},{ts '2016-11-10 17:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (12,8,1,{ts '2016-11-08 15:00:00.0'},{ts '2016-11-08 16:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (13,8,2,{ts '2016-11-07 19:00:00.0'},{ts '2016-11-07 20:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (14,8,3,{ts '2016-11-10 04:00:00.0'},{ts '2016-11-10 05:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (15,8,3,{ts '2016-11-07 08:00:00.0'},{ts '2016-11-07 09:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (16,8,4,{ts '2016-11-07 03:00:00.0'},{ts '2016-11-07 04:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (17,11,5,{ts '2016-11-12 04:00:00.0'},{ts '2016-11-12 06:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (18,5,6,{ts '2016-11-14 05:00:00.0'},{ts '2016-11-14 07:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (19,12,0,{ts '2016-11-11 04:00:00.0'},{ts '2016-11-11 08:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (20,8,0,{ts '2016-11-06 16:00:00.0'},{ts '2016-11-06 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (21,8,7,{ts '2016-11-08 10:00:00.0'},{ts '2016-11-08 11:00:00.0'},'Fee', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (22,5,8,{ts '2016-11-08 11:00:00.0'},{ts '2016-11-08 12:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (23,11,7,{ts '2016-11-08 12:00:00.0'},{ts '2016-11-08 13:00:00.0'},'Fee', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (24,7,3,{ts '2016-11-09 04:00:00.0'},{ts '2016-11-09 05:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (25,1,1,{ts '2016-11-12 18:00:00.0'},{ts '2016-11-12 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (26,2,1,{ts '2016-11-12 15:00:00.0'},{ts '2016-11-12 16:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (27,1,2,{ts '2016-11-13 10:00:00.0'},{ts '2016-11-13 12:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (28,1,0,{ts '2016-11-15 10:00:00.0'},{ts '2016-11-15 18:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (29,1,0,{ts '2016-11-15 19:00:00.0'},{ts '2016-11-15 20:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (30,8,0,{ts '2016-11-09 08:00:00.0'},{ts '2016-11-09 12:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (31,8,0,{ts '2016-11-11 17:00:00.0'},{ts '2016-11-11 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (32,8,0,{ts '2016-11-18 17:00:00.0'},{ts '2016-11-18 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (33,8,0,{ts '2016-11-25 17:00:00.0'},{ts '2016-11-25 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (34,8,0,{ts '2016-12-02 17:00:00.0'},{ts '2016-12-02 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (35,8,0,{ts '2016-12-09 17:00:00.0'},{ts '2016-12-09 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (36,8,0,{ts '2016-12-16 17:00:00.0'},{ts '2016-12-16 19:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (37,8,0,{ts '2016-11-09 17:00:00.0'},{ts '2016-11-09 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (38,8,0,{ts '2016-11-16 17:00:00.0'},{ts '2016-11-16 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (39,8,0,{ts '2016-11-23 17:00:00.0'},{ts '2016-11-23 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (40,8,0,{ts '2016-11-30 17:00:00.0'},{ts '2016-11-30 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (41,8,0,{ts '2016-12-07 17:00:00.0'},{ts '2016-12-07 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (42,8,0,{ts '2016-12-14 17:00:00.0'},{ts '2016-12-14 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (43,8,0,{ts '2016-12-21 17:00:00.0'},{ts '2016-12-21 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (44,8,0,{ts '2016-12-28 17:00:00.0'},{ts '2016-12-28 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (45,8,0,{ts '2017-01-04 17:00:00.0'},{ts '2017-01-04 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (46,8,0,{ts '2017-01-11 17:00:00.0'},{ts '2017-01-11 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (47,8,0,{ts '2017-01-18 17:00:00.0'},{ts '2017-01-18 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (48,8,0,{ts '2017-01-25 17:00:00.0'},{ts '2017-01-25 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (49,8,0,{ts '2017-02-01 17:00:00.0'},{ts '2017-02-01 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (50,8,0,{ts '2017-02-08 17:00:00.0'},{ts '2017-02-08 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (51,8,0,{ts '2017-02-15 17:00:00.0'},{ts '2017-02-15 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (52,8,0,{ts '2017-02-22 17:00:00.0'},{ts '2017-02-22 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (53,8,0,{ts '2017-03-01 17:00:00.0'},{ts '2017-03-01 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (54,8,0,{ts '2017-03-08 17:00:00.0'},{ts '2017-03-08 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (55,8,0,{ts '2017-03-15 17:00:00.0'},{ts '2017-03-15 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (56,8,0,{ts '2017-03-22 17:00:00.0'},{ts '2017-03-22 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (57,8,0,{ts '2017-03-29 17:00:00.0'},{ts '2017-03-29 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (58,8,0,{ts '2017-04-05 17:00:00.0'},{ts '2017-04-05 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (59,8,0,{ts '2017-04-12 17:00:00.0'},{ts '2017-04-12 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (60,8,0,{ts '2017-04-19 17:00:00.0'},{ts '2017-04-19 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (61,8,0,{ts '2017-04-26 17:00:00.0'},{ts '2017-04-26 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (62,8,0,{ts '2017-05-03 17:00:00.0'},{ts '2017-05-03 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (63,8,0,{ts '2017-05-10 17:00:00.0'},{ts '2017-05-10 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (64,8,0,{ts '2017-05-17 17:00:00.0'},{ts '2017-05-17 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (65,8,0,{ts '2017-05-24 17:00:00.0'},{ts '2017-05-24 21:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (72,8,0,{ts '2016-11-12 09:00:00.0'},{ts '2016-11-12 11:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (73,8,0,{ts '2016-11-19 09:00:00.0'},{ts '2016-11-19 11:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (74,8,0,{ts '2016-11-26 09:00:00.0'},{ts '2016-11-26 11:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (75,8,0,{ts '2016-11-06 06:00:00.0'},{ts '2016-11-06 09:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (76,1,0,{ts '2016-11-08 05:00:00.0'},{ts '2016-11-08 07:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (77,1,0,{ts '2016-11-07 02:00:00.0'},{ts '2016-11-07 03:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (78,1,1,{ts '2016-11-07 11:00:00.0'},{ts '2016-11-07 13:00:00.0'},'Cash', 0, null,null,'Valid', null, null);
INSERT INTO "FACILITYBOOKING"  VALUES (79,2,2,{ts '2016-11-07 11:00:00.0'},{ts '2016-11-07 13:00:00.0'},'Cash', 0, null,null,'Valid', null, null);



------ CREACION DE RESERVAS DE ACTIVIDADES ------

INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (1,4, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (1,5, 8);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (2,4, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (2,5, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,30, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,37, 5);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,38, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,39, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,40, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,41, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,42, 3);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,43, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,44, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,45, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,46, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,47, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,48, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,49, 3);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,50, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,51, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,52, 8);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,53, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,54, 5);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,55, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,56, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,57, 3);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,58, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,59, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,60, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,61, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,62, 5);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,63, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,64, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (3,65, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (4,72, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (4,73, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (4,74, 8);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,31, 1);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,32, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,33, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,34, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,35, 3);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (6,36, 7);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (8,6, 2);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (10,75, 3);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (10,76, 4);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (10,77, 5);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (11,20, 6);
INSERT INTO "ACTIVITYBOOKING" (activity_id, facilitybooking_id, monitor_id) VALUES (11,28, 7);




------ CREACION DE SOCIOS APUNTADOS A ACTIVIDES ------

INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,1,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,7,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,8,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,9,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,10,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,4,12,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,5,1,0,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (1,5,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (2,4,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (2,4,7,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,5,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,7,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,8,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,11,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,12,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,30,13,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,41,1,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,41,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,41,3,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,41,4,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (3,41,5,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (6,31,2,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (6,31,3,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (6,31,4,1,0);
INSERT INTO "ACTIVITYMEMBER" (ACTIVITY_ID,FACILITYBOOKING_ID,MEMBER_ID,ASSISTANCE,DELETED) VALUES (10,77,1,1,0);
