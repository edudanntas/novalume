# NovaLume Order Service

## Description

NovaLume Order Service is a RESTful API developed using Spring Boot for managing customer orders. It provides a complete solution for order processing, payment handling, and integration with other services through asynchronous communication with RabbitMQ.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- RabbitMQ (AMQP)
- Spring Cloud OpenFeign
- Maven
- Lombok
- JaCoCo (Java Code Coverage)
- JUnit 5
- Mockito

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── br/com/eduardo/novalumeorderservice/
│   │       ├── controller/   # REST endpoints
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── infra/
│   │       │   ├── exception/ # Exception handling
│   │       │   └── config/    # Configuration classes
│   │       ├── mapper/       # DTO-Entity mappers
│   │       ├── model/        # Domain entities
│   │       ├── repository/   # Data access layer
│   │       ├── service/      # Business logic
│   │       ├── client/       # Feign clients for external services
│   │       └── queue/        # Message producers and consumers
│   └── resources/
│       ├── application.yaml  # Application configuration
│       └── application-dev.yaml # Development configuration
└── test/
    └── java/
        └── br/com/eduardo/novalumeorderservice/
            ├── controller/   # Controller tests
            ├── service/      # Service tests
            ├── repository/   # Repository tests
            └── queue/        # Message queue tests
```

## Features

- Order creation and management
- Integration with Catalog Service
- Asynchronous communication for decoupled services
- Global exception handling
- Comprehensive test coverage with JaCoCo

## How to Run

### Prerequisites

- Java 21+
- Maven
- PostgreSQL
- RabbitMQ

### Commands

```bash
# Clone the repository
git clone https://github.com/edudanntas/novalumeorderservice.git

# Enter the project directory
cd novalumeorderservice

# Compile the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Using Docker

You can also run the application using Docker:

```bash
# Start the database and RabbitMQ
docker-compose up -d

# Run the application
mvn spring-boot:run
```

## API Endpoints

### Orders

- **POST /api/orders** - Create a new order
- **GET /api/orders** - List all orders
- **GET /api/orders/{id}** - Get order by ID
- **PUT /api/orders/{id}/status** - Update order status
- **GET /api/orders/customer/{customerId}** - Get orders by customer ID

## Error Handling

The service implements global exception handling with standardized responses:

- `400 Bad Request` - When the request is invalid
- `404 Not Found` - When an order is not found
- `409 Conflict` - When there's a conflict with the current state
- `500 Internal Server Error` - When an unexpected error occurs

## Integration

This service integrates with other NovaLume services:

- **Catalog Service** - To retrieve product information
- **Notification Service** - To send order status updates

## Development

### Compilation

```bash
mvn clean install
```

### Tests

```bash
mvn test
```

### Test Coverage

This project uses JaCoCo for test coverage analysis. To generate the coverage report:

```bash
mvn verify
```

The coverage report will be available at `target/site/jacoco/index.html`

## CI/CD

The project uses GitHub Actions for continuous integration and deployment. (in progress)

## Documentation

API documentation is available via Swagger UI when the application is running: (in progress)
<http://localhost:8081/swagger-ui.html>
