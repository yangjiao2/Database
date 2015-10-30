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

SELECT b.id, b.name, a.id, DATE(a.datetime) AS date, a.severity, cc.make, cc.model
FROM Borrower b RIGHT OUTER JOIN Accident a ON b.id = a.driver_id, Car c, CarCatalog cc 
WHERE a.details LIKE '%alcohol%' AND cc.id = c.car_catalog_id AND a.car_id = c.id;







SELECT b.id, count(*) as accidentCount, b.name, TIMESTAMPDIFF(year, b.birthdate, NOW()) as age
FROM Borrower b RIGHT OUTER JOIN Accident a ON b.id = a.driver_id
GROUP BY b.id
HAVING count(*) > 1
ORDER BY accidentCount DESC;







CREATE VIEW CarColor AS
SELECT  cc.color, c.id
FROM CarCatalog cc RIGHT OUTER JOIN Car c ON cc.id = c.car_catalog_id;

CREATE VIEW ColorNumber AS 
SELECT cc.color, COUNT(*) AS numberOfCar
FROM CarColor cc
GROUP BY cc.color;

CREATE VIEW mergeCarAccident AS
SELECT  cc.color, c.id, a.severity
FROM CarCatalog cc RIGHT OUTER JOIN Car c ON cc.id = c.car_catalog_id, Accident a
WHERE a.car_id = c.id;


SELECT mca.color, cn.numberOfCar, COUNT(*) AS numberofAccident, MIN(mca.severity), MAX(mca.severity), AVG(mca.severity)
FROM mergeCarAccident mca RIGHT OUTER JOIN ColorNumber cn on mca.color = cn.color
GROUP BY mca.color;






CREATE VIEW mergeDeal AS
SELECT bp.borrower_id, lp.car_id, d.id
FROM Deal d, Offer o, BorrowPost bp, LendPost lp
WHERE d.id = o.id AND o.borrow_post_id = bp.id AND o.lend_post_id = lp.id;

CREATE VIEW borrowCount AS
SELECT md.borrower_id, count(*) as borrowCountPerCar
FROM mergeDeal md
GROUP BY md.borrower_id;

CREATE VIEW maxBorrowCount AS
SELECT bc.borrower_id,  bc.borrowCountPerCar as computedBorrowCount
FROM borrowCount bc
WHERE bc.borrowCountPerCar >= (SELECT MAX(borrowCount.borrowCountPerCar) FROM borrowCount);

SELECT b.id, b.name, b.email, b.count, mbc.computedBorrowCount
FROM Borrower b, maxBorrowCount mbc
WHERE b.id = mbc.borrower_id;








SELECT cc.model, COUNT(*) AS NumberOfDeals
FROM CarCatalog cc RIGHT OUTER JOIN Car c ON cc.id = c.car_catalog_id, Deal d LEFT OUTER JOIN Offer o ON d.id = o.id, Lendpost lp
WHERE o.lend_post_id = lp.id AND lp.car_id = c.id AND 
IF (YEAR(o.borrower_accept_datetime)> YEAR(o.lender_accept_datetime), YEAR(o.borrower_accept_datetime),
YEAR(o.lender_accept_datetime)) = 2014
GROUP BY cc.model
ORDER BY COUNT(*) DESC
LIMIT 5;







CREATE VIEW YearCarMake AS
SELECT IF (YEAR(o.borrower_accept_datetime)> YEAR(o.lender_accept_datetime), YEAR(o.borrower_accept_datetime), YEAR(o.lender_accept_datetime)) AS OfferYear, cc.make, COUNT(*) AS NumberOfDeals
FROM Deal d LEFT OUTER JOIN Offer o ON d.id = o.id, CarCatalog cc RIGHT OUTER JOIN Car c ON cc.id = c.car_catalog_id, Lendpost lp
WHERE o.lend_post_id = lp.id AND lp.car_id = c.id 
GROUP BY YEAR(o.offer_datetime), cc.make
ORDER BY YEAR(o.offer_datetime);

SELECT ycm.OfferYear, ycm.make, MAX(ycm.NumberOfDeals) AS NumberOfDeals
FROM YearCarMake ycm
GROUP BY ycm.OfferYear
ORDER BY NumberOfDeals DESC;



*/
create view DealTime as
select d.id, o.lend_post_id, 
       if(
			timestampdiff(second, o.borrower_accept_datetime, o.lender_accept_datetime) > 0, 
            o.lender_accept_datetime,
			o.borrower_accept_datetime
	   ) as deal_datetime
from Deal d, Offer o
where d.id = o.id;

select t.model, count(*) as deal_count
from DealTime d, LendPost l, Car c, CarCatalog t
where year(d.deal_datetime) = 2014 and 
      d.lend_post_id = l.id and
      l.car_id = c.id and
	  c.car_catalog_id = t.id
group by t.model
order by count(*) desc
limit 5;


create view DealYearMonth as
select d.id, d.lend_post_id, year(d.deal_datetime) as deal_year, month(d.deal_datetime) as deal_month
from DealTime d, LendPost l
where d.lend_post_id = l.id;

create view YearMakeDealCount as
select d.deal_year, t.make, count(*) as deal_count
from DealYearMonth d, LendPost l, Car c, CarCatalog t
where d.lend_post_id = l.id and
      l.car_id = c.id and
	  c.car_catalog_id = t.id
group by d.deal_year, t.make;

create view YearMaxDealCount as
select d.deal_year, max(d.deal_count) as max_deal_count
from YearMakeDealCount d
group by d.deal_year;

select y.deal_year, y.make, y.deal_count
from YearMakeDealCount y, YearMaxDealCount m
where y.deal_year = m.deal_year and
      y.deal_count = m.max_deal_count
order by y.deal_count desc;