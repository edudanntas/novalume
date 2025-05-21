# NovaLume Catalog Service

## Description
NovaLume Catalog Service is a RESTful API developed using Spring Boot for managing product catalogs. It provides a complete solution for product management with robust validation, error handling, and comprehensive test coverage.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data MongoDB
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
│   │   └── br/com/eduardo/novalumecatalogservice/
│   │       ├── controller/   # REST endpoints
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── infra/
│   │       │   └── exception/ # Exception handling
│   │       ├── mapper/       # DTO-Entity mappers
│   │       ├── model/        # Domain entities
│   │       ├── repository/   # Data access layer
│   │       └── service/      # Business logic
│   └── resources/
│       └── application.yaml  # Application configuration
└── test/
    └── java/
        └── br/com/eduardo/novalumecatalogservice/
            ├── controller/   # Controller tests
            ├── service/      # Service tests
            └── repository/   # Repository tests
```

## Features

- Product creation and management
- Product search and filtering
- Validation to avoid product duplication
- Global exception handling
- Comprehensive test coverage with JaCoCo

## How to Run

### Prerequisites
- Java 21+
- Maven

### Commands
```bash
# Clone the repository
git clone https://github.com/edudanntas/novalumecatalogservice.git

# Enter the project directory
cd novalumecatalogservice

# Compile the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## API Endpoints

### Products
- **POST /api/products** - Create a new product
- **GET /api/products** - List all products
- **GET /api/products/{id}** - Get product by ID
- **PUT /api/products/{id}** - Update a product
- **PATCH /api/products/{id}** - Upload images to a Bucket
- **DELETE /api/products/{id}** - Delete a product

## Error Handling

The service implements global exception handling with standardized responses:

- `400 Bad Request` - When the request is invalid
- `404 Not Found` - When a product is not found
- `409 Conflict` - When trying to create a product with an existing name
- `500 Internal Server Error` - When an unexpected error occurs

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

The project uses GitHub Actions for continuous integration and deployment.

## Documentation

API documentation is available via Swagger UI when the application is running:
http://localhost:8080/swagger-ui.html
```