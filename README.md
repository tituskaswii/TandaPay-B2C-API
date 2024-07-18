# Here's the complete content for the `README.md` file:

markdown

# TandaPay - B2C API for Daraja Integration

## Introduction

This project implements handlers for the Daraja B2C API using Java Spring Boot, Kafka, and MongoDB.

## Requirements

- Java 17 or later
- Docker
- Docker Compose

## Setup

### Build

```bash
./mvnw clean package
Run
```bash

docker-compose up --build

*Tests*

*Unit Tests*
```bash
./mvnw test

*Integration Tests*
```bash
./mvnw verify

*API Endpoints*

POST /api/b2c/request - Create a B2C request.
GET /api/b2c/status/{id} - Get the status of a B2C request.
PUT /api/b2c/status/{id} - Update the status of a B2C request.

*Configuration*

Configuration settings are located in src/main/resources/application.properties.

*properties*
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/b2c_db

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=b2c-consumer-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=false

# Daraja B2C API Configuration
daraja.api.url=https://sandbox.safaricom.co.ke/mpesa/b2c/v3/paymentrequest
daraja.api.initiator-name=testapi
daraja.api.security-credential=EsJocK7+NjqZPC3I3EO+TbvS+xVb9TymWwaKABoaZr/Z/n0UysSs..
daraja.api.command-id=BusinessPayment
daraja.api.queue-timeout-url=https://tanda.africa/b2c/queue
daraja.api.result-url=https://tanda.africa/b2c/result
daraja.api.party-a=600996

*Kafka Topics*
b2c-request - Topic for sending B2C requests.
b2c-response - Topic for receiving B2C responses.

*Docker*

The application uses Docker and Docker Compose for containerization. Ensure Docker is installed and running.

*Dockerfile*:

dockerfile

FROM openjdk:11-jdk-slim
VOLUME /tmp
COPY target/b2c-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

*docker-compose.yml*:

```yaml

version: '3.7'

services:
  mongodb:
    image: mongo:4.2
    container_name: mongodb
    ports:
      - 27017:27017
    networks:
      - b2c-network

  kafka:
    image: wurstmeister/kafka:2.13-2.7.0
    container_name: kafka
    ports:
      - 9092:9092
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
    networks:
      - b2c-network

  zookeeper:
    image: wurstmeister/zookeeper:3.4.6
    container_name: zookeeper
    ports:
      - 2181:2181
    networks:
      - b2c-network

  b2c-api:
    image: b2c-api
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - 8080:8080
    depends_on:
      - mongodb
      - kafka
      - zookeeper
    networks:
      - b2c-network

networks:
  b2c-network:
    driver: bridge

## Further Improvements

- Implement additional validations.
- Enhance error handling.
```

This `README.md` file provides detailed instructions on how to set up, run, and test the B2C API application, along with the necessary configurations and Docker setup.

************************** Complete Application's Architecture ****************************

                                         +--------------------+
                                         |  M-Pesa Daraja API |
                                         +--------------------+
                                                   |
                                                   v
        +------------------------------------------+------------------------------------------+
        |                                                                                     |
        |                                        Kafka                                         |
        |     +----------------------+                 +----------------------+                |
        |     |  Kafka Topic:        |                 |  Kafka Topic:        |                |
        |     |  b2c-request         |                 |  b2c-response        |                |
        |     +----------------------+                 +----------------------+                |
        |               |                                          ^                           |
        |               v                                          |                           |
+-------+---------------+-----------------+   +--------------------+----------------+          |
|                                       +-----+                                          +-----+
|                                       |     |                                          |
|          +--------------------+       |     |       +--------------------+             |
|          |  B2C Controller    |       |     |       |  Kafka Consumer    |             |
|          +--------------------+       |     |       +--------------------+             |
|                    |                  |     |                    |                     |
|                    v                  |     |                    v                     |
|          +--------------------+       |     |       +--------------------+             |
|          |  B2C Service       |       |     |       |  B2C Service       |             |
|          +--------------------+       |     |       +--------------------+             |
|                    |                  |     |                    |                     |
|                    v                  |     |                    v                     |
|          +--------------------+       |     |       +--------------------+             |
|          |  Kafka Producer    |       |     |       |  B2C Request       |             |
|          +--------------------+       |     |       |  Repository        |             |
|                    |                  |     |       +--------------------+             |
|                    v                  |     |                                          |
|          +--------------------+       |     |       +--------------------+             |
|          |  B2C Request       |       |     |       |  B2C Response      |             |
|          |  Repository        +-------+     |       |  Repository        |             |
|          +--------------------+             |       +--------------------+             |
|                                             |                                          |
+---------------------------------------------+------------------------------------------+
                                                  ^                         |
                                                  |                         v
                                                  |                +--------------------+
                                                  +----------------+   MongoDB Database |
                                                                   +--------------------+

### Explanation of Components:

1. **B2C Controller**:
   - Receives API requests from clients (e.g., `POST /api/b2c/request`, `GET /api/b2c/status/{id}`, `PUT /api/b2c/status/{id}`).
   - Forwards requests to the `B2C Service`.

2. **B2C Service**:
   - Processes the B2C requests.
   - Calls `Kafka Producer` to send messages to Kafka topics.
   - Interacts with the `B2C Request Repository` and `B2C Response Repository` for database operations.

3. **Kafka Producer**:
   - Sends B2C requests to the `b2c-request` Kafka topic.

4. **Kafka Consumer**:
   - Listens for responses on the `b2c-response` Kafka topic.
   - Forwards responses to the `B2C Service` for processing.

5. **B2C Request Repository**:
   - Stores B2C request details in the MongoDB database.

6. **B2C Response Repository**:
   - Stores B2C response details in the MongoDB database.

7. **Kafka**:
   - Manages messaging between different components using topics (`b2c-request`, `b2c-response`).

8. **MongoDB Database**:
   - Stores details of B2C requests and responses.

9. **M-Pesa Daraja API**:
   - External API that handles B2C payment transactions.
   - Receives B2C requests from the system and sends back responses.

### Data Flow:

1. **B2C Request Initiation**:
   - A client sends a B2C request to the `B2C Controller`.
   - The `B2C Controller` forwards the request to the `B2C Service`.
   - The `B2C Service` saves the request to the `B2C Request Repository`.
   - The `B2C Service` calls the `Kafka Producer` to send the request to the `b2c-request` Kafka topic.

2. **Processing by M-Pesa**:
   - The message is picked up from the `b2c-request` Kafka topic and sent to the `M-Pesa Daraja API`.
   - The `M-Pesa Daraja API` processes the request and sends a response to the `b2c-response` Kafka topic.

3. **Response Handling**:
   - The `Kafka Consumer` listens for responses on the `b2c-response` Kafka topic.
   - The `Kafka Consumer` forwards the response to the `B2C Service`.
   - The `B2C Service` updates the request status in the `B2C Request Repository`.
   - The `B2C Service` saves the response in the `B2C Response Repository`.

4. **Client Status Check**:
   - A client can check the status of a B2C request via the `B2C Controller`.
   - The `B2C Controller` fetches the request status from the `B2C Request Repository` and returns it to the client.

This architecture ensures a robust, scalable, and efficient way to handle B2C transactions using the Daraja API, Kafka, and MongoDB.


*****************************************************************************************************************************************
************************************** Application's Data Flow Guide *************************************

To understand the complete data flow and how the components interact within the application, particularly regarding the HTTP request to the M-Pesa Daraja URL for making new requests, let's break down the flow step by step:

### Components Involved:

1. **User (Client)**
2. **B2C Controller**
3. **B2C Service**
4. **B2C Request Repository**
5. **Kafka Producer**
6. **Kafka (b2c-request topic)**
7. **Kafka Consumer**
8. **HTTP Client in Kafka Consumer**
9. **M-Pesa Daraja API**
10. **Kafka (b2c-response topic)**
11. **Kafka Consumer**
12. **B2C Response Repository**
13. **MongoDB**

### Data Flow:

1. **User Initiates Request**:
   - A user (client) sends a B2C request to the application's REST API endpoint (`POST /api/b2c/request`).

2. **B2C Controller Receives Request**:
   - The `B2C Controller` receives the HTTP request.
   - The controller forwards the request data to the `B2C Service`.

3. **B2C Service Processes Request**:
   - The `B2C Service` processes the request:
     - Validates the request data.
     - Saves the request details in the `B2C Request Repository` (MongoDB) with an initial status (e.g., "Pending").

4. **B2C Service Calls Kafka Producer**:
   - The `B2C Service` then calls the `Kafka Producer` to send the request data to the `b2c-request` Kafka topic.

5. **Kafka Producer Sends Message**:
   - The `Kafka Producer` sends the message containing the B2C request data to the `b2c-request` Kafka topic.

6. **Kafka Consumer Listens**:
   - The `Kafka Consumer` listens for messages on the `b2c-request` Kafka topic.

7. **HTTP Client in Kafka Consumer Makes Request to M-Pesa Daraja API**:
   - The `Kafka Consumer` processes the message:
     - Uses an HTTP client to make an HTTP POST request to the M-Pesa Daraja API (`https://sandbox.safaricom.co.ke/mpesa/b2c/v3/paymentrequest`).
     - This request includes all necessary parameters such as `InitiatorName`, `SecurityCredential`, `CommandID`, `Amount`, `PartyA`, `PartyB`, `Remarks`, `QueueTimeOutURL`, `ResultURL`, and `Occasion`.

8. **M-Pesa Daraja API Processes Request**:
   - The M-Pesa Daraja API processes the B2C payment request.
   - M-Pesa Daraja API sends an immediate response (acknowledgment) back to the Kafka Consumer and later sends the final result (success or failure) to the `ResultURL`.

9. **Kafka Consumer Processes Response**:
   - The `Kafka Consumer` receives the immediate response.
   - It updates the status of the request in the `B2C Request Repository` based on the response (e.g., "Processed", "Failed").

10. **M-Pesa Daraja API Sends Final Result**:
   - The final result from M-Pesa Daraja API is sent to the `ResultURL` specified in the initial request.
   - The application should have an endpoint to receive this callback and process it.

11. **Application Callback Endpoint Receives Final Result**:
   - The callback endpoint updates the status of the request in the `B2C Request Repository` (MongoDB) to the final status (e.g., "Completed", "Failed").

12. **Status Check by User**:
   - The user can check the status of the request by sending a GET request to the endpoint (`GET /api/b2c/status/{id}`).
   - The `B2C Controller` fetches the status from the `B2C Request Repository` and returns it to the user.

### Text-Based Architecture Diagram:

User (Client)
   |
   v
+------------------------+
|   B2C Controller       |
|  (POST /api/b2c/request)|
+------------------------+
   |
   v
+------------------------+
|    B2C Service         |
+------------------------+
   |
   v
+------------------------+
| B2C Request Repository |
|      (MongoDB)         |
+------------------------+
   |
   v
+------------------------+
|   Kafka Producer       |
+------------------------+
   |
   v
+------------------------+
|  Kafka Topic:          |
|   b2c-request          |
+------------------------+
   |
   v
+------------------------+
|   Kafka Consumer       |
+------------------------+
   |
   v
+------------------------+
|  HTTP Client (in       |
|  Kafka Consumer)       |
+------------------------+
   |
   v
+------------------------+
|  M-Pesa Daraja API     |
+------------------------+
   |
   v
+------------------------+
|  Immediate Response    |
+------------------------+
   |
   v
+------------------------+
|   Kafka Consumer       |
+------------------------+
   |
   v
+------------------------+
| B2C Request Repository |
|      (MongoDB)         |
+------------------------+
   |
   v
+------------------------+
|   Result Callback      |
|   Endpoint             |
+------------------------+
   |
   v
+------------------------+
|  Final Result Update   |
+------------------------+
   |
   v
+------------------------+
| B2C Request Repository |
|      (MongoDB)         |
+------------------------+
   |
   v
+------------------------+
|    Status Check        |
|   Endpoint (GET        |
|   /api/b2c/status/{id})|
+------------------------+
   |
   v
User (Client)

This diagram and explanation illustrate the flow of data from the user to the M-Pesa Daraja API and back, highlighting how the various components in the application interact to process B2C payment requests.

