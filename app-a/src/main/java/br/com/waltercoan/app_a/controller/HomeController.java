package br.com.waltercoan.app_a.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.ConfigurationItem;
import io.dapr.client.domain.ConversationInput;
import io.dapr.client.domain.ConversationRequest;
import io.dapr.client.domain.ConversationResponse;
import io.dapr.client.domain.HttpExtension;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class HomeController {
    
    @Value("${dapr.service.app-b.name}")
    private String SERVICE_APP_B;
    @Value("${dapr.service.app-p.name}")
    private String SERVICE_APP_P;
    @Value("${dapr.configurationstore.name}")
    private String DAPR_CONFIGURATON_STORE;

    @PostMapping()
    public ResponseEntity index() {
        System.out.println("App A started");
        try(DaprClient daprClient = new DaprClientBuilder().build()){
            var message = "Hello from App A";
            daprClient.invokeMethod(SERVICE_APP_B, "/api", message, HttpExtension.POST).block();
            daprClient.invokeMethod(SERVICE_APP_P, "/api", message, HttpExtension.POST).block();

        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error starting App A");
        }
        return ResponseEntity.ok().body("App A started");
    }

    @PostMapping("/chat")
    public ResponseEntity chat(@RequestBody String message) {
        System.out.println("App A received message: " + message);
        try (DaprPreviewClient clientPreview = new DaprClientBuilder().buildPreviewClient()) {
            ConversationInput daprConversationInput = new ConversationInput(message);
            daprConversationInput.setRole("user");
            

            var conversationName = "echo";
            try (DaprClient client = (new DaprClientBuilder()).build()) {
               ConfigurationItem item = client.getConfiguration(DAPR_CONFIGURATON_STORE, "CONVERSATION-NAME").block();
               conversationName = item.getValue();
            } catch (Exception e) {
                System.out.println("Could not get config item, err:" + e.getMessage());
            }
            


            Mono<ConversationResponse> responseMono = clientPreview.converse(new ConversationRequest(conversationName,
                    List.of(daprConversationInput))
                    .setScrubPii(true)
                    .setTemperature(1.0d));
            ConversationResponse response = responseMono.block();

            var talk = response.getConversationOutputs().get(0).getResult();
            System.out.printf("Conversation output: %s", talk);

            return ResponseEntity.ok().body(talk);
            
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error starting App A");
        }
        
    }
}
