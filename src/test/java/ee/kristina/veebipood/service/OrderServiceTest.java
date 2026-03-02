package ee.kristina.veebipood.service;

import ee.kristina.veebipood.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    Product product = new Product();

    @BeforeEach
    void setUp() {
        product.setPrice(4.5);
    }

    @Test
    void save() {
    }

    @Test
    void calculateOrderSum() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        double sum = orderService.calculateOrderSum(products);
        assertEquals(4.5, sum);
    }
}