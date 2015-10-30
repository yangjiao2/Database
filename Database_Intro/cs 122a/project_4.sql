DROP SCHEMA `cs122a_project3` ;
CREATE SCHEMA `cs122a_project3` ;
USE `cs122a_project3`;

create table Borrower(
id 						char(20),  /* driver license number */
name 					varchar(100), 
email 					varchar(100), 
birthdate 				date, 
count 					integer, 
rating 					integer,
primary key (id));

create table Lender(
id 						char(20),  /* driver license number */
count 					integer, 
rating 					integer,
primary key (id),
foreign key (id) references Borrower(id) on delete cascade);

create table CarCatalog(
id						integer,   
category				varchar(20),
make					varchar(20),
model					varchar(30),
year					integer,
color					varchar(20),
primary key (id));

create table Car(
id						char(20), /* car license plate number */
car_catalog_id			integer not null,
owner_id				char(20) not null,
primary key (id),
foreign key (car_catalog_id) references CarCatalog(id) on delete cascade,
foreign key (owner_id) references Lender(id) on delete cascade);

create table BorrowPost(
id 						integer,
pickup_datetime 		datetime,
return_datetime 		datetime,
pickup_location 		varchar(200),
return_location 		varchar(200),
daily_price_min 		integer,
daily_price_max 		integer,
car_mileage_min 		integer,
car_mileage_max 		integer,
car_category			varchar(20),	
borrower_id 				char(20) not null,
primary key (id),
foreign key (borrower_id) references Borrower(id) on delete cascade);

create table LendPost(
id 						integer,
pickup_datetime 		datetime,
return_datetime 		datetime,
pickup_location 		varchar(200),
return_location 		varchar(200),
daily_price 			integer,
car_mileage 			integer,
lender_id 				char(20) not null,
car_id					char(20) not null,
primary key (id),
foreign key (lender_id) references Lender(id) on delete cascade,
foreign key (car_id) references Car(id) on delete cascade);

create table Offer(
id 							integer,
offer_datetime				datetime,
borrower_accept_datetime	datetime,
lender_accept_datetime		datetime,
borrow_post_id				integer not null,
lend_post_id				integer not null,
offerer_id					char(20) not null,
primary key (id),
foreign key (borrow_post_id) references BorrowPost(id) on delete cascade,
foreign key (lend_post_id) references LendPost(id) on delete cascade,
foreign key (offerer_id) references Borrower(id) on delete cascade);

create table Deal(
id						integer,
borrower_fee			double,
lender_fee				double,
borrower_rating			integer,
lender_rating			integer,
primary key (id),
foreign key (id) references Offer(id) on delete cascade);

create table Accident(
id						integer,
datetime				datetime,
severity				decimal,
details					varchar(1000),
driver_id				char(20) not null,
car_id					char(20) not null,
primary key (id),
foreign key (driver_id) references Borrower(id) on delete cascade,
foreign key (car_id) references Car(id) on delete cascade);


LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/Borrower.csv" INTO TABLE Borrower
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/lender.csv" INTO TABLE Lender
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/CarCatalog.csv" INTO TABLE CarCatalog
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/Car.csv" INTO TABLE Car
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/BorrowPost.csv" INTO TABLE BorrowPost
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/LendPost.csv" INTO TABLE LendPost
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/Offer.csv" INTO TABLE Offer
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/Deal.csv" INTO TABLE Deal
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
LOAD DATA LOCAL INFILE "/Users/yangjiao/Downloads/sharecar_csv/Accident.csv" INTO TABLE Accident
COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n';
/*
INSERT INTO Accident (id, `datetime`, severity, details, driver_id, car_id) 
VALUES (10000001, "2015-5-27-15:48:12", 5, "overturned", "Y8902019", "1SAM123");

SELECT * FROM Accident;
*/

/*

SELECT bp.borrower_id, COUNT(*) AS NumDeals, d.id
FROM Deal d RIGHT OUTER JOIN Offer o ON d.id = o.id, BorrowPost bp 
WHERE o.borrow_post_id = bp.id AND bp.borrower_id = "B2451110"
GROUP BY bp.borrower_id;

DELETE FROM Borrower 
WHERE Borrower.id = "B2451110";

SELECT bp.borrower_id, COUNT(*) AS NumDeals, d.id
FROM Deal d RIGHT OUTER JOIN Offer o ON d.id = o.id, BorrowPost bp 
WHERE o.borrow_post_id = bp.id AND bp.borrower_id = "B2451110"
GROUP BY bp.borrower_id;

*/

/*
SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id, lp.car_id;
*/
/*
UPDATE Borrower, 
(SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id, lp.car_id) AS temp
SET Borrower.count =  temp.computed_borrow_count
WHERE Borrower.id = temp.id;
*/

/*
SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id;


SET SQL_SAFE_UPDATES = 0;

UPDATE Borrower, 
(SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id) AS temp
SET Borrower.count =  temp.computed_borrow_count
WHERE Borrower.id = temp.id;




SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id;

*/
/*
DELETE FROM Car
WHERE Car.owner_id = "B0506674";
*/


/*
DELIMITER $$

CREATE Trigger demoteLenders 
AFTER DELETE ON Car 
FOR EACH ROW
BEGIN
	IF OLD.owner_id NOT IN (
		SELECT c.owner_id 
		FROM Lender l RIGHT OUTER JOIN Car c ON l.id = c.owner_id
		WHERE l.id = OLD.owner_id
		GROUP BY c.owner_id
		HAVING COUNT(*) >= 1) 
	THEN
		DELETE FROM Lender WHERE Lender.id = OLD.owner_id;
	END IF;


END; $$

DELIMITER ;


SELECT * FROM Car;

DELETE FROM Car
WHERE Car.owner_id = "B5941026";

SELECT * FROM Lender;

SELECT l.id, count(c.id) numCarsOwned
FROM Car c RIGHT OUTER JOIN Lender l ON c.owner_id = l.id
GROUP BY l.id;
*/

/*
SELECT * FROM Car;

SELECT l.id, COUNT(c.id)
FROM Lender l LEFT OUTER JOIN Car c ON l.id = c.owner_id
GROUP BY l.id;



SET SQL_SAFE_UPDATES = 0;

UPDATE Borrower, 
(SELECT b.id, b.count as borrow_count, count(*) as computed_borrow_count 
FROM Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND bp.borrower_id = b.id AND o.lend_post_id = lp.id 
GROUP BY b.id) AS temp
SET Borrower.count =  temp.computed_borrow_count
WHERE Borrower.id = temp.id;

SELECT b.id, b.count as borrow_count, count(d.id) as computed_borrow_count
FROM Borrower b, BorrowPost bp, Deal d, Offer o, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND o.lend_post_id = lp.id AND bp.borrower_id = b.id
GROUP BY b.id;

*/

/*

SELECT b.id, b.count as borrow_count, count(d.id) as computed_borrow_count
FROM Borrower b, BorrowPost bp, Deal d, Offer o, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND o.lend_post_id = lp.id AND bp.borrower_id = b.id
GROUP BY b.id;
*/

/*
select b.id, lp.car_id, b.name, b.email, b.count as borrow_count, count(*) as borrow_count_per_car
from Deal d, Offer o, BorrowPost bp, Borrower b, LendPost lp
where d.id = o.id
and o.borrow_post_id = bp.id
and bp.borrower_id = b.id
and o.lend_post_id = lp.id
group by b.id, lp.car_id;


SELECT b.id, b.count as borrow_count, count(temp.id) as computed_borrow_count
FROM Borrower b LEFT OUTER JOIN (
SELECT bp.borrower_id, d.id 
FROM Deal d, Offer o LEFT OUTER JOIN BorrowPost bp ON o.borrow_post_id = bp.id
WHERE d.id = o.id) AS temp
ON b.id = temp.borrower_id
GROUP BY b.id;

SELECT Borrower.id FROM Borrower;

*/


















