CREATE USER tukarta WITH PASSWORD 'TuKartaP4$$' superuser;

CREATE TYPE provincia AS
ENUM('Álava', 'Albacete', 'Alicante', 'Almería', 'Asturias', 'Ávila', 'Badajoz', 'Barcelona','Burgos','Cáceres', 'Cádiz','Cantabria','Castellón', 'Ceuta', 'Ciudad Real', 'Córdoba', 'A Coruña', 'Cuenca', 'Girona', 'Granada', 'Guadalajara', 'Guipuzkoa', 'Huelva', 'Huesca', 'Islas Baleares', 'Jaén', 'León', 'Lleida', 'Lugo', 'Madrid', 'Málaga', 'Melilla', 'Murcia','Navarra', 'Ourense', 'Palencia', 'Las Palmas', 'Pontevedra', 'La Rioja', 'Salamanca', 'Segovia', 'Sevilla', 'Soria', 'Tarragona', 'Tenerife', 'Teruel', 'Toledo', 'Valencia', 'Valladolid', 'Bizkaia', 'Zamora', 'Zaragoza');

CREATE TYPE alergeno AS
ENUM('pescado','frutos secos','lacteos','moluscos','gluten','crustáceos','huevo','cacahuete','soja','apio','mostaza','sesamo','altramuz','sulfito');

CREATE TYPE rol AS
ENUM('camarero', 'cocinero', 'gestor');

CREATE TYPE estado AS
ENUM('preparando', 'listo', 'pendiente');

CREATE TABLE Usuario(	
	-- Usuario base
	usuario 		varchar(30) NOT NULL,
	pwd				varchar(30) NOT NULL,	
	email 			varchar(30),
	nombre 			varchar(30),
	apellidos 		varchar(30),
	fecha_alta 		date NOT NULL,
	fecha_modificacion date NOT NULL,
	fecha_baja 		date,
	isGestor 		boolean DEFAULT false,
	-- datos del Empleado
	gestor 			varchar(30),
	trabajadorDe	varchar(30),
	salario			float,
	rol				rol,
	active			boolean DEFAULT true,
	-- datos del gestor
	
	
	CONSTRAINT "FK_gestor_USUARIO" FOREIGN KEY (gestor) REFERENCES public.usuario (email) ON UPDATE CASCADE ON DELETE SET NULL,	
	PRIMARY KEY (email)
);

ALTER TABLE Usuario OWNER to tukarta;
	
CREATE TABLE producto (
	-- Parte del producto
	id			varchar(30) PRIMARY KEY,
	nombre		varchar(30) NOT NULL,
	alergenos	alergeno[] DEFAULT '{}',
	precio		float DEFAULT 0,
	disponible	integer,
	tiempo_elaboracion	integer,
	-- Parte del Menú
	contenido  	TEXT[] DEFAULT '{}',
	precio_real	float DEFAULT 0,
	imagen BYTEA DEFAULT NULL
);

CREATE TABLE mesa (
	id 			varchar(30) PRIMARY KEY,
	num_comensales int NOT NULL,
	restaurante varchar(30)
);

CREATE TABLE pedido (
	id 			varchar(30) PRIMARY KEY,
	empleado 	varchar(30) NOT NULL, 
	mesa		varchar(30) NOT NULL,
	precio_final float,
	fecha		date,
	activo		boolean,
	
	CONSTRAINT "FK_pedido_empleado" FOREIGN KEY (empleado) REFERENCES public.usuario (email) ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT "FK_pedido_mesa" FOREIGN KEY (mesa) REFERENCES public.mesa (id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE pedido_estado (
	id varchar(30) PRIMARY KEY,
  	id_pedido	varchar(30),
	id_producto varchar(30),
	estado estado,
	
	CONSTRAINT "FK_pedido_estado_pedido" FOREIGN KEY (id_pedido) REFERENCES pedido (id) ON UPDATE CASCADE ON DELETE SET NULL,	
	CONSTRAINT "FK_pedido_estado_producto" FOREIGN KEY (id_producto) REFERENCES producto (id) ON UPDATE CASCADE ON DELETE SET NULL	
);


CREATE TABLE Restaurante (
	id				varchar(30) PRIMARY KEY,
	nombre			varchar(30) NOT NULL,
	provincia		provincia,
	direccion		varchar(100),
	gestor 			varchar(30) NOT NULL,	
	
	CONSTRAINT "FK_restaurante_gestor" FOREIGN KEY (gestor) REFERENCES public.usuario (email) ON UPDATE CASCADE ON DELETE SET NULL
);

ALTER TABLE Restaurante OWNER to tukarta;	

ALTER TABLE usuario ADD CONSTRAINT "FK_empleado_restaurante" FOREIGN KEY (trabajadorDe) REFERENCES public.restaurante (id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE mesa ADD CONSTRAINT "FK_mesa_restaurante" FOREIGN KEY (restaurante) REFERENCES public.restaurante (id) ON UPDATE CASCADE ON DELETE SET NULL;


-- USUARIOS GESTORES
INSERT INTO usuario VALUES ('Marc', 'marcPass', 'marc@tukarta.com', 'Marc', 'Abad', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null, true);
INSERT INTO usuario VALUES ('TuKarta', 'TuKartaPass', 'tukarta@tukarta.com', 'Tu', 'Karta', '2020-11-05 12:10:00', '2020-11-05 12:10:00', null, true);

-- RESTAURANTES
INSERT INTO restaurante VALUES ('res1Marc', 'Can Marc', 'Barcelona', 'Calle de la buena Suerte, 23', 'marc@tukarta.com');
INSERT INTO restaurante VALUES ('res1TuKarta', 'Can Fortunata', 'Girona', 'Calle de Abajo, 05', 'tukarta@tukarta.com');

-- USUARIOS EMPLEADOS
INSERT INTO usuario VALUES ('Manu', 'manuPass', 'manu@tukarta.com', 'Manuel', 'Mora', '2020-10-22 12:10:00', '2020-10-22 12:10:00', null, false, 'marc@tukarta.com', 'res1Marc', 1600, 'camarero');
INSERT INTO usuario VALUES ('David', 'davidPass', 'david@tukarta.com', 'David', 'Domenech', '2020-10-22 12:10:00','2020-10-22 12:10:00', null, false, 'marc@tukarta.com', 'res1Marc', 1500, 'cocinero');
-- PRODUCTOS
INSERT INTO producto VALUES ('P001', 'Patatas Bravas', '{gluten, lacteos}', 3.50, 15, 15);
INSERT INTO producto VALUES ('P002', 'Patatas All i Oli', '{gluten, lacteos, huevo}', 4.50, 10, 15);
INSERT INTO producto VALUES ('B001', 'Coca Cola', '{}', 1.50, 200, 0);

-- MENUS
INSERT INTO producto VALUES ('M001', 'Menú patatas', '{gluten, lacteos, huevo}', 8.50, 10, 15, '{P001, B001, B001, B001}');
INSERT INTO producto VALUES ('M002', 'Menú ali oli', '{gluten, lacteos, huevo}', 9.00, 10, 15, '{P002, B001, B001, B001}');

-- MESAS
INSERT INTO mesa VALUES ('mesa1CanMarc', 4, 'res1Marc');
INSERT INTO mesa VALUES ('mesa2CanMarc', 6, 'res1Marc');
INSERT INTO mesa VALUES ('mesa1CanFortunata', 8, 'res1TuKarta');

-- PEDIDOS
INSERT INTO pedido VALUES('pedido1M', 'manu@tukarta.com', 'mesa1CanMarc', 0,'2020-11-20', true );
INSERT INTO pedido VALUES('pedido2M', 'david@tukarta.com', 'mesa2CanMarc', 0,'2020-11-20', true);
INSERT INTO pedido VALUES('pedido1T', 'manu@tukarta.com', 'mesa1CanFortunata', 0,'2020-11-20', false);
INSERT INTO pedido VALUES('pedido2T', 'david@tukarta.com', 'mesa1CanFortunata', 0,'2020-11-20', true);

-- pedido_estado
INSERT INTO pedido_estado VALUES ('pedido1M-1', 'pedido1M', 'P001', 'preparando');
INSERT INTO pedido_estado VALUES ('pedido1M-2', 'pedido1M', 'B001', 'listo');
INSERT INTO pedido_estado VALUES ('pedido2M-1', 'pedido2M', 'B001', 'listo');

INSERT INTO pedido_estado VALUES ('pedido1T-1','pedido1T', 'P002', 'preparando');
INSERT INTO pedido_estado VALUES ('pedido2T-1','pedido2T', 'B001', 'preparando');
INSERT INTO pedido_estado VALUES ('pedido2T-2','pedido2T', 'B001', 'listo');
