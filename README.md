# Event Management RSVP Service
**JHU EN.605.789.81 — Final Project | Group B**

A RESTful API for managing events and RSVPs. Users can create accounts, create events, and register or unregister for events. Built with Spring Boot and secured with JWT authentication.

**Live API:** `https://event-service-jhu-52bc6daa6d33.herokuapp.com`  
**Swagger UI:** `https://event-service-jhu-52bc6daa6d33.herokuapp.com/swagger-ui.html`

---

## Tech Stack
- Java 17 + Spring Boot
- Spring Data JPA + MySQL
- Spring Security + JWT (stateless)
- Swagger / OpenAPI 3
- Deployed on Heroku with JawsDB MySQL

---

## Running Locally

**Prerequisites:** Java 17, Maven, MySQL running on port 3306

1. Clone the repo and navigate to the project directory
2. Create a MySQL database named `event_service`
3. Build and run:
```bash
mvn clean package
mvn spring-boot:run
```
4. Access locally:
   - Swagger UI → http://localhost:8080/swagger-ui.html
   - OpenAPI JSON → http://localhost:8080/v3/api-docs

To override the default DB connection:
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/your_db
export SPRING_DATASOURCE_USERNAME=your_user
export SPRING_DATASOURCE_PASSWORD=your_password
```

---

## API Endpoints

### Auth
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/auth/register` | No | Register a new user, returns JWT |
| POST | `/api/v1/auth/login` | No | Login, returns JWT |

### Users
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/v1/users` | No | List all users (name & phone only) |
| GET | `/api/v1/users/me` | Yes | Get your own full profile |
| GET | `/api/v1/users/{userId}` | No | Get a user's public profile |
| PUT | `/api/v1/users/{userId}` | Yes | Update your own profile |
| DELETE | `/api/v1/users/{userId}` | Yes | Delete your own account |

### Events
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/v1/events` | No | List all events |
| GET | `/api/v1/events?upcoming=true` | No | List upcoming events only |
| GET | `/api/v1/events/{eventId}` | No | Get event details |
| POST | `/api/v1/events` | Yes | Create an event (caller becomes organizer) |
| PUT | `/api/v1/events/{eventId}` | Yes | Update an event (organizer only) |
| DELETE | `/api/v1/events/{eventId}` | Yes | Cancel an event (organizer only) |

### Registration
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/events/register/{eventId}` | Yes | Register yourself for an event |
| DELETE | `/api/v1/events/register/{eventId}` | Yes | Unregister yourself from an event |
| GET | `/api/v1/events/register/me` | Yes | View your upcoming registered events |
| GET | `/api/v1/users/register/{eventId}` | Yes | View attendee list (organizer only) |

---

## Authentication

Protected endpoints require a Bearer token in the `Authorization` header:
```
Authorization: Bearer <token>
```
Obtain a token via `POST /api/v1/auth/register` or `POST /api/v1/auth/login`.
