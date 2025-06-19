package com.example.routes;

import com.example.model.Order;
import com.example.service.DroolsService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelRoutes extends RouteBuilder {

    @Autowired
    private DroolsService droolsService;

    @Override
    public void configure() throws Exception {
        
        // Exception handling
        onException(Exception.class)
            .handled(true)
            .setHeader("CamelHttpResponseCode", constant(500))
            .setBody(simple("Error processing request: ${exception.message}"))
            .log("Error occurred: ${exception.message}");

        // REST API Configuration
        restConfiguration()
            .component("servlet")
            .host("localhost")
            .port(8080)
            .contextPath("/api")
            .bindingMode("json")
            .dataFormatProperty("prettyPrint", "true");

        // REST API Endpoints
        rest("/orders")
            .description("Order Management API")
            .consumes("application/json")
            .produces("application/json")
            
            .post("/process")
                .description("Process order through rules engine")
                .type(Order.class)
                .outType(Order.class)
                .to("direct:processOrder")
                
            .get("/health")
                .description("Health check endpoint")
                .to("direct:healthCheck");

        // Direct route for order processing
        from("direct:processOrder")
            .routeId("processOrderRoute")
            .log("Received order for processing: ${body}")
            .to("direct:validateOrder")
            .to("direct:applyRules")
            .log("Order processing completed: ${body}");

        // Order validation route
        from("direct:validateOrder")
            .routeId("validateOrderRoute")
            .choice()
                .when(simple("${body.orderId} == null || ${body.orderId} == ''"))
                    .throwException(new IllegalArgumentException("Order ID is required"))
                .when(simple("${body.customerId} == null || ${body.customerId} == ''"))
                    .throwException(new IllegalArgumentException("Customer ID is required"))
                .when(simple("${body.amount} <= 0"))
                    .throwException(new IllegalArgumentException("Order amount must be positive"))
                .otherwise()
                    .log("Order validation passed for: ${body.orderId}");

        // Rules engine route
        from("direct:applyRules")
            .routeId("applyRulesRoute")
            .log("Applying business rules to order: ${body.orderId}")
            .bean(droolsService, "processOrder")
            .log("Rules applied successfully to order: ${body.orderId}");

        // Health check route
        from("direct:healthCheck")
            .routeId("healthCheckRoute")
            .setBody(constant("{\"status\":\"UP\",\"service\":\"Camel-Drools-REST\"}"))
            .setHeader("Content-Type", constant("application/json"));

        // Internal communication routes
        from("timer://orderProcessor?period=30000")
            .routeId("scheduledOrderProcessor")
            .log("Running scheduled order processing check")
            .setBody(constant(new Order("SCHED-001", "SYSTEM", 750.0, "REGULAR")))
            .to("direct:applyRules")
            .log("Scheduled processing result: ${body}");

        // Message queue simulation
        from("seda:orderQueue?concurrentConsumers=2")
            .routeId("orderQueueProcessor")
            .log("Processing order from queue: ${body}")
            .to("direct:applyRules")
            .to("seda:processedOrders");

        from("seda:processedOrders")
            .routeId("processedOrdersHandler")
            .log("Handling processed order: ${body}")
            .choice()
                .when(simple("${body.approved} == true"))
                    .log("Order approved and ready for fulfillment: ${body.orderId}")
                .otherwise()
                    .log("Order requires manual review: ${body.orderId}");
    }
}