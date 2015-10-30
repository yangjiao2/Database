DROP SCHEMA `cs122b_project1` ;
CREATE SCHEMA `cs122b_project1` ;
USE `cs122b_project1`;

create table movies(
id integer auto_increment,
title varchar(100), 
year integer ,
director varchar(100) ,
banner_url varchar(200) ,
trailer_url varchar(200) ,
primary key (id)
);

create table stars(
id integer auto_increment,
first_name varchar(50) ,
last_name varchar(50) ,
dob date ,
photo_url varchar(200) ,
primary key (id)
);

create table stars_in_movies(
star_id integer ,
movie_id integer ,
foreign key (star_id) references stars(id),
foreign key (movie_id) references movies(id)
);

create table genres(
id integer auto_increment,
name varchar(32) ,
primary key (id)
);

create table genres_in_movies(
genre_id integer, 
movie_id integer, 
foreign key (genre_id) references genres(id),
foreign key (movie_id) references movies(id)
);

create table customers(
id integer auto_increment,
first_name varchar(50) ,
last_name varchar(50) ,
cc_id varchar(20), 
address varchar(200) ,
email varchar(50) ,
password varchar(20) ,
primary key (id),
foreign key (cc_id) references creditcards(id)
);

create table sales(
id integer auto_increment,
customer_id integer, 
movie_id integer, 
sale_date date ,
primary key (id),
foreign key (customer_id) references customers(id),
foreign key (movie_id) references movies(id)
);

create table creditcards(
id varchar(20), 
first_name varchar(50) ,
last_name varchar(50) ,
expiration date ,
primary key (id)
);



