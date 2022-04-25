CREATE TABLE Lieferanten (
	LieferantId int primary key auto_increment,
	Name varchar(128) not null unique,
	Email varchar(255) not null,
	Ort varchar(128) not null,
	PLZ varchar(128) not null,
	Strasse varchar(128) not null,
	Hnr varchar(128) not null,
	Land varchar(128) not null
);

CREATE TABLE Artikel (
	ArtikelId int primary key auto_increment,
	Bezeichnung varchar(128) not null,
	Beschreibung varchar(20000) not null,
	VerkaufspreisNetto decimal(10,2) not null,
	LieferantId int references Lieferanten(LieferantId)
);

