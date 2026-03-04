(Currently in progress, working on the project step-by-step and aligning it with module lectures) 

# Ocean View Resort 🏝️🍹⛱️🌞🌊
A hotel management system that assists staff with handling resort room reservations. From secure user authentication to making reservations with a unique ID and a helpful guide section, the system is user-friendly and meets the requirements. Built with Java EE and MySQL, following a layered architecture, this system is designed to automate and manage room reservations and guest records. 

## Demo-Video: 
https://vimeo.com/1170358813?share=copy&fl=sv&fe=ci

## About
Ocean View Resort is a popular beachside hotel in Galle, serving hundreds of guests each month and enhancing their stay. To improve reservation and guest management, the resort has transitioned to a modern, automated system. This project delivers a user-friendly, menu-driven application with features including reservation management, guest registration and handling, and detailed reporting to facilitate decision-making.

* **Objectives:** Streamline hotel service operations, ease staff's day-to-day workload, and provide administrative tools for assistance.
* **End-users:** Hotel staff with role-based access
* **Technology**: Java EE, MySQL, Maven, GitHub, Layered architecture

## Key features
### Authentication & Security
* Secure login authentication with uswrname and password
* Passwords are salted and hashed
* Emails sent to users upon password change to enforce security

### User management
* Register new staff accounts with email notifications
* Update user details like contact number and address
* Safe session handling and destroying with logout feature
* Currency converter to improve staff's daily tasks

### Room management
* CRUD functionalities for rooms, with each room being of a selected room type
* Advanced room Search & Filtering (dates, status, floor number, max occupancy, amenities)

### Guest management
* Registering new guests with a unique Ocean View Membership ID
* Enhancing customer experience by providing an option between NIC or Passport Number for identification purposes
* CRUD functionalities (Guests are removed permanently when deleted to comply with GDPR standards)
* Search & Filtering by registration-number, Checked-In / Checked-Out status, and Identification documents
* Managing guest preferences according to guest's stay history

### Reservation management
* Making reservations with a unique Reservation Number
* Total cost calculated upon runtime; guests may select preferred amenities
* Ease of Check-in / Check-out process and payment reminders
* Email sent to guest upon successful reservation
* Search & Filtering by reservation number, status and dates

### Bill & Payment Handling
* Total Cost for a reservation is computed and taxes are applied
* PDF of the bill is generated upon successful payment
* In the case of multiple payments, a bill's status will not be updated until total amount is paid

### Application Guidelines
* Access system guidelines for new staff in the Help section
* Contact Form provided for any queries 

### Reports
* Accurate data reports about total revenue, total number of stays (sorted by status), room occupancy rate, new and returning guests rate, paid and oustanding revenue to facilitate decision-making
* Export weekly/monthly reports as PDF files

## Architecture & Technology Stack


## Getting started
### Prerequisites
* Java 25:  [Download JDK 25](https://www.oracle.com/apac/java/technologies/downloads/)
* MySQL Workbench 8.0: [Download MySQL Workbench 8.0](https://dev.mysql.com/downloads/workbench/)
* Apache Tomcat 9: [Download Apache Tomcat 9](https://tomcat.apache.org/download-90.cgi)
* Maven (IntelliJ IDEA uses a bundled version): [Download Maven](https://maven.apache.org/download.cgi)
* Git: [Download Git](https://git-scm.com/install/windows)

### How to install
#### Option 1: Download the ZIP file

1. Click **Code** at the top of the repository
2. Click **Download ZIP**
3. Once the repository is downloaded, extract the ZIP file to access the repository's files

 #### Option 2: Clone the repository
1. Click **Code** at the top of the repository
2. Copy the URL from the HTTPS tab
3. Open your terminal and change the working directory to your preferred location
4. Run the clone command below:
```bash
git clone https://github.com/salma-shah/cis6003-hotel-management-system.git
```

#### After download
1. Create a database named ```ocean_view_hotel_db```
2. Import the schema files from ```src/main/sql```
4. Update DBConnection with your database credentials
```bash
private static final String USER = "your_username"
private static final String PASSWORD = "your_password"
```
4. Run the application:
Access the application by pasting this into your URL:
 ```bash
http://localhost:8080/ocean_view_hotel_war_exploded/
```
5. Register User with your credentials
 ```bash
http://localhost:8080/ocean_view_hotel_war_exploded/user/register
```

## Optimizations

## Usage

## CI/CD Pipeline

## Security Mechanisms
* Passwords were hashed securely using Bcrypt, preventing unauthorized access even during a security breach. Salting before hashing ensures passwords are secure, protecting it from rainbow table attacks. Ensures data integrity.
* Hard delete to comply with GDPR standards
* Security Audit Trail: User actions are tracked (createdAt, updatedAt, dateOfReservation, paymentDate) with LocalDateTime fields for monitoring and audit purposes.
* Sessions: Effective use of sessions for storing / destroying user attributes through the modules

These security measures create a protected environment against unauthorized access. The hotel manages sensitive data like guest's personal information and financial transactions; therefore, confidentiality, integrity and availability is required.

## Version
* Version: Ocean View Hotel 1.10.2
* Last updated: 5th March 2025

## Hosted on: 
![Tomcat Logo](https://tomcat.apache.org/res/images/tomcat.png)

## Contribution
If you would like to contribute to this repository:
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Make your changes then commit them (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature-name`)
5. Open a pull request

## Contact
* Email: salma.shah.0516@gmail.com
* LinkedIn: www.linkedin.com/in/salma-shah-0b499724a

## Acknowledgement
* I appreciate Cardiff Metropolitan University for providing us with the sources to access the Ultimate IntelliJ IDEA 2025.3.2.
* I extend my gratitude towards the CIS6003 Advanced Programming Lecturer, Mr. Bhagya Rathnayaka, for his support and guidance throughout the module.

