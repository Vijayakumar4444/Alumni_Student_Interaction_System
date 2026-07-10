# Alumni–Student Interaction System

A full-stack web application designed to connect students and alumni through mentorship, career opportunities, events, forums, notifications, and real-time private communication.

## Features

- Role-based Student and Alumni authentication
- Student and Alumni dashboards
- Profile management with profile picture upload
- Resume PDF upload for students
- GitHub and LinkedIn profile integration
- Alumni–Student mentorship request system
- Job and internship posting and applications
- Event creation and student registration
- Forum Q&A with likes and helpful answers
- Notification system with read/unread status
- Real-time private chat using WebSocket
- Responsive and professional user interface

## Tech Stack

### Frontend
- React.js
- React Router DOM
- Axios
- CSS3
- Lucide React
- Vite

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate ORM
- REST APIs
- WebSocket

### Database
- PostgreSQL

### Development Tools
- VS Code
- IntelliJ IDEA
- Maven
- Git
- GitHub
- Postman

## Architecture

The backend follows a modular monolithic architecture with separate modules for:

- Authentication
- Students
- Alumni
- Mentorship
- Jobs / Internships
- Events
- Forum
- Notifications
- Private Chat
- Dashboard Analytics

## Project Structure

### Backend

```text
src/main/java/com/vijay/alumniportal/
├── auth/
├── student/
├── alumni/
├── mentorship/
├── job/
├── event/
├── forum/
├── notification/
├── chat/
├── dashboard/
└── config/
Frontend
src/
├── assets/
├── components/
├── pages/
├── services/
├── styles/
├── App.jsx
└── main.jsx
 Getting Started
Prerequisites

Make sure the following are installed:

Java 17+
Node.js
PostgreSQL
Maven
Git
1. Clone the Repository
git clone <your-repository-url>
cd <your-project-folder>
2. Configure PostgreSQL

Create a PostgreSQL database:

CREATE DATABASE alumniportal;

Configure backend environment variables or application properties with your database credentials.

Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/alumniportal
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
3. Run the Backend
./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run

Backend runs on:

http://localhost:8080
4. Run the Frontend
npm install
npm run dev

Frontend runs on:

http://localhost:5173
 Security
Role-based authentication for Students and Alumni
Spring Security integration
Protected backend APIs
Secure authorization workflows
Environment variables for sensitive credentials
 Real-Time Communication

WebSocket is used to support real-time private messaging between connected students and alumni.

Purpose

The project aims to strengthen student–alumni engagement by providing a centralized platform for mentorship, career guidance, professional networking, events, discussions, and direct communication.

 Contributors

Developed as a Full-Stack Development Project.

 License

This project is intended for educational and academic purposes.
