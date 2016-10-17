DROP TABLE IF EXISTS facilityBooking;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS facility;
DROP TABLE IF EXISTS fee;
DROP TABLE IF EXISTS feeItem;

CREATE TABLE facilityBooking (
	facility_id INTEGER,
	member_id INTEGER,
	time_start TIMESTAMP,
	time_end TIMESTAMP,
	payment_method VARCHAR(16),
    paid BOOLEAN,
    facilitybooking_deleted BOOLEAN,
	entrance TIMESTAMP,
	abandon TIMESTAMP,
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('Fee', 'Cash'))
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

INSERT INTO member VALUES (0, 'Administrator');
INSERT INTO member VALUES (1, 'Gabriel');
INSERT INTO member VALUES (2, 'Jorge');
INSERT INTO member VALUES (3, 'Sergio');

INSERT INTO facility VALUES (1, 5,'Tennis 1');
INSERT INTO facility VALUES (2, 3,'Tennis 2');

INSERT INTO facilitybooking VALUES (1, 1,
    PARSEDATETIME('17-10-2016 18:0:0', 'dd-MM-yyyy hh:mm:ss'),
    PARSEDATETIME('17-10-2016 19:0:0', 'dd-MM-yyyy hh:mm:ss'),
    'Cash', false, false, null, null)