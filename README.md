# Here's the complete content for the `README.md` file:
NB: The application is well tested and running with Docker containers (including:- Tanda-B2C-API-APP, Kafka, MongoDB, etc)

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
./mvnw clean install
Run
```bash
docker-compose up --build -d # to run the container in a detached mode.
or
docker-compose up --build

################################## Building & Interactign with the specified containers #################################
#########################################################################################################################

1. tanda-b2c-api
Build the Docker Image (assuming you have a Dockerfile in the project):

   sh
      docker build -t b2c-api .
      Run the tanda-b2c-api Container:

   sh
      docker run -d --name tanda-b2c-api -p 8080:8080 --link kafka:kafka --link mongodb:mongodb b2c-api

2. Kafka

Start Zookeeper:

   sh
      docker run -d --name zookeeper -p 2181:2181 wurstmeister/zookeeper:3.4.6

Start Kafka:

   sh
      docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 wurstmeister/kafka:2.13-2.7.0

3. MongoDB
   Start MongoDB:
   sh
      docker run -d --name mongodb -p 27017:27017 mongo:4.2

#########################################################################################################################

#################################### Building & Running the App with Docker Compose #####################################
#########################################################################################################################

Ensure there is docker-compose.yml file in the root directory of the project.

Start all services:

sh
   docker-compose up -d

Stop all services:

sh
   docker-compose down

Verifying the Setup
#After starting the services with Docker Compose, you can verify that everything is working correctly by checking the logs:

Check Zookeeper logs:

sh #Check zookeeper logs for errors and warnings in zookeeper logs for example docker container logs using shell commands
   docker logs zookeeper

Check Kafka logs:

sh #Check Kafka logs for errors and warnings in Kafka logs for example docker container logs
   docker logs kafka

Check MongoDB logs:

sh #Check MongoDB logs for errors and warnings in MongoDB logs for example docker
   docker logs mongodb

Check tanda-b2c-api logs:

sh #Check tanda-b2c-api logs for errors and warnings in  tanda-b2c logs for example docker
   docker logs tanda-b2c-api

#########################################################################################################################

#################################### Exec into the Running Docker Containers in our B2C Service #####################################
#########################################################################################################################

Here are the commands to exec into the containers and carry out some operations:

### Exec into Containers

1. **Exec into Zookeeper Container**:
    ```sh
    docker exec -it zookeeper /bin/bash
    ```

2. **Exec into Kafka Container**:
    ```sh
    docker exec -it kafka /bin/bash
    ```

3. **Exec into MongoDB Container**:
    ```sh
    docker exec -it mongodb /bin/bash
    ```

4. **Exec into tanda-b2c-api Container**:
    ```sh
    docker exec -it tanda-b2c-api /bin/bash
    ```

### Carry Out Operations

#### Zookeeper

Zookeeper typically doesn't require direct interaction for this setup, but you can use the `zkCli.sh` to interact with it:

```sh
docker exec -it zookeeper /bin/bash
./bin/zkCli.sh
```

#### Kafka

1. **List Kafka Topics**:
    ```sh
    docker exec -it kafka /bin/bash
    /opt/kafka/bin/kafka-topics.sh --list --zookeeper zookeeper:2181
    ```

2. **Create a Kafka Topic**:
    ```sh
    docker exec -it kafka /bin/bash
    /opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic b2c-request
    ```

3. **Describe a Kafka Topic**:
    ```sh
    docker exec -it kafka /bin/bash
    /opt/kafka/bin/kafka-topics.sh --describe --zookeeper zookeeper:2181 --topic b2c-request
    ```

4. **Produce Messages to a Kafka Topic**:
    ```sh
    docker exec -it kafka /bin/bash
    /opt/kafka/bin/kafka-console-producer.sh --broker-list kafka:9092 --topic b2c-request
    ```

5. **Consume Messages from a Kafka Topic**:
    ```sh
    docker exec -it kafka /bin/bash
    /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic b2c-request --from-beginning
    ```

#### MongoDB

1. **MongoDB Shell**:
    ```sh
    docker exec -it mongodb mongo
    ```

2. **Show Databases**:
    ```sh
    docker exec -it mongodb mongo --eval "show dbs"
    ```

3. **Use Database**:
    ```sh
    docker exec -it mongodb mongo --eval "use b2c-api"
    ```

4. **Show Collections**:
    ```sh
    docker exec -it mongodb mongo --eval "use b2c-api; show collections"
    ```

5. **Find Documents in a Collection**:
    ```sh
    docker exec -it mongodb mongo --eval "use b2c-api; db.b2c_requests.find().pretty()"
    ```

#### tanda-b2c-api

You typically don't need to exec into the `tanda-b2c-api` container for regular operations, but you can do so for debugging or log checks:

1. **Check Application Logs**:
    ```sh
    docker logs tanda-b2c-api
    ```

2. **Exec into tanda-b2c-api for Debugging**:
    ```sh
    docker exec -it tanda-b2c-api /bin/bash
    ```

### Example Operations

1. **Create a B2C Request**:
    ```sh
    curl -X POST http://localhost:8080/api/b2c/request \
        -H "Content-Type: application/json" \
        -d '{
            "amount": 1000,
            "originatorConversationID": "SGH&Y9NGBT",
            "initiatorName": "initiator",
            "securityCredential": "security",
            "commandID": "command",
            "partyA": "1234567890",
            "partyB": "0729024146",
            "remarks": "Payment for services",
            "queueTimeoutURL": "http://example.com/timeout",
            "resultURL": "http://example.com/result"
        }'
    ```

2. **Fetch Request Status**:
    ```sh
    curl -X GET http://localhost:8080/api/b2c/status/{id}
    ```

3. **Update Request Status**:
    ```sh
    curl -X PUT http://localhost:8080/api/b2c/status/{id} \
        -H "Content-Type: application/json" \
        -d '{"status": "Completed"}'
    ```

These commands will help you set up, run, and interact with the application and its components using Docker and Docker Compose.

#########################################################################################################################

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
spring.kafka.bootstrap-servers=kafka:9092
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




#################################################################################################################
########################################### **Testing the Application** #########################################
#################################################################################################################

To test the Tanda-B2C-API application's endpoints, we can use a tool like `**curl**` for command-line testing or **Postman** for a graphical interface. Below are examples of how you can test each endpoint with realistic parameters and statuses using `curl`.

### 1. POST /api/b2c/request - Create a B2C request

**Request Body Example:**

**Curl Command:**

```sh
   curl -X POST http://localhost:8080/api/b2c/request \
      -H "Content-Type: application/json" \
      -d '{
         "amount": 1000,
         "originatorConversationID": "SGH&Y9NGBT",
         "initiatorName": "initiator",
         "securityCredential": "security",
         "commandID": "command",
         "partyA": "1234567890",
         "partyB": "0729024146",
         "remarks": "Payment for services",
         "queueTimeoutURL": "http://example.com/timeout",
         "resultURL": "http://example.com/result"
      }'

```

### 2. GET /api/b2c/status/{id} - Get the status of a B2C request

**Path Parameter:**

- `id` - The ID of the B2C request (e.g., `12345`)

**Curl Command:**

```sh
curl -X GET http://localhost:8080/api/b2c/status/12345
```

### 3. PUT /api/b2c/status/{id} - Update the status of a B2C request

**Path Parameter:**

- `id` - The ID of the B2C request (e.g., `12345`)

**Request Body Example:**

```json
   curl -X PUT http://localhost:8080/api/b2c/status/{id} \
      -H "Content-Type: application/json" \
      -d '{"status": "Completed"}'
```

**Curl Command:**

```sh
curl -X PUT http://localhost:8080/api/b2c/status/12345 \
    -H "Content-Type: application/json" \
    -d '{
        "status": "COMPLETED"
    }'
```

### Additional Tips:

- Ensure the Spring Boot application is running in a detached mode (docker-compose up --build -d) and the port mappings are correct.
- Replace `localhost:8080` with the appropriate host and port if the application is running on a different host or port.
- Use realistic values for `amount`, `recipient`, `accountReference`, and `transactionDesc` based on the application's business logic and requirements.
- For the `GET` and `PUT` requests, replace `12345` with the actual ID or reference code of a B2C request you have created.

### Testing with Postman:

1. **POST /api/b2c/request**:
    - Open Postman.
    - Create a new POST request.
    - Set the URL to `http://localhost:8080/api/b2c/request`.
    - Set the Headers to include `Content-Type: application/json`.
    - Set the Body to raw JSON and paste the JSON example provided above.
    - Send the request and check the response.

2. **GET /api/b2c/status/{id}**:
    - Create a new GET request.
    - Set the URL to `http://localhost:8080/api/b2c/status/12345` (replace `12345` with a valid ID).
    - Send the request and check the response.

3. **PUT /api/b2c/status/{id}**:
    - Create a new PUT request.
    - Set the URL to `http://localhost:8080/api/b2c/status/12345` (replace `12345` with a valid ID).
    - Set the Headers to include `Content-Type: application/json`.
    - Set the Body to raw JSON and paste the JSON example provided above.
    - Send the request and check the response.

These examples should help you test the endpoints of the Tanda B2C API application effectively.


#################################################################################################################
############################### **Data Flow with Kafka Producer & Consumer** ####################################
#################################################################################################################

#### Kafka Producer #####

### KafkaProducer Overview

1. **Sending B2C Requests**:
    - The `KafkaProducer` is responsible for sending B2C requests to the Kafka topic `b2c-request`.
    - It receives the B2C request from the `B2CService` after validating and saving it to the MongoDB database.

### Detailed Explanation of the KafkaProducer Data Flow

### Data Flow with KafkaProducer

1. **Receive B2C Request**:
    - A B2C request is received via the `B2CController`.
    - The request is passed to the `B2CService` for processing.

2. **Process B2C Request**:
    - The `B2CService` validates the request (e.g., checks for valid mobile number and amount).
    - The request details, including the status as "Pending", are saved to the MongoDB database.
    - The `B2CService` then invokes the `KafkaProducer` to send the request to Kafka.

3. **Send B2C Request to Kafka**:
    - The `KafkaProducer` takes the validated and saved B2C request and sends it to the `b2c-request` Kafka topic.
    - The `KafkaProducer` uses the configured Kafka template to send the message to the appropriate Kafka topic.

4. **Kafka Broker**:
    - The Kafka broker receives the B2C request message on the `b2c-request` topic.
    - This message can then be consumed by any service subscribed to this topic for further processing (e.g., initiating the payment transaction).

### Summary

The `KafkaProducer` plays a key role in the data flow of the application by ensuring that validated B2C requests are sent to Kafka for further processing. This setup allows the application to handle asynchronous communication and processing of B2C transactions, leveraging Kafka for reliable messaging and MongoDB for persistent storage.


##### Kafka Consumer #####

The `KafkaConsumer` in the application is responsible for listening to a Kafka topic for responses related to B2C requests and processing these responses. Here's an overview of what the `KafkaConsumer` does in the context of the application's data flow:

### KafkaConsumer Overview

1. **Listening for Responses**:
    - The `KafkaConsumer` is configured to listen to the `b2c-response` Kafka topic.
    - When a message (B2C response) is published to this topic, the `KafkaConsumer` receives it.

2. **Processing Responses**:
    - Upon receiving a message, the consumer logs the received response for debugging and monitoring purposes.
    - It then retrieves the corresponding B2C request from the MongoDB database using the `requestId` from the response.

3. **Updating Request Status**:
    - If the corresponding B2C request is found in the database, the consumer updates the status of this request with the status from the response.
    - If the request is not found, it logs an error indicating that the request ID does not exist in the database.

### Data Flow with KafkaConsumer

1. **Receive B2C Request**:
    - A B2C request is created and processed by `B2CService`.
    - The request is validated and saved to the MongoDB database with a status of "Pending".
    - The `KafkaProducer` sends this request to the `b2c-request` Kafka topic.

2. **Process B2C Request**:
    - A separate service (not shown in the current code) listens to the `b2c-request` Kafka topic, processes the payment, and sends a response to the `b2c-response` Kafka topic.

3. **Listen for B2C Response**:
    - The `KafkaConsumer` listens to the `b2c-response` Kafka topic for responses.
    - When a response is received, the consumer logs the response and processes it.

4. **Update Request Status**:
    - The consumer looks up the original B2C request in the MongoDB database using the `requestId` from the response.
    - If the request is found, it updates the request status with the status provided in the response.
    - The updated request is saved back to the MongoDB database.
    - If the request is not found, an error is logged.

### Summary
The `KafkaConsumer` plays a crucial role in the application's data flow by ensuring that the status of B2C requests is updated based on the responses received from the Kafka topic. This setup ensures that the application can handle asynchronous communication and processing of B2C transactions efficiently, leveraging Kafka for messaging and MongoDB for persistent storage.