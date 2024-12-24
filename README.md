# Customer API

## Description
Customer API is a simple CRUD (Create, Read, Update, Delete) application designed for managing customer data.

## Functionality
- **Create Customer**: Add a new customer record with details such as name, email, and phone number.
- **Get Customers**:
    - Fetch all customer records.
    - Fetch a single customer by ID.
    - Fetch customers by matching any part of any name.
    - Fetch customers by matching any part of phone number.
- **Update Customer**: Modify existing customer details.
- **Delete Customer**: Remove a customer record by ID.

## Requirements
- **Java 11 or higher**
- **Maven 3.6.0 or higher**

## Setup, Build, Test, Run, Monitoring
### Install on your machine
[Docker](https://docs.docker.com/get-docker/)  
[Docker Compose](https://docs.docker.com/compose/install/)
### Clone the Repository
```bash
git clone https://github.com/viachaslaubarkou/customer-api.git
cd customer-api
```
### Setup Instructions
#### 1. Create a .env file in the root directory of the project to define environment variables for MySQL:
```env
MYSQL_ROOT_PASSWORD='your_root_password'
MYSQL_DATABASE=customer_api
MYSQL_USER='your_username'
MYSQL_PASSWORD='your_password'
```
#### 2. Initialize the Database (Optional)
If you have SQL scripts to initialize your database, place them in the /initdb directory.
#### 3. Containers  
* **Build and Start**  
```bash
docker-compose up --build
```
* **Stopping the Application**
```bash
docker-compose down
```
* **View Logs**
```bash
docker-compose logs -f
```
* **Rebuild Containers After Code Changes**
```bash
docker-compose up --build
```
### Access the Application
*URL*: `http://localhost:8080`
### Run Tests
```bash
mvn test
```
#### API Endpoints:
* **Retrieve All Customers**  
*Method*: `GET`  
*URL*: `http://localhost:8080/api/customers/getAll`
* **Get a Customer by ID**  
*Method*: `GET`  
*URL*: `http://localhost:8080/api/customers/getById?id={id}`  
Replace `{id}` with a valid customer ID.
* **Get a Customers by Name**  
  *Method*: `GET`  
  *URL*: `http://localhost:8080/api/customers/getByName?name={name}`  
  Replace `{name}` with any part of any customer names.
* **Get a Customers by Phone Number**  
  *Method*: `GET`  
  *URL*: `http://localhost:8080/api/customers/getByPhoneNumber?phoneNumber={phoneNumber}`  
  Replace `{phoneNumber}` with any part of customer phone numbers.
* **Create a New Customer**  
*Method*: `POST`  
*URL*: `http://localhost:8080/api/customers/create`  
*Request Body*: `{ "firstName": "Ivan", "middleName": "Ivanovich", "lastName": "Ivanov", "emailAddress": "ivan.ivanov@example.com", "phoneNumber": "+1234567890" }`
* **Update an Existing Customer**  
*Method*: `POST`  
*URL*: `http://localhost:8080/api/customers/update`  
*Request Body*: `{ "id": "5dd6b153-c0f1-11ef-a4d8-0242ac110002", "firstName": "Ivan", "middleName": "Ivanavich", "lastName": "Ivanou", "emailAddress": "ivan.ivanou@example.com", "phoneNumber": "1112223333" }`
* **Delete a Customer**  
*Method*: `DELETE`  
*URL*: `http://localhost:8080/api/customers/delete?id={id}`  
Replace `{id}` with the customer ID to delete.
### Monitoring
* **Health Check:** `http://localhost:8080/actuator/health`  
* **Metrics:** `http://localhost:8080/actuator/metrics`

## CI/CD Pipeline
This repository uses GitHub Actions for CI/CD. The process includes:
1. Building and testing the application using Maven.
2. Building the Docker image and pushing it to Docker Hub.
### Setup
1. Add the following secrets to the repository:
  - `DOCKER_USERNAME`: Your Docker Hub username.
  - `DOCKER_PASSWORD`: Your Docker Hub password.
2. Push to the `master` branch to trigger the pipeline.

## Author
Viachaslau Barkou

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
