DROP TABLE IF EXISTS facilityBooking;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS facility;
DROP TABLE IF EXISTS fee;
DROP TABLE IF EXISTS feeItem;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS activitybooking;

CREATE TABLE facilityBooking (
	facility_id INTEGER NOT NULL,
	member_id INTEGER NOT NULL,
	time_start TIMESTAMP NOT NULL,
	time_end TIMESTAMP,
	payment_method VARCHAR(16),
    paid BOOLEAN,
    facilitybooking_deleted BOOLEAN,
	entrance TIMESTAMP,
	abandon TIMESTAMP,
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('Fee', 'Cash')),
    PRIMARY KEY (facility_id, time_start)
);

CREATE TABLE member (
	member_id INTEGER NOT NULL PRIMARY KEY,
	member_name VARCHAR(32) NOT NULL,
);

CREATE TABLE facility (
	facility_id INTEGER NOT NULL PRIMARY KEY,
	price INTEGER,
	facility_name VARCHAR(32)
);

CREATE TABLE fee (
    fee_id INTEGER NOT NULL PRIMARY KEY,
    fee_month DATE NOT NULL,
    fee_member_id INTEGER NOT NULL
);

CREATE TABLE feeitem (
    feeitem_id INTEGER NOT NULL PRIMARY KEY,
    feeitem_concept VARCHAR(64),
	fee_id INTEGER NOT NULL,
    feeitem_amount INTEGER
);

CREATE TABLE activity (
    activity_id INTEGER NOT NULL PRIMARY KEY,
    activity_name VARCHAR(32) NOT NULL,
    assistant_limit INTEGER,
    activity_time_start TIMESTAMP,
    activity_recursive BOOLEAN,
    activity_duration INTEGER,
    activity_time_end TIMESTAMP,
);

CREATE TABLE activitybooking (
    activity_id INTEGER NOT NULL,
    facility_id INTEGER NOT NULL,
    booking_time_start TIMESTAMP NOT NULL,
    PRIMARY KEY (activity_id, facility_id, booking_time_start)
);

CREATE TABLE activitymembers (
    activity_id INTEGER NOT NULL,
    member_id INTEGER NOT NULL,
    assistance BOOLEAN,
    PRIMARY KEY(activity_id, member_id),
);

INSERT INTO member VALUES (0, 'Administrator');
INSERT INTO member VALUES (1, 'Gabriel');
INSERT INTO member VALUES (2, 'Jorge');
INSERT INTO member VALUES (3, 'Sergio');

INSERT INTO facility VALUES (1, 5,'Tennis 1');
INSERT INTO facility VALUES (2, 3,'Tennis 2');

INSERT INTO facilitybooking VALUES (1, 1,
    PARSEDATETIME('17-10-2016 18:0:0', 'dd-MM-yyyy hh:mm:ss'),
    PARSEDATETIME('17-10-2016 19:0:0', 'dd-MM-yyyy hh:mm:ss'),
    'Cash', false, false, null, null);