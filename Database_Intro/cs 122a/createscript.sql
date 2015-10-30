create schema mytestdb;
use mytestdb;

CREATE TABLE Borrower(
driver_id INTEGER, 
name VARCHAR(20), 
email VARCHAR(20), 
date_of_birth DATE, 
borrow_count INTEGER, 
rating FLOAT,
PRIMARY KEY (driver_id)
);


CREATE TABLE CarCatalog (
car_catalog_id INTEGER,
category VARCHAR(20),
make VARCHAR(20),
	model VARCHAR(20),
	year INTEGER,
	color VARCHAR(20),
	PRIMARY KEY (car_catalog_id)
	);

CREATE TABLE Car(
car_id INTEGER, 
driver_id INTEGER NOT NULL,
car_cataolog_id INTEGER NOT NULL,
PRIMARY KEY(car_id),
FOREIGN KEY (driver_id) REFERENCES Lender ON DELETE CASCADE,
FOREIGN KEY (car_catalog_id) REFERENCES CarCatalog ON DELETE NO ACTION
);

CREATE TABLE Lender(
driver_id INTEGER, 
lend_count INTEGER, 
rating FLOAT,
car_id INTEGER NOT NULL,
PRIMARY KEY (driver_id),
FOREIGN KEY (driver_id) REFERENCES Borrower,
FOREIGN KEY (car_id) REFERENCES Car
);





CREATE TABLE AccidentReport(
	accident_id INTEGER,
	datetime DATETIME,
	severity INTEGER,
	details VARCHAR(100),
	driver_id INTEGER NOT NULL,
	car_id INTEGER NOT NULL,
	PRIMARY KEY(accident_id),
FOREIGN KEY (driver_id) REFERENCES Borrower ON DELETE NO ACTION,
FOREIGN KEY(car_id) REFERENCES Car ON DELETE NO ACTION
	);


CREATE TABLE Post(
	post_id INTEGER,
	pickup_datetime DATETIME,
	return_datetime DATETIME,
	pickup_location VARCHAR(80),
	return_location VARCHAR(80),
	driver_id INTEGER NOT NULL,
	PRIMARY KEY (post_id),
	FOREIGN KEY (driver_id) REFERENCES Borrower ON DELETE NO ACTION
	)

CREATE TABLE LendPost(
	post_id INTEGER,
	pickup_datetime DATETIME,
	return_datetime DATETIME,
	pickup_location VARCHAR(80),
	return_location VARCHAR(80),
	driver_id INTEGER NOT NULL,
	daily_price FLOAT,
	car_milage INTEGER,
	car_id INTEGER,
	PRIMARY KEY (post_id),
	FOREIGN KEY (driver_id) REFERENCES Lender ON DELETE NO ACTION,
	FOREIGN KEY (car_id) REFERENCES Car ON DELETE NO ACTION
	);

CREATE TABLE BorrowPost(
	post_id INTEGER,
pickup_datetime DATETIME,
	return_datetime DATETIME,
	pickup_location VARCHAR(80),
	return_location VARCHAR(80),
	driver_id INTEGER NOT NULL,
	daily_price_min FLOAT,
	daily_price_max FLOAT,
	car_mileage_min INTEGER,
	car_mileage_max INTEGER,
	car_category VARCHAR(20),
PRIMARY KEY (post_id),
	FOREIGN KEY (driver_id) REFERENCES Borrower ON DELETE NO ACTION
	);

CREATE TABLE Offer (
	offer_id INTEGER,
	offer_datetime DATETIME,
	lender_accept_datetime DATETIME,
	borrower_accept_datetime DATETIME,
	borrow_post_id INTEGER NOT NULL,
	lend_post_id INTEGER NOT NULL,
	driver_id INTEGER NOT NULL,
	PRIMARY KEY(offer_id),
FOREIGN KEY (driver_id) REFERENCES Borrower ON DELETE NO ACTION,
FOREIGN KEY (lend_post_id) REFERENCES LendPost ON DELETE NO ACTION,
FOREIGN KEY (borrow_post_id) REFERENCES BorrowPost ON DELETE NO ACTION
);

CREATE TABLE  Deal (
	offer_id INTEGER,
lender_fee FLOAT,
	borrow_fee FLOAT,
	lender_rating FLOAT,
	borrower_rating FLOAT,
PRIMARY KEY (offer_id),
FOREIGN KEY (offer_id) REFERENCES Offer #ON DELETE CASCADE
	);
	
use information_schema;
select * from tables where table_schema = 'mytestdb'; 
select * from columns where table_schema = 'mytestdb';

