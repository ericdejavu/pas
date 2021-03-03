create database pas;

create table scanner_record (
  id int primary key auto_increment,
  opt_date varchar (12) NOT NULL,
  opt_cmd varchar (12) NOT NULL,
  qrcode varchar (32) NOT NULL,
  param1 varchar (128),
  param2 varchar (128),
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) charset=utf8;

create table stash (
  id int primary key auto_increment,
  qrcode varchar(32) NOT NULL,
  pay decimal(5,2) NOT NULL,
  amount int default 0,
  consume int default 0,
  opt_date varchar (12) NOT NULL,
  is_sold_out tinyint default 0,
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) charset=utf8;

create table goods (
  id int primary key auto_increment,
  qrcode varchar (32) UNIQUE NOT NULL,
  name varchar (128) NOT NULL,
  price decimal (5,2),
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) charset=utf8;