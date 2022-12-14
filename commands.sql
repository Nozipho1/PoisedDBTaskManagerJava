# CREATE DATABASE Poised_db;
#
# USE Poised_db;
#
#
# create table Projects (
#                           projNum varchar(50) not null,
#                           projName varchar(50) not null,
#                           buildingType varchar(50),
#                           address varchar(50),
#                           erfNum varchar(50),
#                           fee float,
#                           amountPaid float,
#                           deadline varchar(50),
#                           architectName varchar(50),
#                           structuralEngineerName varchar(50),
#                           customerName varchar (50),
#                           Finalised varchar (50),
#                           dateFinalised varchar (50),
#                           primary key (projNum));


insert into Projects values (
                                "2","Tyson House","House","77 Juj street","76453",70000,700,"2020-06-07","John Andrews","Mary Andrews","Kai Andrews", "Incomplete","n/a");
Insert into Projects values ("123","Ntsebeza house","House","16 Star street","1999",25000.0,2000.0,"2009/02/23","God Star", "Universe Star", "Galaxy Star","Finalised", "2022-12-04");
Insert into Projects values ("45","Helios","House","12 lane road","12345",45200.0,22000.0,"2020/04/12","Sandy Lane", "Bob Lane", "Wendy Lane", "Finalised", "2022-11-02");


create table architectName (
                               projNum varchar(50)not null,
                               name varchar(50) not null,
                               surname varchar(50) not null,
                               telephone varchar(50),
                               email varchar(50),
                               address varchar(50),
                               foreign key (projNum) references Projects(projNum)
                               on delete cascade
                           );



Insert into architectName values ("2","John","Andrews","01173662","john@Gmail.com","7 Juj street");
Insert into architectName values
    ("123", "God","Star","777888999222","god@gmail.com","heaven avenue");
Insert into architectName values
    ("45","Sandy","Lane","1231231234","sandy@mail","12 lane road");


Create table structuralEngineerName (
                                        projNum varChar(50) not null,
                                        name varchar(50) not null,
                                        surname varchar(50) not null,
                                        telephone varchar(50),
                                        email varchar(50),
                                        address varchar(50),
                                        foreign key (projNum) references Projects(projNum)
                                            on delete cascade);


Insert into structuralEngineerName values ("2","Mary","Andrews","0878266352","mary@gmail.com","8 juj street");
Insert into structuralEngineerName values
    ("123","Universe","Star","0828242720","treehouse@gmail","galaxy street");
Insert into structuralEngineerName values
    ("45","Bob","Lane","1231231234","bob@mail","13 lane road");


create table customerName (
                              projNum varchar (50),
                              name varchar(50) not null,
                              surname varchar(50) not null,
                              telephone varchar(50),
                              email varchar(50),
                              address varchar(50),
                              foreign key (projNum) references Projects(projNum)
                                  on delete cascade);


Insert into customerName values
    ("2","Kai","Andrews","017625237","kai@gmail.com","716 juj street");
Insert into customerName values
    ("123","Gaia","Star","333444555","gaia@gmail.com","earth street");
Insert into customerName values
    ("45","Wendy","Lane","555666777","wendy@mail","14 lane road");

create table completedProjects (
                                   projNum varchar(50) not null,
                                   projName varchar(50) not null,
                                   buildingType varchar(50),
                                   address varchar(50),
                                   erfNum varchar(50),
                                   fee float,
                                   amountPaid float,
                                   deadline varchar(50),
                                   architectName varchar(50),
                                   structuralEngineerName varchar(50),
                                   customerName varchar (50),
                                   Finalised varchar (50),
                                   dateFinalised varchar (50),
                                   primary key (projNum));

Insert into completedProjects values ("123","Ntsebeza house","House","16 Star street","1999",25000.0,2000.0,"2009/02/23","God Star", "Universe Star", "Galaxy Star","Finalised", "2022-12-04");
Insert into completedProjects values ("45","Helios","House","12 lane road","12345",45200.0,22000.0,"2020/04/12","Sandy Lane", "Bob Lane", "Wendy Lane", "Finalised", "2022-11-02");


delete from projects where projNum=6969;