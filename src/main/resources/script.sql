create database tunvistaJ;
use tunvistaJ;

    create table reclamation(
        id int auto-increment primary key,
        description varchar(100) not null ,
        categorie varchar(50) not null,
        etat varchar(20) not null ,
        iduser varchar(20) not null ,
        date date
    );
insert into reclamation(description,categorie,etat,iduser,date)