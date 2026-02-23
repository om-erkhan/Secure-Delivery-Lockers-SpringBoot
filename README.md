🔐 Secure Delivery Lockers – Backend

Backend service for the Secure Delivery Lockers System, built to support smart parcel delivery and retrieval using secure authentication and locker management.

This backend powers the mobile application (built with Flutter) and handles authentication, locker allocation, delivery flow, and user management.

📌 Project Overview

Secure Delivery Lockers is a smart system designed to:

📦 Allow delivery agents to securely deposit parcels

🔐 Allow users to retrieve parcels using secure verification

🗄 Manage locker allocation dynamically

📲 Communicate with the Flutter mobile app via REST APIs

🛡 Ensure secure authentication & authorization

🛠 Tech Stack

Java

Spring Boot

Spring Security

JPA / Hibernate

MySQL

REST APIs

JWT Authentication

🏗 System Architecture

Client (Flutter App)
⬇
REST API (Spring Boot Backend)
⬇
Service Layer
⬇
Repository Layer (JPA)
⬇
MySQL Database

🚀 Features
👤 Authentication & Authorization

User Registration

Login with JWT token

Role-based access (Admin, User, Delivery Agent)

📦 Locker Management

Create lockers

Check locker availability

Assign locker to delivery

Update locker status (Available / Occupied)

🚚 Parcel Handling

Generate secure pickup code

Store parcel details

Validate pickup code on retrieval

Mark parcel as delivered

🛠 Admin Controls

View all lockers

Monitor parcel logs

Manage users

⚙️ Installation & Setup

1️⃣ Clone the Repository
git clone link of this repo.
cd secure-delivery-lockers-backend

2️⃣ Configure Database
Update application.properties:
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Run the Application

Using Maven:
mvn spring-boot:run

Or run the main class from your IDE.

Server will start at:

http://localhost:8080
🔑 API Endpoints (You can check in the Docs Folder)

🔒 Security
Passwords encrypted using BCrypt
JWT-based authentication
Role-based authorization
Protected endpoints

🧪 Future Improvements
QR Code-based pickup
OTP verification
IoT locker hardware integration
Cloud deployment (AWS / Azure)
Docker containerization

📸 Related Project
This backend is integrated with the Flutter Mobile Application for Secure Delivery Lockers.

👨‍💻 Author
Final Year Project
Secure Delivery Lockers System
Built with ❤️ using Spring Boot & Flutter
