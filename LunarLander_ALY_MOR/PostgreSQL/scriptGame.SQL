 CREATE DATABASE game;
 CREATE TABLE IF NOT EXISTS usuario(
 id_usuario VARCHAR(30) NOT NULL,
 nombre VARCHAR(20) NOT NULL,
 apellido VARCHAR(20) NOT NULL,
 contraseña VARCHAR(255) NOT NULL,
 CONSTRAINT PK_Usuario 
 PRIMARY KEY (id_usuario)
 );  
 CREATE TABLE IF NOT EXISTS configuracion(
  id_configuracion VARCHAR(50) NOT NULL,
  id_usuario VARCHAR(30) NOT NULL,
  dificultad INT(11),
  nave INT(11),
  luna INT(11),
  CONSTRAINT PK_Configuracion PRIMARY KEY (id_configuracion)
 );
 CREATE TABLE IF NOT EXISTS score(
	id_usuario		   VARCHAR(30) NOT NULL,
	id_puntuacion      INT         NOT NULL,
    id_configuracion   VARCHAR(50) NOT NULL,
    velocidad          NUMERIC(3,2),
    fuel               INT,
    init_time          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	end_time           TIMESTAMP,
	CONSTRAINT PK_Score PRIMARY KEY (id_puntuacion),
    CONSTRAINT FK_Usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT FK_Configuracion FOREIGN KEY (id_configuracion) REFERENCES configuracion(id_configuracion)
 );



