# Networking Platform

It is the backend service of an event and interest platform where people meet people with similar interests to help each other...

## How to Setup and Run the project

### Prerequisites

You would need the following tools installed before running the project locally:

- Java 17
- Maven
- IntelliJ IDEA (or any preferred IDE)
- Docker

### Running the project

1. Create .env file in the root folder with database credentials:
   ```
   JMS_DATABASE_NAME=hackathon-db
   JMS_DATABASE_USER=admin
   JMS_DATABASE_PASSWORD=db-password
   ```
2. Start DB
    - run `docker-compose up -d` in a terminal in the root folder
      - This command will start a postgreSQL DB in a docker container with the properties we've entered in the .env file
3. Setup IntelliJ environment variables
    - Run -> Edit Configurations, then under Environment Variables, you should add the following:
   ```
   DATABASE_NAME=hackathon-db;DATABASE_PASSWORD=db-password;DATABASE_USER=admin;JWT_SECRET=36763979244226452948404D635166546A576D5A7134743777217A25432A462D
   ```
4. Start the app
    - run `mvn clean install` in a terminal to get all the needed dependencies and to build the project
    - Run -> Run 
        - The app should be running on localhost:8080
5. Interact with the app
6. Api documentation:
    - http://localhost:8080/auth/swagger-ui/index.html#/
