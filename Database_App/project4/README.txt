For the xml parsing part we have optimized the program by
1) USing the SAX parser over the DOM parser
	The SAX parser stateless design is more efficient than the DOM parser for 
	larger datasets since the DOM parser loads the entire file into memory whereas the 
	SAX parser loads parts as they are needed.

2) Prepared statements
	We use prepared statements in order to reduce the overhead of of MySQL queries 
	to the database

3) We also minimize logical loops and control flow in the program in order to reduce the
	run time as a small optimization

The maximum run time for the large dataset is approximately 90 seconds with an 
average run time of 50 seconds

The maximum run time for the small dataset is approximately 5 seconds with an 
average run time of 3 seconds.

Instructions

In order to run these files, set the paths for 
File data and File dtd in MainXML.java
Additionally set the values of the two strings
in XmlParser constructor to the file path of the 
xml files.

Run MainXML.java as a java application