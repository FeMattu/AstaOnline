/*Elimina DB se presente, crea se non è presente*/
DROP DATABASE IF EXISTS gambit;
CREATE DATABASE IF NOT EXISTS gambit;

/*Entra all'interno del db*/
USE gambit;

CREATE TABLE Categorie(
	id_categoria INT(3) NOT NULL AUTO_INCREMENT,
	categoria VARCHAR(50) NOT NULL,
	PRIMARY KEY(id_categoria)
);

CREATE TABLE Clienti(
	UserName VARCHAR(32) NOT NULL,
	nome VARCHAR(40) NOT NULL,
	cognome VARCHAR(40) NOT NULL,
	data_nascita DATE NOT NULL,
	residenza VARCHAR(500) NOT NULL,
	password VARCHAR(20) NOT NULL,
	email VARCHAR(319) NOT NULL,
	PRIMARY KEY (UserName)
);

CREATE TABLE Prodotti(
	id_prodotto INT(6) NOT NULL AUTO_INCREMENT,
	nome VARCHAR(255) NOT NULL,
	descrizione TEXT NOT NULL,
	prezzoDiBase FLOAT NOT NULL,
	venduto BOOLEAN NOT NULL,
	venditore varchar(32) NOT NULL,
	id_categoria INT(3) NOT NULL,
	dataOra_aggiunta DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY(id_prodotto),
  CONSTRAINT ProdottiVenditori FOREIGN KEY(venditore)
		REFERENCES Clienti(UserName),
	CONSTRAINT ProdottiCategorie FOREIGN KEY(id_categoria)
		REFERENCES Categorie(id_categoria)
);

CREATE TABLE Aste(
	id_asta INT(5) NOT NULL AUTO_INCREMENT,
	dataOra_inizio DATETIME NOT NULL DEFAULT NOW(),
	dataOra_fine DATETIME,
	vincitore VARCHAR(32) NOT NULL,
	id_prodotto INT(6) NOT NULL,
	ip VARCHAR(15) NOT NULL,
	
	PRIMARY KEY(id_asta),
	CONSTRAINT AsteClienti FOREIGN KEY(vincitore)
		REFERENCES Clienti(UserName),
	CONSTRAINT AsteProdotti FOREIGN KEY(id_prodotto)
		REFERENCES Prodotti(id_prodotto)
);

CREATE TABLE Offerte(
	id_offerta INT(255) NOT NULL AUTO_INCREMENT,
	dataOra_offerta DATETIME NOT NULL DEFAULT NOW(),
	offerta FLOAT NOT NULL,
	UserName VARCHAR(32) NOT NULL,
	id_asta INT(5) NOT NULL,

	PRIMARY KEY(id_offerta),
	CONSTRAINT OffertePersone FOREIGN KEY (UserName)
		REFERENCES Clienti(UserName),
	CONSTRAINT OfferteAste FOREIGN KEY (id_asta)
		REFERENCES Aste(id_asta)
);
