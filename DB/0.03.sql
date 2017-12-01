create database homepro
go
use homepro
go

create table tbl_persona(
email_persona varchar(100) primary key,
nombre_persona varchar(100) not null,
apellido_persona varchar(100) not null,
telefono_persona varchar(100) not null,
direccion_persona varchar(100) not null,
contrasena_persona varchar(64) not null,
foto_persona nvarchar(max) null
)
go

create table tbl_especialidad(
id_especialidad int identity primary key not null,
descripcion_especialidad varchar(100) not null
)
go
create table tbl_especialidad_persona(
email_persona_especialidad_persona varchar(100) not null,
id_especialidad_especialidad_persona int not null,
especialidad_principal_especialidad_persona varchar(150) not null
)
go

/*LLAVES FORANEAS*/
 
 alter table tbl_especialidad_persona
 add constraint FK_email
 foreign key (email_persona_especialidad_persona)
 references tbl_persona (email_persona)
 go
 alter table tbl_especialidad_persona
 add constraint fk1
 foreign key (id_especialidad_especialidad_persona)
 references tbl_especialidad(id_especialidad)
 go

