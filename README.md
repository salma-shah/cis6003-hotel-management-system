(Currently in progress, working on the project step-by-step and aligning it with module lectures) 

# Ocean View Resort (v 1.10.2) 🏝️🍹⛱️🌞🌊 
A hotel management system that assists staff with handling resort room reservations. From secure user authentication to making reservations with a unique ID and a helpful guide section, the system is user-friendly and meets the requirements. Built with Java EE and MySQL, following a layered architecture, this system is designed to automate and manage room reservations and guest records. 

## Demo-Video: 
https://vimeo.com/1170358813?share=copy&fl=sv&fe=ci

## About
Ocean View Resort is a popular beachside hotel in Galle, serving hundreds of guests each month and enhancing their stay. To improve reservation and guest management, the resort has transitioned to a modern, automated system. This project delivers a user-friendly, menu-driven application with features including reservation management, guest registration and handling, and detailed reporting to facilitate decision-making. Every step carefully followed the Unified Process's phases, saving cost, time, and prioritizing a flexible, incremental, and iterative approach. 

* **Objectives:** Streamline hotel service operations, ease staff's day-to-day workload, and provide administrative tools for assistance.
* **End-users:** Hotel staff with role-based access
* **Technology**: Java EE, MySQL, Maven, GitHub, Layered architecture

## Key features
### Authentication & Security
* Secure login authentication with username and password
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

## Screenshots 
<img width="400" height="400" alt="OVR_index_pg" src="https://github.com/user-attachments/assets/7281bc2f-f823-4395-85bf-91842310caa8" />
<img width="400" height="400" alt="OVR_dashboard_pg" src="https://github.com/user-attachments/assets/583d26d0-c6b8-4eb0-9268-ae8d55b0dc74" />
<img width="400" height="400" alt="OVR_guests_pg" src="https://github.com/user-attachments/assets/388aa448-bb61-4ef2-bc1c-7423e2aa5562" />
<img width="400" height="400" alt="OVR_rooms_all" src="https://github.com/user-attachments/assets/d8ba1381-1cbc-444b-8814-f28bbf4439a2" />
<img width="400" height="400" alt="OVR_res_all" src="https://github.com/user-attachments/assets/aa2bc833-64f3-45ef-a104-5d9a85a104b3" />
<img width="400" height="400" alt="OVR_payment" src="https://github.com/user-attachments/assets/62963671-71a3-4554-9798-a22115857a2a" />
<img width="400" height="400" alt="OVR_help_pg" src="https://github.com/user-attachments/assets/adcf3835-581c-4a69-9425-74780a91f36a" />
<img width="400" height="400" alt="OVR_report" src="https://github.com/user-attachments/assets/4f4920ed-be53-4664-9e2a-6ab8cbdf8c8b" />

## Architecture & Technology Stack
System follows a layered-architecture approach to separate concerns for both modularity and maintainability:
* **Presentation** layer: Interfaces built with JSP forms and Java EE Web-Servlets for initial validation and URL-mapping
* **Service** layer: Handles business logic and delegates responsibilities across modules
* **Persistence** layer: Handles communication with the database only
* **Data** layer: MySQL database schema to hold records of user, rooms, guests, reservations, bills and financial transactions

Following the layered-architecture approach supports the system, making it more scalable and easier to read and maintain.

* Programming Languages: Java, JavaScript, JSP, CSS
* Java EE application, distributed with web services
* Database: MySQL 8.0
* Build tool: Maven
* CI/CD pipeline: GitHub
* Currency Converter API: [ExchangeRate-API](https://www.exchangerate-api.com/) (Get your own free API key, then insert it in CurrencyConverterServlet)
```bash
 private static final String CURRENCY_CONVERTER_API_KEY = "your_api_key"
```

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
3. Update DBConnection with your database credentials
```bash
private static final String USER = "your_username"
private static final String PASSWORD = "your_password"
```
4. Update STMP email credentials with your own in EmailUtility
```bash
final String sender = "your_email"
final String password = "your_app_password"
```

## Usage
1. Run the application:
Access the application by pasting this into your URL:
 ```bash
http://localhost:8080/ocean_view_hotel_war_exploded/
```
2. Register a user (Manager) with your credentials
 ```bash
http://localhost:8080/ocean_view_hotel_war_exploded/user/register
```

## CI/CD Pipeline
Ocean View Resort features a CI/CD pipeline by GitHub actions, where it followed Test-Driven-Development assisted with jUnit. This ensures smooth, seamless integration between components across the system and enhances quality. A mixture of manual and automated testing was carried out; throughout functionality development, feature branches were created and then merged into the development branch after a strict code comparison review. This helped build, test, and deliver code safely. 

Branch strategy:
* ```development``` : Development environment for new features
* ```q/a```: Isolated, stable environment to test features and verify code stability safely for QA
* ```regression```: Address and prevent software regression, to make isolated changes by refactoring skeletal code to ensure bugs do not 'sneak in' past initial tests again 
* ```master```: Production environment for deployment and live-monitoring

Version-control techniques:
* Merging: Once a feature was fully-complete and working, its branch was merged into ```development``` branch before deletion
* Commits & Pushes: Following Unified Process, small incremental commits were pushed to the ```development``` branch, serving a single purpose and making it easier to track
* Cherry-pick: When pushing commits from ```development``` to ```q/a```, cherry-pick was used to selectively integrate changes
* Deployment: After thorough testing, code is pushed from ```q/a``` branch to ```master``` branch

Commit-History: Commits to ```development``` branch history can be viewed [here](https://github.com/salma-shah/cis6003-hotel-management-system/commits/development/)

```master``` branch holds only the 'shippable' code; therefore, only merge requests were made to it, following real-world best practices. 

Following and integrating into the CI/CD pipeline ensures that only safely tested and reviewed code is delivered to the production environment, with minimal disruptions that could cause damage. This improves performance and ensures client requirements are fulfilled.   

## Security Mechanisms
* Passwords were hashed securely using Bcrypt, preventing unauthorized access even during a security breach. Salting before hashing ensures passwords are secure, protecting it from rainbow table attacks. Ensures data integrity.
* Hard delete to comply with GDPR standards
* Security Audit Trail: User actions are tracked (createdAt, updatedAt, dateOfReservation, paymentDate) with LocalDateTime fields for monitoring and audit purposes.
* Sessions: Effective use of sessions for storing/destroying user attributes through the modules

These security measures create a protected environment against unauthorized access. The hotel manages sensitive data like guest information and financial transactions; therefore, confidentiality, integrity, and availability are required.

## Version
* Version: Ocean View Hotel 1.10.2
* Last updated: 5th March 2025

## Hosted on: 
![Tomcat Logo](https://tomcat.apache.org/res/images/tomcat.png)

## Lessons Learned

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

Any contributions or suggestions are welcome! I'd appreciate a star on the project :)
Thank you!
