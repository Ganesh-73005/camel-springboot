package com.example.fulfillment.routes;

import com.example.fulfillment.model.FulfillmentOrder;
import com.example.fulfillment.service.FulfillmentRulesService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FulfillmentRoutes extends RouteBuilder {

    @Autowired
    private FulfillmentRulesService fulfillmentRulesService;

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
            .port(8081)
            .contextPath("/api")
            .bindingMode("json")
            .dataFormatProperty("prettyPrint", "true");

        // REST API Endpoints
        rest("/fulfillment")
            .description("Order Fulfillment API")
            .consumes("application/json")
            .produces("application/json")
            
            .post("/orders")
                .description("Process order fulfillment")
                .type(FulfillmentOrder.class)
                .outType(FulfillmentOrder.class)
                .to("direct:processFulfillment")
                
            .get("/health")
                .description("Health check endpoint")
                .to("direct:healthCheck");

        // Main fulfillment processing route
        from("direct:processFulfillment")
            .routeId("processFulfillmentRoute")
            .log("Received order for fulfillment: ${body}")
            .to("direct:applyFulfillmentRules")
            .choice()
                .when(simple("${body.fulfillmentStatus} == 'PENDING'"))
                    .log("Order ${body.orderId} requires manual fulfillment review")
                .otherwise()
                    .log("Order ${body.orderId} fulfillment processed: ${body.fulfillmentStatus}")
            .end()
            .log("Fulfillment processing completed: ${body}");

        // Rules engine route for fulfillment
        from("direct:applyFulfillmentRules")
            .routeId("applyFulfillmentRulesRoute")
            .log("Applying fulfillment rules to order: ${body.orderId}")
            .bean(fulfillmentRulesService, "processFulfillment")
            .log("Fulfillment rules applied to order: ${body.orderId}");

        // Health check route
        from("direct:healthCheck")
            .routeId("healthCheckRoute")
            .setBody(constant("{\"status\":\"UP\",\"service\":\"Fulfillment-Service\"}"))
            .setHeader("Content-Type", constant("application/json"));

        // Scheduled task to process pending fulfillments
        from("timer://fulfillmentProcessor?period=60000")
    .routeId("scheduledFulfillmentProcessor")
    .log("Running scheduled fulfillment processing check")
    .process(exchange -> {
        FulfillmentOrder order = new FulfillmentOrder();
        order.setOrderId("SCHED-001");
        order.setAmount(750.0);
        order.setCategory("REGULAR");
        order.setStatus("APPROVED");
        exchange.getMessage().setBody(order);
    })
    .to("direct:processFulfillment")
    .log("Scheduled fulfillment processing completed: ${body}");
    }
}