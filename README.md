# Wash on Wheels – Mobile Car Wash Service Management System

A Java Servlet + JDBC + MySQL web application for managing a mobile car
wash business, with role-based access for Admin and Staff.

## Tech Stack
Java, Servlets, JDBC, MySQL

## Setup
1. Create the MySQL database `luminar_servlet` and required tables.
2. Each servlet file has a `Password` field near the top set to
   `YOUR_MYSQL_PASSWORD_HERE` — replace it with your own local MySQL
   password before running.
3. Deploy to a servlet container (e.g. Apache Tomcat).

## Features
- Admin dashboard: manage staff, customers, services, packages, payments, expenses
- Staff dashboard: bookings, attendance, salary, customer records
- Role-based login and session management
