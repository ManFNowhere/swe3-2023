CREATE TABLE Nutzer(
  UserID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  Vorname VARCHAR(30) NOT NULL,
  Nachname VARCHAR(30) NOT NULL,
  Mail VARCHAR(50) NOT NULL UNIQUE,
  Passwort VARBINARY(20) NOT NULL
);

CREATE TABLE Impfstoff(
  Bezeichnung VARCHAR(30) NOT NULL PRIMARY KEY,
  Hersteller VARCHAR(30) NOT NULL
);

CREATE TABLE Impfzentrum(
  ImpfzentrumsID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  Stadt VARCHAR(30) NOT NULL,
  Ort VARCHAR(30) NOT NULL,
  Impfkapazität INT NOT NULL
);

CREATE TABLE Impfangebot(
  ImpfangebotsID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  Bezeichnung VARCHAR(30),
  ImpfzentrumsID INT,
  FOREIGN KEY(Bezeichnung) REFERENCES Impfstoff(Bezeichnung),
  FOREIGN KEY(ImpfzentrumsID) REFERENCES Impfzentrum(ImpfzentrumsID)
);

CREATE TABLE Slot(
  SlotID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  Uhrzeit TIME NOT NULL,
  ImpfzentrumsID INT, 
  FOREIGN KEY(ImpfzentrumsID) REFERENCES Impfzentrum(ImpfzentrumsID)
);

CREATE TABLE Buchung(
  BuchungsID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  Buchungsdatum date NOT NULL,
  Vorname VARCHAR(30) NOT NULL,
  Nachname VARCHAR(30) NOT NULL,
  Impftermin VARCHAR(30) NOT NULL,
  SlotID INT,
  UserID INT,
  Bezeichnung VARCHAR(30),
  ImpfzentrumsID INT,
  FOREIGN KEY(SlotID) REFERENCES Slot(SlotID),
  FOREIGN KEY(UserID) REFERENCES Nutzer(UserID),
  FOREIGN KEY(Bezeichnung) REFERENCES Impfstoff(Bezeichnung),
  FOREIGN KEY(ImpfzentrumsID) REFERENCES Impfzentrum(ImpfzentrumsID)
);

CREATE TABLE Stornierung(
  StornierungsID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  BuchungsID INT UNIQUE,
  Stornierungsdatum date NOT NULL,
  FOREIGN KEY(BuchungsID) REFERENCES Buchung(BuchungsID)
);
