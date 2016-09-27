DROP TABLE reserva;
DROP TABLE socio;
DROP TABLE instalacion;

CREATE OR REPLACE TABLE reserva (
	fecha DATE,
	hora_inicio NUMBER,
	hora_fin NUMBER,
	socio_id NUMBER
);

CREATE OR REPLACE TABLE socio (
	socio_id NUMBER NOT NULL PRIMARY KEY,
);

CREATE OR REPLACE TABLE instalacion (
	intalacion_id NUMBER PRIMARY KEY
);