🚀 Vehicle Reservation API - Setup & Installation

1️⃣ Prerequisites
Before setting up the project, ensure you have the following installed:

Java 17+
Apache Maven (mvn -v to check)
MySQL Database
Tomcat Server (or any Java EE Server)

2️⃣ Clone the Repository

git clone https://github.com/your-repo/vehicle-reservation-api.git
cd vehicle-reservation-api


3️⃣ Configure Database
Create a new MySQL database:

CREATE DATABASE vehicle_reservation;
Update database credentials in DBConnection.java:

private static final String URL = "jdbc:mysql://localhost:3306/vehicle_reservation";
private static final String USER = "root";
private static final String PASSWORD = "yourpassword";


4️⃣ Build & Run the Project
Install dependencies and build the project:
mvn clean install

Run the application on Tomcat:
mvn tomcat7:run

API will be available at:
http://localhost:8080/vehicle-reservation-api/


5️⃣ Running Tests
Run unit tests using Maven:
mvn test
