package com.example.fulfillment.service;

import com.example.fulfillment.model.FulfillmentOrder;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class FulfillmentRulesService {
    
    private KieContainer kieContainer;
    
    @PostConstruct
    public void init() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        String rules = "package com.example.fulfillment.rules\n" +
            "\n" +
            "import com.example.fulfillment.model.FulfillmentOrder\n" +
            "\n" +
            "rule \"Express Shipping for High Value Orders\"\n" +
            "when\n" +
            "    $order : FulfillmentOrder(amount >= 500, fulfillmentStatus == \"PENDING\")\n" +
            "then\n" +
            "    $order.setShippingMethod(\"EXPRESS\");\n" +
            "    $order.setPriority(1);\n" +
            "    $order.setFulfillmentStatus(\"PROCESSING\");\n" +
            "    System.out.println(\"Express shipping set for high value order: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Standard Shipping for Regular Orders\"\n" +
            "when\n" +
            "    $order : FulfillmentOrder(amount < 500, amount >= 100, fulfillmentStatus == \"PENDING\")\n" +
            "then\n" +
            "    $order.setShippingMethod(\"STANDARD\");\n" +
            "    $order.setPriority(2);\n" +
            "    $order.setFulfillmentStatus(\"PROCESSING\");\n" +
            "    System.out.println(\"Standard shipping set for regular order: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Economy Shipping for Low Value Orders\"\n" +
            "when\n" +
            "    $order : FulfillmentOrder(amount < 100, fulfillmentStatus == \"PENDING\")\n" +
            "then\n" +
            "    $order.setShippingMethod(\"ECONOMY\");\n" +
            "    $order.setPriority(3);\n" +
            "    $order.setFulfillmentStatus(\"PROCESSING\");\n" +
            "    System.out.println(\"Economy shipping set for low value order: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Priority Handling for Premium Customers\"\n" +
            "when\n" +
            "    $order : FulfillmentOrder(category == \"PREMIUM\", fulfillmentStatus == \"PROCESSING\")\n" +
            "then\n" +
            "    $order.setPriority(0);\n" +
            "    System.out.println(\"Priority handling set for premium customer order: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Mark Ready for Shipment\"\n" +
            "when\n" +
            "    $order : FulfillmentOrder(fulfillmentStatus == \"PROCESSING\", shippingMethod != null)\n" +
            "then\n" +
            "    $order.setFulfillmentStatus(\"READY_TO_SHIP\");\n" +
            "    System.out.println(\"Order ready for shipment: \" + $order.getOrderId());\n" +
            "end";
            
        kieFileSystem.write("src/main/resources/rules/fulfillment.drl", rules);
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Fulfillment rules compilation failed: " + kieBuilder.getResults());
        }
        
        KieModule kieModule = kieBuilder.getKieModule();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
    }
    
    public FulfillmentOrder processFulfillment(FulfillmentOrder order) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(order);
            kieSession.fireAllRules();
            return order;
        } finally {
            kieSession.dispose();
        }
    }
}