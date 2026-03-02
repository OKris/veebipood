package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.dto.OrderProductDto;
import ee.kristina.veebipood.entity.Product;
import ee.kristina.veebipood.model.websocket.Greeting;
import ee.kristina.veebipood.model.websocket.HelloMessage;
import ee.kristina.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
public class WebsocketController {

    @Autowired
    private ProductRepository productRepository;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/stock")
    @SendTo("/topic/products")
    public List<Product> updatedProductsStock() {
        return productRepository.findAll();
    }
}
