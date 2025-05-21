# NovaLume Catalog Service

## Description
NovaLume Catalog Service is a product catalog service developed with Spring Boot, offering an API for product management.

## Technologies Used

- Java
- Spring Boot
- Spring Mongo
- Maven
- Lombok

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── br/com/eduardo/novalumecatalogservice/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── infra/
│   │       │   └── exception/
│   │       ├── mapper/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
└── test/
```

## Features

- Product creation
- Validation to avoid product duplication
- Global exception handling

## How to Run

### Prerequisites
- Java 17+
- Maven

### Commands
```bash
# Clone the repository
git clone https://github.com/seu-usuario/novalumecatalogservice.git

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

## Error Handling

The service implements global exception handling with standardized responses:

- `409 Conflict` - When trying to create a product with an existing name

## Development

### Compilation
```bash
mvn clean install
```

### Tests
```bash
mvn test
```