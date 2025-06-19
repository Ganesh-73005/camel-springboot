package com.example.service;

import com.example.model.Order;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DroolsService {
    
    private KieContainer kieContainer;
    
    @PostConstruct
    public void init() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        String rules = "package com.example.rules\n" +
            "\n" +
            "import com.example.model.Order\n" +
            "\n" +
            "rule \"High Value Order Approval\"\n" +
            "when\n" +
            "    $order : Order(amount >= 1000, approved == false)\n" +
            "then\n" +
            "    $order.setApproved(true);\n" +
            "    $order.setStatus(\"APPROVED\");\n" +
            "    System.out.println(\"High value order approved: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Premium Customer Discount\"\n" +
            "when\n" +
            "    $order : Order(category == \"PREMIUM\", discount == 0.0)\n" +
            "then\n" +
            "    $order.setDiscount($order.getAmount() * 0.1);\n" +
            "    System.out.println(\"Premium discount applied: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Regular Customer Discount\"\n" +
            "when\n" +
            "    $order : Order(category == \"REGULAR\", amount >= 500, discount == 0.0)\n" +
            "then\n" +
            "    $order.setDiscount($order.getAmount() * 0.05);\n" +
            "    System.out.println(\"Regular discount applied: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Reject Low Value Orders\"\n" +
            "when\n" +
            "    $order : Order(amount < 100, approved == false)\n" +
            "then\n" +
            "    $order.setStatus(\"REJECTED\");\n" +
            "    System.out.println(\"Low value order rejected: \" + $order.getOrderId());\n" +
            "end\n" +
            "\n" +
            "rule \"Standard Approval\"\n" +
            "when\n" +
            "    $order : Order(amount >= 100, amount < 1000, approved == false, status == \"PENDING\")\n" +
            "then\n" +
            "    $order.setApproved(true);\n" +
            "    $order.setStatus(\"APPROVED\");\n" +
            "    System.out.println(\"Standard order approved: \" + $order.getOrderId());\n" +
            "end";
            
        kieFileSystem.write("src/main/resources/rules/order.drl", rules);
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Rules compilation failed: " + kieBuilder.getResults());
        }
        
        KieModule kieModule = kieBuilder.getKieModule();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
    }
    
    public Order processOrder(Order order) {
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