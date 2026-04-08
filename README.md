# Career Gateway Backend

A Spring Boot REST API backend for the Career Gateway application — a career assessment and guidance platform for students.

## Tech Stack

- **Java 17** with **Spring Boot 3.2.4**
- **Spring Data JPA** for ORM and database access
- **MySQL / PostgreSQL** dual database support
- **JWT (jjwt)** for authentication tokens
- **BCrypt** for secure password hashing
- **Docker** support with multi-stage builds

## Project Structure

```
src/main/java/com/example/careergateway/
├── CareerGatewayBackendApplication.java   # Main entry point
├── config/
│   └── WebConfig.java                     # CORS configuration
├── controller/
│   ├── AuthController.java                # Sign-up & Sign-in endpoints
│   ├── UserController.java                # User profile management
│   ├── AdminController.java               # Admin dashboard endpoints
│   └── AssessmentController.java          # Assessment CRUD operations
├── dto/
│   ├── ApiResponse.java                   # Generic API response wrapper
│   ├── SignUpRequest.java                 # Registration request DTO
│   ├── SignInRequest.java                 # Login request DTO
│   └── AssessmentSaveRequest.java         # Assessment submission DTO
├── entity/
│   ├── User.java                          # User JPA entity
│   └── AssessmentResult.java              # Assessment result JPA entity
├── repository/
│   ├── UserRepository.java                # User data access layer
│   └── AssessmentResultRepository.java    # Assessment data access layer
└── util/
    └── JwtUtil.java                       # JWT token utility
```

## API Endpoints

### Authentication (`/api/auth`)
| Method | Endpoint    | Description         |
|--------|-------------|---------------------|
| POST   | `/signup`   | Register a new user |
| POST   | `/signin`   | Login existing user |

### Users (`/api/users`)
| Method | Endpoint     | Description          |
|--------|--------------|----------------------|
| PUT    | `/{userId}`  | Update user profile  |
| DELETE | `/{userId}`  | Delete user account  |

### Assessments (`/api/assessments`)
| Method | Endpoint            | Description              |
|--------|---------------------|--------------------------|
| POST   | `/save`             | Save assessment result   |
| GET    | `/history/{userId}` | Get user's assessments   |

### Admin (`/api/admin`)
| Method | Endpoint  | Description           |
|--------|-----------|-----------------------|
| GET    | `/users`  | List all users        |
| GET    | `/stats`  | Get dashboard stats   |

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8.0+ (or PostgreSQL for production)
- Maven 3.8+

### Local Development

1. **Configure the database** in `src/main/resources/application.properties`
2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
3. The server starts at `http://localhost:8080`

### Docker

```bash
docker build -t career-gateway-backend .
docker run -p 8080:8080 career-gateway-backend
```

## Environment Variables

| Variable                    | Default                          | Description            |
|-----------------------------|----------------------------------|------------------------|
| `SPRING_DATASOURCE_URL`    | `jdbc:mysql://localhost:3306/...` | Database JDBC URL      |
| `SPRING_DATASOURCE_USERNAME`| `root`                          | Database username      |
| `SPRING_DATASOURCE_PASSWORD`| `root123`                       | Database password      |
| `JPA_DIALECT`              | `MySQLDialect`                   | Hibernate SQL dialect  |
| `PORT`                     | `8080`                           | Server port            |
| `FRONTEND_URL`             | (empty)                          | Frontend origin URL    |

## License

This project is developed as a final year academic project.
