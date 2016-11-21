
------ BORRAR TODAS LAS TABLAS ------
DROP TABLE IF EXISTS facilityBooking;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS facility;
DROP TABLE IF EXISTS fee;
DROP TABLE IF EXISTS feeItem;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS activitybooking;
DROP TABLE IF EXISTS activitymember;
DROP TABLE IF EXISTS monitor;



------ CREACION DE LAS TABLAS ------

CREATE TABLE activity (
    activity_id INTEGER NOT NULL,
    activity_name VARCHAR(32) NOT NULL,
    assistant_limit INTEGER,
    deleted BOOLEAN,
    PRIMARY KEY(activity_id)
);

CREATE TABLE activitybooking (
    activity_id INTEGER NOT NULL,
    facilitybooking_id INTEGER NOT NULL,
    monitor_id INTEGER,
    PRIMARY KEY (activity_id, facilitybooking_id)
);

CREATE TABLE activitymember (
    activity_id INTEGER NOT NULL,
    facilitybooking_id INTEGER NOT NULL,
    member_id INTEGER NOT NULL,
    assistance BOOLEAN,
    deleted BOOLEAN,
    PRIMARY KEY(activity_id, facilitybooking_id, member_id),
);

CREATE TABLE facility (
	facility_id INTEGER NOT NULL PRIMARY KEY,
	price INTEGER,
	facility_name VARCHAR(32)
);

CREATE TABLE facilityBooking (
    facilitybooking_id INTEGER NOT NULL AUTO_INCREMENT,
	facility_id INTEGER NOT NULL,
	member_id INTEGER NOT NULL,
	time_start TIMESTAMP NOT NULL,
	time_end TIMESTAMP,
	payment_method VARCHAR(16),
    paid BOOLEAN,
	entrance TIMESTAMP,
	abandon TIMESTAMP,
	state VARCHAR(16),
	cancellation_cause VARCHAR(128),
	cancellation_date TIMESTAMP,
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('Fee', 'Cash')),
    CONSTRAINT chk_state CHECK (state IN ('Valid', 'Annulled', 'Canceled')),
    PRIMARY KEY (facilitybooking_id)
);

CREATE TABLE fee (
    fee_month DATE NOT NULL,
    fee_member_id INTEGER NOT NULL,
	fee_base INTEGER NOT NULL,
	PRIMARY KEY (fee_month, fee_member_id)

);

CREATE TABLE feeitem (
    feeitem_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fee_member_id INTEGER NOT NULL,
    feeitem_amount DOUBLE,
    feeitem_concept VARCHAR(64),
	fee_month DATE NOT NULL,
	CONSTRAINT fk_FEE_ITEM FOREIGN KEY(fee_month, fee_member_id) REFERENCES FEE(fee_month, fee_member_id)
);

CREATE TABLE member (
	member_id INTEGER NOT NULL PRIMARY KEY,
	member_name VARCHAR(32) NOT NULL,
	subscribed BOOLEAN NOT NULL
);

CREATE TABLE monitor (
    monitor_id INTEGER NOT NULL,
	monitor_nombre VARCHAR(80),
    PRIMARY KEY(monitor_id)
);







------ AÑADIR DATOS A LAS TABLAS ------
/*

INSERT INTO member VALUES (1, 'Gabriel',true);
INSERT INTO member VALUES (2, 'Jorge',true);
INSERT INTO member VALUES (3, 'Sergio',true);

INSERT INTO facility VALUES (1, 5,'Tennis 1');
INSERT INTO facility VALUES (2, 3,'Tennis 2');
INSERT INTO facility VALUES (3, 5,'Sala Libre 1');
INSERT INTO facility VALUES (4, 3,'Sala Libre 2');
INSERT INTO facility VALUES (5, 5,'Futbol 7');
INSERT INTO facility VALUES (6, 3,'Tatami');

INSERT INTO facilitybooking VALUES (1, 1, 1,
    PARSEDATETIME('12-11-2016 18:00:00', 'dd-MM-yyyy HH:mm:ss'),
    PARSEDATETIME('12-11-2016 19:00:00', 'dd-MM-yyyy HH:mm:ss'),
    'Cash', false, false, null, null, 'Valid');
INSERT INTO facilitybooking VALUES (2, 2, 1,
    PARSEDATETIME('12-11-2016 15:00:00', 'dd-MM-yyyy HH:mm:ss'),
    PARSEDATETIME('12-11-2016 16:00:00', 'dd-MM-yyyy HH:mm:ss'),
    'Cash', false, false, null, null, 'Valid');
INSERT INTO facilitybooking VALUES (3, 1, 2,
    PARSEDATETIME('13-11-2016 10:00:00', 'dd-MM-yyyy HH:mm:ss'),
    PARSEDATETIME('13-11-2016 12:00:00', 'dd-MM-yyyy HH:mm:ss'),
    'Cash', false, false, null, null, 'Valid');
INSERT INTO facilitybooking VALUES (4, 1, 0,
    PARSEDATETIME('15-11-2016 10:00:00', 'dd-MM-yyyy HH:mm:ss'),
    PARSEDATETIME('15-11-2016 18:00:00', 'dd-MM-yyyy HH:mm:ss'),
    'Cash', false, false, null, null, 'Valid');
INSERT INTO facilitybooking VALUES (5, 1, 0,
    PARSEDATETIME('15-11-2016 19:00:00', 'dd-MM-yyyy HH:mm:ss'),
    PARSEDATETIME('15-11-2016 20:00:00', 'dd-MM-yyyy HH:mm:ss'),
    'Cash', false, false, null, null, 'Valid');

INSERT INTO monitor VALUES(1,'Rodolfo Perez');
INSERT INTO monitor VALUES(2,'Otro Monitor');

INSERT INTO activity VALUES (1, 'Taller 1', 25, 1);
INSERT INTO activity VALUES (2, 'Taller 2', 50, 1);
INSERT INTO activity VALUES (3, 'Taller 3', 75, 1);
INSERT INTO activity VALUES (4, 'Yoga 1', 10, 2);

INSERT INTO activitybooking VALUES(1, 4);
INSERT INTO activitybooking VALUES(2, 4);
INSERT INTO activitybooking VALUES(1, 5);
INSERT INTO activitybooking VALUES(2, 5);

INSERT INTO activitymember VALUES (1, 4, 1, true, false);
INSERT INTO activitymember VALUES (1, 4, 2, true, false);

INSERT INTO activitymember VALUES (1, 5, 2, true, false);
INSERT INTO activitymember VALUES (1, 5, 1, false, false);
*/