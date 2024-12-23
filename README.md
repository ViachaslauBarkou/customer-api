# Customer API

## Description
Customer API is a simple CRUD (Create, Read, Update, Delete) application designed for managing customer data.

## Functionality
- **Create Customer**: Add a new customer record with details such as name, email, and phone number.
- **Retrieve Customers**:
    - Fetch all customer records.
    - Fetch a single customer by ID.
- **Update Customer**: Modify existing customer details.
- **Delete Customer**: Remove a customer record by ID.

## Requirements
- **Java 11 or higher**
- **Maven 3.6.0 or higher**

## Setup, Build, Test and Run
### Setup Instructions
#### 1. Install MySQL
[MySQL Installer (Windows)](https://dev.mysql.com/downloads/installer/)  
For Linux/macOS, install MySQL using a package manager:
```bash
sudo apt update && sudo apt install mysql-server  # For Ubuntu/Debian
brew install mysql  # For macOS
```
#### 2. Start the MySQL service:
```bash
sudo service mysql start  # For Ubuntu/Debian
brew services start mysql  # For macOS
```
#### 3. Create the database and user (if not already created):
```sql
CREATE DATABASE `customer-api`;
CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_username';
GRANT ALL PRIVILEGES ON `customer-api`.* TO 'your_username'@'localhost';
FLUSH PRIVILEGES;
```
#### 4. Set Environment Variables
Linux/macOS:
```bash
export MYSQL_USERNAME=your_username
export MYSQL_PASSWORD=your_password
```
Windows (PowerShell):
```powershell
[System.Environment]::SetEnvironmentVariable("MYSQL_USERNAME", "your_username", "User")
[System.Environment]::SetEnvironmentVariable("MYSQL_PASSWORD", "your_password", "User")
```
#### 5. Run the Database Schema Script
```
src/main/resources/schema.sql;
```
#### 6. Run the Data Population Script (optional)
```
src/main/resources/data.sql;
```
### Build the Project
```bash
mvn clean compile
```
### Run Tests
```bash
mvn test
```
### Start the Application
#### Run 
```bash
mvn spring-boot:run
```
#### API Endpoints:
* **Retrieve All Customers**  
*Method*: `GET`  
*URL*: `http://localhost:8080/api/customers/getAll`
* **Retrieve a Customer by ID**  
*Method*: `GET`  
*URL*: `http://localhost:8080/api/customers/getById/{id}`  
Replace `{id}` with a valid customer ID.
* **Create a New Customer**  
*Method*: `POST`  
*URL*: `http://localhost:8080/api/customers/create`  
*Request Body*: `{ "firstName": "Ivan", "middleName": "Ivanovich", "lastName": "Ivanov", "emailAddress": "ivan.ivanov@example.com", "phoneNumber": "123-456-7890" }`
* **Update an Existing Customer**  
*Method*: `POST`  
*URL*: `http://localhost:8080/api/customers/update`  
*Request Body*: `{ "id": "5dd6b153-c0f1-11ef-a4d8-0242ac110002", "firstName": "Ivan", "middleName": "Ivanavich", "lastName": "Ivanou", "emailAddress": "ivan.ivanou@example.com", "phoneNumber": "111-222-3333" }`
* **Delete a Customer**  
*Method*: `DELETE`  
*URL*: `http://localhost:8080/api/customers/delete/{id}`  
Replace `{id}` with the customer ID to delete.

## Clone the Repository
```bash
git clone https://github.com/viachaslaubarkou/customer-api.git
cd customer-api
```

## Author
Viachaslau Barkou

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
