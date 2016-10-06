DROP TABLE facilityBooking;
DROP TABLE member;
DROP TABLE facility;

CREATE TABLE facilityBooking (
	fecha DATE,
	hora_inicio NUMBER,
	hora_fin NUMBER,
	socio_id NUMBER
);

CREATE TABLE member (
	socio_id NUMBER NOT NULL PRIMARY KEY,
);

CREATE TABLE facility (
	facility_id NUMBER PRIMARY KEY,
	facility_name VARCHAR(32)
);

INSERT INTO facility VALUES (1, 'Tennis 1');
INSERT INTO facility VALUES (2, 'Tennis 2');