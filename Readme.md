Camel-Drools-REST Application
A Spring Boot application that integrates Apache Camel with Drools rules engine and REST API endpoints.

Architecture
The application consists of two main engines:

Drools Rules Engine: Processes business logic for order validation and approval
REST API Engine: Provides HTTP endpoints for order processing
Apache Camel acts as the integration layer, orchestrating communication between these engines.

Features
Order Processing: Submit orders via REST API
Business Rules: Automatic order approval/rejection based on configurable rules
Discount Calculation: Dynamic discount application based on customer category
Validation: Input validation with error handling
Health Monitoring: Health check endpoints
Scheduled Processing: Background order processing
Business Rules
The Drools engine implements the following rules:

High Value Orders: Orders ≥ $1000 are automatically approved
Premium Customer Discount: Premium customers get 10% discount
Regular Customer Discount: Regular customers get 5% discount on orders ≥ $500
Low Value Rejection: Orders < $100 are rejected
Standard Approval: Orders between $100-$999 are approved after validation
Project Structure
src/main/java/com/example/
├── CamelDroolsApplication.java # Main Spring Boot application
├── model/
│ └── Order.java # Order domain model
├── service/
│ └── DroolsService.java # Drools rules engine service
└── routes/
└── CamelRoutes.java # Camel route definitions
Getting Started
Prerequisites
Java 17 or higher
Maven 3.6+
Build and Run
Clone and build:
bash
mvn clean install
Run the application:
bash
mvn spring-boot:run
The application will start on port 8080
API Endpoints
POST /api/orders/process - Process an order through the rules engine
GET /api/orders/health - Health check endpoint
Testing
Make the test script executable and run it:

bash
chmod +x test-commands.sh
./test-commands.sh
Or run individual cURL commands:

Health Check
bash
curl -X GET http://localhost:8080/api/orders/health
Process High Value Order
bash
curl -X POST http://localhost:8080/api/orders/process \
 -H "Content-Type: application/json" \
 -d '{
"orderId": "ORD-001",
"customerId": "CUST-001",
"amount": 1500.00,
"category": "REGULAR"
}'
Process Premium Customer Order
bash
curl -X POST http://localhost:8080/api/orders/process \
 -H "Content-Type: application/json" \
 -d '{
"orderId": "ORD-002",
"customerId": "CUST-002",
"amount": 800.00,
"category": "PREMIUM"
}'
Expected Responses
High Value Order (≥ $1000)
json
{
"orderId": "ORD-001",
"customerId": "CUST-001",
"amount": 1500.0,
"category": "REGULAR",
"discount": 0.0,
"status": "APPROVED",
"approved": true
}
Premium Customer Order
json
{
"orderId": "ORD-002",
"customerId": "CUST-002",
"amount": 800.0,
"category": "PREMIUM",
"discount": 80.0,
"status": "APPROVED",
"approved": true
}
Low Value Order (< $100)
json
{
"orderId": "ORD-004",
"customerId": "CUST-004",
"amount": 50.0,
"category": "REGULAR",
"discount": 0.0,
"status": "REJECTED",
"approved": false
}
Camel Integration Points
REST to Drools: Camel routes handle REST requests and forward to Drools engine
Validation Layer: Camel validates input before processing
Error Handling: Centralized exception handling in Camel routes
Message Queues: Internal SEDA queues for asynchronous processing
Scheduled Tasks: Timer-based routes for background processing
Monitoring
Application Health: http://localhost:8080/api/orders/health
Spring Actuator: http://localhost:8080/actuator/health
Camel Routes: http://localhost:8080/actuator/camelroutes
Extending the Application
Adding New Rules
Edit the DroolsService.java file and add new rules to the rules string:

java
rule "New Business Rule"
when
$order : Order(/_ your conditions _/)
then
// your actions
end
Adding New Endpoints
Add new REST endpoints in CamelRoutes.java:

java
rest("/orders")
.post("/newEndpoint")
.to("direct:newRoute");
Custom Processors
Create custom processors for complex business logic:

java
from("direct:customProcessing")
.process(exchange -> {
// custom processing logic
});
Dependencies
Spring Boot 3.2.0: Application framework
Apache Camel 4.0.0: Integration framework
Drools 8.44.0: Rules engine
Jackson: JSON processing
Maven: Build tool
Troubleshooting
Port conflicts: Change server port in application.yml
Rules compilation errors: Check Drools rule syntax
JSON parsing issues: Verify request content-type headers
Route startup issues: Check Camel route definitions
License
This project is for demonstration purposes.
