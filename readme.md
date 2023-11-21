# BotBlend API

---

## Introduction

BotBlend integrates Telegram bot with Chat GPT, offering a unified platform for smooth communication.
The service also includes a user-friendly web interface for easy administration.

## Technologies and Tools

- **Spring Boot:** A Java-based framework for building web applications.
- **Spring Security:** Provides authentication and authorization capabilities.
- **Spring Data JPA:** Simplifies database interactions within Hibernate.
- **Swagger:** Allows for API documentation and testing.
- **PostgreSQL:** Used as the relational database to store book and user data.
- **Liquibase:** Library for tracking, managing, and applying database schema changes.
- **Docker:** A platform for delivering software in containers.
- **Telegram API:** Integrated to harness the capabilities of Telegram bots, enabling seamless communication with OpenAI Chat.
- **OpenAI API:** Utilized to leverage advanced natural language processing capabilities, enhancing the intelligence of the system.

## Video Presentation

[![https://youtu.be/je94b48P8GA?si=BjrNyUUWF0zb-knZ](https://img.youtube.com/vi/USgx101zYx0/0.jpg)](https://youtu.be/USgx101zYx0)

## Functionalities

### Authentication:

The API uses token-based authentication. You must obtain a JSON Web Token (JWT) and include it in the Authorization
header for protected routes.

### Telegram bot:

The API offers functionality for integrating with Telegram bots, enabling seamless communication with the OpenAI Chat.

### User management:

Facilitates the creation of new administrative users, updating user information and credentials, as well as user deletion.


### Chat management:

Empowers administrative users to explore and manage chats and messages. Additionally, users can send messages to specific chats.

## Setup and Usage

### Environment Configuration (.env)

To set up your environment, create a `.env` file in the project root directory and add the following content:

```.env
# Database Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=botblend_app
POSTGRES_LOCAL_PORT=5434
POSTGRES_DOCKER_PORT=5432

# Spring Boot Configuration
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005

# Open Ai Configuration
OPEN_AI_API_KEY=yourOpenAiSecretKey

# Telegram Bot Configuration
TELEGRAM_API_BOT_KEY=yourTelegramBotSecretKey
TELEGRAM_API_BOT_NAME=botblend_chat_bot

# JWT Secret key
JWT_SECRET=useStrongSecretKeyRotateYourSecretRegularlyDoNotUseTheSameSecretForMultipleJWTs

```
> [!WARNING]
> Make sure to specify properties with appropriate values.

### User Credentials

To access the API, use the following credentials:

- **Root (Admin User):**
    - Email: root@botblend.app
    - Password: Test1234

### Running the Project

1. Clone the project from
   GitHub: [BotBlend GitHub Repository](https://github.com/Dimagaa/botblend).

2. Ensure you have Java 17 and Docker installed on your system.

3. Open a terminal and navigate to the project directory.

4. Build project by running the following commands regarding your system:
    - Windows:
      ```
      mvnw.cmd clean install
      ```

    - Unix:
      ```
      ./mvnw clean install
      ```

5. Build docker container and start Spring Boot App:
   ```
   docker-compose build
   docker-compose up
   ```
This command will start the project, including the PostgreSQL database, Spring Boot application, and other required
services.

6. To access and test the API using Swagger, please ensure to use the appropriate port. By default, the Spring Boot
   application may run on port 8088, but the port can be configured in the `.env` file under `SPRING_LOCAL_PORT`
   and `SPRING_DOCKER_PORT`. If you've customized the port configuration in the `.env` file, replace `8088` in the
   Swagger URL accordingly.

    - Swagger Documentation URL: [Swagger Documentation](http://localhost:<PORT>/api/swagger-ui/index.html#/)

7. Alternatively, you can use Postman for API
   testing: [BotBlend Postman Collection](https://www.postman.com/lively-spaceship-141404/workspace/botblend).
> [!NOTE]
> Fork collections and environments to use them
   

---
