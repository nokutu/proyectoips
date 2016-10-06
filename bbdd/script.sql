DROP TABLE facilityBooking;
DROP TABLE member;
DROP TABLE facility;
DROP TABLE fee;
DROP TABLE feeItem;

CREATE TABLE facilityBooking (
	facility_id INTEGER,
	member_id INTEGER,
	dayDate DATE,
	hour_start INTEGER,
	hour_end INTEGER,
	payment_method VARCHAR(16),
    paid BOOLEAN,
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('fee', 'cash'))
);

CREATE TABLE member (
	member_id INTEGER NOT NULL PRIMARY KEY,
	member_name VARCHAR(32) NOT NULL,
);

CREATE TABLE facility (
	facility_id INTEGER NOT NULL PRIMARY KEY,
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
    feeitem_amount INTEGER
);

INSERT INTO member VALUES (1, 'Gabriel');
INSERT INTO member VALUES (2, 'Jorge');
INSERT INTO member VALUES (3, 'Sergio');

INSERT INTO facility VALUES (1, 'Tennis 1');
INSERT INTO facility VALUES (2, 'Tennis 2');