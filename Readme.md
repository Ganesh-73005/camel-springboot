
# Camel-Drools-REST Integration

![Java](https://img.shields.io/badge/Java-11%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7-green)
![Apache Camel](https://img.shields.io/badge/Apache_Camel-3.21-red)
![Drools](https://img.shields.io/badge/Drools-7.74-orange)

A demonstration of how to integrate Apache Camel with Drools rules engine in a Spring Boot application, featuring REST API endpoints for order processing with business rules.

## Table of Contents
- [Introduction](#introduction)
- [Architecture Overview](#architecture-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Business Rules](#business-rules)
- [How It Works](#how-it-works)
- [Camel Routes Explained](#camel-routes-explained)
- [Error Handling](#error-handling)
- [Testing](#testing)
- [Monitoring](#monitoring)
- [Extending the Project](#extending-the-project)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Introduction

This project demonstrates the integration of three powerful technologies:
1. **Spring Boot** - For building the REST API
2. **Apache Camel** - For message routing and integration
3. **Drools** - For business rule processing

The application processes orders through a series of validation steps and business rules to determine:
- Automatic approval/rejection
- Discount application
- Status updates

## Architecture Overview

![arch](https://github.com/Ganesh-73005/camel-springboot/blob/main/Editor%20_%20Mermaid%20Chart-2025-06-19-070916.png)

## Features

- **REST API** for order processing
- **Validation layer** for input sanity checks
- **Business rules** for:
  - Automatic approval decisions
  - Discount calculations
  - Order rejection policies
- **Asynchronous processing** via SEDA queues
- **Scheduled jobs** for background processing
- **Comprehensive error handling**

## Technology Stack

| Component       | Purpose                          |
|-----------------|----------------------------------|
| Spring Boot     | Application framework            |
| Apache Camel    | Integration and routing          |
| Drools          | Business rules engine            |
| Jackson         | JSON processing                  |
| Maven           | Build and dependency management  |

## Getting Started

### Prerequisites

- Java 11+
- Maven 3.6+
- (Optional) IDE like IntelliJ or Eclipse

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/camel-drools-rest.git
   cd camel-drools-rest
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Process Order
- **Endpoint**: `POST /api/orders/process`
- **Request Body**:
  ```json
  {
    "orderId": "ORD-001",
    "customerId": "CUST-001",
    "amount": 500.00,
    "category": "PREMIUM"
  }
  ```
- **Response**:
  ```json
  {
    "orderId": "ORD-001",
    "customerId": "CUST-001",
    "amount": 500.00,
    "category": "PREMIUM",
    "discount": 50.00,
    "status": "APPROVED",
    "approved": true
  }
  ```

### Health Check
- **Endpoint**: `GET /api/orders/health`
- **Response**:
  ```json
  {
    "status": "UP",
    "service": "Camel-Drools-REST"
  }
  ```

## Business Rules

The Drools engine implements these business rules:

1. **High Value Orders** (≥ $1000): Auto-approved
2. **Premium Customers**: 10% discount
3. **Regular Customers** (≥ $500): 5% discount
4. **Low Value Orders** (< $100): Rejected
5. **Standard Orders** ($100-$999): Approved after validation

## How It Works

1. **Request Received**: Client sends order data via REST API
2. **Validation**: Camel validates required fields and data types
3. **Rules Processing**: Valid orders are processed by Drools engine
4. **Response**: Processed order returned with status/discounts

## Camel Routes Explained

Key routes in the application:

| Route | Description |
|-------|-------------|
| `direct:processOrder` | Main processing pipeline |
| `direct:validateOrder` | Validates input data |
| `direct:applyRules` | Executes Drools rules |
| `seda:orderQueue` | Async processing queue |
| `timer://orderProcessor` | Scheduled background job |

Example route definition:
```java
from("direct:processOrder")
    .log("Received order: ${body}")
    .to("direct:validateOrder")
    .to("direct:applyRules");
```

## Error Handling

The application handles:
- Missing required fields
- Invalid data types
- Negative amounts
- Internal processing errors

Example error response:
```json
{
  "error": "Order ID is required"
}
```

## Testing

Run the included test script:
```bash
./test-comments.bat  # Windows
```

Or test manually with curl:
```bash
# Health check
curl -X GET http://localhost:8080/api/orders/health

# Process order
curl -X POST http://localhost:8080/api/orders/process \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-001","customerId":"CUST-001","amount":500,"category":"REGULAR"}'
```

## Monitoring

Available monitoring endpoints:
- `http://localhost:8080/actuator/health` - Application health
- `http://localhost:8080/actuator/camelroutes` - Camel route status

## Extending the Project

### Adding New Rules
Edit `DroolsService.java`:
```java
rule "New Rule"
when
    $order : Order(amount > 2000)
then
    $order.setStatus("PRIORITY");
end
```

### Adding New Endpoints
Add to `CamelRoutes.java`:
```java
rest("/orders")
    .get("/{orderId}")
    .to("direct:getOrder");
```

## Troubleshooting

Common issues:
- **Port conflicts**: Change port in `application.yml`
- **Rule errors**: Check Drools syntax in `DroolsService.java`
- **JSON issues**: Verify Content-Type headers

## License

This project is for educational purposes.
```

This README provides:
1. Clear introduction to the technologies
2. Visual architecture diagram
3. Step-by-step setup instructions
4. Detailed API documentation
5. Explanation of Camel concepts
6. Practical examples for extension
7. Troubleshooting guidance

The markdown format makes it easy to maintain and renders beautifully on GitHub. You can further customize it with your project's specific details or additional sections as needed.
