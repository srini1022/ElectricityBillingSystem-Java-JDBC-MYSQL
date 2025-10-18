create database nebs;
use nebs;
 create table Signup(
    meter_no varchar(20),
    username varchar(20),
    name varchar(30),
    password varchar(30),
    usertype varchar(20)
);
INSERT INTO Signup (meter_no, username, name, password, usertype)
VALUES ('MTR001', 'john_doe', 'John Doe', 'password123', 'Admin');

INSERT INTO Signup (meter_no, username, name, password, usertype)
VALUES ('MTR001', 'john_doe', 'John Doe', 'password123', 'Customer');

select * from Signup;

CREATE TABLE new_customer(
    name VARCHAR(30),
    meter_no VARCHAR(20),
    address VARCHAR(50),
    city VARCHAR(30),
    state VARCHAR(30),
    email VARCHAR(30),
    phone_no VARCHAR(12)
);


select * from new_customer;


CREATE TABLE meter_info(
    meter_number VARCHAR(30),
    meter_location VARCHAR(30),
    meter_type VARCHAR(30),
    phase_code VARCHAR(30),
    bill_type VARCHAR(30),
    days VARCHAR(10)
);

select * from meter_info;

CREATE TABLE tax(
    cost_per_unit VARCHAR(20),
    meter_rent VARCHAR(20),
    service_charge VARCHAR(20),
    service_tax VARCHAR(20),
    swacch_bharat VARCHAR(20),
    fixed_tax VARCHAR(20)
);
INSERT INTO tax VALUES ('10', '45', '20', '58', '5', '18');
select * from tax;


CREATE TABLE bill(
    meter_no VARCHAR(20),
    month VARCHAR(20),
    unit VARCHAR(20),
    total_bill VARCHAR(20),
    status VARCHAR(20)
);

select * from bill;

