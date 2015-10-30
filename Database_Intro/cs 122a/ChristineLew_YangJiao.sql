/* ChristineLew_YangJiao */

/* ---------------- query1 --------------- */
INSERT INTO Accident (id, datetime, severity, details, driver_id, car_id)
VALUES (10000001, "2015-5-27-15:48:12",5, "overturned", "Y8902019", "1SAN123");

INSERT INTO Accident (id, datetime, severity, details, driver_id, car_id)
VALUES (10000001, "2015-5-27-15:48:12",5, "overturned", "Y8902019", "1SAM123");


/* ---------------- query2 --------------- */
SELECT bp.borrower_id, COUNT(*) AS NumDeals
FROM Deal d RIGHT OUTER JOIN Offer o ON d.id = o.id, BorrowPost bp 
WHERE o.borrow_post_id = bp.id AND bp.borrower_id = "B2451110"
GROUP BY bp.borrower_id;


DELETE FROM Borrower 
WHERE Borrower.id = "B2451110";

SELECT bp.borrower_id, COUNT(*) AS NumDeals
FROM Deal d RIGHT OUTER JOIN Offer o ON d.id = o.id, BorrowPost bp 
WHERE o.borrow_post_id = bp.id AND bp.borrower_id = "B2451110"
GROUP BY bp.borrower_id;


/* ---------------- query3 --------------- */
SELECT b.id, b.count as borrow_count, count(temp.id) as computed_borrow_count
FROM Borrower b LEFT OUTER JOIN (
SELECT bp.borrower_id, d.id 
FROM Deal d, Offer o LEFT OUTER JOIN BorrowPost bp ON o.borrow_post_id = bp.id
WHERE d.id = o.id) AS temp
ON b.id = temp.borrower_id
GROUP BY b.id;

SET SQL_SAFE_UPDATES = 0;

UPDATE Borrower, 
(SELECT b.id, b.count as borrow_count, count(temp.id) as computed_borrow_count
FROM Borrower b LEFT OUTER JOIN (
SELECT bp.borrower_id, d.id 
FROM Deal d, Offer o LEFT OUTER JOIN BorrowPost bp ON o.borrow_post_id = bp.id
WHERE d.id = o.id) AS temp
ON b.id = temp.borrower_id
GROUP BY b.id) AS temp
SET Borrower.count =  temp.computed_borrow_count
WHERE Borrower.id = temp.id;


SELECT b.id, b.count as borrow_count, count(temp.id) as computed_borrow_count
FROM Borrower b LEFT OUTER JOIN (
SELECT bp.borrower_id, d.id 
FROM Deal d, Offer o LEFT OUTER JOIN BorrowPost bp ON o.borrow_post_id = bp.id
WHERE d.id = o.id) AS temp
ON b.id = temp.borrower_id
GROUP BY b.id;


/* ---------------- query4 --------------- */
DELETE FROM Car
WHERE Car.owner_id = "B0506674";

SELECT l.id, count(c.id) numCarsOwned
FROM Car c RIGHT OUTER JOIN Lender l ON c.owner_id = l.id
GROUP BY l.id;

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

DELETE FROM Car
WHERE Car.owner_id = "B5941026";

SELECT l.id, count(c.id) numCarsOwned
FROM Car c RIGHT OUTER JOIN Lender l ON c.owner_id = l.id
GROUP BY l.id;






















