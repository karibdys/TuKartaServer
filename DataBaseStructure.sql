CREATE USER tukarta WITH PASSWORD 'TuKartaP4$$' superuser;

CREATE TYPE provincia AS
ENUM('Álava', 'Albacete', 'Alicante', 'Almería', 'Asturias', 'Ávila', 'Badajoz', 'Barcelona','Burgos','Cáceres', 'Cádiz','Cantabria','Castellón', 'Ceuta', 'Ciudad Real', 'Córdoba', 'A Coruña', 'Cuenca', 'Girona', 'Granada', 'Guadalajara', 'Guipuzkoa', 'Huelva', 'Huesca', 'Islas Baleares', 'Jaén', 'León', 'Lleida', 'Lugo', 'Madrid', 'Málaga', 'Melilla', 'Murcia','Navarra', 'Ourense', 'Palencia', 'Las Palmas', 'Pontevedra', 'La Rioja', 'Salamanca', 'Segovia', 'Sevilla', 'Soria', 'Tarragona', 'Tenerife', 'Teruel', 'Toledo', 'Valencia', 'Valladolid', 'Bizkaia', 'Zamora', 'Zaragoza');

CREATE TABLE Usuario
(
	usuario 		varchar(30) NOT NULL,
	pwd				varchar(20) NOT NULL,
	email 			varchar(40) NOT NULL,
	nombre 			varchar(30),
	apellidos 		varchar(30),
	fecha_alta 		date NOT NULL,
	fecha_modificacion date NOT NULL,
	fecha_baja 		date,
	isGestor 		boolean DEFAULT false,
	gestor 			varchar(30),
	trabajadorDe	varchar(20),
	salario			float,
	rol				varchar(20),
	
	CONSTRAINT "FK_gestor_USUARIO" FOREIGN KEY (gestor) REFERENCES public.usuario (email)	ON UPDATE CASCADE ON DELETE SET NULL,
	PRIMARY KEY (email)
);

ALTER TABLE Usuario OWNER to tukarta;


CREATE TABLE Restaurante
(
	id				varchar(20),
	nombre			varchar(30),
	provincia		provincia,
	direccion		varchar(100),
	gestor 			varchar(30),		
	CONSTRAINT "FK_restaurante_gestor" FOREIGN KEY (gestor) REFERENCES public.usuario (email) ON UPDATE CASCADE ON DELETE SET NULL;
);

ALTER TABLE Restaurante OWNER to tukarta;	


INSERT INTO usuario VALUES ('David', 'davidPass', 'david@tukarta.com', 'David', 'Domenech', '2020-10-22 12:10:00','2020-10-22 12:10:00', null);
INSERT INTO usuario VALUES ('Manu', 'manuPass', 'manu@tukarta.com', 'Manuel', 'Mora', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null);
INSERT INTO usuario VALUES ('Marc', 'marcPass', 'marc@tukarta.com', 'Marc', 'Abad', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null, true);
