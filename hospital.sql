CREATE DATABASE IF NOT EXISTS hospital;
USE hospital;

CREATE TABLE receta (
    id int auto_increment primary key,
    identificacion varchar(80),
    pacienteId int NOT NULL,
    fechaEntrega DATE NOT NULL,
    recetaDetalleId INT NOT NULL,
    estado VARCHAR(30) NOT NULL,

    CONSTRAINT fk_receta_paciente
        FOREIGN KEY (pacienteId)
        REFERENCES paciente(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_receta_detalle
        FOREIGN KEY (recetaDetalleId)
        REFERENCES recetaDetalle(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE recetaDetalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medicamentoId int NOT NULL,
    cantidad INT NOT NULL,
    indicaciones VARCHAR(500),
    diasDuracion INT NOT NULL,

    CONSTRAINT fk_detalle_medicamento
        FOREIGN KEY (medicamentoId)
        REFERENCES medicamento(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE paciente (
    id int auto_increment primary key,
    identificacion varchar(80),
    nombre VARCHAR(100) NOT NULL,
    fechaNacimiento DATE NOT NULL,
   telefono VARCHAR(20)
);

create table medicamento (
    id int auto_increment primary key,
    codigo varchar(80),
    nombre varchar(80) not null,
    descripcion varchar(80) not null
)

create table medico (
    id int auto_increment primary key,
    identificacion varchar(80),
	 clave varchar(80) not null,
    nombre varchar(80) not null,
    especialidad varchar(80) not null
)

create table farmaceuta (
     id int auto_increment primary key,
     identificacion varchar(80),
     clave varchar(80) not null,
     nombre varchar(80) not null
)

CREATE TABLE administrador (
     id int auto_increment primary key,
     identificacion varchar(80),
     clave varchar(80) not null
)