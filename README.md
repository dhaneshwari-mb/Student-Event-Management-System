# Student-Event-Management-System
The Student Event Management System is a Java + SQLite console app for managing college events. It supports Admin (add events, register students, manage attendance, view/remove participants) and User (self-registration). Includes Email &amp; SMS notification simulation for confirmations.
# Student Event Management System

This is a simple console-based Java project for managing student events with role-based access for **Admin** and **User**.  
The system uses **SQLite database** for storing event, student, and registration details.

---

## Features

### Admin
- Secure login with password authentication
- Add events
- Register students
- View participants with full details (name, roll number, email, phone, event)
- Mark attendance
- Remove participants
- Exit

### User
- Register for an event
- Exit
Features
Admin login with password protection
Add and manage events
Register students with details (name, roll number, email, phone number)
Register students for events (by admin or student)
View participant details including name, roll number, email, and phone number
Mark attendance for events
Remove participants from events
Exit option for both admin and student roles
Console-based simulation of email and SMS notifications

## Technology Stack
- **Java** – Core programming language  
- **SQLite** – Lightweight database for storing event and student records  
- **SQLite JDBC Driver** – To connect Java with SQLite  
- **VS Code / IntelliJ / Eclipse** – IDE for development  
- **Console-based Notifications** – Simulated email/SMS confirmations for user registrations  

---

## Database Design

**Tables Used:**
1. `students` – Stores student details (name, roll number, email, phone)  
2. `events` – Stores event details (event name, venue, date)  
3. `registrations` – Links students with events and tracks attendance  

---

## How It Works
1. When the program starts, the user selects their role (Admin or User).  
2. If **Admin** is chosen, a password (`dhane1203`) is required to access features.  
3. Users can only register for events and exit.  
4. Admins can manage events, view participants, mark attendance, and remove participants.  
5. All details are stored in the SQLite database (`event.db`).  
6. After registration, the system simulates **email and SMS notifications** in the console.  

---
student-event-management/
│
├── src/
│ ├── Main.java
│ └── NotificationService.java
│
├── lib/
│ └── sqlite-jdbc-x.x.x.jar
│
├── database/
│ └── event.db



**DHANESHWARI MB**
**Email: hey.dhaneshwari@gmail.com**  

---

## Future Enhancements
- Add a **QR Code ticket system** for each registration  
- Send real **email/SMS notifications** using APIs  
- Build a **web-based frontend** using React for user-friendly interaction 

