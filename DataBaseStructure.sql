CREATE USER tukarta WITH PASSWORD 'TuKartaP4$$' SUPERADMIN;

CREATE TABLE IF NOT EXISTS usuario (
	usuario 			varchar(30) PRIMARY KEY,
	pwd					varchar(20) NOT NULL,
	email				varchar(40) NOT NULL,
	nombre				varchar(30),
	apellidos			varchar(30),
	fecha_alta			date  NOT NULL,
	fecha_modificacion	date  NOT NULL,
	fecha_baja			date ,
	isGestor			boolean DEFAULT false
);

INSERT INTO usuario VALUES ('Manu', 'manuPass', 'manu@tukarta.com', 'Manuel', 'Mora', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null);
INSERT INTO usuario VALUES ('David', 'davidPass', 'david@tukarta.com', 'David', 'Domenech', '2020-10-22 12:10:00','2020-10-22 12:10:00', null);
INSERT INTO usuario VALUES ('Marc', 'marcPass', 'marc@tukarta.com', 'Marc', 'Abad', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null, true);
select * from usuario;